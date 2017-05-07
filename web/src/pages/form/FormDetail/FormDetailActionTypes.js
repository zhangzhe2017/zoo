'use strict';

import {doAction} from '../../../redux/actions/Action';
import FormService from '../../../services/FormService';
import Util from '../../../utils/Util';

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
                    if (type === 'activity') {
                        realTitle = fieldValuesObject.title;
                    } else {
                        realTitle = title;
                    }
                    if (realTitle) {
                        formDetail.pageTitle = realTitle;
                        formDetail.initShare();
                    }
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.formDetail.changeState, {loading: false});
                }
            });
        },

        register(dispatch) {
            const {id, register, formDetail} = this || {};
            doAction(dispatch, ActionTypes.formDetail.changeState, {loading: true});
            FormService.register({
                data: {id, register},
                success: (result = {}) => {
                    const {data = {}} = result;
                    const {qrCodeResult = ''} = data;
                    Util.later(function () {
                        Toast.info(
                            register ? `报名成功！即将${qrCodeResult ? '跳转' : '刷新'}...` : '已取消报名！即将刷新...',
                            0
                        );
                        Util.later(function () {
                            Toast.hide();
                            if (register && qrCodeResult) {
                                location.href = qrCodeResult;
                            } else {
                                formDetail.reset();
                                formDetail.init();
                            }
                        }, 3000);
                    }, 1);
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.formDetail.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
