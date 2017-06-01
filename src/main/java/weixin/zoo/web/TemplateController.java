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

        return ResultUtils.assembleResult(true, "true", jsonObject);
    }

    @RequestMapping("/proposeVote")
    @ResponseBody
    public String proposeVote(HttpServletRequest request){
        //requeset里 有fieldsArray 以及 template基础信息
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        String fields = request.getParameter("fields");

        String wxid = (String)request.getSession().getAttribute("wxid");

        Long id = templateService.saveVote(type,name,fields,wxid);
        return ResultUtils.assembleResult(true, "true", id);
    }

}
