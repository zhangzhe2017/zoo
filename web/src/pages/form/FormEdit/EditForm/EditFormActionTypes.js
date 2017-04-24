'use strict';

import {doAction} from '../../../../redux/actions/Action';
import AllActionTypes from '../../../../redux/actions/ActionTypes';
import {Routes} from '../../../../components/Routes/Routes';

const ActionTypes = {

    feEditForm: {

        saveForm(dispatch, getState) {
            const formData = this || {};
            doAction(dispatch, AllActionTypes.formEdit.changeState, {loading: true});
            FormService.saveForm({
                data: formData,
                success: () => {
                    //todo 跳转到详情页面
                },
                complete: () => {
                    doAction(dispatch, AllActionTypes.formEdit.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
