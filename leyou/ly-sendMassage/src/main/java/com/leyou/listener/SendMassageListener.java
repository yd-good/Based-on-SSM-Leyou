/*
信息:
*/
package com.leyou.listener;

import com.alibaba.fastjson.JSONObject;
import com.leyou.config.SendMassageConfig;
import com.leyou.utils.SendMassageUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@EnableConfigurationProperties(SendMassageConfig.class)
@Component
public class SendMassageListener {
    @Autowired
    private SendMassageConfig sendMassageConfig;
    @Autowired
    private SendMassageUtils sendMassageUtils;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "send.verify.code.queue",durable ="true"),
            exchange = @Exchange(name = "ly.send.exchange",type = ExchangeTypes.TOPIC),
            key = "send.verify.code"
    ))
    public void listenSendMassage(Map<String,String> map){
        if (CollectionUtils.isEmpty(map)){
          return;
       }
        String phone = map.remove("phone");
       if (StringUtils.isEmpty(phone)){
           return;
       }
        sendMassageUtils.sendMassage(phone,sendMassageConfig.getSignName(),sendMassageConfig.getVerifyCodeTemplate(),JSONObject.toJSONString(map) );
    }

}
