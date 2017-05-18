package weixin.zoo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weixin.zoo.infrastructure.model.Form;
import weixin.zoo.infrastructure.model.Register;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
public interface FormService {
    /*
     * 保存表单
     * 表单继承自模板，保存表单需要知道模板id，以及表单内容，保存者wxid，以及表单名
     *
     * 表单中的图片，需要转存至oss
     *
     */
    public Long saveForm(String templateId, String formValues, String wxid, String name);

    /*
     * 获取表单数据
     * 根据表单id，以及用户id来获取表单数据。
     * 本接口方法要拿到表单页全部数据，包括：活动内容、活动发起人、活动当前参与者、当前用户是否报名
     */
    public Object getForm(Long id, String userId);

    /*
     * 活动报名接口
     * 涉及register表，仅面向活动类型的表单。
     * 通过rOc（register or cancel）来判断报名或者取消报名
     *
     * 报名功能中如果存在群二维码，需要转换成链接返回
     *
     */
    public Object doRegister(long id, boolean rOc, String userId);

    /*
     * 获取用户已发起表单列表
     * 通过type来区分表单类型，当前只有activity(活动)，后续会新增投票问卷类型
     *
     */
    public List<Form> getFormsByUserId(String userId, String type);

    /*
     * 获取用户已报名的活动列表，仅面向activity类型form
     */
    public List<Register> getAttendListByUserId(String userId);

    /*
     * 获取支付信息
     */
    public boolean receiveUserPayInfo(long id, String userId);

    /*
     * 根据formId获取form信息
     */
    public Form getFormById(Long id);
}
