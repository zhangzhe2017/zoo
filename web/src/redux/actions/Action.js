'use strict';

const {_} = window._external;

const doAction = (dispatch, actionType, actionData) => {
    if (_.isFunction(actionType)) {
        dispatch(actionType.bind(actionData));
    } else {
        dispatch({
            type: actionType,
            data: actionData
        });
    }
};

export {doAction};
