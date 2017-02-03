package com.alibaba.cardscanner.infrastructure.http.utils;

import org.apache.commons.lang.StringUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rmy on 16/6/29.
 */
public class HttpUtils {
    public static String buildGetUrl(String gateway, Map<String, String> params){
        StringBuilder getUrl = new StringBuilder();
        getUrl.append(gateway);
        if(params == null){
            return getUrl.toString();
        }

        if(!StringUtils.endsWith(gateway,"?")) {
            getUrl.append("?");
        }
        List<String> keys = new ArrayList<String>(params.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            try {
                value = URLEncoder.encode(value, "utf-8");
            }catch(Exception ignored) {

            }
            if (i == keys.size() - 1) {
                getUrl.append(key).append("=").append(value);
            } else {
                getUrl.append(key).append("=").append(value).append("&");
            }
        }

        return getUrl.toString();
    }
}
