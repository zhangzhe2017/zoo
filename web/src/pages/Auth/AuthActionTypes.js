'use strict';

import {doAction} from '../../redux/actions/Action';
import AppService from '../../services/AppService';
import Util from '../../utils/Util';
import {Auth} from './Auth';

const ActionTypes = {

    auth: {

        load(dispatch) {
            const {code} = Util.pageParams;
            doAction(dispatch, ActionTypes.auth.changeState, {loading: true});
            AppService.getAppData({
                data: {code},
                success: (result = {}) => {
                    Auth.doAuth(dispatch, result);
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.auth.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
