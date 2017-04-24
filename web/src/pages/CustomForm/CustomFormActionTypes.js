'use strict';

import {doAction} from '../../redux/actions/Action';
import TemplateService from '../../services/TemplateService';

const ActionTypes = {

    customForm: {

        load(dispatch) {
            const {id} = this || {};
            doAction(dispatch, ActionTypes.customForm.changeState, {loading: true});
            TemplateService.getTemplate({
                data: {id},
                success: (result = {}) => {
                    const {data = {}} = result;
                    const {type = '', title = '', fields = []} = data;
                    doAction(dispatch, ActionTypes.customForm.changeState, {type, title, fields});
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.customForm.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
