package weixin.zoo.infrastructure.repository.impl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import weixin.zoo.infrastructure.mapper.TemplateMapper;
import weixin.zoo.infrastructure.model.Template;
import weixin.zoo.infrastructure.model.TemplateExample;
import weixin.zoo.infrastructure.repository.TemplateRepository;
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
}
