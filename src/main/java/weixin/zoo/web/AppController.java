package weixin.zoo.web;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.zoo.service.UserService;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import weixin.zoo.utils.*;

/**
 * Created by viczhang.zhangz on 2017/4/28.
 *
 *
 */
@Controller
@RequestMapping("/app")
public class AppController {

    @Autowired
    public UserService userService;

    @RequestMapping("/getAppData")
    @ResponseBody
    public String getUserInfo(HttpServletRequest request){
        JSONObject result = new JSONObject();
        String code = request.getParameter("code");
        if(StringUtils.isEmpty(code))
            return ResultUtils.assembleResult("false","","");;

        //首先使用code直接获取webaccessToken以及openid，判断accessToken是否失效，若失效则刷新。
        JSONObject jsonObject = userService.getUserInfo(code);

        String url = request.getRequestURL().toString();
        try {
            result = userService.getJsapiSignatrue(url);
            result.put("attention",jsonObject.getString("subscribe"));
            result.put("wxid",jsonObject.getString("openid"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultUtils.assembleResult("true", "true", result.toString());
    }

}
