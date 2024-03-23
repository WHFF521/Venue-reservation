package com.ygstar.backend.sys.consumer;

import com.ygstar.backend.sys.entity.VenueReservation;
import com.ygstar.backend.sys.mapper.VenueReservationMapper;
import com.ygstar.backend.sys.service.IVenueReservationService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "venue-reservation", consumerGroup = "venue-reservation-consumer")
public class VenueReservationConsumer implements RocketMQListener<VenueReservation>, RocketMQPushConsumerLifecycleListener {
//    @Autowired
//    private VenueReservationMapper venueReservationMapper;
    private static final int CONCURRENT_CONSUMERS = 4; // 并发消费者数量
    @Autowired
    private IVenueReservationService venueReservationService;
    @Override
    public void onMessage(VenueReservation venueReservation) {
        System.out.println("收到预约消息：" + venueReservation.getIdCardNum());
//        venueReservationMapper.insertVenueReservation(venueReservation);
        venueReservationService.insertVenueReservation(venueReservation);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setConsumeThreadMin(CONCURRENT_CONSUMERS);
        consumer.setConsumeThreadMax(CONCURRENT_CONSUMERS);
    }
}
