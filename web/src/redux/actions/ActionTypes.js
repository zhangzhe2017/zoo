'use strict';

import AppDataActionTypes from '../../components/AppData/AppDataActionTypes';
import FormEditActionTypes from '../../pages/form/FormEdit/FormEditActionTypes';
import FEEditFormActionTypes from '../../pages/form/FormEdit/EditForm/EditFormActionTypes';
import FormDetailActionTypes from '../../pages/form/FormDetail/FormDetailActionTypes';

const {_} = window._external;

const ActionTypes = {

    ...AppDataActionTypes,
    ...FormEditActionTypes,
    ...FEEditFormActionTypes,
    ...FormDetailActionTypes

};

_.forEach(ActionTypes, (value, key) => {
    _.assign(value, {
        changeState: `${key}.changeState`,
        replaceState: `${key}.replaceState`
    });
});

export default ActionTypes;
