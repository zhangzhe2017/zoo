package com.alibaba.cardscanner.web;

import com.alibaba.cardscanner.infrastructure.dto.ResultDTO;
import com.alibaba.cardscanner.service.openapi.auth.AuthHelper;
import com.alibaba.cardscanner.service.openapi.user.User;
import com.alibaba.cardscanner.web.utils.CommonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by viczhang.zhangz on 2016/6/29.
 *
 * 获取jsapi签名数据
 */

@Controller
public class GetJSAuthConfigHandler{

    @RequestMapping("/getAuthConfig")
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authConfig = AuthHelper.getConfig(request);
        PrintWriter writer = response.getWriter();
        writer.write(authConfig);
        writer.flush();
    }

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public ResultDTO<String> getUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultDTO<String> resultDTO = new ResultDTO<>();
        resultDTO.setSuccess(Boolean.FALSE);

        try {
            String code = request.getParameter("code");
            String corpId = request.getParameter("corpid");
            User user = CommonUtils.praseUserInfo(code, corpId);

            resultDTO.setResult(user.userid);
            resultDTO.setSuccess(Boolean.TRUE);
        }catch (Exception e){
            e.printStackTrace();
            resultDTO.setFailedInfo(e.getMessage());
        }
        return resultDTO;
    }
}
