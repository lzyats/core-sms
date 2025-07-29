package com.platform.common.sms.service.impl;

import com.platform.common.sms.service.SmsService;
import com.platform.common.sms.vo.SmsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * 本地短信
 */
@Slf4j
@Service("smsLocalService")
@Configuration
@ConditionalOnProperty(prefix = "sms", name = "smsType", havingValue = "local")
public class SmsLocalServiceImpl implements SmsService {

    @Override
    public SmsVo sendMessage(String mobile, String code) {
        return sendMessage("template", mobile, code);
    }

    @Override
    public SmsVo sendMessage(String template, String mobile, String code) {
        return new SmsVo()
                .setResult(true)
                .setCode(code)
                .setBody("special");
    }

}
