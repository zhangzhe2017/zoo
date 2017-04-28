'use strict';

import {Toast, List, InputItem, TextareaItem, ImagePicker, Button} from 'antd-mobile';
import $ from 'jquery';
import assign from 'lodash/assign';
/*import cloneDeep from 'lodash/cloneDeep';
import compact from 'lodash/compact';
import concat from 'lodash/concat';
import difference from 'lodash/difference';
import drop from 'lodash/drop';
import escape from 'lodash/escape';
import flatten from 'lodash/flatten';*/
import forEach from 'lodash/forEach';
//import includes from 'lodash/includes';
import indexOf from 'lodash/indexOf';
import isArray from 'lodash/isArray';
//import isEmpty from 'lodash/isEmpty';
import isFunction from 'lodash/isFunction';
//import isNumber from 'lodash/isNumber';
import isPlainObject from 'lodash/isPlainObject';
import isString from 'lodash/isString';
import isUndefined from 'lodash/isUndefined';
/*import keys from 'lodash/keys';
import map from 'lodash/map';
import noop from 'lodash/noop';
import reduce from 'lodash/reduce';
import remove from 'lodash/remove';
import size from 'lodash/size';
import slice from 'lodash/slice';
import sortBy from 'lodash/sortBy';
import startsWith from 'lodash/startsWith';*/
import toArray from 'lodash/toArray';
/*import trim from 'lodash/trim';
import uniq from 'lodash/uniq';
import uniqueId from 'lodash/uniqueId';
import values from 'lodash/values';*/
//import moment from 'moment';
import React, {Component, PropTypes} from 'react';
import {render} from 'react-dom';
import {Provider, connect} from 'react-redux';
import {hashHistory, Link, Router, Route, IndexRedirect} from 'react-router';
import {syncHistoryWithStore, routerReducer} from 'react-router-redux';
import {createStore, combineReducers, applyMiddleware} from 'redux';
import thunk from 'redux-thunk';
import reactMixin from 'react-mixin';

window._external = {
    //antd-mobile
    Toast, List, InputItem, TextareaItem, ImagePicker, Button,
    //jquery
    $,
    //lodash
    _: {assign, forEach, indexOf, isArray, isFunction, isPlainObject, isString, isUndefined, toArray},
    //moment
    //moment,
    //react
    React, Component, PropTypes,
    //react-dom
    render,
    //react-redux
    Provider, connect,
    //react-router
    hashHistory, Link, Router, Route, IndexRedirect,
    //react-router-redux
    syncHistoryWithStore, routerReducer,
    //redux
    createStore, combineReducers, applyMiddleware,
    //redux-thunk
    thunk,
    //react-mixin
    reactMixin
};
