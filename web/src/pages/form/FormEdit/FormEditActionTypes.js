'use strict';

import {doAction} from '../../../redux/actions/Action';
import TemplateService from '../../../services/TemplateService';
import FormService from '../../../services/FormService';
import {EditForm} from './EditForm/EditForm';
import AllActionTypes from '../../../redux/actions/ActionTypes';

const {_, moment} = window._external;

const getTemplateSuccessFn = (result, dispatch, formEdit) => {
    const {data = {}} = result;
    const {type = '', title = '', fields = []} = data;
    doAction(dispatch, ActionTypes.formEdit.changeState, {type, title, fields});
    if (title) {
        formEdit.pageTitle = title;
        formEdit.initShare();
    }
};

const ActionTypes = {

    formEdit: {

        getTemplate(dispatch) {
            const {id, formEdit} = this || {};
            doAction(dispatch, ActionTypes.formEdit.changeState, {loading: true});
            TemplateService.getTemplate({
                data: {id},
                success: (result = {}) => {
                    getTemplateSuccessFn(result, dispatch, formEdit);
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.formEdit.changeState, {loading: false});
                }
            });
        },

        getForm(dispatch) {
            const {id, formEdit} = this || {};
            doAction(dispatch, ActionTypes.formEdit.changeState, {loading: true});
            FormService.getForm({
                data: {id},
                success: (result = {}) => {
                    getTemplateSuccessFn(result, dispatch, formEdit);
                    const {data = {}} = result;
                    const {
                        type = '', title = '', fields = [], fieldValues = '{}', registered = false,
                        creatorNickName = '', creatorWxid = '', attenderList = [], timestamp = 0
                    } = data;
                    let fieldValuesObject = null;
                    try {
                        fieldValuesObject = JSON.parse(fieldValues);
                    } catch (e) {
                        fieldValuesObject = {};
                    }
                    const editForm = EditForm.instance;
                    if (editForm) {
                        const formData = {};
                        const imageFilesMap = {};
                        const richtextMap = {};
                        _.forEach(fields, field => {
                            const {name, type} = field;
                            const value = fieldValuesObject[name];
                            if (type === 'image') {
                                imageFilesMap[name] = _.map(value || [], url => {
                                    return {
                                        url,
                                        serverId: url
                                    };
                                });
                            } else if (type === 'datetime') {
                                if (value) {
                                    formData[name] = moment(value);
                                }
                            } else if (type === 'richtext') {
                                richtextMap[name] = value || '';
                            } else {
                                formData[name] = fieldValuesObject[name];
                            }
                        });
                        editForm.setFormData(formData);
                        doAction(dispatch, AllActionTypes.feEditForm.changeState, {
                            imageFilesMap,
                            richtextMap,
                            _refresh: {}
                        });
                    }
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.formEdit.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
