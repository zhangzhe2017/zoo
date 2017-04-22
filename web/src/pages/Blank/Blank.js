'use strict';

import CommonMixin from '../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class Blank extends Component {

    render() {
        return (
            <div className="x-page"></div>
        );
    }

}

Blank.propTypes = {
    location: PropTypes.object.isRequired
};

export {Blank};

export default connect(state => ({}))(Blank);
