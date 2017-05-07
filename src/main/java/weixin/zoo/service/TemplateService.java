package weixin.zoo.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
public interface TemplateService {
    /*
     * 根据模板id获取模板信息
     */
    public JSONObject getTemplate(long id);
}
