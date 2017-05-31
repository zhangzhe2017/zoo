'use strict';

import AuthActionTypes from '../../pages/Auth/AuthActionTypes';
import FormEditActionTypes from '../../pages/form/FormEdit/FormEditActionTypes';
import FEEditFormActionTypes from '../../pages/form/FormEdit/EditForm/EditFormActionTypes';
import FormDetailActionTypes from '../../pages/form/FormDetail/FormDetailActionTypes';
import FormListActionTypes from '../../pages/list/FormList/FormListActionTypes';
import RegisterSuccessActionTypes from '../../pages/result/RegisterSuccess/RegisterSuccessActionTypes';

const {_} = window._external;

const ActionTypes = {

    ...AuthActionTypes,
    ...FormEditActionTypes,
    ...FEEditFormActionTypes,
    ...FormDetailActionTypes,
    ...FormListActionTypes,
    ...RegisterSuccessActionTypes

};

_.forEach(ActionTypes, (value, key) => {
    _.assign(value, {
        changeState: `${key}.changeState`,
        replaceState: `${key}.replaceState`
    });
});

export default ActionTypes;
