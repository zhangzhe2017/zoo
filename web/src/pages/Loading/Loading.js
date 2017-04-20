'use strict';

import CommonMixin from '../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class Loading extends Component {

    render() {
        return (
            <div className="x-page"></div>
        );
    }

}

Loading.propTypes = {
    location: PropTypes.object.isRequired
};

export {Loading};

export default connect(state => ({}))(Loading);
