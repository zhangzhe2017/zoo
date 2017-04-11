'use strict';

import Util from '../utils/Util';

export default {

    getAppData(config) {
        return Util.ajax({
            url: '/app/getAppData.json',
            ...config
        });
    }

};
