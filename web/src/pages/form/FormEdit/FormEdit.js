'use strict';

import {doAction} from '../../../redux/actions/Action';
import ActionTypes from '../../../redux/actions/ActionTypes';
import CommonMixin from '../../../mixins/CommonMixin';
import EditForm, {EditForm as OriginEditForm} from './EditForm/EditForm';
import {Routes} from '../../../components/Routes/Routes';
import Util from '../../../utils/Util';

const {React, Component, PropTypes, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class FormEdit extends Component {

    static defaultState = {
        loading: false,
        type: '',
        title: '',
        fields: []
    };

    pageTitle = '表单填写';

    init() {
        const {dispatch, location} = this.props;
        const {pathname, query} = location;
        const {templateId} = query;
        if (_isQQBrowser() && templateId === '1') {
            Util.later(() => {
                Routes.goto({
                    pathname,
                    query: {
                        templateId: 2
                    }
                });
            }, 1);
        } else {
            doAction(dispatch, ActionTypes.formEdit.getTemplate, {
                id: templateId,
                formEdit: this
            });
        }
    }

    reset() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.formEdit.replaceState, FormEdit.defaultState);
        const editForm = OriginEditForm.instance;
        editForm && editForm.reset();
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
