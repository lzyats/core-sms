package com.platform.common.sms.service.impl;


import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.platform.common.sms.service.SmsService;
import com.platform.common.sms.vo.SmsVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云短信
 */
@Slf4j
@Service("smsAliService")
@Configuration
@ConditionalOnProperty(prefix = "sms", name = "smsType", havingValue = "ali")
public class SmsAliServiceImpl implements SmsService {

    /**
     * accessKey
     */
    @Value("${sms.accessKey}")
    private String accessKey;
    /**
     * secretKey
     */
    @Value("${sms.secretKey}")
    private String secretKey;
    /**
     * 签名
     */
    @Value("${sms.signName}")
    private String signName;
    /**
     * 模板id
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
        Config config = new Config()
                .setAccessKeyId(accessKey)
                .setAccessKeySecret(secretKey)
                .setEndpoint("dysmsapi.aliyuncs.com");
        Client client = new Client(config);
        Map<String, String> param = new HashMap<>();
        param.put("code", code);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(signName)
                .setTemplateCode(template)
                .setPhoneNumbers(mobile)
                .setTemplateParam(JSONUtil.toJsonStr(param));
        SmsVo smsVo = new SmsVo();
        try {
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
            smsVo.setResult(200 == response.getStatusCode())
                    .setBody(JSONUtil.toJsonStr(response.getBody()));
        } catch (Exception e) {
            log.error("短信发送失败，请稍后重试", e);
            smsVo.setResult(false)
                    .setBody(e.getMessage());
        }
        return smsVo;
    }

}
