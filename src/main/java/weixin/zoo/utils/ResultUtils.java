package weixin.zoo.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by viczhang.zhangz on 2017/4/28.
 */
public class ResultUtils {

    public static String assembleResult(boolean success, String message, Object data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", success);
        jsonObject.put("message", message);
        jsonObject.put("data", data);
        return jsonObject.toString();
    }
}
