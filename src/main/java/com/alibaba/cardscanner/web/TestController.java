package com.alibaba.cardscanner.web;

import com.alibaba.cardscanner.infrastructure.dto.ResultDTO;
import com.alibaba.cardscanner.infrastructure.http.HttpProtocolHandler;
import com.alibaba.cardscanner.infrastructure.model.UserCard;
import com.alibaba.cardscanner.infrastructure.repository.UserCardRepository;
import com.alibaba.cardscanner.service.CardRecognizeService;
import com.alibaba.cardscanner.service.FileServerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viczhang.zhangz on 2016/6/23.
 */
@Controller
public class TestController {
    @Autowired
    private UserCardRepository userCardRepository;

    @Autowired
    private CardRecognizeService cardRecognizeService;

    @Autowired
    private FileServerService fileServerService;


    @RequestMapping("/list.html")
    public String home() {
        return "list";
    }

    @RequestMapping("/detail.html")
    public String detail() { return "detail";}

    @RequestMapping("/getUserCard")
    @ResponseBody
    public UserCard getUserCard(Long id){
        return userCardRepository.findById(id);
    }

    @RequestMapping("/queryUserCards")
    @ResponseBody
    public List<UserCard> queryUserCard(String userId){
        return userCardRepository.queryUserIdOrderByCreate(userId, null);
    }

    @RequestMapping("/testRecognize")
    @ResponseBody
    public ResultDTO<Map<String, Object>> testRecognize(String fileName){
//        return cardRecognizeService.recognizeCard("HTB1IKCTMpXXXXaPXVXXq6xXFXXXt.jpg");
        return cardRecognizeService.recognizeCard(fileName);
    }

    @RequestMapping("/testRecognizePost")
    @ResponseBody
    public String testRecognizePost(){
        String uri = "http://gw.api.alibaba.com/openapi/param2/1/ali.intl.mobile/alibaba.ascRecognizeImageNew/7312"; //?name=HTB1IKCTMpXXXXaPXVXXq6xXFXXXt.jpg&type=0";
        Map<String, String> param = new HashMap<>();
        param.put("name","HTB1Hvk1KFXXXXXyXVXXq6xXFXXXJ.jpg");
        param.put("type","0");
        return HttpProtocolHandler.sendPostRequest(uri, param);
    }

    @RequestMapping("/getImage")
    @ResponseBody
    public byte[] getImage(String url){
        url = StringUtils.isBlank(url) ? "https://sc01.alicdn.com/kf/HTB16igeJFXXXXcqXpXX760XFXXXO.png" : url;
//        String url = "file://127.0.0.1/Users/rmy/ypd.png";
//        return HttpProtocolHandler.getUrlBytes(url);
        return null;
//        return HttpUtils.convertInputStreamToByte(HttpProtocolHandler.getUrlBytes(url));
    }
/*    @RequestMapping("/testFileServer")
    @ResponseBody
    public String testFileServer(){
        byte[] inputStream = HttpProtocolHandler.getUrlBytes("https://sc01.alicdn.com/kf/HTB16igeJFXXXXcqXpXX760XFXXXO.png");
        return fileServerService.uploadFileServer("text.png", inputStream);
    }*/


}
