package weixin.zoo.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.zoo.infrastructure.model.Form;
import weixin.zoo.infrastructure.model.Register;
import weixin.zoo.service.FormService;
import weixin.zoo.utils.ResultUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
@Controller
@RequestMapping("/form")
public class FormController {

    @Autowired
    private FormService formService;

    @RequestMapping("/saveForm")
    @ResponseBody
    public String saveForm(HttpServletRequest request) {
        String id = request.getParameter("templateId");
        String fieldValues = request.getParameter("fieldValues");

        JSONObject jsonObject = JSON.parseObject(fieldValues);
        String formName = jsonObject.getString("title");

        //从session里取到wxid
        String wxid = (String)request.getSession().getAttribute("wxid");

        long formId = formService.saveForm(id, fieldValues, wxid, formName);
        JSONObject result = new JSONObject();
        result.put("id",formId);

        return ResultUtils.assembleResult(true, "true", result);
    }

    @RequestMapping("/getForm")
    @ResponseBody
    public String getForm(HttpServletRequest request){
        String id = request.getParameter("id");
        //从session里取到wxid
        String wxid = (String)request.getSession().getAttribute("wxid");
        Object form = formService.getForm(Long.valueOf(id),wxid);

        return ResultUtils.assembleResult(true, "true", form);
    }

    @RequestMapping("/register")
    @ResponseBody
    public String register(HttpServletRequest request){
        String id = request.getParameter("id");
        String rOc = request.getParameter("register");
        String wxid = (String)request.getSession().getAttribute("wxid");
        Object form = formService.doRegister(Long.valueOf(id), rOc.equals("true"), wxid);

        return ResultUtils.assembleResult(true, "true", form);
    }

    @RequestMapping("/getMyFormList")
    @ResponseBody
    public String getMyFormList(HttpServletRequest request){
        String wxid = (String)request.getSession().getAttribute("wxid");

        List<Form> forms = formService.getFormsByUserId(wxid, "activity");

        JSONArray jsonArray = new JSONArray();
        for(Form form : forms){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",form.getId());
            jsonObject.put("title",form.getFormName());
            jsonObject.put("type","activity");
            jsonObject.put("formOwner",form.getFormOwner());
            jsonArray.add(jsonObject);
        }

        return ResultUtils.assembleResult(true, "true", jsonArray);
    }

    @RequestMapping("/getAttendedActivityList")
    @ResponseBody
    public String getAttendedActivityList(HttpServletRequest request){
        String wxid = (String)request.getSession().getAttribute("wxid");

        List<Register> registers = formService.getAttendListByUserId(wxid);

        JSONArray jsonArray = new JSONArray();
        for(Register register : registers){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",register.getFormId());
            jsonObject.put("type","activity");

            Form form = formService.getFormById(register.getFormId());
            jsonObject.put("formOwner",form.getFormOwner());
            jsonObject.put("title",form.getFormName());
            jsonArray.add(jsonObject);
        }

        return ResultUtils.assembleResult(true, "true", jsonArray);
    }

    @RequestMapping("/receiveUserPayInfo")
    @ResponseBody
    public String receiveUserPayInfo(HttpServletRequest request){
        String id = request.getParameter("id");
        String wxid = (String)request.getSession().getAttribute("wxid");
        formService.receiveUserPayInfo(Long.valueOf(id), wxid);
        return ResultUtils.assembleResult(true, "true", "true");
    }

}
