'use strict';

import AppDataActionTypes from '../../components/AppData/AppDataActionTypes';

const {_} = window._external;

const ActionTypes = {

    ...AppDataActionTypes

};

_.forEach(ActionTypes, (value, key) => {
    _.assign(value, {
        changeState: `${key}.changeState`,
        replaceState: `${key}.replaceState`
    });
});

export default ActionTypes;
