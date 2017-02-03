package com.alibaba.cardscanner.web.openapi;

import com.alibaba.cardscanner.service.openapi.OApiException;
import com.alibaba.cardscanner.service.openapi.auth.AuthHelper;
import com.alibaba.cardscanner.service.openapi.user.User;
import com.alibaba.cardscanner.service.openapi.user.UserHelper;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by viczhang.zhangz on 2016/6/23.
 */

@Controller
public class UserInfoController {

    @RequestMapping("/userInfo")
    public void userInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO Auto-generated method stub
        String code = request.getParameter("code");
        String corpId = request.getParameter("corpid");
        System.out.println("code:"+code+" corpid:"+corpId);

        try {
            response.setContentType("text/html; charset=utf-8");

            String accessToken = AuthHelper.getAccessToken(corpId);
            System.out.println("access token:"+accessToken);
            User user = (User) UserHelper.getUser(accessToken, UserHelper.getUserInfo(accessToken, code).getString("userid"));
            String userJson = JSON.toJSONString(user);
            response.getWriter().append(userJson);
            System.out.println("userjson:"+userJson);



        } catch (OApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.getWriter().append(e.getMessage());
        }
    }
}
