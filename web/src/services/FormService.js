'use strict';

import Util from '../utils/Util';

export default {

    getForm(config) {
        return Util.ajax({
            url: '/form/getForm.json',
            ...config
        });
    }

};
