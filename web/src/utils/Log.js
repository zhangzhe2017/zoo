'use strict';

const {_} = window._external;

export default {

    renderLog(componentName) {
        const {pageParams} = this;
        const {component} = pageParams;
        if (this.isDebug() && component && (component === 'all' || component.split(',').includes(componentName))) {
            this.log(`${componentName} render`);
        }
    },

    debug(message) {
        if (this.isDebug()) {
            this.log(message);
        }
    },

    log(message) {
        console.log(message);
        const {debugMsgEl} = this;
        if (debugMsgEl) {
            debugMsgEl.html(debugMsgEl.html() + _.escape(message) + '<br/>');
            debugMsgEl.scrollTop(debugMsgEl.prop('scrollHeight'))
        }
    }

};
