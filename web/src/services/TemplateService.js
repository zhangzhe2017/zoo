'use strict';

import Util from '../utils/Util';

export default {

    getTemplate(config) {
        return Util.ajax({
            url: '/template/getTemplate.json',
            ...config
        });
    }

};
