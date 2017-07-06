package weixin.zoo.infrastructure.repository.impl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import weixin.zoo.infrastructure.mapper.FormMapper;
import weixin.zoo.infrastructure.model.Form;
import weixin.zoo.infrastructure.model.FormExample;
import weixin.zoo.infrastructure.model.PageDto;
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
    public long saveForm(String templateId, String formValue, String wxid ,String name, String fieldIds) {
        Form form =  transferForm(templateId,formValue,wxid,name,fieldIds);
        int num = formMapper.insert(form);
        if(num>0){
            FormExample formExample =  new FormExample();
            formExample.createCriteria().andIsDeleteEqualTo("n").andTemplateIdEqualTo(Long.valueOf(templateId))
                    .andFormNameEqualTo(name)
                    .andFormOwnerEqualTo(wxid)
                    .andFieldIdsEqualTo(fieldIds);
            List<Form> forms = formMapper.selectByExample(formExample);
            Form formAdd = forms.get(0);

            return formAdd.getId();
        }

        return 0;
    }

    @Override
    public Form getFormById(long id) {
        Form form = formMapper.selectByPrimaryKey(id);
        return form;
    }

    @Override
    public List<Form> getFormByUserId(String userId, PageDto pageDto) {
        FormExample formExample = new FormExample();
        formExample.createCriteria().andFormOwnerEqualTo(userId).andIsDeleteEqualTo("n");
        formExample.setLimit(pageDto.getLimit());
        formExample.setOffset(pageDto.getOffset());

        return formMapper.selectByExample(formExample);
    }

    @Override
    public long updateForm(Long id, String formValue, String name, String fieldIds) {
        FormExample formExample = new FormExample();
        formExample.createCriteria().andIdEqualTo(id);

        Form form = new Form();
        form.setFieldIds(fieldIds);
        form.setFormValue(formValue);
        form.setFormName(name);

        return formMapper.updateByExample(form,formExample);
    }

    private Form transferForm(String templateId, String formValue, String wxid, String name, String fieldIds){
        Form form = new Form();
        form.setGmtCreate(new Date());
        form.setGmtModified(new Date());
        form.setIsDelete("n");
        form.setFormName(name);
        form.setFormOwner(wxid);
        form.setFormValue(formValue);
        form.setTemplateId(Long.valueOf(templateId));
        form.setFieldIds(fieldIds);
        return form;
    }
}
