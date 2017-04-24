'use strict';

import {doAction} from '../../../redux/actions/Action';
import TemplateService from '../../../services/TemplateService';

const ActionTypes = {

    formEdit: {

        load(dispatch) {
            const {id} = this || {};
            doAction(dispatch, ActionTypes.formEdit.changeState, {loading: true});
            TemplateService.getTemplate({
                data: {id},
                success: (result = {}) => {
                    const {data = {}} = result;
                    const {type = '', title = '', fields = []} = data;
                    doAction(dispatch, ActionTypes.formEdit.changeState, {type, title, fields});
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.formEdit.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
