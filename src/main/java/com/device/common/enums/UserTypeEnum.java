package com.device.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum UserTypeEnum {

    ADMIN(1, "管理员"),

    OPERATOR(2, "操作员");

    private int code;
    private String val;

    UserTypeEnum(int code, String description) {
        this.code = code;
        this.val = description;
    }

    public int getCode() {
        return code;
    }

    public String getVal() {
        return val;
    }

    public static List<UserTypeEnum> getAllValues() {
        List<UserTypeEnum> result = new ArrayList<>();
        for (UserTypeEnum item : UserTypeEnum.values()) {
            result.add(item);
        }

        return result;
    }

    public static boolean isAdmin(String userTypeName) {
        return userTypeName!=null && ADMIN.getVal().equals(userTypeName);
    }

    public static UserTypeEnum getEnumByVal(String val) {
        for (UserTypeEnum typeEnum : UserTypeEnum.values()) {
            if (typeEnum.getVal().equalsIgnoreCase(val)) {
                return typeEnum;
            }
        }

        return null;
    }
}
