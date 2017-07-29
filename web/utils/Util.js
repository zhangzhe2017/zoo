!function () {
    'use strict';
    var UA = window.navigator.userAgent;
    var isIOS = function () {
        return (/iPhone|iPad|iPod/i).test(UA);
    };
    var isAndroid = function () {
        return (/Android/i).test(UA);
    };
    var isPhone = function () {
        return isIOS() || isAndroid();
    };
    window._Util = {
        isPC: function () {
            return !isPhone();
        }
    };
}();
