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
    
    /*检验工程师固定为3 这个角色*/
    int    ENGINEER_ROLE_ID= 3;
    
    /*测试工程师角色*/
    int TEST_ENGINEER_ROLE_ID = 4;
    
    /*下卡任务、 测试任务、调整任务、审核任务、分配任务*/
    String DISCARD_TASK = "下卡";
    String TEST_TASK = "测试";
    String MODIFY_TASK="修改";
    String VERIFY_TASK = "审核";
    String MAIL_TASK = "邮寄";
}
