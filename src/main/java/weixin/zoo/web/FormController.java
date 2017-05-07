package weixin.zoo.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.zoo.service.FormService;
import weixin.zoo.utils.ResultUtils;

import javax.servlet.http.HttpServletRequest;

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
        String id = request.getParameter("id");
        String fieldValues = request.getParameter("fieldValues");

        JSONObject jsonObject = JSON.parseObject(fieldValues);
        String  formName = jsonObject.getString("title");

        long formId = formService.saveForm(id, fieldValues, "test", formName);
        JSONObject result = new JSONObject();
        result.put("id",request);

        return ResultUtils.assembleResult("true", "true", result.toJSONString());

    }

    @RequestMapping("/getForm")
    @ResponseBody
    public String getForm(HttpServletRequest request){
        String id = request.getParameter("id");
        formService.getForm(Long.valueOf(id),"test");
        return null;
    }
}
