'use strict';

const {_, Toast} = window._external;

export default {

    appId: 'wx88ba27baed50d054',
    loadingCount: 0,

    blankImageData: 'data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==',
    loadingImageData: 'data:image/gif;base64,R0lGODlhEAAQAKIAAP///9bW1szMzL29vXt7e3Nzc2ZmZv4BAiH/C05FVFNDQVBFMi4wAwEAAAAh+QQFBwAHACwAAAAAEAAQAAADPAi6QRQrymJMkcwFatuLXOFt1bWEYBmNq6awGDCicUhjIYzBwTBAmc9CQBSkLJGiYKZi+EgdV60ZCwIjCQAh+QQFBwAHACwAAAYABgAIAAADE3gz1/LCwaYYbYTQYkxpnEdlRwIAIfkEBQcABwAsAAACAAYACAAAAxN4OqMnUKgoVzuEuGJMUZznZE4CACH5BAUHAAcALAIAAAAIAAYAAAMUeKozqyKKQ8iRohhTTjsa91DWkwAAIfkEBQcABwAsBgAAAAgABgAAAxR4RNddxhR3oDxjtHWEF9QHUtmRAAAh+QQFBwAHACwKAAIABgAIAAADFHhE12WmOCjbaviMgYUXzQc225EAACH5BAUHAAcALAoABgAGAAgAAAMTeEqkV8YUBaVjagwnulDel21KAgAh+QQFBwAHACwGAAoACAAGAAADFHiqRKvFmHLGOHGKLU47XPdYz5EAADs=',

    /**
     * 将参数字符串 str 还原为对象
     * @param {String} str 参数字符串
     * @return {Object} 参数对象
     */
    unparam(str) {
        const params = {};
        if (str) {
            if (str.substr(0, 1) === '?') {
                str = str.substr(1);
            }
            _.forEach(str.split('&'), keyValue => {
                const keyValueArr = keyValue.split('=');
                const key = keyValueArr[0];
                let value = keyValueArr[1];
                value = decodeURIComponent(value || '');
                const oldValue = params[key];
                if (oldValue == null) {
                    params[key] = value;
                } else if (Array.isArray(oldValue)) {
                    oldValue.push(value);
                } else {
                    params[key] = [oldValue, value];
                }
            });
        }
        return params;
    },

    /**
     * 将对象 obj 转换为参数字符串, 用于发送 http 请求
     * @param {Object} obj 参数键值对对象
     * @return {String} 参数字符串
     */
    param(obj) {
        obj = obj || {};
        const str = [];
        _.forEach(obj, (value, key) => {
            if (value == null) {
                return;
            }
            if (Array.isArray(value)) {
                _.forEach(value, aValue => {
                    str.push(key + '=' + encodeURIComponent(aValue));
                });
            } else {
                str.push(key + '=' + encodeURIComponent(value));
            }
        });
        return str.join('&');
    },

    bind(target, fns = []) {
        _.forEach(fns, item => {
            let fnName = null;
            let fn = null;
            if (_.isString(item) && _.isFunction(target[item])) {
                fnName = item;
                fn = target[item];
            } else if (_.isArray(item) && _.isString(item[0]) && _.isFunction(item[1])) {
                fnName = item[0];
                fn = item[1];
            }
            if (fnName && fn) {
                target[fnName] = fn.bind(target);
            }
        });
    },

    later(fn, when, periodic, context, data) {
        const self = this;
        when = when || 0;
        let m = fn;
        const d = _.toArray(data);
        let f = null;
        let r = null;
        if (typeof fn == 'string') {
            m = context[fn];
        }
        if (!m) {
            self.debug('method undefined');
        }
        f = function () {
            m.apply(context, d);
        };
        r = (periodic) ? setInterval(f, when) : setTimeout(f, when);
        return {
            id: r,
            interval: periodic,
            cancel: function () {
                if (this.interval) {
                    clearInterval(r);
                } else {
                    clearTimeout(r);
                }
            }
        };
    },

    buffer(fn, ms, context) {
        const self = this;
        ms = ms || 200;
        if (ms === -1) {
            return function () {
                fn.apply(context || this, arguments);
            };
        }
        let bufferTimer = null;
        const f = function () {
            f.stop();
            bufferTimer = self.later(fn, ms, 0, context || this, arguments);
        };
        f.stop = function () {
            if (bufferTimer) {
                bufferTimer.cancel();
                bufferTimer = 0;
            }
        };
        return f;
    },

    arrayToMap({data = [], key = 'id', label}) {
        const map = {};
        if (_.isArray(data)) {
            _.forEach(data, item => {
                if (_.isPlainObject(item)) {
                    map[item[key]] = label ? item[label] : item;
                } else {
                    map[item] = true;
                }
            });
        }
        return map;
    },

    parseToString(value) {
        if (value == null) {
            return '';
        }
        return value + '';
    },

    showLoading() {
        this.loadingCount++;
        Toast.loading(this.Const.loadingMessage, 0);
    },

    hideLoading() {
        this.loadingCount--;
        if (this.loadingCount <= 0) {
            Toast.hide();
        }
    }

};
