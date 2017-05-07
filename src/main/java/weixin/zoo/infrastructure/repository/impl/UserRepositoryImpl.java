package weixin.zoo.infrastructure.repository.impl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import weixin.zoo.infrastructure.mapper.UserMapper;
import weixin.zoo.infrastructure.model.User;
import weixin.zoo.infrastructure.model.UserExample;
import weixin.zoo.infrastructure.repository.UserRepository;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/4.
 */
@Repository
@MapperScan("weixin.zoo.infrastructure.mapper")
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserInfoByWxid(String wxid) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdEqualTo(wxid).andIsDeleteEqualTo("n");

        List<User> users = userMapper.selectByExample(userExample);
        if(users.isEmpty())
            return null;

        return users.get(0);
    }
}
