/*
信息:
*/
package com.leyou.mq;

import com.leyou.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageListener {
     @Autowired
     private PageService pageService;
     @RabbitListener(bindings =
     @QueueBinding(
        value = @Queue(value = "page.item.insert.queue",durable ="true"),
        exchange = @Exchange(name = "ly.item.exchange",type = ExchangeTypes.TOPIC),
        key = {"item.insert","item.update"}
     ))
    public void createHtml(Long spuId){
         System.out.println(spuId+"测试");
         pageService.createTemplateHtml(spuId);
     }
}
