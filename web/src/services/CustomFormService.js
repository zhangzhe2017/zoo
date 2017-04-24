'use strict';

import Util from '../utils/Util';

export default {

    getCustomForm(config) {
        return Util.ajax({
            url: '/customForm/getCustomForm.json',
            ...config
        });
    }

};
