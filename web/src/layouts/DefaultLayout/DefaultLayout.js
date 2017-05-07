'use strict';

import CommonMixin from '../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class DefaultLayout extends Component {

    render() {
        const {children} = this.props;
        return (
            <div className="x-layout">
                <div className="x-layout-pageContent">
                    {children}
                </div>
            </div>
        );
    }

}

DefaultLayout.propTypes = {
    children: PropTypes.element.isRequired
};

export {DefaultLayout};

export default connect(state => ({}))(DefaultLayout);
