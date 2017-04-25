'use strict';

import {doAction} from '../../../../redux/actions/Action';
import AllActionTypes from '../../../../redux/actions/ActionTypes';
import FormService from '../../../../services/FormService';
import Util from '../../../../utils/Util';
import {Routes} from '../../../../components/Routes/Routes';

const {Toast} = window._external;

const ActionTypes = {

    feEditForm: {

        saveForm(dispatch) {
            const params = this || {};
            doAction(dispatch, AllActionTypes.formEdit.changeState, {loading: true});
            FormService.saveForm({
                data: params,
                success: (result) => {
                    const {data = {}} = result;
                    const {id} = data;
                    Util.later(function () {
                        Toast.info('保存成功！即将跳转...', 0);
                        Util.later(function () {
                            Toast.hide();
                            Routes.goto({
                                pathname: '/form/view',
                                query: {
                                    formId: id
                                }
                            });
                        }, 3000);
                    }, 1);
                },
                complete: () => {
                    doAction(dispatch, AllActionTypes.formEdit.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
