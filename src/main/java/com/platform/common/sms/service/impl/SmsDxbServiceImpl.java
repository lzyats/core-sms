package com.platform.common.sms.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.platform.common.sms.service.SmsService;
import com.platform.common.sms.vo.SmsVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 短信宝短信
 */
@Slf4j
@Service("smsDxbService")
@Configuration
@ConditionalOnProperty(prefix = "sms", name = "smsType", havingValue = "dxb")
public class SmsDxbServiceImpl implements SmsService {

    /**
     * 账号
     */
    @Value("${sms.user}")
    private String user;
    /**
     * 密码或key
     */
    @Value("${sms.pass}")
    private String pass;
    /**
     * 签名
     */
    @Value("${sms.signName}")
    private String signName;
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
        if (StringUtils.isEmpty(template)) {
            template = "【{}】您的验证码为：{}，该验证码5分钟内有效，请勿泄露给他人。";
        }
        SmsVo smsVo = new SmsVo();
        try {
            String content = StrUtil.format(template, signName, code);
            String httpUrl = "https://api.smsbao.com/sms?";
            StringBuffer httpArg = new StringBuffer();
            httpArg.append("u=").append(user).append("&");
            httpArg.append("p=").append(pass).append("&");
            httpArg.append("m=").append(mobile).append("&");
            httpArg.append("c=").append(URLUtil.encode(content));
            String json = HttpUtil.get(httpUrl + httpArg.toString());
            smsVo.setResult("0".equals(json))
                    .setBody(json);
        } catch (Exception e) {
            log.error("短信发送失败，请稍后重试", e);
            smsVo.setResult(false)
                    .setBody(e.getMessage());
        }
        return smsVo;
    }

}
