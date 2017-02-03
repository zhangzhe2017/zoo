package com.alibaba.cardscanner.web;

import com.alibaba.cardscanner.infrastructure.dto.ResultDTO;
import com.alibaba.cardscanner.infrastructure.model.UserCard;
import com.alibaba.cardscanner.service.CardRecognizeService;
import com.alibaba.cardscanner.service.UserCardService;
import com.alibaba.cardscanner.service.openapi.auth.AuthHelper;
import com.alibaba.cardscanner.service.openapi.user.User;
import com.alibaba.cardscanner.web.utils.CommonUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rmy on 16/6/29.
 */
@Controller
@RequestMapping("/card")
public class UserCardController {

    @Autowired
    private UserCardService userCardService;

    @Autowired
    private CardRecognizeService cardRecognizeService;


    /*
     *  保存或新增名片信息
     *  @param usercard  名片信息数据
     *         code      用户身份码
     *         corpid    企业信息
     *
     *  @return  名片对应id
     */
    @RequestMapping("/saveOrUpdateUserCard")
    @ResponseBody
    public ResultDTO<Long> saveOrUpdateUserCard(HttpServletRequest request){
        Map<String,String[]> test = request.getParameterMap();
        for (String details : test.keySet()) {
            System.out.println(details);
        }
        ResultDTO<Long> resultDTO = new ResultDTO<>();
        resultDTO.setSuccess(Boolean.FALSE);

        try {
            String code =  request.getParameter("code");
            String corpId =  request.getParameter("corpid");
            String userId = request.getParameter("userId");
//            User user = CommonUtils.praseUserInfo(code, corpId);

            String userCardJson = request.getParameter("usercard");
            System.err.println("------------userCardJson: " + userCardJson);
            UserCard userCard = JSONObject.parseObject(userCardJson, UserCard.class);
            System.err.println("------------userCard: " + userCard);

            Long result = userCardService.saveOrUpdateUserCard(userCard, CommonUtils.pieceUserId(corpId, userId));
            if (result != 0) {
                resultDTO.setSuccess(Boolean.TRUE);
                resultDTO.setResult(result);
            }
        }catch (Exception e){
            resultDTO.setFailedInfo(e.getMessage());
            e.printStackTrace();
        }
        return resultDTO;
    }


    /*
     *  根据id删除对应名片
     *  @param id        名片在数据库中pk
     *         code      用户身份码
     *         corpid    企业信息
     *
     *  @return  名片对应id
     */
    @RequestMapping("/deleteUserCard")
    @ResponseBody
    public ResultDTO<Boolean> deleteUserCard(HttpServletRequest request){
        ResultDTO<Boolean> resultDTO = new ResultDTO<>();
        resultDTO.setSuccess(Boolean.FALSE);

        try{
            String code =  request.getParameter("code");
            String corpId =  request.getParameter("corpid");
//            User user = CommonUtils.praseUserInfo(code, corpId);
            String userId = request.getParameter("userId");

            Long cardId = Long.valueOf(request.getParameter("id"));


            boolean result = userCardService.deleteUserCard(cardId, CommonUtils.pieceUserId(corpId, userId));
            resultDTO.setSuccess(result);
            resultDTO.setResult(result);

        }catch (Exception e){
            resultDTO.setFailedInfo(e.getMessage());
            e.printStackTrace();
        }

        return resultDTO;
    }

    /*
     *  查询用户名片列表
     *  @param pagesize  页数
     *         code      用户身份码
     *         corpid    企业信息
     *         enddate   查询页结束时间
     *
     *  @return  名片list
     */
    @RequestMapping("/queryUserCards")
    @ResponseBody
    public ResultDTO<List<UserCard>> queryUserCards(HttpServletRequest request) throws ParseException {
        ResultDTO<List<UserCard>> resultDTO = new ResultDTO<>();
        resultDTO.setSuccess(Boolean.FALSE);
        try{
            int pageSize = Integer.valueOf(request.getParameter("pagesize"));
            String code =  request.getParameter("code");
            String corpId =  request.getParameter("corpid");
//            User user = CommonUtils.praseUserInfo(code, corpId);
            String userId = request.getParameter("userId");
            System.out.println("enddate : "+request.getParameter("enddate"));
            Long time = Long.valueOf(request.getParameter("enddate"));
            Date endDate = new Date(time);

//            System.out.println("code: "+ code+" corpid: "+corpId+" userInfo: "+ user.toString()+" time:"+time);

            List<UserCard> userCards = userCardService.queryUserCards(pageSize, endDate, CommonUtils.pieceUserId(corpId, userId));
            resultDTO.setSuccess(Boolean.TRUE);
            resultDTO.setResult(userCards);

        }catch (Exception e){
            resultDTO.setFailedInfo(e.getMessage());
            e.printStackTrace();
        }

        return resultDTO;
    }

    @RequestMapping("/findUserCardById")
    @ResponseBody
    public ResultDTO<UserCard> findUserCardById(HttpServletRequest request){
        ResultDTO<UserCard> resultDTO = new ResultDTO<>();
        resultDTO.setSuccess(Boolean.FALSE);
        try {
            Long cardId = Long.valueOf(request.getParameter("id"));

            UserCard userCard =  userCardService.findUserCardById(cardId);

            resultDTO.setSuccess(Boolean.TRUE);
            resultDTO.setResult(userCard);
        }catch (Exception e){
            resultDTO.setFailedInfo(e.getMessage());
            e.printStackTrace();
        }
        return resultDTO;
    }

    /*
     *  名片识别
     *  @param corpid    企业信息
     *         code      用户身份码
     *         picurl    手机扫描后获取的图片
     *
     *  @return  识别后的名片数据
     */
    @RequestMapping("/recognizeCard")
    @ResponseBody
    public ResultDTO<UserCard> recognizeCard(HttpServletRequest request){
        ResultDTO<UserCard> resultDTO = new ResultDTO<>();
        resultDTO.setSuccess(Boolean.FALSE);
        try {
            String picUrl = request.getParameter("picurl");

            System.out.println("picurl : "+ picUrl);
            resultDTO = cardRecognizeService.recognizeCardFromDD(picUrl);

        }catch (Exception e){
            resultDTO.setFailedInfo(e.getMessage());
            e.printStackTrace();
        }
        return resultDTO;
    }
}