package weixin.zoo.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.zoo.service.TemplateService;
import weixin.zoo.utils.ResultUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by viczhang.zhangz on 2017/5/3.
 */
@Controller
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @RequestMapping("/getTemplate")
    @ResponseBody
    public String getTemplate(HttpServletRequest request){
        String id = request.getParameter("id");
        JSONObject jsonObject = templateService.getTemplate(Long.valueOf(id));

        return ResultUtils.assembleResult("true", "true", jsonObject.toString());
    }

}
