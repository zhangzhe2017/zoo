package com.alibaba.cardscanner.infrastructure.model;

import com.alibaba.cardscanner.infrastructure.enums.YesNoEnum;

import java.util.Date;

/**
 * Created by rmy on 16/6/25.
 */
public class UserCard {
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private String isDeleted;

    private String userId;

    private String fileServerUri;

    private String cardInfo;

    private String fileName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileServerUri() {
        return fileServerUri;
    }

    public void setFileServerUri(String fileServerUri) {
        this.fileServerUri = fileServerUri;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void beforeInsert(String operator){
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
        this.isDeleted = YesNoEnum.No.getVal();
        this.creator = operator;
        this.modifier = operator;
    }

    public void beforeUpdate(String operator){
        this.gmtModified = new Date();
        this.modifier = operator;
    }

    public void beforeDelete(String operator){
        this.gmtModified = new Date();
        this.modifier = operator;
        this.isDeleted = YesNoEnum.Yes.getVal();
    }
}
