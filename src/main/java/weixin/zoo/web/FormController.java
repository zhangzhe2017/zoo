package weixin.zoo.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.zoo.infrastructure.model.Form;
import weixin.zoo.infrastructure.model.PageDto;
import weixin.zoo.infrastructure.model.Register;
import weixin.zoo.service.CommonService;
import weixin.zoo.service.FormService;
import weixin.zoo.utils.ActivityTypeEnum;
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

    @Autowired
    private CommonService commonService;

    @RequestMapping("/saveForm")
    @ResponseBody
    public String saveForm(HttpServletRequest request) {
        String templateId = request.getParameter("templateId");
        String formId = request.getParameter("formId");
        String fieldValues = request.getParameter("fieldValues");
        JSONObject jsonObject = JSON.parseObject(fieldValues);

        String formName = jsonObject.getString("title");
        String formFields = request.getParameter("formFields");

        //从session里取到wxid
        String wxid = (String)request.getSession().getAttribute("wxid");

        JSONObject result = new JSONObject();
        if(StringUtils.isNotEmpty(templateId)){
            long id = formService.saveForm(templateId, fieldValues, wxid, formName,formFields);
            result.put("id",id);
        }else{
            long id = formService.updateForm(Long.valueOf(formId), fieldValues, formName, formFields);
            result.put("id",id);
        }

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
        String fieldValues = request.getParameter("fieldValues");

        Object form = formService.doRegister(Long.valueOf(id), rOc.equals("true"), wxid, fieldValues);

        return ResultUtils.assembleResult(true, "true", form);
    }

    @RequestMapping("/getMyFormList")
    @ResponseBody
    public String getMyFormList(HttpServletRequest request){
        String wxid = (String)request.getSession().getAttribute("wxid");
        Integer currentPage = Integer.parseInt(request.getParameter("currentPage"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));

        PageDto pageDto = new PageDto(pageSize*(currentPage-1), pageSize);

        List<Form> forms = formService.getFormsByUserId(wxid, ActivityTypeEnum.ACTIVITY.getName(), pageDto);

        JSONArray jsonArray = new JSONArray();
        for(Form form : forms){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",form.getId());
            jsonObject.put("title", form.getFormName());
            jsonObject.put("type", ActivityTypeEnum.ACTIVITY.getName());
            jsonObject.put("formOwner", form.getFormOwner());

            //从详情中拿到cover
            JSONObject json = (JSONObject)JSONObject.parse(form.getFormValue());
            String pic = JSONObject.parseArray(json.getString("cover")).getString(0);
            String startTime = json.getString("startTime");
            jsonObject.put("pic", pic);
            jsonObject.put("startTime",startTime);
            jsonArray.add(jsonObject);
        }

        return ResultUtils.assembleResult(true, "true", jsonArray);
    }

    @RequestMapping("/getAttendedActivityList")
    @ResponseBody
    public String getAttendedActivityList(HttpServletRequest request){
        String wxid = (String)request.getSession().getAttribute("wxid");
        Integer currentPage = Integer.parseInt(request.getParameter("currentPage"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));

        PageDto pageDto = new PageDto(pageSize*(currentPage-1), pageSize);

        List<Register> registers = formService.getAttendListByUserId(wxid, pageDto);

        JSONArray jsonArray = new JSONArray();
        for(Register register : registers){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",register.getFormId());
            jsonObject.put("type",ActivityTypeEnum.ACTIVITY.getName());

            Form form = formService.getFormById(register.getFormId());
            jsonObject.put("formOwner",form.getFormOwner());
            jsonObject.put("title",form.getFormName());

            //从详情中拿到cover
            JSONObject json = (JSONObject)JSONObject.parse(form.getFormValue());
            String pic = json.getString("cover");
            String startTime = json.getString("startTime");
            jsonObject.put("pic", pic);
            jsonObject.put("startTime",startTime);

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

    @RequestMapping("/transferPic")
    @ResponseBody
    public String transferPic(HttpServletRequest request){
        String picIds = request.getParameter("pic");
        JSONArray jsonArray = JSONArray.parseArray(picIds);
        JSONArray urls = commonService.transferPics(jsonArray);

        return ResultUtils.assembleResult(true, "true", urls);
    }
}
