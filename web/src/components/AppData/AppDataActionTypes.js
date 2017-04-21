'use strict';

import {doAction} from '../../redux/actions/Action';
import AppService from '../../services/AppService';
import Util from '../../utils/Util';
import {Routes} from '../../components/Routes/Routes';

const {Toast} = window._external;

const ActionTypes = {

    appData: {

        load(dispatch) {
            const {code, state} = Util.pageParams;
            doAction(dispatch, ActionTypes.appData.changeState, {loading: true});
            AppService.getAppData({
                data: {code},
                success: (result = {}) => {
                    const {data = {}} = result;
                    const {timestamp, nonceStr, signature, attention} = data;
                    doAction(dispatch, ActionTypes.appData.changeState, {timestamp, nonceStr, signature, attention});
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
                    if (!attention) {
                        Toast.info('请先关注[组局官]！', 0);
                        Util.later(function () {
                            Toast.hide();
                            //location.href = 'https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzIxMzc5MjcyNQ==&scene=124#wechat_redirect';
                        }, 3000);
                    } else if (code && state) {
                        Routes.goto(state);
                    }
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.appData.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
