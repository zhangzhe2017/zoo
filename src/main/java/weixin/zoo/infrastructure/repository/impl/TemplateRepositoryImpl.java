package weixin.zoo.infrastructure.repository.impl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import weixin.zoo.infrastructure.mapper.TemplateMapper;
import weixin.zoo.infrastructure.model.Template;
import weixin.zoo.infrastructure.model.TemplateExample;
import weixin.zoo.infrastructure.repository.TemplateRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
@Repository
@MapperScan("weixin.zoo.infrastructure.mapper")
public class TemplateRepositoryImpl implements TemplateRepository {

    @Autowired
    private TemplateMapper templateMapper;

    @Override
    public Template getTemplateById(long id) {
        TemplateExample templateExample = new TemplateExample();
        templateExample.createCriteria().andIdEqualTo(id).andIsDeleteEqualTo("n");

        List<Template> templates = templateMapper.selectByExample(templateExample);
        if(templates.isEmpty())
            return null;

        return templates.get(0);
    }

    @Override
    public Long saveTemplate(String type, String name, String owner, String fields) {
        Template template = new Template();
        template.setGmtModified(new Date());
        template.setFiledIds(fields);
        template.setGmtCreate(new Date());
        template.setIsDelete("n");
        template.setTemplateName(name);
        template.setTemplateType(type);
        template.setTemplateOwner(owner);

        long num = templateMapper.insertSelective(template);
        if(num > 0){
            TemplateExample templateExample = new TemplateExample();
            templateExample.createCriteria().andIsDeleteEqualTo("n").andFiledIdsEqualTo(fields).andTemplateNameEqualTo(name)
                    .andTemplateTypeEqualTo(type).andTemplateOwnerEqualTo(owner);

            List<Template> templates = templateMapper.selectByExample(templateExample);
            Template templateOk = templates.get(0);

            return templateOk.getId();
        }
        return 0l;
    }
}
