'use strict';

import {doAction} from '../../../redux/actions/Action';
import ActionTypes from '../../../redux/actions/ActionTypes';
import CommonMixin from '../../../mixins/CommonMixin';
import EditForm, {EditForm as OriginEditForm} from './EditForm/EditForm';

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
            id: query.templateId
        });
    }

    reset() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.formEdit.replaceState, FormEdit.defaultState);
        const editForm = OriginEditForm.instance;
        editForm.reset();
    }

    render() {
        return (
            <div className="x-page">
                <EditForm/>
            </div>
        );
    }

}

FormEdit.propTypes = {
    location: PropTypes.object.isRequired
};

export {FormEdit};

export default connect(state => ({}))(FormEdit);
