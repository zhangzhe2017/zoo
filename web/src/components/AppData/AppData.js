'use strict';

import {doAction} from '../../redux/actions/Action';
import ActionTypes from '../../redux/actions/ActionTypes';
import CommonMixin from '../../mixins/CommonMixin';

const {React, Component, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class AppData extends Component {

    static defaultState = {
        loading: false,
        timestamp: 0,
        nonceStr: '',
        signature: ''
    };

    init() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.appData.load);
    }

    reset() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.appData.replaceState, AppData.defaultState);
    }

    render() {
        return null;
    }

}

AppData.propTypes = {};

export {AppData};

export default connect(state => ({}))(AppData);
