'use strict';

import ClassMap from './ClassMap';
import ActionTypes from '../actions/ActionTypes';

const {_} = window._external;

const Reducers = {};

_.forEach(ClassMap, (value, key) => {
    Reducers[key] = (state = value.defaultState || {}, action) => {
        const actionType = action.type;
        const actionData = action.data || {};
        if (actionType === ActionTypes[key].changeState) {
            state = {
                ...state,
                ...actionData
            };
        } else if (actionType === ActionTypes[key].replaceState) {
            state = {
                ...actionData
            };
        }
        return state;
    };
});

export default Reducers;
