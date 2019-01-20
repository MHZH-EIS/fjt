package com.ai.eis.common;

import org.apache.commons.lang.StringUtils;

public class Tools {

    public static String liker(String field) {
        if (!StringUtils.isEmpty(field)) {
            field = "%" + field + "%";
        }
        return field;
    }

}
