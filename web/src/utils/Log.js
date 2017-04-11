'use strict';

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
    }

};
