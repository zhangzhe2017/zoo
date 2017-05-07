package weixin.zoo.wxapi.auth;

import com.alibaba.fastjson.JSONObject;
import weixin.zoo.wxapi.Env;
import java.text.SimpleDateFormat;
import java.util.Date;
import weixin.zoo.utils.*;

/**
 * Created by viczhang.zhangz on 2017/4/28.
 */
public class AuthHelper {

    //本地accessToken有效时间为1小时55分钟
    private static final long cacheTime = 1000 * 60 * 55 * 2;

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /*
     *   先用本地读取accessToken，如果过期或者本地无则重新获取；
     */
    public static String getAccessToken() throws OApiException {
        long curTime = System.currentTimeMillis();
        JSONObject accessTokenValue = (JSONObject) FileUtils.getValue("accesstoken","accesstoken");
        String accessToken = null;

        if (accessTokenValue == null || curTime - accessTokenValue.getLong("begin_time") >= cacheTime){
            System.out.println(df.format(new Date())+" authhelper: get new access_token");

            JSONObject jsontemp = new JSONObject();

            String url = Env.OAPI_HOST + "/cgi-bin/token?" + "grant_type=client_credential" + "&appid=" + Env.APP_ID +"&secret="+Env.APP_SECRET;
            JSONObject response = HttpHelper.httpGet(url);
            if (response.containsKey("access_token")) {
                accessToken = response.getString("access_token");
                // save accessToken
                JSONObject jsonAccess = new JSONObject();
                jsontemp.clear();
                jsontemp.put("access_token", accessToken);
                jsontemp.put("begin_time", curTime);
                jsonAccess.put("accessToken", jsontemp);

                FileUtils.write2File(jsonAccess, "accesstoken");
            } else {
                throw new OApiException(1,"access_token");
            }
        }

        return accessToken;
    }

    /*
     * 根据accessToken获取jsticket
     */
    public static String getJsTicket(String accessToken) throws OApiException{
        long curTime = System.currentTimeMillis();
        JSONObject jsTicketObject = (JSONObject) FileUtils.getValue("jsTicket","jsTicket");
        String jsTicket = null;

        if (jsTicketObject == null || curTime - jsTicketObject.getLong("begin_time") >= cacheTime){
            System.out.println(df.format(new Date())+" authhelper: get new access_token");

            JSONObject jsontemp = new JSONObject();

            String url = Env.OAPI_HOST + "/cgi-bin/ticket/getticket?" + "access_token=" + accessToken + "&type=jsapi";
            JSONObject response = HttpHelper.httpGet(url);
            if (response.containsKey("ticket")) {
                jsTicket = response.getString("ticket");
                // save accessToken
                JSONObject jsonAccess = new JSONObject();
                jsontemp.clear();
                jsontemp.put("jsTicket", jsTicket);
                jsontemp.put("begin_time", curTime);
                jsonAccess.put("jsTicket", jsontemp);

                FileUtils.write2File(jsonAccess, "jsTicket");
            } else {
                throw new OApiException(1,"jsTicket");
            }
        }

        return jsTicket;
    }

    /*
     * 通过页面code获取页面accessToken以及对应openid
     */
    public static JSONObject getWebTokenByCode(String code) throws OApiException {
        String url = Env.OAPI_HOST + "/sns/oauth2/access_token?" + "&appid=" + Env.APP_ID +"&secret="+Env.APP_SECRET +"&code="+code+"&grant_type=authorization_code";
        JSONObject response = HttpHelper.httpGet(url);
        return response;
    }


    /*
     *  根据webaccessToken以及openid 获取用户身份
     */
    public static String getUserInfo(String webAccessToken, String openid) throws OApiException {
        String url = Env.OAPI_HOST + "/sns/userinfo?" + "&access_token=" + webAccessToken +"&openid="+openid +"lang=zh_CN";
        JSONObject response = HttpHelper.httpGet(url);
        return response.toString();
    }

    /*
     * 判断webaccessToken是否有效
     */
    public static boolean isTokenEffective(String webAccessToken, String openid) throws OApiException {
        String url = Env.OAPI_HOST + "/sns/auth?" + "&access_token=" + webAccessToken +"&openid="+openid;
        JSONObject response = HttpHelper.httpGet(url);
        String errcode = response.getString("errcode");
        if(errcode.equals("0"))
            return true;
        return false;
    }

    /*
     * 在token失效的情况下，使用refreshtoken获取新webaccessToken以及openid
     */
    public static JSONObject refreshAccessToken(String refreshToken) throws  OApiException {
        String url = Env.OAPI_HOST + "/sns/oauth2/refresh_token?" + "&appid=" + Env.APP_ID + "&grant_type=refresh_token" + "&refresh_token=" + refreshToken;
        JSONObject response = HttpHelper.httpGet(url);
        return response;
    }


}
