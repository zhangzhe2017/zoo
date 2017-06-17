package weixin.zoo.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import weixin.zoo.service.CommonService;
import weixin.zoo.utils.OssUtils;
import weixin.zoo.wxapi.WxServiceCenter;
import java.util.Date;
import java.util.Iterator;
/**
 * Created by viczhang.zhangz on 2017/6/17.
 */
@Service
@EnableAutoConfiguration
public class CommonServiceImpl implements CommonService {

    @Override
    public JSONArray transferPics(JSONArray picids) {

        JSONArray picUrls = new JSONArray();
        Iterator itr = picids.iterator();
        while(itr.hasNext()){
            String mediaId = (String)itr.next();
            String mediaLocalPath = WxServiceCenter.downLoadMediaSource(mediaId);
            //图片上传
            String keyStr = String.valueOf(new Date().getTime() /1000).concat(".jpg");
            boolean upload = OssUtils.uploadFile(mediaLocalPath, keyStr);
            if(upload){
                String url = OssUtils.getFileUrl(keyStr);
                picUrls.add(url);
            }
        }
        return picUrls;
    }
}
