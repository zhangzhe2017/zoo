var dd;
$(document).ready(function () {
    //localStorage.clear();

    // alert('I am ready'+window.location.href)

    scanCardList.init();

});


var scanCardList = (function () {
    var params = {},
        container = $("#main-list"),
        rootHttpUrl = "http://114.55.143.176",
        loading = false,
        debug = false;

    var _init = function () {
            params = _parseAnchor();
            _initDDConfig();

            //$.init();
            $(".pull-to-refresh-layer").remove();
            $(".infinite-scroll-preloader").remove();

        }, _initView = function () {

            dd.device.notification.showPreloader({
                text: "使劲加载中..", //loading显示的字符，空表示不显示文字
                showIcon: true, //是否显示icon，默认true
                onSuccess : function(result) {
                    /*{}*/
                },
                onFail : function(err) {}
            });

            // 2. 获取list
            _getAndRenderList();
            _initEvent();

        },
        _getAndRenderList = function () {
            // 2.1.get Code
            dd.runtime.permission.requestAuthCode({
                corpId: params.corpid,
                onSuccess: function (result) {
                    //alert('I am onSuccess');
                    // 2.2 get LIST
                    params.code = result.code;
                    _getUserInfo();
                },
                onFail: function (err) {
                    alert('出现错误,请刷新!')
                    dd.device.notification.hidePreloader();
                }
            });
        },
        _getUserInfo = function(){

            $.ajax({
                url: rootHttpUrl+'/getUserInfo.json',
                type: 'get',
                cache: false,
                data: {
                    'code': params.code,
                    'corpid': params.corpid
                },
                success: function (obj) {

                    var data = JSON.parse(obj);
                    debug && console.log(data);
                    if(data.success){
                        params.userid = data.result;
                        document.cookie = "userid="+params.userid;
                        _queryUserCards();
                    }

                },
                error: function (error) {
                    //alert('I am list err' + JSON.stringify(error));
                    console.log('出现错误,请刷新!');
                    dd.device.notification.hidePreloader();
                }
            });

        },
        _queryUserCards = function () {
            //authCode每次进页面都重新获取，保证授权码一直有效性

            $.ajax({
                url: rootHttpUrl+'/card/queryUserCards',
                type: 'get',
                cache: false,
                data: {
                    'pagesize': 1000,
                    'enddate': 900000000000000, //new Date().getTime(),
                    'userId': params.userid,
                    'corpid': params.corpid
                },
                success: function (obj) {

                    var data = JSON.parse(obj);
                    debug && console.log(data);
                    //2.3 render page;
                    // TODO len == 0 显示无图片
                    if (data.success == true) {
                        if (data.result.length != 0) {
                            //render page
                            var htmlArr = [];

                            // alert(_browser());

                            var kContent = '';//'style="margin-left: 1rem"';
                            var kContentDiv = '';
                            if (_browser() == "Safari") {
                                kContent = 'style="margin-top: -.2rem;"';// margin-left: 1rem"';
                                kContentDiv = 'style="margin-bottom: .15rem;"';
                            }

                            for (var i = 0; i < data.result.length; i++) {
                                var curData = data.result[i];
                                var cardInfo = JSON.parse(curData.cardInfo);
                                var name = '',
                                    title = '',
                                    company = '';
                                if(cardInfo.names){
                                    name = _fieldFilter(cardInfo.names[0],"");
                                }

                                if(cardInfo.titles){
                                    title = _fieldFilter(cardInfo.titles[0],"");
                                }

                                if(cardInfo.companys){
                                    company = _fieldFilter(cardInfo.companys[0], "" );
                                }

                                htmlArr.push('<div class="sub-list-item" data-id='+curData.id +'>');
                                htmlArr.push('  <div class="item-pic"><img src="' + curData.fileServerUri + '" /></div>');
                                htmlArr.push('  <div class="item-content"' + kContent + '>');
                                // htmlArr.push('    <div class="item-label">');
                                htmlArr.push('      <div ' + kContentDiv + '>' + _fieldFilter(name,"暂无") + '</div>');
                                htmlArr.push('      <div ' + kContentDiv + '>' + _fieldFilter(title,"暂无") + '</div>');
                                htmlArr.push('      <div' + '>' + _fieldFilter(company,"暂无") + '</div>');
                                // htmlArr.push('      <div>' + _fieldFilter(name,"暂无") + '</div>');
                                // htmlArr.push('      <div>' + _fieldFilter(title,"暂无") + '</div>');
                                // htmlArr.push('      <div>' + _fieldFilter(company,"暂无") + '</div>');
                                // htmlArr.push('    </div>');
                                //htmlArr.push('    <div class="item-time">');
                                //htmlArr.push('      <p>' + _fieldFilter(time,"暂无") + '</p>');
                                //htmlArr.push('    </div>');
                                htmlArr.push('  </div>');
                                htmlArr.push('</div>');
                            }

                            $('#card-list').find(".list-empty").empty().addClass("hide");
                            $('.sub-list').empty().append(htmlArr.join(''));

                        } else {

                            $('.sub-list').empty();
                            $('#card-list').find(".list-empty").removeClass("hide").empty().append(
                                '<div class="nophoto"><img src="//gw.alicdn.com/tps/TB1DcI0KFXXXXa0XXXXXXXXXXXX-436-430.png" />'+
                                '</div>'+
                                '<p>没有名片，请点击下方按钮扫描名片</p>');
                        }
                    }

                    dd.device.notification.hidePreloader();
                },
                error: function (error) {
                    //alert('I am list err' + JSON.stringify(error));
                    //alert('出现错误,请刷新!');
                    dd.device.notification.hidePreloader();
                }
            });

        },
        _initEvent = function () {

            container.delegate(".sub-list-item","click",function(e){
                e.preventDefault();
                var item = $(this),
                    itemKey = item.attr("data-id");

                dd.biz.util.openLink({
                    url: "http://114.55.143.176/detail.html?corpid="+params.corpid+"&queryid="+itemKey+"&dd_progress=false",//要打开链接的地址
                // &dd_nav_bgcolor=ffffc309
                    onSuccess: function (result) {
                        /**/
                        debug && console.log(result);
                    },
                    onFail: function (err) {
                        debug && console.log(err)
                    }
                });

            });

            $('#photo-btn').click(function () {

                dd.biz.util.uploadImageFromCamera({
                    compression: true,//(是否压缩，默认为true)
                    onSuccess: function (result) {
                        //onSuccess将在图片上传成功之后调用
                        debug && console.log(result[0]);

                        dd.device.notification.showPreloader({
                            text: "上传照片中..", //loading显示的字符，空表示不显示文字
                            showIcon: true, //是否显示icon，默认true
                            onSuccess : function(result) {
                                /*{}*/
                            },
                            onFail : function(err) {}
                        });

                        $.ajax({
                            url: 'http://114.55.143.176/card/recognizeCard',
                            cache: false,
                            data: {
                                'corpid': params.corpid,
                                'code': params.code,
                                'picurl': result[0]
                            },
                            'success': function (obj) {
                                dd.device.notification.hidePreloader();
                                try {

                                    var data = JSON.parse(obj);
                                    debug && console.log(data);
                                    if (data.success == true) {
                                        var result = data.result;
                                        //localStorage['tmpInfo'] = data.result;
                                        localStorage.setItem("cardInfo",result.cardInfo);
                                        localStorage.setItem("fileName",result.fileName);
                                        localStorage.setItem("fileServerUri",result.fileServerUri);
                                        //window.location.href = "/detail.html?corpid="+params.corpid

                                        dd.biz.util.openLink({
                                            url: "http://114.55.143.176/detail.html?corpid="+params.corpid+"&dd_progress=false",//要打开链接的地址
                                            onSuccess: function (result) {
                                                /**/
                                                debug && console.log(result);
                                            },
                                            onFail: function (err) {
                                                debug && console.log(err)
                                            }
                                        });

                                    } else {
                                        alert('请输入正确图片')
                                    }
                                } catch (e) {
                                    alert(JSON.stringify(e))
                                }
                            },
                            error: function (err) {
                                dd.device.notification.hidePreloader();
                                alert("出现错误,请刷新!");
                            }
                        })
                    },
                    onFail: function (err) {
                        //alert(JSON.stringify(err))
                    }
                });

            });
        },
        /********************* Helpers ***************************/
        _browser = function () {
            var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
            // alert(userAgent);
            var isOpera = userAgent.indexOf("Opera") > -1;
            if (isOpera) {
                return "Opera"
            }; //判断是否Opera浏览器
            if (userAgent.indexOf("Firefox") > -1) {
                return "FF";
            } //判断是否Firefox浏览器
            if (userAgent.indexOf("Chrome") > -1){
                return "Chrome";
            }
            if (userAgent.indexOf("Safari") > -1 || userAgent.indexOf("AppleWebKit") > -1) {
                return "Safari";
            } //判断是否Safari浏览器
            if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
                return "IE";
            }; //判断是否IE浏览器
        },
        _parseAnchor = function () {
            var retParams = {};
            var href = window.location.href;

            if (!href) {
                return retParams;
            }

            var split = href.split('?');

            if (!split || split.length <= 1) {
                return retParams;
            }

            split = split[1];
            split = split.split('&');

            for (var i = 0, length = split.length; i < length; i++) {
                var param = split[i];

                if (!param) {
                    continue;
                }

                param = param.split('=');

                if (param.length != 2) {
                    continue;
                }

                retParams[param[0]] = param[1];
            }

            return retParams;
        },
        _fieldFilter = function(str,defaultStr){
            return str== undefined || str==null || str==""? defaultStr:str;
        },
        _initDDConfig = function () {
            // 1. 从URL中获取corpid
            var paramsData = {
                'corpid': params.corpid,
                'appid': 1570,
                'dd_progress':false,
                'sign_url':location.href
            };

            $.ajax({
                url: 'http://114.55.143.176/getAuthConfig',
                type: 'get',
                cache: false,
                data: paramsData,
                'success': function (data) {

                    var obj = JSON.parse(data);
                    dd.config({
                        agentId: obj.agentid, // 必填，微应用ID
                        corpId: params.corpid,//必填，企业ID
                        timeStamp: obj.timeStamp, // 必填，生成签名的时间戳
                        signature: obj.signature, // 必填，签名
                        nonceStr: obj.nonceStr,
                        jsApiList: ['device.notification.alert',
                            'device.notification.confirm', 'biz.util.uploadImageFromCamera',
                        'biz.util.openLink']  // 必填，需要使用的jsapi列表
                    });

                    dd.ready(function () {
                        //dd环境ready，初始化页面
                        _initView();

                        document.addEventListener('resume', function () {

                            //console.log("resume");
                            var needRefresh = localStorage.getItem("needRefresh");
                            //console.log(needRefresh);
                            if(needRefresh == "true"){
                                dd.device.notification.showPreloader({
                                    text: "使劲加载中..", //loading显示的字符，空表示不显示文字
                                    showIcon: true, //是否显示icon，默认true
                                    onSuccess : function(result) {
                                        /*{}*/
                                    },
                                    onFail : function(err) {}
                                });
                                _queryUserCards();

                                localStorage.setItem("needRefresh",false);
                            }

                        });

                        /*dd.biz.navigation.setRight({
                                show: false//控制按钮显示， true 显示， false 隐藏， 默认true
                        });*/

                        dd.ui.webViewBounce.enable();
                        //下拉刷新页面
                        dd.ui.pullToRefresh.disable();

                    });

                    dd.error(function (err) {
                        console.log(err);
                    });

                    //"needRefresh"

                },
                'error': function (error) {
                    console.log(error);
                }
            });
        };

    return {
        init: _init
    }

})();



