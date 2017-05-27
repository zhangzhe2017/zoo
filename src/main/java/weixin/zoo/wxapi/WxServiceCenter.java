package weixin.zoo.wxapi;

import com.alibaba.fastjson.JSONObject;
import weixin.zoo.utils.HttpHelper;
import weixin.zoo.wxapi.auth.AuthHelper;

/**
 * Created by viczhang.zhangz on 2017/5/5.
 */
public class WxServiceCenter {
    /*
     * 把临时文件下载到本地
     */
    public static String downLoadMediaSource(String medieId){

        try{
            String accessToken = AuthHelper.getAccessToken();
            String url = Env.OAPI_HOST + "/cgi-bin/media/get?" + "access_token=" + accessToken + "&media_id=" + medieId;
            String filePath = HttpHelper.downloadFile(url , Env.FILE_DIR);
            return filePath;
        }catch (Exception e){
             e.printStackTrace();
        }

        return null;
    }
}
