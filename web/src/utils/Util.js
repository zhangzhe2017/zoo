'use strict';

import config from '../../webpackConfig.json';
import Base from './Base';
import Widget from './Widget';
import Ajax from './Ajax';
import Tree from './Tree';
import Log from './Log';
import Const from './Const';
import Task from './Task';

export default {

    pageParams: Base.unparam(location.search),
    isDebug() {
        return this.isProxy() || location.pathname === '/index-debug.html';
    },
    isProxy() {
        return location.port === config.devServerPort;
    },
    ...Base,
    ...Widget,
    ...Ajax,
    ...Tree,
    ...Log,
    ...Const,
    ...Task

};
