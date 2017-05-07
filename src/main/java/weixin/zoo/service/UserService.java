package weixin.zoo.service;

import com.alibaba.fastjson.JSONObject;
import weixin.zoo.utils.*;
/**
 * Created by viczhang.zhangz on 2017/4/28.
 *
 * 微信接口类，获取用户信息以及js签名
 *
 */
public interface UserService {

    /*
     * 通过code换取微信用户信息
     */
    public JSONObject getUserInfo(String code);

    /*
     * jsapi签名
     */
    public JSONObject getJsapiSignatrue(String url) throws Exception;
}
