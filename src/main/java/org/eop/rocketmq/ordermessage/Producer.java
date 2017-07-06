package org.eop.rocketmq.ordermessage;

import java.util.List;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;

/**
 * @author lixinjie
 * @since 
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup2");
		producer.setNamesrvAddr("192.168.95.57:9876;192.168.95.52:9876");
		producer.start();
		
		String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
		for (int i = 0; i < 100; i++) {
			int orderId = i % 10;
			Message msg = new Message("Topic2", tags[i % tags.length], "key" + i, ("helloworld " + i).getBytes());
			SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
				
				//把一组相关且有顺序的消息发送到同一个队列中，在消费端每个队列只有一个线程消费
				//这样消费顺序就和发送顺序一致了，此时队列的数目就是并发数，增大并发就要增加队列个数
				/**
				 * @param msg 要发送的消息，send方法的第一个参数
				 * @param mqs 消息的topic对应的所有队列
				 * @param arg send方法的最后一个参数
				 */
				@Override
				public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
					Integer id = (Integer)arg;
					return mqs.get(id % mqs.size());
				}
			}, orderId);
			System.out.println(sendResult);
		}
		
		producer.shutdown();
	}

}
