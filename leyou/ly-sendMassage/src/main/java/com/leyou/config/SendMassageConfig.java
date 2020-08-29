/*
信息:
*/
package com.leyou.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "ly.massage")
public class SendMassageConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String verifyCodeTemplate;
}
