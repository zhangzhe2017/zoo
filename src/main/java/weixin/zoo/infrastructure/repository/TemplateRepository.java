package weixin.zoo.infrastructure.repository;

import weixin.zoo.infrastructure.model.Template;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
public interface TemplateRepository {

    /*
     * 根据模板id获取模板详情
     */
    public Template getTemplateById(long id);

    /*
     * 新增模板数据
     */
    public Long saveTemplate(String type, String name, String owner, String fields);

}
