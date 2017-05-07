package weixin.zoo.infrastructure.model;

import java.util.Date;

public class Form {
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String isDelete;

    private Long templateId;

    private String formName;

    private String formValue;

    private String formOwner;

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

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete == null ? null : isDelete.trim();
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName == null ? null : formName.trim();
    }

    public String getFormValue() {
        return formValue;
    }

    public void setFormValue(String formValue) {
        this.formValue = formValue == null ? null : formValue.trim();
    }

    public String getFormOwner() {
        return formOwner;
    }

    public void setFormOwner(String formOwner) {
        this.formOwner = formOwner == null ? null : formOwner.trim();
    }
}