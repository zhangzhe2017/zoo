'use strict';

import CommonMixin from '../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class CustomForm extends Component {

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
                CustomForm
            </div>
        );
    }

}

CustomForm.propTypes = {
    location: PropTypes.object.isRequired
};

export {CustomForm};

export default connect(state => ({}))(CustomForm);
