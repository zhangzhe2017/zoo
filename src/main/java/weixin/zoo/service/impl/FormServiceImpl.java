package weixin.zoo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
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
    public Long saveForm(String templateId, String formValues, String wxid, String name, String fields) {
        //判断表单中是否有图片，如果有图片则转存到oss
        //从template找到对应的fields，和formValues中name做匹配，找到其中的image类型做转存。
        List<TemplateField> templateFields = getImageFieldsFromTemplate(Long.valueOf(templateId));

        JSONObject jsonObject = JSON.parseObject(formValues);
        for(String key : jsonObject.keySet()){
            for(TemplateField templateField : templateFields){
                if(templateField.getFieldName().equals(key) && !jsonObject.getString(key).isEmpty()){
                    //转存
                    //获取到的value为list结构,遍历处理
                    JSONArray result = new JSONArray();
                    JSONArray jsonArray = jsonObject.getJSONArray(key);
                    Iterator itr = jsonArray.iterator();
                    while(itr.hasNext()){
                        String value = (String)itr.next();
                        //
                        if(value.startsWith("http:")){
                            result.add(value);
                            continue;
                        }
                        String mediaLocalPath = WxServiceCenter.downLoadMediaSource(value);
                        //图片上传
                        String keyStr = String.valueOf(new Date().getTime() /1000).concat(".jpg");
                        boolean upload = OssUtils.uploadFile(mediaLocalPath, keyStr);
                        if(upload){
                            String url = OssUtils.getFileUrl(keyStr);
                            result.add(url);
                        }
                    }
                    jsonObject.put(key, result);
                }
            }
        }

        JSONArray ids = new JSONArray();
        if(!StringUtils.isEmpty(fields)){
            //转换fields,逐条保存后获取id
            JSONArray jsonArray = JSONArray.parseArray(fields);
            Iterator itr = jsonArray.iterator();
            while(itr.hasNext()){
                JSONObject jsonObjectField = (JSONObject)itr.next();
                Long id = templateFieldRepository.saveTemplateField(jsonObjectField, wxid);
                ids.add(id);
            }
        }

        return formRepository.saveForm(templateId, jsonObject.toJSONString(), wxid, name, ids.toJSONString());
    }

    @Override
    public Object getForm(Long id, String userId) {
        JSONObject data = new JSONObject();
        //表单信息需要查询五张表：
        //1. form表  2. template  3.user 4. register 5.template_field
        Form form = formRepository.getFormById(id);
        data.put("title",form.getFormName());
        data.put("fieldValues",form.getFormValue());

        //获取template信息
        long templateId =  form.getTemplateId();
        Template template = templateRepository.getTemplateById(templateId);
        //根据fields获取详细数据
        JSONArray jsonArray = new JSONArray();
        JSONArray fieldIds = JSONArray.parseArray(template.getFiledIds());
        Iterator itr = fieldIds.iterator();
        while(itr.hasNext()){
            Integer field = (Integer)itr.next();
            TemplateField templateField = templateFieldRepository.getTemplateField(Long.valueOf(field));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",templateField.getFieldName());
            jsonObject.put("type",templateField.getFieldType());
            jsonObject.put("required",templateField.getIsEmpty().equals("false"));
            jsonObject.put("label",templateField.getFieldLabel());
            jsonArray.add(jsonObject);
        }

        data.put("fields",jsonArray);
        data.put("type",template.getTemplateType());

        //用户数据获取
        String wxid =  form.getFormOwner();
        User user = userRepository.getUserInfoByWxid(wxid);
        data.put("creatorNickName",user.getUserName());
        data.put("creatorWxid", wxid);

        JSONArray attenders = new JSONArray();
        List<Register> registers = registerRepository.getRegistersByFormId(id);
        for(Register register : registers){
            User attender = userRepository.getUserInfoByWxid(register.getAttender());
            attenders.add(attender.getUserName());
        }

        data.put("attenderList",attenders);
        data.put("timestamp", new Date());

        boolean isRegistered = registerRepository.isUserRegistered(id,userId);
        data.put("registered", isRegistered);


        //加一个formFields字段
        JSONArray jsonArrayFields = new JSONArray();
        JSONArray formFields = JSONArray.parseArray(form.getFieldIds());
        Iterator itrFormFields = formFields.iterator();
        while(itrFormFields.hasNext()){
            Integer field = (Integer)itrFormFields.next();
            TemplateField templateField = templateFieldRepository.getTemplateField(Long.valueOf(field));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",templateField.getFieldName());
            jsonObject.put("type",templateField.getFieldType());
            jsonObject.put("required",templateField.getIsEmpty().equals("false"));
            jsonObject.put("label",templateField.getFieldLabel());
            jsonArrayFields.add(jsonObject);
        }
        data.put("formFields",jsonArrayFields);

        return data;
    }

    @Override
    public Object doRegister(long id, boolean rOc, String userId, String fieldValues) {
        if(rOc)
            registerRepository.registerUser(id,userId,fieldValues);
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

    @Override
    public boolean receiveUserPayInfo(long id, String userId) {
        //先判断是否存在报名信息，如果不存在报名信息则新增一条报名信息
        boolean isRegistered = registerRepository.isUserRegistered(id, userId);
        if(isRegistered){
            registerRepository.updateRegisterPayed(id,userId);
        }else{
            registerRepository.registerPayedInfo(id, userId);
        }

        return true;
    }

    @Override
    public Form getFormById(Long id) {
        Form form = formRepository.getFormById(id);
        return form;
    }

    @Override
    public Long updateForm(long id, String formValues, String name, String fields) {
        //form表单中的字段全部保存，其中要特殊处理的是image类型的数据。图片类型，如果是oss的图片则不用替换，如果是微信服务器的图片则要转存一次
        //从formId中找到templateid
        Form form = formRepository.getFormById(id);
        Long templateId = form.getTemplateId();
        String wxid = form.getFormOwner();

        List<TemplateField> templateFields = getImageFieldsFromTemplate(templateId);

        JSONObject jsonObject = JSON.parseObject(formValues);
        for(String key : jsonObject.keySet()){
            for(TemplateField templateField : templateFields){
                if(templateField.getFieldName().equals(key) && !jsonObject.getString(key).isEmpty()){
                    //转存
                    //获取到的value为list结构,遍历处理
                    JSONArray result = new JSONArray();
                    JSONArray jsonArray = jsonObject.getJSONArray(key);
                    Iterator itr = jsonArray.iterator();
                    while(itr.hasNext()){
                        String value = (String)itr.next();
                        if(value.startsWith("http:")){
                            result.add(value);
                            continue;
                        }
                        String mediaLocalPath = WxServiceCenter.downLoadMediaSource(value);
                        //图片上传
                        String keyStr = String.valueOf(new Date().getTime() /1000).concat(".jpg");
                        boolean upload = OssUtils.uploadFile(mediaLocalPath, keyStr);
                        if(upload){
                            String url = OssUtils.getFileUrl(keyStr);
                            result.add(url);
                        }
                    }
                    jsonObject.put(key, result);
                }
            }
        }

        JSONArray ids = new JSONArray();
        if(!StringUtils.isEmpty(fields)){
            //转换fields,逐条保存后获取id
            JSONArray jsonArray = JSONArray.parseArray(fields);
            Iterator itr = jsonArray.iterator();
            while(itr.hasNext()){
                JSONObject jsonObjectField = (JSONObject)itr.next();
                Long fieldId = templateFieldRepository.saveTemplateField(jsonObjectField, wxid);
                ids.add(fieldId);
            }
        }

        return formRepository.updateForm(id,formValues,name,fields);
    }

    /*
     * 从模板中找到image类型的字段
     */
    private List<TemplateField> getImageFieldsFromTemplate(long templateId){
        List<TemplateField> templateFields = new ArrayList<>();
        //获取模板基础信息
        Template template = templateRepository.getTemplateById(templateId);

        //根据fields获取详细数据
        JSONArray fieldIds = JSONArray.parseArray(template.getFiledIds());
        Iterator itr = fieldIds.iterator();
        while(itr.hasNext()){
            Integer field = (Integer)itr.next();
            TemplateField templateField = templateFieldRepository.getTemplateField(Long.valueOf(field));
            if(templateField.getFieldType().equals("image")){
                templateFields.add(templateField);
            }
        }

        return templateFields;
    }
}
