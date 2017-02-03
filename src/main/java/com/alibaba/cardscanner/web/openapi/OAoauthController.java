package com.alibaba.cardscanner.web.openapi;

import com.alibaba.cardscanner.service.openapi.Env;
import com.alibaba.cardscanner.service.openapi.OApiException;
import com.alibaba.cardscanner.service.openapi.auth.AuthHelper;
import com.alibaba.cardscanner.service.openapi.user.UserHelper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by viczhang.zhangz on 2016/6/23.
 */

@Controller
public class OAoauthController {

    @RequestMapping("/oaoauth")
    public void oaoauth(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String code = request.getParameter("code");
        if(code != null){
            try {
                String ssoToken = AuthHelper.getSsoToken();
                response.getWriter().append(ssoToken);
                JSONObject js = UserHelper.getAgentUserInfo(ssoToken, code);
                response.getWriter().append(js.toString());
            } catch (OApiException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{
            String reurl = "https://oa.dingtalk.com/omp/api/micro_app/admin/landing?corpid=" +
                    Env.CORP_ID + "&redirect_url=" + Env.OA_BACKGROUND_URL;//配置的OA后台地址
            response.addHeader("location", reurl);
            response.setStatus(302);
        }
    }
}
