/*
信息:
*/
package com.leyou.test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class SendTest {
   @Autowired
    private AmqpTemplate amqpTemplate;
   @Test
    public void sendMassage(){
      Map map =new HashMap();
      map.put("phone","13158797816");
      map.put("code","0154");
      amqpTemplate.convertAndSend("ly.send.exchange","send.verify.code",map);

   }
}
