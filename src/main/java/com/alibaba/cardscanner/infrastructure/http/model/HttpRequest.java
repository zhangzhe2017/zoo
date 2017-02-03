package com.alibaba.cardscanner.infrastructure.http.model;

import org.apache.http.Header;

import java.util.List;
import java.util.Map;

/**
 * Created by rmy on 16/6/29.
 */
public class HttpRequest {
    private String url;
    private Map<String, String> params;
    private Header[] headers;
    private List<MultiPartParam> multiPartParams;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header... headers) {
        this.headers = headers;
    }

    public List<MultiPartParam> getMultiPartParams() {
        return multiPartParams;
    }

    public void setMultiPartParams(List<MultiPartParam> multiPartParams) {
        this.multiPartParams = multiPartParams;
    }
}
