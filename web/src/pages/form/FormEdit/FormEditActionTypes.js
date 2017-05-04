'use strict';

import {doAction} from '../../../redux/actions/Action';
import TemplateService from '../../../services/TemplateService';

const ActionTypes = {

    formEdit: {

        getTemplate(dispatch) {
            const {id, formEdit} = this || {};
            doAction(dispatch, ActionTypes.formEdit.changeState, {loading: true});
            TemplateService.getTemplate({
                data: {id},
                success: (result = {}) => {
                    const {data = {}} = result;
                    const {type = '', title = '', fields = []} = data;
                    doAction(dispatch, ActionTypes.formEdit.changeState, {type, title, fields});
                    if (title) {
                        formEdit.pageTitle = title;
                        formEdit.initShare();
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
