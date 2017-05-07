package weixin.zoo.infrastructure.repository.impl;

import com.alibaba.fastjson.JSONObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import weixin.zoo.infrastructure.mapper.TemplateFieldMapper;
import weixin.zoo.infrastructure.model.TemplateField;
import weixin.zoo.infrastructure.model.TemplateFieldExample;
import weixin.zoo.infrastructure.repository.TemplateFieldRepository;
import java.util.Date;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
@Repository
@MapperScan("weixin.zoo.infrastructure.mapper")
public class TemplateFieldRepositoryImpl implements TemplateFieldRepository {

    @Autowired
    private TemplateFieldMapper templateFieldMapper;

    @Override
    public TemplateField getTemplateField(long id) {

        TemplateFieldExample templateFieldExample = new TemplateFieldExample();
        templateFieldExample.createCriteria().andIdEqualTo(id).andIsDeleteEqualTo("n");

        List<TemplateField> TemplateFields = templateFieldMapper.selectByExample(templateFieldExample);
        if(TemplateFields.isEmpty())
            return null;

        return TemplateFields.get(0);
    }

    @Override
    public long saveTemplateField(JSONObject fieldJson) {
        TemplateField templateField = transferTemplateField(fieldJson);
        return templateFieldMapper.insertSelective(templateField);
    }

    private TemplateField transferTemplateField(JSONObject fieldJson){
        TemplateField templateField = new TemplateField();
        templateField.setGmtCreate(new Date());
        templateField.setGmtModified(new Date());
        templateField.setIsDelete("n");
        templateField.setIsEmpty("n");
        templateField.setFieldName(fieldJson.getString("fieldName"));
        templateField.setFieldOption(fieldJson.getString("fieldOption"));
        templateField.setFieldType(fieldJson.getString("fieldType"));
        return templateField;
    }
}
