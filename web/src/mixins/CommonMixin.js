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
            //document.body.style.overflow = 'auto';
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
                currentSearch: null
            });
        }
        this.destroy && this.destroy();
        this.constructor.instance = null;
    },

    initShare() {
        wx.ready(() => {
            const {location} = this.props;
            const {pathname, search} = location;
            const redirectUrl = `http://www.zujuguan.com/index.html?redirectUrl=${encodeURIComponent(pathname + search)}`;
            const shareConfig = {
                title: this.pageTitle,
                desc: this.pageDesc,
                imgUrl: this.pageImage,
                link: `http://www.zujuguan.com/app/redirectShareUrl?redirectUrl=${encodeURIComponent(redirectUrl)}`,
                success: () => {
                },
                cancel: () => {
                }
            };
            wx.onMenuShareTimeline(shareConfig);
            wx.onMenuShareAppMessage(shareConfig);
        });
    },

    doInit() {
        const {location} = this.props;
        if (location) {
            const {pathname, search} = location;
            const {currentPathname, currentSearch} = this.constructor;
            if (pathname !== currentPathname || search !== currentSearch) {
                this.reset && this.reset();
                this.init && this.init();
                if (!this.isApp && !this.isAuth) {
                    this.initShare();
                }
                _.assign(this.constructor, {
                    currentPathname: pathname,
                    currentSearch: search
                });
            }
        }
    },

    renderLog() {
        Util.renderLog(this.name || this.constructor.name);
    }

};
