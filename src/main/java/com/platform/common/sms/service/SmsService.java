package com.platform.common.sms.service;

import com.platform.common.sms.vo.SmsVo;

/**
 * 短信服务
 */
public interface SmsService {

    /**
     * 发送短信
     */
    SmsVo sendMessage(String mobile, String code);

    /**
     * 发送短信
     */
    SmsVo sendMessage(String template, String mobile, String code);

}
