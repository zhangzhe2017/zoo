package com.alibaba.cardscanner.infrastructure.enums;

/**
 * Created by rmy on 16/6/26.
 */
public enum YesNoEnum {
    Yes("y"),
    No("n");

    private String val;

    YesNoEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
