'use strict';

import {doAction} from '../../../redux/actions/Action';
import AllActionTypes from '../../../redux/actions/ActionTypes';
import FormService from '../../../services/FormService';
import Util from '../../../utils/Util';
import {Routes} from '../../../components/Routes/Routes';

const {Toast} = window._external;

const ActionTypes = {

    formDetail: {

        getForm(dispatch) {
            const {id, formDetail} = this || {};
            doAction(dispatch, ActionTypes.formDetail.changeState, {loading: true});
            FormService.getForm({
                data: {id},
                success: (result = {}) => {
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
                    doAction(dispatch, ActionTypes.formDetail.changeState, {
                        type, title, fields, fieldValues: fieldValuesObject, registered, creatorNickName, creatorWxid,
                        attenderList, timestamp
                    });
                    let realTitle = null;
                    const isActivity = type === 'activity';
                    if (isActivity) {
                        realTitle = fieldValuesObject.title;
                    } else {
                        realTitle = title;
                    }
                    if (realTitle) {
                        formDetail.pageTitle = realTitle;
                        if (isActivity) {
                            formDetail.pageDesc = fieldValuesObject.description;
                        } else {
                            formDetail.pageDesc = '';
                        }
                        formDetail.initShare();
                    }
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.formDetail.changeState, {loading: false});
                }
            });
        },

        register(dispatch) {
            const {id, register, formDetail, qrCodeUrl} = this || {};
            doAction(dispatch, ActionTypes.formDetail.changeState, {loading: true});
            FormService.register({
                data: {id, register},
                success: () => {
                    if (register) {
                        doAction(dispatch, AllActionTypes.registerSuccess.changeState, {qrCodeUrl});
                        Routes.goto('/result/registerSuccess');
                    } else {
                        Util.later(function () {
                            Toast.info('已取消报名！即将刷新...', 0);
                            Util.later(function () {
                                Toast.hide();
                                formDetail.reset();
                                formDetail.init();
                            }, 1000);
                        }, 1);
                    }
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.formDetail.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
