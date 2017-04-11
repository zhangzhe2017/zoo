/**
 * @author wenhan.wwh [xsjohn@163.com]
 * @class eachFile
 * 遍历指定文件夹中的所有文件
 */

'use strict';

//fs模块用来操作文件/文件夹
var fs = require('fs');

/**
 * @method eachFile
 * 遍历指定文件夹中的所有文件
 * @param {Object} config 方法配置对象
 * @param {String} config.dirPath 指定需要遍历文件的文件夹路径
 * @param {Function} config.handler 针对每个文件需要执行的处理函数,函数包括三个参数:fileName,filePath,dirPath
 */
var eachFile = module.exports = function (config) {
    config = config || {};
    var dirPath = config.dirPath;
    var handler = config.handler;
    var files = fs.readdirSync(dirPath);
    for (var index = 0, len = files.length; index < len; index++) {
        var fileName = files[index];
        var filePath = dirPath + '/' + fileName;
        var stat = fs.lstatSync(filePath);
        if (stat.isDirectory() == true) {
            eachFile({
                dirPath: filePath,
                handler: handler
            });
        } else {
            handler(fileName, filePath, dirPath);
        }
    }
};
