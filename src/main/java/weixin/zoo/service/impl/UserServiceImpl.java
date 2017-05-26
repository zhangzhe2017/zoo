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
        Long timestamp = new Date().getTime();
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
            String[] array = new String[] { jsTicket, timestamp, nonceStr, url };
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
