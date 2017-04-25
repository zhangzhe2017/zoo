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
                    const {timestamp = 0, nonceStr = '', signature = '', attention = false, wxid = ''} = data;
                    doAction(dispatch, ActionTypes.appData.changeState, {
                        timestamp, nonceStr, signature, attention, wxid
                    });
                    if (timestamp && nonceStr && signature) {
                        wx.config({
                            debug: false,
                            appId: Util.appId,
                            timestamp,
                            nonceStr,
                            signature,
                            jsApiList: ['chooseImage', 'uploadImage', 'previewImage']
                        });
                    }
                    if (!attention) {
                        Util.later(function () {
                            Toast.info('请先关注[组局官]！即将跳转...', 0);
                            Util.later(function () {
                                Toast.hide();
                                location.href = 'https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzIxMzc5MjcyNQ==&scene=124#wechat_redirect';
                            }, 3000);
                        }, 1);
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
