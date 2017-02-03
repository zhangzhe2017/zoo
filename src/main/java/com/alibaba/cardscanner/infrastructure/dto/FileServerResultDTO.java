package com.alibaba.cardscanner.infrastructure.dto;

import java.io.Serializable;

/**
 * Created by rmy on 16/6/29.
 */
public class FileServerResultDTO implements Serializable{
    private static final long serialVersionUID = 6785763527578607950L;

    private String fs_url;
    private String code;
    private String url;
    private String size;
    private String width;
    private String hash;
    private String height;

    public String getFs_url() {
        return fs_url;
    }

    public void setFs_url(String fs_url) {
        this.fs_url = fs_url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
