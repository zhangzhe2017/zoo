# zoo接口列表
1. 获取应用信息的接口
    * 接口功能描述: 网页授权, 获取用户信息并保存到数据库, 获取jssdk签名, 判断用户是否关注了公众号
    * 接口地址: `/app/getAppData.json`
    * 参数
        * code: 用来获取授权access_token和openid等信息
    * 返回的json格式
        <pre>
        {
            "success": true,
            "message": "",
            "data": {
                "timestamp": 0,//jssdk签名时间戳
                "nonceStr": "",//jssdk签名随机字符串
                "signature": "",//jssdk签名
                "attention": false,//是否已关注
                "wxid": 当前用户id
            }
        }
        </pre>
2. 获取模板信息的接口, 用来渲染自定义表单页面
    * 接口功能描述: 根据模板id, 获取到字段列表, 每个字段包含字段标签, 字段名, 字段类型(input/number/textarea/datetime/image)等信息
    * 接口地址: `/template/getTemplate.json`
    * 参数
        * id: 模板id
    * 返回的json格式
        <pre>
        {
            "success": true,
            "message": "",
            "data": {
                "type": "",//模板类型, 目前只有活动, activity
                "title": "",//模板标题, 目前只有活动, 发起活动
                "fields": []//字段信息
            }
        }
        </pre>
3. 保存自定义表单的接口
    * 接口功能描述: 把用户填写的字段值都保存起来
    * 接口地址: `/form/saveForm.json`
    * 参数
        * templateId: 模板id
        * fieldValues: "{field1: value1, field2: value2, ...}"//其他字段值
    * 返回的json格式
        <pre>
        {
            "success": true,
            "message": "",
            "data": {
                "id": 0//保存之后返回的表单id
            }
        }
        </pre>
4. 获取自定义表单的接口
    * 接口功能描述: 获取表单数据, 渲染查看详情的页面
    * 接口地址: `/form/getForm.json`
    * 参数
        * id: 表单id
    * 返回的json格式
        <pre>
        {
            "success": true,
            "message": "",
            "data": {
                "type": "",//表单类型, 即模板类型
                "title": "",//表单标题, 即模板标题
                "fields": [],//字段信息, 每个字段包含字段标签, 字段名, 字段类型(input/number/textarea/datetime/image)等信息
                "fieldValues": "{field1: value1, field2: value2, ...}",//字段值
                "registered": false,//如果type为activity, 则需要这个标识告诉我当前用户是否已报名
                "creatorNickName": 活动发起者昵称,
                "creatorWxid": 活动发起者wxid,
                "attenderList": 参加者昵称名单,
                "timestamp": 服务器时间
            }
        }
        </pre>
5. 报名/取消报名的接口
    * 接口功能描述: 查看详情的页面, 点击报名按钮进行报名
    * 接口地址: `/form/register.json`
    * 参数
        * id: 表单id
        * register: true为报名, false为取消报名
    * 返回的json格式
        <pre>
        {
            "success": true,
            "message": "",
            "data": {
                "qrCodeResult": "..."
            }
        }
        </pre>
6. 我发起的活动列表
    * 接口功能描述: 获取我发起的活动列表
    * 接口地址: `/form/getMyFormList.json`
    * 参数
        * currentPage: 1//第几页
        * pageSize: 10//每页多少条数据
    * 返回的json格式
        <pre>
        {
            "success": true,
            "message": "",
            "data": [
                {
                    "id": 0,
                    "title": "一个爬山活动",
                    "type": "activity"
                    ...
                },
                ...
            ]
        }
        </pre>
7. 我参加的活动列表
    * 接口功能描述: 获取我参加的活动列表
    * 接口地址: `/form/getAttendedActivityList.json`
    * 参数
        * currentPage: 1//第几页
        * pageSize: 10//每页多少条数据
    * 返回的json格式
        <pre>
        {
            "success": true,
            "message": "",
            "data": [
                {
                    "id": 0,
                    "title": "一个爬山活动",
                    "type": "activity"
                    ...
                },
                ...
            ]
        }
        </pre>
