'use strict';

import {doAction} from '../../redux/actions/Action';
import AppService from '../../services/AppService';
import Util from '../../utils/Util';

const ActionTypes = {

    appData: {

        load(dispatch) {
            const {code} = Util.pageParams;
            doAction(dispatch, ActionTypes.appData.changeState, {loading: true});
            AppService.getAppData({
                data: {code},
                success: (result = {}) => {
                    const {data = {}} = result;
                    const {timestamp, nonceStr, signature} = data;
                    if (timestamp && nonceStr && signature) {
                        wx.config({
                            debug: false,
                            appId: Util.appId,
                            timestamp,
                            nonceStr,
                            signature,
                            jsApiList: []
                        });
                    }
                    doAction(dispatch, ActionTypes.appData.changeState, {timestamp, nonceStr, signature});
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.appData.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
