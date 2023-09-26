package com.lee.ssxy.cart.receiver;

import com.lee.ssxy.cart.service.CartInfoService;
import com.lee.ssxy.common.mq.constant.MqConst;
import com.rabbitmq.client.Channel;
import org.aspectj.bridge.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class CartReceiver {
    @Resource
    public CartInfoService cartInfoService;
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConst.QUEUE_DELETE_CART,durable = "true")
            ,exchange = @Exchange(value = MqConst.EXCHANGE_CANCEL_ORDER_DIRECT)
            ,key = {MqConst.QUEUE_DELETE_CART}))
    public void deleteCartChecked(String userId, Message message, Channel channel){
        Long userIdL = Long.valueOf(userId);
        if(userId != null){
            cartInfoService.deleteCartChecked(userIdL);
        }
    }
}
