'use strict';

import {doAction} from '../../redux/actions/Action';
import ActionTypes from '../../redux/actions/ActionTypes';
import CommonMixin from '../../mixins/CommonMixin';
import Util from '../../utils/Util';
import {Routes} from '../../components/Routes/Routes';

const {
    React, Component, PropTypes, connect, reactMixin//, Toast
} = window._external;

@reactMixin.decorate(CommonMixin)
class Auth extends Component {

    static defaultState = {
        loading: false,
        timestamp: 0,
        nonceStr: '',
        signature: '',
        attention: false,
        wxid: ''
    };
    static authed = false;

    isAuth = true;

    static doAuth(dispatch, result = {}) {
        Auth.authed = true;
        sessionStorage.setItem('authResult', JSON.stringify(result));
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
            const {code, redirectUrl} = Util.pageParams;
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
    }

    init() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.auth.load);
    }

    /*reset() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.auth.replaceState, Auth.defaultState);
    }*/

    render() {
        return (
            <div className="x-page"></div>
        );
    }

}

Auth.propTypes = {
    location: PropTypes.object.isRequired
};

export {Auth};

export default connect(state => ({}))(Auth);
