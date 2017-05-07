package weixin.zoo.infrastructure.repository;

import weixin.zoo.infrastructure.model.User;

/**
 * Created by viczhang.zhangz on 2017/5/4.
 */
public interface UserRepository {

    /*
     * 根据wxid获取用户信息
     */
    public User getUserInfoByWxid(String wxid);
}
