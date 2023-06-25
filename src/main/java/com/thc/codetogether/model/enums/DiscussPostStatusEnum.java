package com.thc.codetogether.model.enums;

/**
 * 队伍状态枚举
 */
public enum DiscussPostStatusEnum {

    NORMAL(0, "正常"),
    PRIME(1, "精华"),
    BLOCK(2, "拉黑");

    private int value;

    private String text;

    public static DiscussPostStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        DiscussPostStatusEnum[] values = DiscussPostStatusEnum.values();
        for (DiscussPostStatusEnum discussPostStatusEnum : values) {
            if (discussPostStatusEnum.getValue() == value) {
                return discussPostStatusEnum;
            }
        }
        return null;
    }

    DiscussPostStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
