/*
信息:
*/
package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringCloudApplication
@EnableEurekaServer
public class LyRegister {
    public static void main(String[] args) {
        SpringApplication.run(LyRegister.class);
    }
}
