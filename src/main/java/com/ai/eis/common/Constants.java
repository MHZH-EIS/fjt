package com.ai.eis.common;

public interface Constants {

    String SESSION_MEMBER_KEY = "s_member";
    String WEB_SOCKET_USERNAME = "socket_member";
    String SESSION_VERIFY_CODE_KEY = "verify_code";
    String SESSION_EIS_KEY = "s_eis";

    int CONTRACT_RESOURCE_ID = 1;
    int DEVICE_RESOURCE_ID = 2;
    int STANDARD_RESOURCE_ID = 3;

    /**
     * 合同录入状态
     */
    String PROJECT_TYPING = "1";

    /**
     * 流程中状态
     */
    String PROJECT_PROCESSING = "2";

    /**
     * 归档状态
     */
    String PROJECT_FINISH = "3";
}
