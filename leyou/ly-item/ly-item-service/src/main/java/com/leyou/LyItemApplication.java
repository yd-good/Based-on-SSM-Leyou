/*
信息:
*/
package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringCloudApplication
@MapperScan("com.leyou.item.mapper")
public class LyItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyItemApplication.class);
    }
}
