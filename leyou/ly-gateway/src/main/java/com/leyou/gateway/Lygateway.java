/*
信息:
*/
package com.leyou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringCloudApplication
@EnableDiscoveryClient
public class Lygateway {
    public static void main(String[] args) {
        SpringApplication.run(Lygateway.class);
    }
}
