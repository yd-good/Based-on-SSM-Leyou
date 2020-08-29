/*
信息:
*/
package com.leyou.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;
@ConfigurationProperties(prefix = "project.upload")
@Data
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;
}
