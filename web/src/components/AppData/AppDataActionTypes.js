'use strict';

import {doAction} from '../../redux/actions/Action';
import AppService from '../../services/AppService';

const ActionTypes = {

    appData: {

        load(dispatch, getState) {
            doAction(dispatch, ActionTypes.appData.changeState, {loading: true});
            AppService.getAppData({
                success: (result = {}) => {
                    const {data = {}} = result;
                    //const {} = data;
                    //doAction(dispatch, ActionTypes.appData.changeState, {});
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.appData.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
