'use strict';

import Util from '../utils/Util';

const {$, React, Toast} = window._external;

export default {

    ajax(config) {
        const {
            success,
            error,
            complete,
            showLoading = true,
            showSuccessMessage = false,
            showErrorMessage = true,
            successMessage = '请求成功',
            errorMessage = '请求失败',
        } = config;
        config = {
            type: 'post',
            dataType: 'json',
            traditional: true,
            cache: false,
            ...config,
            success(result = {}, textStatus, jqXHR) {
                if (result.success) {
                    showSuccessMessage && Toast.success(result.message || successMessage);
                    success && success.apply(this, arguments);
                } else {
                    showErrorMessage && Toast.fail(result.message || errorMessage);
                    error && error.apply(this, [jqXHR, textStatus]);
                }
            },
            error(jqXHR, textStatus, errorThrown) {
                if (textStatus !== 'abort' && showErrorMessage) {
                    const result = jqXHR.responseJSON || {};
                    if (result.success === false) {
                        Toast.fail(result.message || errorMessage);
                    } else {
                        Toast.fail(errorThrown || errorMessage);
                    }
                }
                error && error.apply(this, arguments);
            },
            complete(jqXHR, textStatus) {
                if (showLoading) {
                    Util.hideLoading();
                }
                complete && complete.apply(this, arguments);
            },
            ...(this.isProxy() ? {type: 'get'} : {})
        };
        if (showLoading) {
            Util.showLoading();
        }
        return $.ajax(config);
    }

};
