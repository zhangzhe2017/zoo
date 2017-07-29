'use strict';

import {doAction} from '../../../../redux/actions/Action';
import ActionTypes from '../../../../redux/actions/ActionTypes';
import CommonMixin from '../../../../mixins/CommonMixin';
import {createForm} from 'rc-form';
import {FormEdit} from '../FormEdit';
import Util from '../../../../utils/Util';
import KindEditor from '../../../../components/KindEditor/KindEditor';
import cameraIcon from './camera.svg';

const {
    React, Component, connect, reactMixin, List, InputItem, TextareaItem, ImagePicker, _, Button, DatePicker, Toast, $,
    Icon
} = window._external;

@reactMixin.decorate(CommonMixin)
class EditForm extends Component {

    static defaultState = {
        imageFilesMap: {
            /*image: [
                {
                    url: 'https://zos.alipayobjects.com/rmsportal/PZUUCKTRIHWiZSY.jpeg',
                    serverId: '1'
                },
                {
                    url: 'https://zos.alipayobjects.com/rmsportal/hqQWgTXdrlmVVYi.jpeg',
                    serverId: '2'
                }
            ]*/
        },
        richtextMap: {},
        _refresh: {}
    };

    bindFns = ['handleSaveBtnClick', ['handleKindEditorChange', Util.buffer(this.handleKindEditorChange)]];

    reset() {
        const {dispatch, form} = this.props;
        doAction(dispatch, ActionTypes.feEditForm.replaceState, {
            ...EditForm.defaultState,
            imageFilesMap: {}
        });
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

    setFormData(data) {
        const {form} = this.props;
        const formData = {...data};
        form.setFieldsValue(formData);
        return formData;
    }

    handleSaveBtnClick() {
        const {dispatch, form, fields} = this.props;
        form.validateFields({force: true}, (error) => {
            if (error) {
                Toast.fail(_.values(error)[0].errors[0].message);
            } else {
                const formData = this.getFormData();
                const formEdit = FormEdit.instance;
                let templateId = null;
                let formId = null;
                if (formEdit) {
                    const {location} = formEdit.props;
                    const {pathname, query} = location;
                    if (pathname === '/form/add') {
                        templateId = query.templateId;
                    } else {
                        formId = query.formId;
                    }
                }
                doAction(dispatch, ActionTypes.feEditForm.saveForm, {
                    templateId,
                    formId,
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
        if (_Util.isPC()) {
            Util.upload((result = {}) => {
                const {data: url} = result;
                const {dispatch, imageFilesMap} = this.props;
                const imageFiles = imageFilesMap[name];
                const imageFile = {
                    serverId: url,
                    url
                };
                if (name === 'cover') {
                    imageFiles.splice(0);
                }
                imageFiles.push(imageFile);
                doAction(dispatch, ActionTypes.feEditForm.changeState, {
                    imageFilesMap: {...imageFilesMap}
                });
            });
        } else {
            wx.chooseImage({
                //todo 多个文件一起上传有问题
                count: 1,
                sizeType: ['compressed'],
                sourceType: ['album', 'camera'],
                success: (result = {}) => {
                    const {localIds = []} = result;
                    Util.debug(`localIds=${JSON.stringify(localIds)}`);
                    _.forEach(localIds, (localId, index) => {
                        Util.debug(`uploadImage, index=${index}, localId=${localId}`);
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
                                if (name === 'cover') {
                                    imageFiles.splice(0);
                                }
                                imageFiles.push(imageFile);
                                Util.debug(`uploadImage success, index=${index}, serverId=${serverId}, name=${name}, imageFiles.length=${imageFiles.length}`);
                                doAction(dispatch, ActionTypes.feEditForm.changeState, {
                                    imageFilesMap: {...imageFilesMap}
                                });
                                if (window.__wxjs_is_wkwebview) {
                                    wx.getLocalImgData({
                                        localId,
                                        success: (result = {}) => {
                                            const {localData} = result;
                                            const {dispatch, imageFilesMap} = this.props;
                                            imageFile.url = localData;
                                            Util.debug(`getLocalImgData success, index=${index}, serverId=${serverId}, name=${name}, imageFilesMap[name].length=${imageFilesMap[name].length}`);
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

    handleKindEditorChange(field, editor) {
        const {form} = this.props;
        const value = editor.html();
        form.setFieldsValue({
            [field]: value
        });
        try {
            form.validateFields([field]);
        } catch (e) {
            Util.debug(e.message);
        }
    }

    render() {
        const {form, title, fields, richtextMap, _refresh} = this.props;
        const {getFieldProps} = form;
        const formData = this.getFormData();
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
                        placeholder={label}
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
                        placeholder={label}
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
                        placeholder={label}
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
                        <List.Item className="x-list-item">
                            {label} {requiredMark}
                            {
                                !formData[name] ?
                                    <input type="text" className="x-placeholder" placeholder={label} readOnly/> : ''
                            }
                        </List.Item>
                    </DatePicker>
                );
            } else if (type === 'image') {
                if (name === 'cover') {
                    const imageFiles = this.getImageFiles(name);
                    items.push(
                        <div
                            key={name}
                            className="x-cover"
                            onClick={this.handleImageAddClick.bind(this, name)}
                        >
                            {
                                imageFiles.length ? (
                                    <img src={imageFiles[0].url} className="x-cover-image"/>
                                ) : (
                                    <table className="x-cover-table">
                                        <tbody>
                                        <tr>
                                            <td rowSpan="2">
                                                <Icon type={cameraIcon}/>
                                            </td>
                                            <td className="x-cover-title">
                                                上传封面图
                                            </td>
                                        </tr>
                                        <tr>
                                            <td className="x-cover-description">
                                                建议上传横屏拍摄的图片
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                )
                            }
                        </div>
                    );
                } else {
                    items.push(
                        <List.Item
                            key={name}
                        >
                            <div className="x-label">{label} {requiredMark}</div>
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
            } else if (_Util.isPC() && type === 'richtext') {
                items.push(
                    <List.Item
                        key={name}
                    >
                        {/*<TextareaItem
                            {...getFieldProps(
                                name,
                                {
                                    rules: [
                                        {required, message: `${label}不能为空！`}
                                    ]
                                }
                            )}
                            id={name}
                            title={<div>{label} {requiredMark}</div>}
                            autoHeight={true}
                            clear={true}
                            rows={2}
                            labelNumber={7}
                            placeholder={label}
                        />*/}
                        <div className="x-label">{label} {requiredMark}</div>
                        <textarea
                            {...getFieldProps(
                                name,
                                {
                                    rules: [
                                        {required, message: `${label}不能为空！`}
                                    ]
                                }
                            )}
                            id={name}
                            placeholder={label}
                            style={{display: 'none'}}
                        />
                        <KindEditor
                            target={`#${name}`}
                            value={richtextMap[name]}
                            onChange={this.handleKindEditorChange.bind(this, name)}
                            _refresh={_refresh}
                        />
                    </List.Item>
                );
            }
        });
        return (
            <form>
                {
                    title ?
                        <h2 className="x-title">{title}</h2> : ''
                }
                <List>{items}</List>
                {
                    items.length ?
                        <div className="x-button-wrapper">
                            <Button
                                type="primary"
                                onClick={this.handleSaveBtnClick}
                                className="x-button"
                            >
                                保存
                            </Button>
                        </div> : ''
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
