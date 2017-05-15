package weixin.zoo.infrastructure.repository.impl;

import com.alibaba.fastjson.JSONObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import weixin.zoo.infrastructure.mapper.UserMapper;
import weixin.zoo.infrastructure.model.User;
import weixin.zoo.infrastructure.model.UserExample;
import weixin.zoo.infrastructure.repository.UserRepository;

import java.util.Date;
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

    @Override
    public int insertUserInfo(JSONObject json) {
        String wxid = json.getString("openid");
        String nickName = json.getString("nickname");
        String sex = json.getString("sex");
        String headImg = json.getString("headimgurl");

        User user = new User();
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        user.setIsDelete("n");
        user.setLatestAccessTime(new Date());
        user.setUserId(wxid);
        user.setUserName(nickName);
        user.setUserSex(sex);
        user.setUserHeadimgurl(headImg);

        return userMapper.insertSelective(user);
    }

    @Override
    public int updateUserInfo(JSONObject json) {
        String wxid = json.getString("openid");
        String nickName = json.getString("nickname");
        String headImg = json.getString("headimgurl");

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdEqualTo(wxid).andIsDeleteEqualTo("n");

        User user = new User();
        user.setGmtModified(new Date());
        user.setIsDelete("n");
        user.setLatestAccessTime(new Date());
        user.setUserName(nickName);
        user.setUserHeadimgurl(headImg);

        return userMapper.updateByExample(user,userExample);
    }


}
