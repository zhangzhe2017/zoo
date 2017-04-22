'use strict';

import CommonMixin from '../../mixins/CommonMixin';
import AppData from '../../components/AppData/AppData';

const {React, Component, PropTypes, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class DefaultLayout extends Component {

    render() {
        const {children} = this.props;
        return (
            <div className="x-layout">
                <AppData/>
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
