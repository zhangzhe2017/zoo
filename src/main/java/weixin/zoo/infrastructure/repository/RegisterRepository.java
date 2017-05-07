package weixin.zoo.infrastructure.repository;

import weixin.zoo.infrastructure.model.Register;

import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/4.
 */
public interface RegisterRepository {

    /*
     * 通过表单id获取报名人员
     */
    public List<Register> getRegistersByFormId(long formId);

    /*
     * 针对某次活动是否报名
     */
    public boolean isUserRegistered(long formId, String userId);

    /*
     * 新增一条报名记录
     */
    public int registerUser(long formId, String userId);

    /*
     * 取消报名记录
     */
    public int cancelRegister(long formId, String userId);

    /*
     * 查询用户参加的活动
     */
    public List<Register> getAttendListByUserId(String userId);

}
