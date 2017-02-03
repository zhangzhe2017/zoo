package com.alibaba.cardscanner.infrastructure.dto;

import java.util.Map;

/**
 * Created by rmy on 16/6/29.
 */
public class CardRecognizeDTO {
    private Map<String, Object> result;
    private boolean success;
    private String failedInfo;
    private int failedCode;

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailedInfo() {
        return failedInfo;
    }

    public void setFailedInfo(String failedInfo) {
        this.failedInfo = failedInfo;
    }

    public int getFailedCode() {
        return failedCode;
    }

    public void setFailedCode(int failedCode) {
        this.failedCode = failedCode;
    }
}
