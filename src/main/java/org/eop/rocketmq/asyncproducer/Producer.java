package org.eop.rocketmq.asyncproducer;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

/**
 * @author lixinjie
 * @since 
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		 DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup5");
		 producer.setNamesrvAddr("192.168.95.57:9876;192.168.95.52:9876");
		 producer.start();
		 
		 for (int i = 0; i < 100; i++) {
			 Message msg = new Message("Topic5", "Tag5", "key", ("hello" + i).getBytes());
			 producer.send(msg, new SendCallback() {
				
				@Override
				public void onSuccess(SendResult sendResult) {
					System.out.println(sendResult);
				}
				
				@Override
				public void onException(Throwable e) {
					e.printStackTrace();
				}
			});
		 }
		 
		 producer.shutdown();
	}

}
