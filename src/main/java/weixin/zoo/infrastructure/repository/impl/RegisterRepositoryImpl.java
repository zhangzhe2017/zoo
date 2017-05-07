package weixin.zoo.infrastructure.repository.impl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import weixin.zoo.infrastructure.mapper.RegisterMapper;
import weixin.zoo.infrastructure.model.Register;
import weixin.zoo.infrastructure.model.RegisterExample;
import weixin.zoo.infrastructure.repository.RegisterRepository;
import java.util.Date;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/4.
 */
@Repository
@MapperScan("weixin.zoo.infrastructure.mapper")
public class RegisterRepositoryImpl implements RegisterRepository {

    @Autowired
    private RegisterMapper registerMapper;

    @Override
    public List<Register> getRegistersByFormId(long formId) {
        RegisterExample registerExample = new RegisterExample();
        registerExample.createCriteria().andFormIdEqualTo(formId).andIsDeleteEqualTo("n");

        return registerMapper.selectByExample(registerExample);
    }

    @Override
    public boolean isUserRegistered(long formId, String userId) {
        RegisterExample registerExample = new RegisterExample();
        registerExample.createCriteria().andFormIdEqualTo(formId).andAttenderEqualTo(userId).andIsDeleteEqualTo("n");

        List<Register> registers = registerMapper.selectByExample(registerExample);
        if(registers.isEmpty())
            return false;
        return true;
    }

    @Override
    public int registerUser(long formId, String userId) {
        Register register = new Register();
        register.setIsDelete("n");
        register.setGmtModified(new Date());
        register.setGmtCreate(new Date());
        register.setAttender(userId);
        register.setFormId(formId);
        return registerMapper.insertSelective(register);
    }

    @Override
    public int cancelRegister(long formId, String userId) {
        RegisterExample registerExample = new RegisterExample();
        registerExample.createCriteria().andFormIdEqualTo(formId).andIsDeleteEqualTo("n").andAttenderEqualTo(userId);

        Register register = new Register();
        register.setIsDelete("y");
        register.setGmtModified(new Date());

        return registerMapper.updateByExampleSelective(register,registerExample);
    }

    @Override
    public List<Register> getAttendListByUserId(String userId) {
        RegisterExample registerExample = new RegisterExample();
        registerExample.createCriteria().andIsDeleteEqualTo("n").andAttenderEqualTo(userId);

        return registerMapper.selectByExample(registerExample);
    }
}
