package com.alibaba.cardscanner.infrastructure.http.model;


import org.apache.http.entity.ContentType;

/**
 * Created by rmy on 16/6/29.
 */
public class MultiPartParam<T> {
    private String name;
    private String fileName;
    private ContentType contentType;
    private T body;

    public MultiPartParam() {
    }

    public MultiPartParam(String name, T body) {
        this.name = name;
        this.body = body;
    }

    public MultiPartParam(String name, T body, ContentType contentType, String fileName) {
        this.name = name;
        this.fileName = fileName;
        this.contentType = contentType;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
