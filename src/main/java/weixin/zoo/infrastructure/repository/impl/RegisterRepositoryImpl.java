package weixin.zoo.infrastructure.repository.impl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import weixin.zoo.infrastructure.mapper.RegisterMapper;
import weixin.zoo.infrastructure.model.PageDto;
import weixin.zoo.infrastructure.model.Register;
import weixin.zoo.infrastructure.model.RegisterExample;
import weixin.zoo.infrastructure.repository.RegisterRepository;
import weixin.zoo.utils.ActivityStatusEnum;

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
        registerExample.createCriteria().andFormIdEqualTo(formId).andStatusEqualTo(ActivityStatusEnum.ATTEND.getName()).andIsDeleteEqualTo("n");

        return registerMapper.selectByExample(registerExample);
    }

    @Override
    public boolean isUserRegistered(long formId, String userId) {
        RegisterExample registerExample = new RegisterExample();
        registerExample.createCriteria().andFormIdEqualTo(formId).andAttenderEqualTo(userId).andStatusEqualTo(ActivityStatusEnum.ATTEND.getName()).andIsDeleteEqualTo("n");

        List<Register> registers = registerMapper.selectByExample(registerExample);
        if(registers.isEmpty())
            return false;
        return true;
    }

    @Override
    public int registerUser(long formId, String userId, String fieldValues) {
        Register register = new Register();
        register.setIsDelete("n");
        register.setGmtModified(new Date());
        register.setGmtCreate(new Date());
        register.setAttender(userId);
        register.setFormId(formId);
        register.setStatus(ActivityStatusEnum.ATTEND.getName());
        register.setFormValue(fieldValues);
        return registerMapper.insertSelective(register);
    }

    @Override
    public int cancelRegister(long formId, String userId) {
        RegisterExample registerExample = new RegisterExample();
        registerExample.createCriteria().andFormIdEqualTo(formId).andIsDeleteEqualTo("n").andAttenderEqualTo(userId);

        Register register = new Register();
        register.setGmtModified(new Date());
        register.setStatus(ActivityStatusEnum.CANCEL.getName());

        return registerMapper.updateByExampleSelective(register,registerExample);
    }

    @Override
    public List<Register> getAttendListByUserId(String userId, PageDto pageDto) {
        RegisterExample registerExample = new RegisterExample();
        registerExample.createCriteria().andIsDeleteEqualTo("n").andStatusEqualTo(ActivityStatusEnum.ATTEND.getName()).andAttenderEqualTo(userId);

        registerExample.setLimit(pageDto.getLimit());
        registerExample.setOffset(pageDto.getOffset());
        registerExample.setOrderByClause("gmt_create");

        return registerMapper.selectByExample(registerExample);
    }

    @Override
    public int updateRegisterPayed(long formId, String userId) {
        Register register = new Register();
        register.setStatus(ActivityStatusEnum.PAY.getName());
        register.setGmtModified(new Date());

        RegisterExample registerExample = new RegisterExample();
        registerExample.createCriteria().andFormIdEqualTo(formId).andIsDeleteEqualTo("n").andAttenderEqualTo(userId);

        return registerMapper.updateByExampleSelective(register,registerExample);
    }

    @Override
    public int registerPayedInfo(long formId, String userId) {
        Register register = new Register();
        register.setIsDelete("n");
        register.setGmtModified(new Date());
        register.setGmtCreate(new Date());
        register.setAttender(userId);
        register.setFormId(formId);
        register.setStatus(ActivityStatusEnum.PAY.getName());

        return registerMapper.insertSelective(register);
    }
}
