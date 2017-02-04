package weixin.connection.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.connection.service.common.aes.AesException;
import weixin.connection.service.common.aes.WXBizMsgCrypt;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by viczhang.zhangz on 2016/6/29.
 *
 * 获取jsapi签名数据
 */

@Controller
public class Validator {

    //处理公众号验证请求
    @RequestMapping("/validate")
    @ResponseBody
    public String validate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        String token = "connection";
        String appSecret = "43d5bd2dcfee1c967e9562252b0ea7f3";

        String validateSha1 = getValidateSHA1(nonce,timestamp,token);
        if(validateSha1.equals(signature)){
            return echostr;
        }

        return null;
    }

    private String getValidateSHA1(String nonce, String timestamp, String token){
        try {
            String[] array = new String[] { token, timestamp, nonce };
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 3; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
