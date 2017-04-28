package weixin.zoo.service;

import com.alibaba.fastjson.JSONObject;
import weixin.zoo.utils.*;
/**
 * Created by viczhang.zhangz on 2017/4/28.
 */
public interface UserService {
    public JSONObject getUserInfo(String code);
    public JSONObject getJsapiSignatrue(String url) throws Exception;
}
