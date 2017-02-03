package com.alibaba.cardscanner.infrastructure.http;

import com.alibaba.cardscanner.infrastructure.http.model.HttpRequest;
import com.alibaba.cardscanner.infrastructure.http.model.MultiPartParam;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rmy on 16/6/29.
 */
public class HttpProtocolHandler {
    /**
     * 发送get http请求
     * @param url 拼接好参数的url
     * @return
     */
    public static String sendGetRequest(String url){
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    return EntityUtils.toString(httpEntity);
                }
        }catch (IOException e){
            //todo add log
            e.printStackTrace();
        }finally {
            try {
                if(response != null) {
                    response.close();
                }
                httpClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 发送post http请求
     * @param url
     * @param map 参数
     * @return
     */
    public static String sendPostRequest(String url, Map<String, String> map) {
        if(StringUtils.isBlank(url)){
            return "";
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            //给参数赋值
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            if(httpEntity != null) {
                return EntityUtils.toString(httpEntity);
            }
        } catch (IOException e) {
            //todo add log
            e.printStackTrace();
        }finally {
            try {
                if(response != null) {
                    response.close();
                }
                httpClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return "";
    }

    /**
     * 发送post http请求
     * @param httpRequest
     * @return
     */
    public static String sendPostRequest(HttpRequest httpRequest) {
        if(StringUtils.isBlank(httpRequest.getUrl())){
            return "";
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //set url
        HttpPost httppost = new HttpPost(httpRequest.getUrl());

        //set param
        if(httpRequest.getParams() != null) {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : httpRequest.getParams().entrySet()) {
                //给参数赋值
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            httppost.setEntity(entity);
        }
        //set headers
        if(httpRequest.getHeaders() != null){
            httppost.setHeaders(httpRequest.getHeaders());
        }

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            if(httpEntity != null) {
                return EntityUtils.toString(httpEntity);
            }
        } catch (IOException e) {
            //todo add log
            e.printStackTrace();
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            }catch (IOException e){

            }
        }

        return "";
    }

    /**
     * 发送multipart 请求
     * @param httpRequest
     * @return
     */
    public static String multiPartPost(HttpRequest httpRequest){
        if(StringUtils.isBlank(httpRequest.getUrl())){
            return "";
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //set url
        HttpPost httppost = new HttpPost(httpRequest.getUrl());

        //set header
        if(httpRequest.getHeaders() != null){
            httppost.setHeaders(httpRequest.getHeaders());
        }
        //add multiPart params
        if(httpRequest.getMultiPartParams() != null){
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for(MultiPartParam multiPartParam : httpRequest.getMultiPartParams()){
                if(multiPartParam.getBody() instanceof String){
                    if(multiPartParam.getContentType() != null){
                        builder.addTextBody(multiPartParam.getName(), (String) multiPartParam.getBody(), multiPartParam.getContentType());
                    }else {
                        builder.addTextBody(multiPartParam.getName(), (String) multiPartParam.getBody());
                    }
                }else if(multiPartParam.getBody() instanceof byte[]){
                    if(multiPartParam.getContentType() != null){
                        builder.addBinaryBody(multiPartParam.getName(), (byte[]) multiPartParam.getBody(), multiPartParam.getContentType(), multiPartParam.getFileName());
                    }else {
                        builder.addBinaryBody(multiPartParam.getName(), (byte[]) multiPartParam.getBody());
                    }
                }
            }
            httppost.setEntity(builder.build());
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            if(httpEntity != null) {
                return EntityUtils.toString(httpEntity);
            }
        } catch (IOException e) {
            //todo add log
            e.printStackTrace();
        }finally {
            try{
                if(response != null) {
                    response.close();
                }
                httpClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return "";
    }

    /**
     * 获取url请求返回的byte[]信息
     * @param url
     * @return
     */
    public static byte[] getUrlBytes(String url){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
//                    return httpEntity.getContent();
                return EntityUtils.toByteArray(httpEntity);
            }
        }catch (IOException e){
            //todo add log
            e.printStackTrace();
        }finally {
            try {
                if(response != null) {
                    response.close();
                }
                httpClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
