'use strict';

var config = require('./webpackConfig.json');

var devServerPort = config.devServerPort;
var host = `http://localhost:${devServerPort}`;

var path = require('path');
var _ = require('lodash');

var webpack = require('webpack');
var HtmlPlugin = require('html-webpack-plugin');
var OpenBrowserPlugin = require('open-browser-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var autoprefixer = require('autoprefixer');
var pxtorem = require('postcss-pxtorem');

var vendorExtractor = new ExtractTextPlugin('css/vendor.css');
var indexExtractor = new ExtractTextPlugin('css/index.css');

var getPath = function (aPath) {
    return path.resolve(__dirname, aPath);
};

var entry = {
    vendor: getPath('src/entries/vendor.js'),
    index: getPath('src/entries/index.js')
};

var keys = _.keys(entry);

var proxy = {};
var eachFile = require('./utils/eachFile');
eachFile({
    dirPath: 'json',
    handler: function (fileName, filePath, dirPath) {
        proxy[filePath.substr(4)] = {
            target: host,
            changeOrigin: true,
            pathRewrite: function (path, request) {
                return `/${filePath}`;
            }
        };
    }
});

var isDev = process.env.NODE_ENV !== 'production';

var nodeModulesPath = /node_modules/;
var srcPath = getPath('src');

module.exports = {
    entry: entry,
    output: {
        path: getPath('../src/main/resources/static'),
        filename: 'js/[name].js',
        publicPath: '/'
    },
    devtool: isDev ? 'source-map' : false,
    resolve: {
        extensions: ['.web.js', '.js', '.json']
    },
    module: {
        rules: [
            /*{
                test: /\.swf$/,
                include: [srcPath],
                loader: 'file-loader',
                options: {
                    name: 'swf/[name].[hash:8].[ext]'
                }
            },
            {
                test: /\.(png|jpg|gif)$/,
                include: [srcPath],
                loader: 'url-loader',
                options: {
                    limit: 10240,
                    name: 'images/[name].[hash:8].[ext]'
                }
            },*/
            {
                test: /\.css$/,
                include: [nodeModulesPath],
                loader: vendorExtractor.extract({
                    fallback: 'style-loader',
                    use: [
                        {
                            loader: 'css-loader',
                            options: {
                                sourceMap: isDev
                            }
                        }
                    ]
                })
            },
            {
                test: /\.scss$/,
                include: [srcPath],
                loader: indexExtractor.extract({
                    fallback: 'style-loader',
                    use: [
                        {
                            loader: 'css-loader',
                            options: {
                                modules: true,
                                localIdentName: '[name]__[local]-[hash:8]',
                                sourceMap: isDev
                            }
                        },
                        {
                            loader: 'postcss-loader',
                            options: {
                                sourceMap: isDev
                            }
                        },
                        {
                            loader: 'sass-loader',
                            options: {
                                sourceMap: isDev
                            }
                        }
                    ]
                })
            },
            {
                test: /\.js$/,
                include: [srcPath],
                loader: 'babel-loader',
                options: {
                    presets: ['es2015', 'react', 'stage-0'],
                    plugins: [
                        [
                            'import',
                            {style: 'css', libraryName: 'antd-mobile'}
                        ],
                        'transform-decorators-legacy',
                        'transform-object-assign'
                    ]
                }
            },
            {
                test: /\.svg$/,
                include: [nodeModulesPath],
                loader: 'svg-sprite-loader'
            }
        ]
    },
    plugins: [
        new HtmlPlugin({
            hash: true,
            chunksSortMode: function (a, b) {
                var aName = a.names[0];
                var bName = b.names[0];
                var aNameIndex = _.indexOf(keys, aName);
                var bNameIndex = _.indexOf(keys, bName);
                return aNameIndex > bNameIndex ? 1 : (aNameIndex < bNameIndex ? -1 : 0);
            },
            template: getPath('src/index.html')
        }),
        new HtmlPlugin({
            inject: false,
            template: getPath('src/index-debug.html'),
            filename: 'index-debug.html'
        }),
        new OpenBrowserPlugin({url: `${host}/index-debug.html`}),
        vendorExtractor,
        indexExtractor,
        new webpack.LoaderOptionsPlugin({
            test: /\.scss$/,
            options: {
                postcss: [
                    autoprefixer(),
                    pxtorem({
                        rootValue: 100,
                        unitPrecision: 5,
                        propList: ['*'],
                        selectorBlackList: [],
                        replace: true,
                        mediaQuery: false,
                        minPixelValue: 0
                    })
                ]
            }
        })
    ],
    devServer: {
        port: devServerPort,
        proxy: proxy,
        headers: {'Access-Control-Allow-Origin': '*'}
    }
};
