/*
信息:
*/
package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemListener {
   @Autowired
  private   SearchService searchService;
  @RabbitListener(bindings =
      @QueueBinding(value =@Queue(value = "search.item.insert.queue",durable = "true"),
      exchange = @Exchange(name = "ly.item.exchange",type = ExchangeTypes.TOPIC),
      key = {"item.insert","item.update"}
      )
  )
  public void insertOrUpdate(Long spuId){
    if (spuId==null){
        return;
    }
   searchService.createOrUpdateIndex(spuId);
  }

}
