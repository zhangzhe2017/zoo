package com.alibaba.cardscanner.infrastructure.dto;

import java.util.Date;

/**
 * Created by rmy on 16/6/28.
 */
public class PageDTO {
    /**
     * 分页大小，默认20
     */
    private int pageSize = 20;
    private Date endDate;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
