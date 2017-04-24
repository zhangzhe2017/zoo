'use strict';

import style from './EditForm.scss';
import {doAction} from '../../../../redux/actions/Action';
import ActionTypes from '../../../../redux/actions/ActionTypes';
import CommonMixin from '../../../../mixins/CommonMixin';
import {createForm} from 'rc-form';

const {React, Component, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class EditForm extends Component {

    static defaultState = {};

    reset() {
        const {dispatch, form} = this.props;
        doAction(dispatch, ActionTypes.feEditForm.replaceState, EditForm.defaultState);
        form.resetFields();
    }

    getFormData(values) {
        const {form} = this.props;
        values = values || form.getFieldsValue();
        const formData = {...values};
        return formData;
    }

    setFormData(data) {
        const {form} = this.props;
        const formData = {...data};
        form.setFieldsValue(formData);
        return formData;
    }

    render() {
        const {form, title} = this.props;
        const {getFieldProps} = form;
        return (
            <form>
                <h2 className={style.title}>{title}</h2>
            </form>
        );
    }

}

EditForm.propTypes = {};

export {EditForm};

export default createForm()(
    connect(state => state.formEdit)(EditForm)
);
