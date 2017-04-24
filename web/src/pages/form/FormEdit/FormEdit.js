'use strict';

import {doAction} from '../../../redux/actions/Action';
import ActionTypes from '../../../redux/actions/ActionTypes';
import CommonMixin from '../../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class FormEdit extends Component {

    static defaultState = {
        loading: false,
        type: '',
        title: '',
        fields: []
    };

    init() {
        const {dispatch, location} = this.props;
        const {query} = location;
        doAction(dispatch, ActionTypes.formEdit.getTemplate, {
            id: query.id
        });
    }

    render() {
        const {type, title, fields} = this.props;
        return (
            <div className="x-page">
            </div>
        );
    }

}

FormEdit.propTypes = {
    location: PropTypes.object.isRequired
};

export {FormEdit};

export default connect(state => ({}))(FormEdit);
