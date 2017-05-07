package weixin.zoo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import weixin.zoo.infrastructure.model.*;
import weixin.zoo.infrastructure.repository.*;
import weixin.zoo.service.FormService;
import weixin.zoo.utils.OssUtils;
import weixin.zoo.wxapi.WxServiceCenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
@Service
@EnableAutoConfiguration
public class FormServiceImpl implements FormService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TemplateFieldRepository templateFieldRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterRepository registerRepository;

    @Override
    public Long saveForm(String templateId, String formValues, String wxid, String name) {
        //判断表单中是否有图片，如果有图片则转存到oss
        JSONArray jsonArray = JSONArray.parseArray(formValues);
        Iterator itr = jsonArray.iterator();
        while(itr.hasNext()){
            JSONObject ob = (JSONObject)itr.next();
            String type = ob.getString("type");
            if(type.equals("image")){
                //图片转存
                String value = ob.getString("value");
                String mediaLocalPath = WxServiceCenter.downLoadMediaSource(value);
                String str = mediaLocalPath.trim();
                String key = str.substring(str.lastIndexOf("//"));
                boolean upload = OssUtils.uploadFile(mediaLocalPath, key);
                if(upload){
                    String url = OssUtils.getFileUrl(key);
                    ob.put("value",url);
                }
            }
        }
        return formRepository.saveForm(templateId, formValues, wxid, name);
    }

    @Override
    public String getForm(Long id, String userId) {
        JSONObject data = new JSONObject();
        //表单信息需要查询五张表：
        //1. form表  2. template  3.user 4. register 5.template_field
        Form form = formRepository.getFormById(id);
        data.put("title",form.getFormName());
        data.put("fieldValues",form.getFormValue());

        //获取template信息
        long templateId =  form.getTemplateId();
        Template template = templateRepository.getTemplateById(id);
        //根据fields获取详细数据
        String[] fieldIds = template.getFiledIds().split(",");
        for(String field: fieldIds){
            TemplateField templateField = templateFieldRepository.getTemplateField(Long.valueOf(field));
            data.put(templateField.getFieldName(),templateField.getFieldType());
        }
        data.put("type",template.getTemplateType());

        //用户数据获取
        String wxid =  form.getFormOwner();
        User user = userRepository.getUserInfoByWxid(wxid);
        data.put("creatorNickName",user.getUserName());
        data.put("creatorWxid", wxid);

        String attenders = "";
        List<Register> registers = registerRepository.getRegistersByFormId(id);
        for(Register register : registers){
            User attender = userRepository.getUserInfoByWxid(register.getAttender());
            attenders.concat(attender.getUserName()).concat(",");
        }
        data.put("attenderList",attenders.indexOf(0,attenders.length()-1));
        data.put("timestamp", new Date());

        boolean isRegistered = registerRepository.isUserRegistered(id,userId);
        data.put("registered", isRegistered);

        return data.toString();
    }

    @Override
    public String doRegister(long id, boolean rOc, String userId) {
        if(rOc)
            registerRepository.registerUser(id,userId);
        else
            registerRepository.cancelRegister(id,userId);

        //从form中查询是否存在二维码，若有则转换。

        return null;
    }

    @Override
    public List<Form> getFormsByUserId(String userId, String type) {
        List<Form> forms = formRepository.getFormByUserId(userId);
        //判断form 集成的template类型，如果为activity 则为活动
        List<Form> result = new ArrayList<Form>();
        for(Form form: forms){
            long templateId = form.getTemplateId();
            Template template = templateRepository.getTemplateById(templateId);
            if(template.getTemplateType().equals(type))
                result.add(form);
        }

        return result;
    }

    @Override
    public List<Register> getAttendListByUserId(String userId) {
        List<Register> registers = registerRepository.getAttendListByUserId(userId);
        return registers;
    }
}
