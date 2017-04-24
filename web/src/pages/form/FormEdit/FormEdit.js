'use strict';

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

    render() {
        const {title} = this.props;
        return (
            <div className="x-page">
                FormEdit
            </div>
        );
    }

}

FormEdit.propTypes = {
    location: PropTypes.object.isRequired
};

export {FormEdit};

export default connect(state => ({}))(FormEdit);
