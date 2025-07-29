package com.platform.common.sms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.platform.common.sms.service.SmsService;
import com.platform.common.sms.vo.SmsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * 邮箱
 */
@Slf4j
@Service("smsEmailService")
@Configuration
@ConditionalOnProperty(prefix = "sms", name = "smsType", havingValue = "email")
public class SmsEmailServiceImpl implements SmsService {

    /**
     * 用户名
     */
    @Value("${sms.user}")
    private String user;
    /**
     * 密码
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

    @Value("${sms.host:smtp.ym.163.com}")
    private String host;

    @Value("${sms.port:25}")
    private String port;

    @Override
    public SmsVo sendMessage(String mobile, String code) {
        return sendMessage(template, mobile, code);
    }

    @Override
    public SmsVo sendMessage(String template, String mobile, String code) {
        MailAccount account = new MailAccount();
        account.setHost(host);
        account.setPort(Integer.valueOf(port));
        account.setFrom(StrUtil.format("{}<{}>", signName, user));
        account.setUser(user);
        account.setPass(pass);
        String subject = StrUtil.format("验证码：{}", code);
        String content = StrUtil.format(template, code);
        SmsVo smsVo = new SmsVo();
        try {
            String body = MailUtil.send(account, mobile, subject, content, false);
            smsVo.setResult(true)
                    .setBody(body);
        } catch (Exception e) {
            log.error("邮件发送失败，请稍后重试", e);
            smsVo.setResult(false)
                    .setBody(e.getMessage());
        }
        return smsVo;
    }

}
