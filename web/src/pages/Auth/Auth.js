'use strict';

import {doAction} from '../../redux/actions/Action';
import ActionTypes from '../../redux/actions/ActionTypes';
import CommonMixin from '../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, reactMixin} = window._external;

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

    isAuth = true;

    init() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.auth.load);
    }

    reset() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.auth.replaceState, Auth.defaultState);
    }

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
