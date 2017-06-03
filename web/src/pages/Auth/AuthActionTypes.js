'use strict';

import {doAction} from '../../redux/actions/Action';
import AppService from '../../services/AppService';
import Util from '../../utils/Util';
import {Routes} from '../../components/Routes/Routes';

const {Toast} = window._external;

const ActionTypes = {

    auth: {

        load(dispatch) {
            const {code, redirectUrl} = Util.pageParams;
            doAction(dispatch, ActionTypes.auth.changeState, {loading: true});
            AppService.getAppData({
                data: {code},
                success: (result = {}) => {
                    const {data = {}} = result;
                    const {timestamp = 0, nonceStr = '', signature = '', attention = false, wxid = ''} = data;
                    doAction(dispatch, ActionTypes.auth.changeState, {
                        timestamp, nonceStr, signature, attention, wxid
                    });
                    if (timestamp && nonceStr && signature) {
                        wx.config({
                            debug: false,
                            appId: Util.appId,
                            timestamp,
                            nonceStr,
                            signature,
                            jsApiList: [
                                'chooseImage', 'uploadImage', 'previewImage',
                                'onMenuShareTimeline', 'onMenuShareAppMessage',
                                'getLocalImgData'
                            ]
                        });
                    }
                    const redirectTo = () => {
                        if (code) {
                            redirectUrl && Routes.replace(redirectUrl);
                        }
                    };
                    /*if (!attention) {
                        Util.later(function () {
                            //Toast.info('请关注公众号[组局官]！页面即将跳转...', 0);
                            Toast.info('请关注公众号[组局官]！页面即将跳转...', 0);
                            Util.later(function () {
                                Toast.hide();
                                //location.href = 'https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzIxMzc5MjcyNQ==&scene=124#wechat_redirect';
                                redirectTo();
                            }, 1000);
                        }, 1);
                    } else {
                        redirectTo();
                    }*/
                    redirectTo();
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.auth.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
