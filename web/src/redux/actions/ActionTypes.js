'use strict';

import AppDataActionTypes from '../../components/AppData/AppDataActionTypes';
import FormEditActionTypes from '../../pages/form/FormEdit/FormEditActionTypes';

const {_} = window._external;

const ActionTypes = {

    ...AppDataActionTypes,
    ...FormEditActionTypes

};

_.forEach(ActionTypes, (value, key) => {
    _.assign(value, {
        changeState: `${key}.changeState`,
        replaceState: `${key}.replaceState`
    });
});

export default ActionTypes;
