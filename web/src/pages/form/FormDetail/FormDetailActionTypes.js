'use strict';

import {doAction} from '../../../redux/actions/Action';
import FormService from '../../../services/FormService';
import Util from '../../../utils/Util';

const {Toast} = window._external;

const ActionTypes = {

    formDetail: {

        getForm(dispatch) {
            const {id} = this || {};
            doAction(dispatch, ActionTypes.formDetail.changeState, {loading: true});
            FormService.getForm({
                data: {id},
                success: (result) => {
                    const {data = {}} = result;
                    const {
                        type = '', title = '', fields = [], fieldValues = '{}', registered = false,
                        creatorNickName = '', creatorWxid = '', attenderList = []
                    } = data;
                    let fieldValuesObject = null;
                    try {
                        fieldValuesObject = JSON.parse(fieldValues);
                    } catch (e) {
                        fieldValuesObject = {};
                    }
                    doAction(dispatch, ActionTypes.formDetail.changeState, {
                        type, title, fields, fieldValues: fieldValuesObject, registered, creatorNickName, creatorWxid,
                        attenderList
                    });
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.formDetail.changeState, {loading: false});
                }
            });
        },

        register(dispatch) {
            const {register, formDetail} = this || {};
            doAction(dispatch, ActionTypes.formDetail.changeState, {loading: true});
            FormService.register({
                data: {register},
                success: (result) => {
                    Util.later(function () {
                        Toast.info(`${register ? '报名成功' : '已取消报名'}！即将刷新...`, 0);
                        Util.later(function () {
                            Toast.hide();
                            formDetail.reset();
                            formDetail.init();
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
