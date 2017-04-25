'use strict';

import style from './EditForm.scss';
import {doAction} from '../../../../redux/actions/Action';
import ActionTypes from '../../../../redux/actions/ActionTypes';
import CommonMixin from '../../../../mixins/CommonMixin';
import {createForm} from 'rc-form';
import {FormEdit} from '../FormEdit';

const {
    React, Component, connect, reactMixin, List, InputItem, TextareaItem, ImagePicker, _, Button
} = window._external;

@reactMixin.decorate(CommonMixin)
class EditForm extends Component {

    static defaultState = {
        imageFilesMap: {
            /*image: [
                {
                    url: 'https://zos.alipayobjects.com/rmsportal/PZUUCKTRIHWiZSY.jpeg',
                    serverId: '2121'
                },
                {
                    url: 'https://zos.alipayobjects.com/rmsportal/hqQWgTXdrlmVVYi.jpeg',
                    serverId: '2122'
                }
            ]*/
        }
    };

    bindFns = ['handleSaveBtnClick'];

    reset() {
        const {dispatch, form} = this.props;
        doAction(dispatch, ActionTypes.feEditForm.replaceState, EditForm.defaultState);
        form.resetFields();
    }

    getFormData(values) {
        const {form, imageFilesMap} = this.props;
        values = values || form.getFieldsValue();
        const formData = {...values};
        _.forEach(imageFilesMap, (imageFiles, name) => {
            let fieldValues = formData[name];
            if (!fieldValues) {
                fieldValues = formData[name] = [];
            }
            _.forEach(imageFiles, imageFile => {
                const {serverId} = imageFile;
                serverId && fieldValues.push(serverId);
            });
        });
        return formData;
    }

    /*setFormData(data) {
        const {form} = this.props;
        const formData = {...data};
        form.setFieldsValue(formData);
        //todo 加载图片数据
        return formData;
    }*/

    handleSaveBtnClick() {
        const {dispatch} = this.props;
        const formData = this.getFormData();
        doAction(dispatch, ActionTypes.feEditForm.saveForm, {
            templateId: FormEdit.instance.props.location.query.templateId,
            fieldValues: JSON.stringify(formData)
        });
    }

    handleImageChange(name, files, operationType, index) {
        const {dispatch, imageFilesMap} = this.props;
        imageFilesMap[name] = files;
        doAction(dispatch, ActionTypes.feEditForm.changeState, {
            imageFilesMap: {...imageFilesMap}
        });
    }

    handleImageAddClick(name, e) {
        e.preventDefault();
        wx.chooseImage({
            count: 1,
            sizeType: ['compressed'],
            sourceType: ['album', 'camera'],
            success: result => {
                const localIds = result.localIds;
                _.forEach(localIds, localId => {
                    wx.uploadImage({
                        localId,
                        isShowProgressTips: 1,
                        success: result => {
                            const serverId = result.serverId;
                            const {dispatch, imageFilesMap} = this.props;
                            const imageFiles = imageFilesMap[name];
                            imageFiles.push({
                                serverId,
                                url: localId
                            });
                            doAction(dispatch, ActionTypes.feEditForm.changeState, {
                                imageFilesMap: {...imageFilesMap}
                            });
                        }
                    });
                });
            }
        });
    }

    getImageFiles(name) {
        const {imageFilesMap} = this.props;
        let imageFiles = imageFilesMap[name];
        if (!imageFiles) {
            imageFiles = imageFilesMap[name] = [];
        }
        return imageFiles;
    }

    handleImageClick(index, files) {
        const file = files[index];
        const urls = [];
        _.forEach(files, file => {
            urls.push(file);
        });
        wx.previewImage({
            current: file.url,
            urls
        });
    }

    render() {
        const {form, title, fields} = this.props;
        const {getFieldProps} = form;
        const items = [];
        _.forEach(fields, field => {
            const {label, name, type} = field;
            if (type === 'input') {
                items.push(
                    <InputItem
                        {...getFieldProps(name)}
                        key={name}
                        clear={true}
                    >
                        {label}
                    </InputItem>
                );
            } else if (type === 'textarea') {
                items.push(
                    <TextareaItem
                        {...getFieldProps(name)}
                        key={name}
                        title={label}
                        autoHeight={true}
                        clear={true}
                        rows={2}
                    />
                );
            } else if (type === 'image') {
                items.push(
                    <List.Item
                        key={name}
                    >
                        <div style={{fontWeight: 'bold'}}>{label}</div>
                        <ImagePicker
                            files={this.getImageFiles(name)}
                            onChange={this.handleImageChange.bind(this, name)}
                            onAddImageClick={this.handleImageAddClick.bind(this, name)}
                            onImageClick={this.handleImageClick}
                        />
                    </List.Item>
                );
            }
        });
        return (
            <form>
                {
                    title ?
                        <h2 className={style.title}>{title}</h2> : ''
                }
                <List>{items}</List>
                {
                    items.length ?
                        <Button
                            type="primary"
                            style={{marginTop: '0.25rem'}}
                            onClick={this.handleSaveBtnClick}
                        >
                            保存
                        </Button> : ''
                }
            </form>
        );
    }

}

EditForm.propTypes = {};

export {EditForm};

export default createForm()(
    connect(state => {
        const {formEdit, feEditForm} = state;
        const {title, fields} = formEdit;
        return {
            ...feEditForm,
            title,
            fields
        };
    })(EditForm)
);
