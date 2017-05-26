'use strict';

import style from './EditForm.scss';
import {doAction} from '../../../../redux/actions/Action';
import ActionTypes from '../../../../redux/actions/ActionTypes';
import CommonMixin from '../../../../mixins/CommonMixin';
import {createForm} from 'rc-form';
import {FormEdit} from '../FormEdit';
import Util from '../../../../utils/Util';

const {
    React, Component, connect, reactMixin, List, InputItem, TextareaItem, ImagePicker, _, Button, DatePicker, Toast
} = window._external;

@reactMixin.decorate(CommonMixin)
class EditForm extends Component {

    static defaultState = {
        imageFilesMap: {
            image: [
                /*{
                    url: 'https://zos.alipayobjects.com/rmsportal/PZUUCKTRIHWiZSY.jpeg',
                    serverId: '1'
                },
                {
                    url: 'https://zos.alipayobjects.com/rmsportal/hqQWgTXdrlmVVYi.jpeg',
                    serverId: '2'
                }*/
            ]
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
        _.forEach(formData, (value, key) => {
            if (value && value._isAMomentObject) {
                formData[key] = value.valueOf();
            }
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
        const {dispatch, form, fields} = this.props;
        form.validateFields({force: true}, (error) => {
            if (error) {
                Toast.fail(_.values(error)[0].errors[0].message);
            } else {
                const formData = this.getFormData();
                const formEdit = FormEdit.instance;
                let templateId = null;
                if (formEdit) {
                    templateId = formEdit.props.location.query.templateId;
                }
                doAction(dispatch, ActionTypes.feEditForm.saveForm, {
                    templateId,
                    title: formData.title,
                    fields: JSON.stringify(fields),
                    fieldValues: JSON.stringify(formData)
                });
            }
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
            success: (result = {}) => {
                const {localIds = []} = result;
                _.forEach(localIds, localId => {
                    wx.uploadImage({
                        localId,
                        isShowProgressTips: 1,
                        success: (result = {}) => {
                            const {serverId} = result;
                            const {dispatch, imageFilesMap} = this.props;
                            const imageFiles = imageFilesMap[name];
                            let url = null;
                            if (window.__wxjs_is_wkwebview) {
                                url = Util.blankImageData;
                            } else {
                                url = localId;
                            }
                            const imageFile = {
                                serverId,
                                url
                            };
                            imageFiles.push(imageFile);
                            doAction(dispatch, ActionTypes.feEditForm.changeState, {
                                imageFilesMap: {...imageFilesMap}
                            });
                            if (window.__wxjs_is_wkwebview) {
                                wx.getLocalImgData({
                                    localId,
                                    success: function (result) {
                                        const {localData} = result;
                                        imageFile.url = localData;
                                        doAction(dispatch, ActionTypes.feEditForm.changeState, {
                                            imageFilesMap: {...imageFilesMap}
                                        });
                                    }
                                });
                            }
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
            urls.push(file.url);
        });
        wx.previewImage({
            current: file.url,
            urls
        });
    }

    validateImages(label, name, required, rule, value, callback) {
        const {imageFilesMap} = this.props;
        const imageFiles = imageFilesMap[name];
        if (required && (!imageFiles || !imageFiles.length)) {
            callback(`${label}不能为空！`);
        } else {
            callback();
        }
    }

    render() {
        const {form, title, fields} = this.props;
        const {getFieldProps} = form;
        const items = [];
        _.forEach(fields, field => {
            const {label, name, type, extra, required} = field;
            const requiredMark = required ? <span className="x-required">*</span> : '';
            if (type === 'input') {
                items.push(
                    <InputItem
                        {...getFieldProps(
                            name,
                            {
                                rules: [
                                    {required, message: `${label}不能为空！`}
                                ]
                            }
                        )}
                        key={name}
                        clear={true}
                        extra={extra}
                        labelNumber={7}
                    >
                        {label} {requiredMark}
                    </InputItem>
                );
            } else if (type === 'number') {
                items.push(
                    <InputItem
                        {...getFieldProps(
                            name,
                            {
                                rules: [
                                    {required, message: `${label}不能为空！`}
                                ]
                            }
                        )}
                        key={name}
                        clear={true}
                        type="number"
                        extra={extra}
                        labelNumber={7}
                    >
                        {label} {requiredMark}
                    </InputItem>
                );
            } else if (type === 'textarea') {
                items.push(
                    <TextareaItem
                        {...getFieldProps(
                            name,
                            {
                                rules: [
                                    {required, message: `${label}不能为空！`}
                                ]
                            }
                        )}
                        key={name}
                        title={<div>{label} {requiredMark}</div>}
                        autoHeight={true}
                        clear={true}
                        rows={2}
                        labelNumber={7}
                    />
                );
            } else if (type === 'datetime') {
                items.push(
                    <DatePicker
                        {...getFieldProps(
                            name,
                            {
                                rules: [
                                    {required, message: `${label}不能为空！`}
                                ]
                            }
                        )}
                        key={name}
                        mode="datetime"
                        extra=""
                    >
                        <List.Item
                            className="x-list-item"
                        >
                            {label} {requiredMark}
                        </List.Item>
                    </DatePicker>
                );
            } else if (type === 'image') {
                items.push(
                    <List.Item
                        key={name}
                    >
                        <div style={{fontWeight: 'bold'}}>{label} {requiredMark}</div>
                        <ImagePicker
                            {...getFieldProps(
                                name,
                                {
                                    rules: [
                                        {validator: this.validateImages.bind(this, label, name, required)}
                                    ]
                                }
                            )}
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
                            onClick={this.handleSaveBtnClick}
                            className="x-button"
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
