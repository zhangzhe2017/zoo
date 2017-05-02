'use strict';

import AppDataActionTypes from '../../components/AppData/AppDataActionTypes';
import FormEditActionTypes from '../../pages/form/FormEdit/FormEditActionTypes';
import FEEditFormActionTypes from '../../pages/form/FormEdit/EditForm/EditFormActionTypes';
import FormDetailActionTypes from '../../pages/form/FormDetail/FormDetailActionTypes';
import FormListActionTypes from '../../pages/list/FormList/FormListActionTypes';

const {_} = window._external;

const ActionTypes = {

    ...AppDataActionTypes,
    ...FormEditActionTypes,
    ...FEEditFormActionTypes,
    ...FormDetailActionTypes,
    ...FormListActionTypes

};

_.forEach(ActionTypes, (value, key) => {
    _.assign(value, {
        changeState: `${key}.changeState`,
        replaceState: `${key}.replaceState`
    });
});

export default ActionTypes;
