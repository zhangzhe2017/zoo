'use strict';

import CommonMixin from '../mixins/CommonMixin';
import DefaultLayout from './DefaultLayout/DefaultLayout';

const {React, Component, PropTypes, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class App extends Component {

    render() {
        const {children} = this.props;
        return (
            <DefaultLayout>
                {children}
            </DefaultLayout>
        );
    }

}

App.propTypes = {
    location: PropTypes.object.isRequired,
    children: PropTypes.element.isRequired
};

export {App};

export default connect(state => ({}))(App);
