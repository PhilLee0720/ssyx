package com.lee.ssxy.search.receiver;

import com.lee.ssxy.common.mq.constant.MqConst;
import com.lee.ssxy.search.service.SkuApiService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class SkuReceiver {
    @Resource
    private SkuApiService skuApiService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_GOODS_UPPER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT),
            key = {MqConst.ROUTING_GOODS_UPPER}
    ))
    public void upperSku(String skuId, Message message, Channel channel) throws IOException {
        if (skuId != null) {
            Long res = Long.valueOf(skuId);
            skuApiService.upperSku(res);
        }
        /**
         * 第一个参数：表示收到的消息的标号
         * 第二个参数：如果为true表示可以签收多个消息
         */
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 商品下架
     * @param skuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_GOODS_LOWER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT),
            key = {MqConst.ROUTING_GOODS_LOWER}
    ))
    public void lowerSku(String  skuId, Message message, Channel channel) throws IOException {
        if (skuId != null) {
            Long res = Long.valueOf(skuId);
            skuApiService.lowerSku(res);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
