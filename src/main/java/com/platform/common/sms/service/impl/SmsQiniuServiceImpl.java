package com.platform.common.sms.service.impl;

import com.platform.common.sms.service.SmsService;
import com.platform.common.sms.vo.SmsVo;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 七牛云短信
 */
@Slf4j
@Service("smsQiniuService")
@Configuration
@ConditionalOnProperty(prefix = "sms", name = "smsType", havingValue = "qiniu")
public class SmsQiniuServiceImpl implements SmsService {

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
     * 模板id
     */
    @Value("${sms.template}")
    private String template;

    /**
     * 获取Auth
     */
    private Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }

    @Override
    public SmsVo sendMessage(String mobile, String code) {
        return sendMessage(template, mobile, code);
    }

    @Override
    public SmsVo sendMessage(String template, String mobile, String code) {
        Auth auth = getAuth();
        SmsManager smsManager = new SmsManager(auth);
        SmsVo smsVo = new SmsVo();
        try {
            Map<String, String> param = new HashMap<>();
            param.put("code", code);
            Response response = smsManager.sendSingleMessage(template, mobile, param);
            smsVo.setResult(response.isOK())
                    .setBody(response.bodyString());
        } catch (QiniuException e) {
            log.error("短信发送失败，请稍后重试", e);
            smsVo.setResult(false)
                    .setBody(e.getMessage());
        }
        return smsVo;
    }

}
