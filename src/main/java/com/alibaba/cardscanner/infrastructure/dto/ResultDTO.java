package com.alibaba.cardscanner.infrastructure.dto;

import java.io.Serializable;

/**
 * Created by rmy on 16/6/29.
 */
public class ResultDTO<T> implements Serializable{
    private static final long serialVersionUID = -1140633423652230660L;

    private T result;
    private boolean success;
    private String failedInfo;
    private int failedCode;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
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
