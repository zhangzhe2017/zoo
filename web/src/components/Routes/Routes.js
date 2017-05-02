'use strict';

import App from '../../layouts/App';
import CommonMixin from '../../mixins/CommonMixin';
import NotFound from '../../pages/NotFound/NotFound';
import Blank from '../../pages/Blank/Blank';
import FormEdit from '../../pages/form/FormEdit/FormEdit';
import FormDetail from '../../pages/form/FormDetail/FormDetail';
import FormList from '../../pages/list/FormList/FormList';

const {React, Component, PropTypes, Router, Route, IndexRedirect, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class Routes extends Component {

    static history = null;

    static replace(config) {
        const {history} = Routes;
        history && history.replace(config);
    }

    static goBack() {
        const {history} = Routes;
        history && history.goBack();
    }

    static goto(config) {
        const {history} = Routes;
        history && history.push(config);
    }

    init() {
        const {history} = this.props;
        Routes.history = history;
    }

    destroy() {
        Routes.history = null;
    }

    render() {
        const {history} = this.props;
        return (
            <Router history={history}>
                <Route path="/" component={App}>
                    <IndexRedirect to="blank"/>
                    <Route path="blank" component={Blank}/>
                    <Route path="form/add" component={FormEdit}/>
                    <Route path="form/view" component={FormDetail}/>
                    <Route path="list/formList" component={FormList}/>
                    <Route path="*" component={NotFound}/>
                </Route>
            </Router>
        );
    }
}

Routes.propTypes = {
    history: PropTypes.object.isRequired
};

export {Routes};

export default connect(state => ({}))(Routes);
