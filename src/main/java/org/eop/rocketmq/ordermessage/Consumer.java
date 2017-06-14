package org.eop.rocketmq.ordermessage;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author lixinjie
 * @since 
 */
public class Consumer {

	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerGroup2");
		consumer.setNamesrvAddr("192.168.95.57:9876;192.168.95.52:9876");
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe("Topic2", "*");
		consumer.registerMessageListener(new MessageListenerOrderly() {
			AtomicLong consumeTimes = new AtomicLong(0);
			
			//每个队列只有一个线程消费，保证消费完上一个才会开始下一个
			//同一队列内消息不会并发执行，队列间是并发执行的
			@SuppressWarnings("deprecation")
			@Override
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				context.setAutoCommit(false);
				System.out.println(Thread.currentThread().getId() + " = " + msgs);
				this.consumeTimes.incrementAndGet();
				
				if (this.consumeTimes.get() % 2 == 0) {
					return ConsumeOrderlyStatus.SUCCESS;
				} else if (this.consumeTimes.get() % 3 == 0) {
					return ConsumeOrderlyStatus.ROLLBACK;
				} else if (this.consumeTimes.get() % 4 == 0) {
					return ConsumeOrderlyStatus.COMMIT;
				} else if (this.consumeTimes.get() % 5 == 0) {
					context.setSuspendCurrentQueueTimeMillis(3000);
					return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
				}
				
				return ConsumeOrderlyStatus.SUCCESS;
			}
		});
		
		consumer.start();
	}

}
