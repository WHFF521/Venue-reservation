package com.ygstar.backend.sys.consumer;

import com.ygstar.backend.sys.entity.ActivityReservation;
import com.ygstar.backend.sys.entity.VenueReservation;
import com.ygstar.backend.sys.service.IActivityReservationService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "act-reservation", consumerGroup = "act-reservation-consumer")
public class ActReservationConsumer implements RocketMQListener<ActivityReservation>, RocketMQPushConsumerLifecycleListener {
    private static final int CONCURRENT_CONSUMERS = 4; // 并发消费者数量
    @Autowired
    private IActivityReservationService iActivityReservationService;

    @Override
    public void onMessage(ActivityReservation activityReservation) {
        System.out.println("收到预约消息：" + activityReservation.getIdCardNumber());
        iActivityReservationService.insertReservation(activityReservation.getActivityId(),activityReservation.getUserMail(),activityReservation.getIdCardNumber());
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setConsumeThreadMin(CONCURRENT_CONSUMERS);
        consumer.setConsumeThreadMax(CONCURRENT_CONSUMERS);
    }
}
