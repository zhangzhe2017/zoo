package weixin.zoo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import weixin.zoo.infrastructure.model.Template;
import weixin.zoo.infrastructure.model.TemplateField;
import weixin.zoo.infrastructure.repository.TemplateFieldRepository;
import weixin.zoo.infrastructure.repository.TemplateRepository;
import weixin.zoo.service.TemplateService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
@Service
@EnableAutoConfiguration
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

        JSONArray jsonArray = new JSONArray();
        for(String field: fieldIds){
            TemplateField templateField = templateFieldRepository.getTemplateField(Long.valueOf(field));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",templateField.getFieldName());
            jsonObject.put("type",templateField.getFieldType());
            jsonObject.put("required",templateField.getIsEmpty().equals("true"));
            jsonObject.put("label",templateField.getFieldLabel());
            jsonArray.add(jsonObject);
        }
        data.put("fields" , jsonArray);

        return data;
    }

    @Override
    public Long saveVote(String type, String name, String fields, String wxid) {
        //转换fields,逐条保存后获取id
        JSONArray ids = new JSONArray();
        JSONArray jsonArray = JSONArray.parseArray(fields);
        Iterator itr = jsonArray.iterator();
        while(itr.hasNext()){
            JSONObject jsonObject = (JSONObject)itr.next();
            Long id = templateFieldRepository.saveTemplateField(jsonObject, wxid);
            ids.add(id);
        }

        Long id = templateRepository.saveTemplate(type,name,wxid,fields);

        return id;
    }

}
