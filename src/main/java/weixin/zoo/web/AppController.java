package weixin.zoo.web;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import weixin.zoo.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import weixin.zoo.utils.*;
import weixin.zoo.wxapi.Env;

/**
 * Created by viczhang.zhangz on 2017/4/28.
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
            return ResultUtils.assembleResult(false,"","");

        //首先使用code直接获取webaccessToken以及openid，判断accessToken是否失效，若失效则刷新。
        JSONObject jsonObject = userService.getUserInfo(code);

        HttpSession httpSession = request.getSession();

        //String url = request.getRequestURL().toString();
        String url = request.getHeader("Referer");
        try {
            result = userService.getJsapiSignatrue(url);
            result.put("attention",jsonObject.getString("subscribe").equals("1"));
            result.put("wxid",jsonObject.getString("openid"));
            httpSession.setAttribute("wxid",jsonObject.getString("openid"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultUtils.assembleResult(true, "true", result);
    }

    @RequestMapping("/redirectShareUrl")
    @ResponseBody
    public void getShareUrl(HttpServletRequest request, HttpServletResponse response){
        String redirectUrl = request.getParameter("redirectUrl");
        try {
            redirectUrl = URLEncoder.encode(redirectUrl,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String shareUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        shareUrl = shareUrl.concat("appid=").concat(Env.APP_ID).concat("&redirect_uri=").concat(redirectUrl).concat("&response_type=code")
                .concat("&scope=snsapi_userinfo").concat("#wechat_redirect");

        try {
            response.sendRedirect(shareUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
