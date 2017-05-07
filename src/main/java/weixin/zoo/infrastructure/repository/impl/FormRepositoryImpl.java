package weixin.zoo.infrastructure.repository.impl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import weixin.zoo.infrastructure.mapper.FormMapper;
import weixin.zoo.infrastructure.model.Form;
import weixin.zoo.infrastructure.model.FormExample;
import weixin.zoo.infrastructure.repository.FormRepository;
import java.util.Date;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/4.
 */
@Repository
@MapperScan("weixin.zoo.infrastructure.mapper")
public class FormRepositoryImpl implements FormRepository{

    @Autowired
    private FormMapper formMapper;

    @Override
    public long saveForm(String templateId, String formValue, String wxid,String name) {
        Form form =  transferForm(templateId,formValue,wxid,name);
        return formMapper.insertSelective(form);
    }

    @Override
    public Form getFormById(long id) {
        Form form = formMapper.selectByPrimaryKey(id);
        return form;
    }

    @Override
    public List<Form> getFormByUserId(String userId) {
        FormExample formExample = new FormExample();
        formExample.createCriteria().andFormOwnerEqualTo(userId).andIsDeleteEqualTo("n");
        return formMapper.selectByExample(formExample);
    }

    private Form transferForm(String templateId, String formValue, String wxid, String name){
        Form form = new Form();
        form.setGmtCreate(new Date());
        form.setGmtModified(new Date());
        form.setIsDelete("n");
        form.setFormName(name);
        form.setFormOwner(wxid);
        form.setFormValue(formValue);
        form.setTemplateId(Long.valueOf(templateId));
        return form;
    }
}