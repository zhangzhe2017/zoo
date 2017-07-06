package weixin.zoo.infrastructure.repository;

import weixin.zoo.infrastructure.model.Form;
import weixin.zoo.infrastructure.model.PageDto;

import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/4.
 */
public interface FormRepository {

    /*
     * 保存表单
     */
    public long saveForm(String templateId, String formValue, String wxid, String name,String fieldIds);

    /*
     * 根据表单id获取表单数据
     */
    public Form getFormById(long id);

    /*
     * 获取用户发起的表单
     */
    public List<Form> getFormByUserId(String userId, PageDto pageDto);

    /*
     *  修改form表单内容
     */
    public long updateForm(Long id , String formValue, String name, String fieldIds);
}
