package org.eop.rocketmq.quickstart;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

/**
 * @author lixinjie
 * @since 
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup1");
		producer.setNamesrvAddr("192.168.95.57:9876;192.168.95.52:9876");
		producer.start();
		
		for (int i = 0; i < 100; i++) {
			Message msg = new Message("Topic1", "Tag1", ("你好" + i).getBytes());
			SendResult sendResult = producer.send(msg);
			System.out.println(sendResult);
		}
		
		producer.shutdown();
	}

}
