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

if (window._isQQBrowser) {
    var KindEditor = window.KindEditor;
    if (KindEditor) {
        KindEditor.kindEditorUploadBtnClickEvent = () => {
            Util.upload((result = {}) => {
                const {data} = result;
                $('#remoteUrl').val(data);
            })
        };
    }
} else {
    window._isQQBrowser = () => {
        return false;
    };
}

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
        $('<a class="x-debug-linker" href="javascript:;">debug</a>').on('click', () => {
            debugMsgEl.toggle();
        }).appendTo('body');
        //Util.debug(`navigator.userAgent=${navigator.userAgent}`);
    });
}

$(() => {
    $('body').on('click', '.x-richtext img', (e) => {
        const currentTargetEl = $(e.currentTarget);
        const richtextEl = currentTargetEl.closest('.x-richtext');
        const imageEls = richtextEl.find('img');
        const url = currentTargetEl.attr('src');
        const urls = [];
        imageEls.each((index, dom) => {
            urls.push($(dom).attr('src'));
        });
        wx.previewImage({
            current: url,
            urls
        });
    });
});
