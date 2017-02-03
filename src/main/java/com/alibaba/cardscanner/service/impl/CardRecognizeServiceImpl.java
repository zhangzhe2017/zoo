package com.alibaba.cardscanner.service.impl;

import com.alibaba.cardscanner.infrastructure.dto.CardRecognizeDTO;
import com.alibaba.cardscanner.infrastructure.dto.FileServerResultDTO;
import com.alibaba.cardscanner.infrastructure.dto.RecognizeParamDTO;
import com.alibaba.cardscanner.infrastructure.dto.ResultDTO;
import com.alibaba.cardscanner.infrastructure.http.HttpProtocolHandler;
import com.alibaba.cardscanner.infrastructure.http.utils.HttpUtils;
import com.alibaba.cardscanner.infrastructure.model.UserCard;
import com.alibaba.cardscanner.service.CardRecognizeService;
import com.alibaba.cardscanner.service.FileServerService;
import com.alibaba.cardscanner.service.openapi.Env;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rmy on 16/6/29.
 */
@Service
@EnableAutoConfiguration
public class CardRecognizeServiceImpl implements CardRecognizeService {
    @Value("${recognize.gateway}")
    private String gateway;

    @Autowired
    private FileServerService fileServerService;

    @Override
    public ResultDTO<Map<String, Object>> recognizeCard(String fileUri) {
        ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<>();
        resultDTO.setSuccess(Boolean.FALSE);

        if(StringUtils.isBlank(gateway)){
            resultDTO.setFailedInfo("get recognize gateway failed.");
            return resultDTO;
        }

        String recognizeResult = getRecognizeJson(fileUri);
        System.err.println("get recognize json, result: " + recognizeResult);
        try {
            CardRecognizeDTO cardRecognizeDTO =JSONObject.parseObject(recognizeResult, CardRecognizeDTO.class);
            if(cardRecognizeDTO != null){
                Map<String, Object> result = cardRecognizeDTO.getResult();
                //成功则解析结果，抽取mText并重新封装
                Boolean success = (Boolean)result.get("success");
                if(success == null || success){
                    System.err.println(JSON.toJSONString(result));
                    Map<String, Object> resultMap = getRecognizeResultMap(result);
                    resultDTO.setSuccess(Boolean.TRUE);
                    resultDTO.setResult(resultMap);
                }else {
                    //失败返回失败信息
                    resultDTO.setFailedInfo(String.valueOf(result.get("info")));
                }
            }
        }catch (Exception e){
            //todo add log
            resultDTO.setFailedInfo("card recognize error, exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return resultDTO;
    }

    @Override
    public ResultDTO<UserCard> recognizeCardFromDD(String dingFileUrl) {
        ResultDTO<UserCard> resultDTO = new ResultDTO<>();
        resultDTO.setSuccess(Boolean.FALSE);

        //获取钉钉文件内容byte[]
        byte[] dingFileContent = HttpProtocolHandler.getUrlBytes(dingFileUrl);
        if(dingFileContent == null){
            resultDTO.setFailedInfo("get dingding file content failed.");
            return resultDTO;
        }

        //上传file server
        FileServerResultDTO fileServerResultDTO = null;
        try {
            String fileName = dingFileUrl.substring(dingFileUrl.lastIndexOf("/")+1);
            fileServerResultDTO = fileServerService.uploadFileServer(fileName, dingFileContent);
            if (fileServerResultDTO == null || !StringUtils.equals(fileServerResultDTO.getCode(), "0")) {
                String fileInfo = "upload to file server failed."
                        + (fileServerResultDTO != null ? " code: " + fileServerResultDTO.getCode() : "");
                resultDTO.setFailedInfo(fileInfo);
                return resultDTO;
            }
        }catch (RuntimeException e){
            resultDTO.setFailedInfo(e.getMessage());
            return resultDTO;
        }

        //调用识别接口
        ResultDTO<Map<String, Object>> recognizeResult = recognizeCard(fileServerResultDTO.getFs_url());
        if(!recognizeResult.isSuccess()){
            resultDTO.setFailedInfo(recognizeResult.getFailedInfo());
            return resultDTO;
        }

        //封装识别结果及文件url信息
        UserCard userCard = new UserCard();
        userCard.setCardInfo(JSON.toJSONString(recognizeResult.getResult()));
        userCard.setFileName(fileServerResultDTO.getFs_url());
        userCard.setFileServerUri(fileServerResultDTO.getUrl());
        resultDTO.setSuccess(Boolean.TRUE);
        resultDTO.setResult(userCard);
        System.err.println("----------recognize resultDTO: " + JSON.toJSONString(resultDTO) + "----------");

        return resultDTO;
    }

    /**
     * 抽取结果中mText字段重新封装，过滤坐标信息
     * @param result
     * @return
     */
    private Map<String, Object> getRecognizeResultMap(Map<String, Object> result) {
        Map<String, Object> resultMap = new HashMap<>();
        for(String key : result.keySet()){
            String value =String.valueOf(result.get(key));
            if(StringUtils.isNotBlank(value)){
                try {
                    List<RecognizeParamDTO> recognizeParamDTOs = JSONArray.parseArray(value, RecognizeParamDTO.class);
                    if (recognizeParamDTOs != null) {
                        List<String> texts = new ArrayList<>();
                        for (RecognizeParamDTO recognizeParamDTO : recognizeParamDTOs) {
                            texts.add(recognizeParamDTO.getmText());
                        }
                        resultMap.put(key, texts);
                    }
                }catch (Exception ignore){

                }
            }
        }
        return resultMap;
    }

    /**
     * json格式
     * 成功：
     * {
     *     result:{
     *          faxes:[{
     *              mText:"xxx",
     *              yPos:xxx,
     *              height:xxx,
     *              xPos:xxx,
     *              width:xxx
     *          }],
     *          ...
     *          rotation:{
     *              direction: "0"
     *          }
     *     }
     * }
     * 失败:{
     *     result:{
     *         success:false,
     *         info:"xxx"
     *     }
     * }
     * @param fileServiceUri
     * @return
     */
    private String getRecognizeJson(String fileServiceUri) {
        Map<String, String> params = new HashMap<>();
        params.put("name", fileServiceUri);
        params.put("type", "0");
        //通过识别http接口，获取识别接口json
        return HttpProtocolHandler.sendGetRequest(HttpUtils.buildGetUrl(gateway, params));
    }


}
