package weixin.zoo.infrastructure.repository;

import com.alibaba.fastjson.JSONObject;
import weixin.zoo.infrastructure.model.User;

import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/4.
 */
public interface UserRepository {

    /*
     * 根据wxid获取用户信息
     */
    public User getUserInfoByWxid(String wxid);

    /*
     * 插入用户信息
     */
    public int insertUserInfo(JSONObject json);

    /*
     * 更新用户信息
     */
    public int updateUserInfo(JSONObject json);

    /*
     * 根据权限获取对应用户列表
     */
    public List<User> getUsersByPermission(String permission);
}
