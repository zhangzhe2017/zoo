package weixin.zoo.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import weixin.zoo.infrastructure.model.User;
import weixin.zoo.infrastructure.repository.UserRepository;
import weixin.zoo.service.UserService;
import weixin.zoo.wxapi.Env;
import weixin.zoo.wxapi.auth.AuthHelper;
import weixin.zoo.utils.*;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;

import  weixin.zoo.service.common.aes.*;

/**
 * Created by viczhang.zhangz on 2017/4/28.
 */
@Service
@EnableAutoConfiguration
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /*
     * 根据code获取到用户信息，包括基本信息以及是否关注本公众号
     * 同时将用户信息入库
     */
    public JSONObject getUserInfo(String code){
        try{
            //首先根据code获取到openid以及WebaccessToken
            JSONObject response = AuthHelper.getWebTokenByCode(code);
            String webAccessToken = response.getString("access_token");
            String openId = response.getString("openid");

            //判断webAccessToken是否失效
            boolean isEffective = AuthHelper.isTokenEffective(webAccessToken,openId);
            if(!isEffective){
                response = AuthHelper.refreshAccessToken(response.getString("refresh_token"));
                openId = response.getString("openid");
            }

            //根据openid获取用户数据
            String accessToken = AuthHelper.getAccessToken();
            String url = Env.OAPI_HOST + "/cgi-bin/user/info?" + "&access_token=" + accessToken +"&openid="+ openId +"&lang=zh_CN";
            response = HttpHelper.httpGet(url);
            if(!StringUtils.isEmpty(response.getString("errcode")))
                return null;

            //把用户信息插入，如果有的话更新
            User user = userRepository.getUserInfoByWxid(response.getString("openid"));
            if(user == null){
                userRepository.insertUserInfo(response);
            }else{
                userRepository.updateUserInfo(response);
            }

            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*
     *  获取jsspi签名数据
     */
    @Override
    public JSONObject getJsapiSignatrue(String url) throws Exception {
        String nonceStr = "zujuguan";
        Long timestamp = new Date().getTime() /1000;
        String accessToken = AuthHelper.getAccessToken();
        String jsTicket =  AuthHelper.getJsTicket(accessToken);

        String signature = getValidateSHA1(jsTicket, String.valueOf(timestamp), nonceStr, url);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("nonceStr", nonceStr);
        jsonObject.put("signature", signature);

        return jsonObject;
    }


    private String getValidateSHA1(String jsTicket, String timestamp, String nonceStr, String url){
        try {
            //注意这里参数名必须全部小写，且必须有序
            String string1 = "jsapi_ticket=" + jsTicket +
                    "&noncestr=" + nonceStr +
                    "&timestamp=" + timestamp +
                    "&url=" + url;
            // SHA1签名生成
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            String signature = byteToHex(crypt.digest());

            return signature;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
