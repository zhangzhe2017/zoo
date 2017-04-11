'use strict';

const {_} = window._external;

var TaskRunner = function (interval) {
    interval = interval || 10;
    var tasks = [],
        removeQueue = [],
        id = 0,
        running = false,
        stopThread = function () {
            running = false;
            clearInterval(id);
            id = 0;
        },
        startThread = function () {
            if (!running) {
                running = true;
                id = setInterval(runTasks, interval);
            }
        },
        removeTask = function (t) {
            removeQueue.push(t);
            if (t.onStop) {
                t.onStop.apply(t.scope || t);
            }
        },
        runTasks = function () {
            var rqLen = removeQueue.length,
                now = new Date().getTime();
            var i = null;
            if (rqLen > 0) {
                for (i = 0; i < rqLen; i++) {
                    //tasks.remove(removeQueue[i]);
                    tasks.splice(_.indexOf(tasks, removeQueue[i]), 1);
                }
                removeQueue = [];
                if (tasks.length < 1) {
                    stopThread();
                    return;
                }
            }
            var t, itime, rt, len = tasks.length;
            for (i = 0; i < len; ++i) {
                t = tasks[i];
                itime = now - t.taskRunTime;
                if (t.interval <= itime) {
                    rt = t.run.apply(t.scope || t, t.args || [++t.taskRunCount]);
                    t.taskRunTime = now;
                    if (rt === false || t.taskRunCount === t.repeat) {
                        removeTask(t);
                        return;
                    }
                }
                if (t.duration && t.duration <= (now - t.taskStartTime)) {
                    removeTask(t);
                }
            }
        };
    this.start = function (task) {
        tasks.push(task);
        task.taskStartTime = new Date().getTime();
        task.taskRunTime = 0;
        task.taskRunCount = 0;
        startThread();
        return task;
    };
    this.stop = function (task) {
        removeTask(task);
        return task;
    };
    this.stopAll = function () {
        stopThread();
        for (var i = 0, len = tasks.length; i < len; i++) {
            if (tasks[i].onStop) {
                tasks[i].onStop();
            }
        }
        tasks = [];
        removeQueue = [];
    };
};
var TaskMgr = new TaskRunner();
var Task = {
    createTask: function (config) {
        var self = this;
        config = config || {};
        if (_.isUndefined(config.interval)) {
            config.interval = 100;
        }
        if (_.isUndefined(config.timeout)) {
            config.timeout = 30000;
        }
        var task = config.task;
        var handler = config.handler;
        if (task && _.isFunction(task) && handler && _.isFunction(handler)) {
            var interval = config.interval;
            var timeout = config.timeout;
            var scope = config.scope;
            var now = new Date().getTime();
            var monitorTask = TaskMgr.start({
                run: function () {
                    var handler = this.handler;
                    var task = this.task;
                    var monitorTask = this.monitorTask;
                    var scope = this.scope || monitorTask;
                    var timeout = this.timeout;
                    var now = this.now;
                    var result;
                    try {
                        result = handler.call(scope);
                    } catch (e) {
                        TaskMgr.stop(monitorTask);
                        delete monitorTask.scope.monitorTask;
                        self.debug('轮询函数执行出错！');
                        return;
                    }
                    if (result) {
                        TaskMgr.stop(monitorTask);
                        delete monitorTask.scope.monitorTask;
                        task.call(scope);
                    } else if (timeout > 0 && new Date().getTime() - now > timeout) {
                        TaskMgr.stop(monitorTask);
                        delete monitorTask.scope.monitorTask;
                    }
                },
                scope: {handler: handler, task: task, scope: scope, timeout: timeout, now: now},
                interval: interval
            });
            monitorTask.scope.monitorTask = monitorTask;
            return monitorTask;
        }
    }
};
export default Task;
