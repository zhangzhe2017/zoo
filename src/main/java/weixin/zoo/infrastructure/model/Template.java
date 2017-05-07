package weixin.zoo.infrastructure.model;

import java.util.Date;

public class Template {
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String isDelete;

    private String templateName;

    private String templateType;

    private String filedIds;

    private String templateOwner;

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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName == null ? null : templateName.trim();
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType == null ? null : templateType.trim();
    }

    public String getFiledIds() {
        return filedIds;
    }

    public void setFiledIds(String filedIds) {
        this.filedIds = filedIds == null ? null : filedIds.trim();
    }

    public String getTemplateOwner() {
        return templateOwner;
    }

    public void setTemplateOwner(String templateOwner) {
        this.templateOwner = templateOwner == null ? null : templateOwner.trim();
    }
}