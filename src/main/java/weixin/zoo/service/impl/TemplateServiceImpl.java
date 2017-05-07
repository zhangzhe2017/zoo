package weixin.zoo.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import weixin.zoo.infrastructure.model.Template;
import weixin.zoo.infrastructure.model.TemplateField;
import weixin.zoo.infrastructure.repository.TemplateFieldRepository;
import weixin.zoo.infrastructure.repository.TemplateRepository;
import weixin.zoo.service.TemplateService;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TemplateFieldRepository templateFieldRepository;

    /*
     * 根据模板id获取模板数据
     */
    @Override
    public JSONObject getTemplate(long id) {
        //获取模板基础信息
        Template template = templateRepository.getTemplateById(id);

        //根据fields获取详细数据
        String[] fieldIds = template.getFiledIds().split(",");

        JSONObject data = new JSONObject();
        data.put("type",template.getTemplateType());
        data.put("title",template.getTemplateName());

        JSONObject fields = new JSONObject();
        for(String field: fieldIds){
            TemplateField templateField = templateFieldRepository.getTemplateField(Long.valueOf(field));
            fields.put(templateField.getFieldName(),templateField.getFieldType());
        }
        data.put("fields" , fields);

        return data;
    }
}
