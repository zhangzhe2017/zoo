package weixin.zoo.service;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by viczhang.zhangz on 2017/6/17.
 */
public interface CommonService {

    /*
     * 图片转换功能，将微信服务器中的mediaId资源转换为oss中的http地址
     */
    public JSONArray transferPics(JSONArray picids);
}
