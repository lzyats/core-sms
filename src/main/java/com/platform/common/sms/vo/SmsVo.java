package com.platform.common.sms.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class SmsVo {

    /**
     * 状态
     */
    private boolean result;
    /**
     * 内容
     */
    private String body;
    /**
     * 验证码
     */
    private String code;

}
