$(document).ready(function () {

    scanCardDetail.init();
});

var scanCardDetail = (function () {

    var maskPage = $(".mask"),
        cardInfoPanel = $(".base-info"),
        baseInfo = {'names': '姓名', 'titles': '职位', 'companys': '公司', 'websites': '网址'},
        contactInfo = {'emails': '邮件', 'addresses': '联系地址', 'workTels': '电话', 'faxes': '传真'},
        categoryList = [{
            key: '姓名', //显示文本
            value: 'names' //值，
        },{
            key: '职位',
            value: 'titles'
        },{
            key: '公司',
            value: 'companys'
        },{
            key: '网址',
            value: 'websites'
        },{
            key: '邮件',
            value: 'emails'
        },{
            key: '联系地址',
            value: 'addresses'
        },{
            key: '电话',
            value: 'workTels'
        },{
            key: '传真',
            value: 'faxes'
        },{
            key: '邮编',
            value: 'zip'
        }],
        isEdit = false,//编辑模式
        $baseInfoSection = $('.base-info'),
        $contactInfoSection = $('.contact-info'),
        infoItemTemplate = $("#template-info-item").html(),
        rootHttpUrl = "http://114.55.143.176",
        cardInfoObj = {},
        params = {},
        debug = true;

    var _init = function () {

            params = _parseAnchor();
            if(!params.queryid){
                isEdit = true;
                $(".btn-wrap a").eq(0).show();
                $(".btn-wrap").eq(1).hide();
            }else{
                $(".btn-wrap a").eq(0).hide();
                $(".btn-wrap a").eq(1).addClass("btn-delete").empty().append("删除");
            }

            // 初始是隐藏 添加其它项 和 取消 的
            $(".btn-wrap a").eq(2).hide();

            _initDDConfig();

            var cookiesStr = document.cookie,
                cookieArr = cookiesStr.split(";");

            for (var i = 0; i < cookieArr.length; i++) {
                var item = cookieArr[i].split("=");
                item[0] = item[0].trim();
                if (item[0] == "userid") {
                    params.userid = cookieArr[i].substr(cookieArr[i].indexOf("=") + 1);
                }
            }

            _initView();
            _bindEvents();

            if(!params.queryid) {
                isEdit = true;
                _initRightButton();
                cardInfoPanel.find("input").removeAttr("readOnly");
                cardInfoPanel.find(".info-item").addClass("edit-mode");
            }

        },
        _initView = function () {

            if (params.queryid) {
                //查询card详情
                $.ajax({
                    url: rootHttpUrl + '/card/findUserCardById',
                    type: 'get',
                    cache: false,
                    data: {
                        userId: params.userid,
                        corpid: params.corpid,
                        id: params.queryid
                    },
                    'success': function (data) {
                        console.log(data);
                        var dataObj = JSON.parse(data);
                        if(dataObj.success){
                            params.fileName = dataObj.result.fileName;
                            params.fileUrl = dataObj.result.fileServerUri;
                            cardInfoObj =  JSON.parse(dataObj.result.cardInfo);
                            _render(cardInfoObj);
                        }

                    },
                    'error': function (error) {
                        debug && console.log(error);
                    }
                });


            } else {
                //从扫秒结果跳转过来
                var tempInfo = localStorage.getItem("cardInfo", "");
                params.fileName = localStorage.getItem("fileName", "");
                params.fileUrl = localStorage.getItem("fileServerUri", "");
                cardInfoObj = JSON.parse(tempInfo);
                _render(cardInfoObj);
            }

        },
        /**
         * 渲染模板
         *
         * @param detailInfo {json} 名片信息参数
         */
        _render = function (detailInfo) {

            $('.preview-image').empty().append('<img src="' +params.fileUrl + '"/>');

            if(JSON.stringify(detailInfo)=="{}"){
                cardInfoPanel.empty().append("<div class='info-empty'>暂时没有内容</div>");
                return;
            }

            var htmlStr = "";
            for (var key in detailInfo) {
                var item = _fieldFilter(detailInfo[key],[]);
                if(item.length>0){
                    for(var j=0;j<item.length;j++){
                        var temp = infoItemTemplate.replace(/{categoryKey}/g,key)
                            .replace(/{categoryName}/g,_formatName(key))
                            .replace(/{categoryValue}/g,item[j]);
                        htmlStr += temp;
                    }

                }
            }

            cardInfoPanel.empty().append(htmlStr);


            //初始化不可编辑
            cardInfoPanel.find("input").attr("readOnly", true);
            cardInfoPanel.find(".info-item").removeClass("edit-mode");

        },
        _formatName = function(str){
          if(str == "names"){
              return "姓名";
          }else if(str == "titles"){
              return "职位";
          }else if(str == "companys"){
              return "公司";
          }else if(str == "websites"){
              return "网址";
          }else if(str == "emails"){
              return "邮件";
          }else if(str == "addresses"){
              return "联系地址";
          }else if(str == "workTels"){
              return "电话";
          }else if(str == "faxes"){
              return "传真";
          }else if(str == "zip"){
              return "邮编";
          }else{
              return "其他";
          }
        },
        _bindEvents = function () {
            // 选择填写内容种类
            $('select').on('change', function (e) {
                $(this).closest('.detail-item').find('i').html(baseInfo[$(this).val()] || contactInfo[$(this).val()]);
                $(this).closest('.detail-item').find('input').attr('name', $(this).val());
            });

            // 清空输入域内容
            $('.delete-row').on('click', function (e) {
                $(this).parent().find('input').val('');
            });

            //添加项目
            $('.btn-wrap a').eq('0').on('click', function (e) {
                e.preventDefault();

                // maskPage.find("input").val("");
                // maskPage.show();
                // $('body').css('overflow', 'hidden');

                // 如果无数据，则关掉 info-empty ，再添加
                if (cardInfoPanel.children(".info-empty").length >= 1) {
                    cardInfoPanel.empty();
                }

                var newItemKey = "names";
                var newItem = infoItemTemplate.replace(/{categoryKey}/g, newItemKey)
                    .replace(/{categoryName}/g, _formatName(newItemKey))
                    .replace(/{categoryValue}/g, "");
                cardInfoPanel.append(newItem);

                isEdit = true;
                _initRightButton();
                cardInfoPanel.find("input").removeAttr("readOnly");
                cardInfoPanel.find(".info-item").addClass("edit-mode");

                window.scrollTo(0, document.body.scrollHeight);
            });

            //取消项目
            $('.btn-wrap a').eq('2').on('click', function (e) {
                e.preventDefault();

                //关闭编辑模式
                cardInfoPanel.find("input").attr("readOnly", true);
                cardInfoPanel.find(".info-item").removeClass("edit-mode");
                //isEdit = false;
                isEdit = !isEdit;

                _initRightButton();

                $('.btn-wrap a').eq('0').hide();
                $('.btn-wrap a').eq('1').show();
                $('.btn-wrap a').eq('2').hide();
            });

            //删除名片
            $('.btn-wrap a').eq('1').on('click', function (e) {
                e.preventDefault();

                dd.device.notification.confirm({
                    message: "是否删除名片？",
                    title: "提示",
                    buttonLabels: ['删除', '取消'],
                    onSuccess : function(result) {

                        if(result.buttonIndex==0){
                            // 确认
                            $.ajax({
                                url: rootHttpUrl + '/card/deleteUserCard.json',
                                type: 'get',
                                cache: false,
                                data: {
                                    userId: params.userid,
                                    corpid: params.corpid,
                                    id: params.queryid
                                },
                                'success': function (data) {
                                    debug &&  console.log(data);

                                    var dataObj = JSON.parse(data);
                                    if(!dataObj.success){
                                        _showToast(dataObj.failedInfo);
                                        return;
                                    }

                                    _showToast("删除成功");

                                    localStorage.setItem("needRefresh", true);
                                    dd.biz.navigation.close();

                                },
                                'error': function (error) {
                                    debug && console.log(error);
                                }
                            });
                        }
                    },
                    onFail : function(err) {}
                });

            });

            maskPage.delegate('.btn-add-item','click', function (e) {
                e.preventDefault();

                var itemKey = maskPage.find("input").attr("name"),
                    keyName = _formatName(itemKey),
                    itemValue = maskPage.find("input").val();

                if(itemValue.trim().length==0){
                    //输入不能为空
                    _showToast("输入不能为空");
                    return;
                }

                var tempStr = infoItemTemplate.replace(/{categoryKey}/g,itemKey)
                    .replace(/{categoryName}/g,keyName)
                    .replace(/{categoryValue}/g,itemValue);

                cardInfoPanel.append(tempStr);
                cardInfoPanel.find(".info-empty").remove();

                maskPage.hide();
                $('body').css('overflow', 'auto');

                isEdit = true;
                _initRightButton();
                cardInfoPanel.find("input").removeAttr("readOnly");
                cardInfoPanel.find(".info-item").addClass("edit-mode");

            }).delegate('.btn-cancel-item','click', function (e) {
                e.preventDefault();
                maskPage.hide();
                $('body').css('overflow', 'auto');
            });


            $(".preview-image").on("click",function(e){
                e.preventDefault();
                var imgUrl = $(this).find("img").attr("src");
                dd.biz.util.previewImage({
                    urls: [imgUrl],//图片地址列表
                    current: imgUrl,//当前显示的图片链接
                    onSuccess : function(result) {
                        /**/
                    },
                    onFail : function() {}
                })
            });

            cardInfoPanel.delegate(".info-category","click",function(e){
                e.preventDefault();
                var item = $(this);

                if(item.parent().hasClass("edit-mode")){
                    dd.biz.util.chosen({
                        source:categoryList,
                        onSuccess : function(result) {
                            var categoryName = result.key,
                                categoryKey= result.value;
                            item.attr("data-key",categoryKey);
                            item.empty().append(categoryName+'<span class="iconfont">&#xe600;</span>');
                        },
                        onFail : function() {}
                    });
                }


            }).delegate(".btn-delete","click",function(e){
                e.preventDefault();
                var item = $(this);
                dd.device.notification.confirm({
                    message: "是否删除该项？",
                    title: "提示",
                    buttonLabels: ['删除', '取消'],
                    onSuccess : function(result) {

                        if(result.buttonIndex==0){

                            item.parent().remove();
                            // 删完显示 暂无
                            if (cardInfoPanel.children().length <= 0) {
                                cardInfoPanel.empty().append("<div class='info-empty'>暂时没有内容</div>");
                            }
                        }
                    },
                    onFail : function(err) {}
                });
            });
        },

        /**
         *
         * @param name 选中的option
         * @param infoObj
         * @returns {string}
         */
        _getOptionsTpl = function (name, infoObj) {
            var optionsTpl = '';
            for (var key in infoObj) {
                optionsTpl += '<option value="' + key + '"' + (key === name ? 'selected' : '') + '>' + infoObj[key] + '</option>';
            }
            return optionsTpl;
        },

        /**
         * @param name 传参关键字
         * @param value 参数值
         * @param type 信息类型
         * @returns {string} 模板
         */
        getItemTpl = function (name, value, type) {
            var itemTpl = '<div class="detail-item">' +
                '<div class="select-category" select-disabled="true">' +
                '<select disabled>' +
                '<optgroup label="选择类别">' +
                (type === 'base_info' ? _getOptionsTpl(name, baseInfo) : _getOptionsTpl(name, contactInfo)) +
                '</optgroup>' +
                '</select>' +
                '</div>' +
                '<div class="underline-wrap">' +
                '<input type="text" name="' + name + '" value="' + value + '" maxlength="128" required="required" disabled>' +
                '<span class="delete-row"></span>' +
                '<label class="underline"><i>' + (type === 'base_info' ? baseInfo[name] : contactInfo[name]) + '</i></label>' +
                '</div>' +
                '</div>';
            return itemTpl;
        },
        _fieldFilter = function(str,defaultStr){
            return str== undefined || str==null || str==""? defaultStr:str;
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
    //保存信息
        _doSave = function () {

            var baseInfoList = cardInfoPanel.find(".info-item");
            var tempObj = {};
            for(var i =0;i<baseInfoList.length;i++){
                var item  = baseInfoList.eq(i);
                var key = item.find(".info-category").attr("data-key"),
                    keyValue = item.find("input").val().trim();
                if (keyValue.length > 0) {
                    if (tempObj[key]) {
                        var array = tempObj[key];
                        array[array.length] = keyValue;
                        tempObj[key] = array;

                    } else {
                        tempObj[key] = [keyValue];
                    }
                } else {
                    item.remove();
                }
            }

            var requestData = {
                cardInfo: tempObj,
                fileName: params.fileName,
                fileServerUri: params.fileUrl
            };

            if(params.queryid){
                requestData.id = parseInt(params.queryid);
            }else{
                //requestData.id="";
            }

            debug && console.log(JSON.stringify(requestData));
            dd.device.notification.showPreloader({
                text: "使劲加载中..", //loading显示的字符，空表示不显示文字
                showIcon: true, //是否显示icon，默认true
                onSuccess : function(result) {
                    /*{}*/
                },
                onFail : function(err) {}
            });

            $.ajax({
                url: rootHttpUrl + '/card/saveOrUpdateUserCard.json',
                type: 'get',
                cache: false,
                data: {
                    userId: params.userid,
                    corpid: params.corpid,
                    usercard: JSON.stringify(requestData)
                },
                'success': function (data) {
                    debug &&  console.log(data);
                    dd.device.notification.hidePreloader();

                    var dataObj = JSON.parse(data);
                    if(!dataObj.success){
                        _showToast(dataObj.failedInfo);
                        return;
                    }

                    _showToast("保存成功");
                    //关闭编辑模式
                    isEdit = false;
                    cardInfoPanel.find("input").attr("readOnly",true);
                    cardInfoPanel.find(".info-item").removeClass("edit-mode");
                    _initRightButton();

                    localStorage.setItem("needRefresh", true);
                    dd.biz.navigation.close();

                },
                'error': function (error) {
                    debug && console.log(error);
                    dd.device.notification.hidePreloader();
                }
            });
        },
        _showToast = function(str){
            dd.device.notification.toast({
                icon: '', //icon样式，有success和error，默认为空 0.0.2
                text: str, //提示信息
                duration: 1, //显示持续时间，单位秒，默认按系统规范[android只有两种(<=2s >2s)]
                delay: 0, //延迟显示，单位秒，默认0
                onSuccess : function(result) {
                    /*{}*/
                },
                onFail : function(err) {}
            })
        },
        _initDDConfig = function () {
            // 1. 从URL中获取corpid
            $.ajax({
                url: 'http://114.55.143.176/getAuthConfig',
                type: 'get',
                cache: false,
                data: {
                    'corpid': params.corpid,
                    'appid': 1570,
                    'sign_url':location.href
                },
                'success': function (data) {

                    var obj = JSON.parse(data);
                    dd.config({
                        agentId: obj.agentid, // 必填，微应用ID
                        corpId: params.corpid,//必填，企业ID
                        timeStamp: obj.timeStamp, // 必填，生成签名的时间戳
                        signature: obj.signature, // 必填，签名
                        nonceStr: obj.nonceStr,
                        jsApiList: ['device.notification.alert', 'device.notification.confirm', 'biz.util.previewImage']  // 必填，需要使用的jsapi列表
                    });
                    dd.ready(function () {

                        _initRightButton();

                        dd.ui.webViewBounce.disable();
                        //下拉刷新页面
                        dd.ui.pullToRefresh.disable();

                    });

                    dd.error(function (err) {
                        console.log(err);
                        _initRightButton();
                    });

                },
                'error': function (error) {
                    console.log(error);
                }
            });
        },
        _initRightButton = function(){
            //dd环境ready，初始化页面
            var title = "编辑"
            if(isEdit){
                title = "保存";
            }

            dd.biz.navigation.setRight({
                show: true,//控制按钮显示， true 显示， false 隐藏， 默认true
                control: true,//是否控制点击事件，true 控制，false 不控制， 默认false
                text: title,//控制显示文本，空字符串表示显示默认文本
                onSuccess: function (result) {
                    //如果control为true，则onSuccess将在发生按钮点击事件被回调

                    if(!isEdit){
                        //开启编辑模式

                        // 打开 添加其它项 , 关闭 删除  按钮
                        $(".btn-wrap a").eq(0).show();
                        $(".btn-wrap a").eq(1).hide();
                        $(".btn-wrap a").eq(2).show();

                        cardInfoPanel.find("input").removeAttr("readOnly");
                        cardInfoPanel.find(".info-item").addClass("edit-mode");
                        //isEdit = true;
                        isEdit = !isEdit;

                    }else{
                        //保存
                        _doSave();

                        // 关闭 添加其它项 , 打开 删除  按钮
                        $(".btn-wrap a").eq(0).hide();
                        $(".btn-wrap a").eq(1).show();
                        $(".btn-wrap a").eq(2).hide();
                    }

                    _initRightButton();

                },
                onFail: function (err) {
                }
            });
        };

    return {
        init: _init
    }

})();