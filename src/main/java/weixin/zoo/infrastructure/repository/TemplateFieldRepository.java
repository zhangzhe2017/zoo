package weixin.zoo.infrastructure.repository;

import com.alibaba.fastjson.JSONObject;
import weixin.zoo.infrastructure.model.TemplateField;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
public interface TemplateFieldRepository {

    /*
     * 根据id获取form field字段详情
     */
    public TemplateField getTemplateField(long id);

    /*
     * 保存模板字段
     */
    public long saveTemplateField(JSONObject fieldJson);
}
