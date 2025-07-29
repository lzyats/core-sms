package com.platform.common.sms.service.impl;


import cn.hutool.core.util.NumberUtil;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.platform.common.sms.service.SmsService;
import com.platform.common.sms.vo.SmsVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * 腾讯云短信
 */
@Slf4j
@Service("smsTxService")
@Configuration
@ConditionalOnProperty(prefix = "sms", name = "smsType", havingValue = "tx")
public class SmsTxServiceImpl implements SmsService {

    /**
     * appId
     */
    @Value("${sms.appId}")
    private String appId;
    /**
     * appKey
     */
    @Value("${sms.appKey}")
    private String appKey;
    /**
     * 签名
     */
    @Value("${sms.smsSign}")
    private String smsSign;
    /**
     * 模板
     */
    @Value("${sms.template}")
    private String template;

    @Override
    public SmsVo sendMessage(String mobile, String code) {
        return sendMessage(template, mobile, code);
    }

    @SneakyThrows
    @Override
    public SmsVo sendMessage(String template, String mobile, String code) {
        SmsVo smsVo = new SmsVo();
        try {
            String[] params = {code};
            SmsSingleSender smsSingleSender = new SmsSingleSender(NumberUtil.parseInt(appId), appKey);
            SmsSingleSenderResult result = smsSingleSender.sendWithParam("86", mobile, NumberUtil.parseInt(template), params, smsSign, "", "");
            smsVo.setResult(0 == result.result)
                    .setBody(result.errMsg);
        } catch (Exception e) {
            log.error("短信发送失败，请稍后重试", e);
            smsVo.setResult(false)
                    .setBody(e.getMessage());
        }
        return smsVo;
    }

}
