'use strict';

import Util from '../utils/Util';

const {_} = window._external;

export default {

    componentWillMount() {
        Util.bind(this, this.bindFns);
        this.constructor.instance = this;
    },

    componentDidMount() {
        this.renderLog();
        const {location} = this.props;
        if (location) {
            document.body.style.overflow = 'auto';
            this.doInit();
        } else {
            this.init && this.init();
        }
    },

    componentDidUpdate() {
        this.renderLog();
        const {location} = this.props;
        if (location) {
            this.doInit();
        }
        this.update && this.update();
    },

    componentWillUnmount() {
        const {location} = this.props;
        this.reset && this.reset();
        if (location) {
            _.assign(this.constructor, {
                currentPathname: null,
                currentSearch: null,
                currentKey: null
            });
        }
        this.destroy && this.destroy();
        this.constructor.instance = null;
    },

    doInit() {
        const {location} = this.props;
        if (location) {
            const {pathname, search, key} = location;
            const {currentPathname, currentSearch, currentKey} = this.constructor;
            if (pathname !== currentPathname || search !== currentSearch || key !== currentKey) {
                if (currentKey != null) {
                    this.reset && this.reset();
                }
                this.init && this.init();
                _.assign(this.constructor, {
                    currentPathname: pathname,
                    currentSearch: search,
                    currentKey: key
                });
            }
        }
    },

    renderLog() {
        Util.renderLog(this.name || this.constructor.name);
    }

};
