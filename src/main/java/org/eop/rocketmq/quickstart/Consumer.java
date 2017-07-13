package org.eop.rocketmq.quickstart;

import java.util.List;
import java.util.Set;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @author lixinjie
 * @since 
 */
public class Consumer {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerGroup1");
		//设置namesrv
		consumer.setNamesrvAddr("192.168.95.57:9876;192.168.95.52:9876");
		//设置从什么地方开始消费
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		//并发消费时，允许偏移量的最大跨度
		consumer.setConsumeConcurrentlyMaxSpan(2000);
		//设置消费线程最小数目
		consumer.setConsumeThreadMin(20);
		//设置消费线程最大数据
		consumer.setConsumeThreadMax(64);
		//设置批量消费大小
		consumer.setConsumeMessageBatchMaxSize(1);
		//设置集群消费或广播消费，默认集群消费
		consumer.setMessageModel(MessageModel.CLUSTERING);
		//设置消费者偏移量持久化的间隔
		consumer.setPersistConsumerOffsetInterval(1000 * 5);
		//设置从namesrv拉取topic信息的间隔
		consumer.setPollNameServerInteval(1000 * 30);
		//当每次拉取时，是否更新注册关系
		consumer.setPostSubscriptionWhenPull(false);
		//批拉取大小
		consumer.setPullBatchSize(32);
		//消息拉取间隔，默认0
		consumer.setPullInterval(0);
		//流量控制，队列拉取阈值
		consumer.setPullThresholdForQueue(1000);
		//订阅topic
		consumer.subscribe("Topic1", "*");
		//根据topic获得队列
		Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("topic");
		//压力太大，挂起，暂停消费
		consumer.suspend();
		//压力变小，恢复，重新消费
		consumer.resume();
		
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.println(Thread.currentThread().getId() + " = " + msgs);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		
		consumer.start();
	}

}
