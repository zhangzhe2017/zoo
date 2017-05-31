'use strict';

import FastClick from 'fastclick';
import '../common/common.scss';
import Reducers from '../redux/reducers/Reducers';
import Routes from '../components/Routes/Routes';
import Util from '../utils/Util';

const {
    React, render, hashHistory, createStore, combineReducers, applyMiddleware, thunk, Provider, syncHistoryWithStore,
    routerReducer, $
} = window._external;

const reducers = combineReducers({
    ...Reducers,
    routing: routerReducer
});
const store = createStore(reducers, {}, applyMiddleware(thunk));
const history = syncHistoryWithStore(hashHistory, store);
render(
    <Provider store={store}>
        <Routes history={history}/>
    </Provider>,
    document.getElementById('root')
);
FastClick.attach(document.body);

if (Util.isDebug()) {
    $(() => {
        const debugMsgEl = Util.debugMsgEl = $('<div class="x-debug-msg"></div>').appendTo('body');
        $('<input type="button" value="debug" class="x-debug-button"/>').on('click', () => {
            debugMsgEl.toggle();
        }).appendTo('body');
    });
}
