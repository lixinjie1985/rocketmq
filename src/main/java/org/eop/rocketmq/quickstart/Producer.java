package org.eop.rocketmq.quickstart;

import java.util.List;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;

/**
 * @author lixinjie
 * @since 
 */
public class Producer {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup1");
		//设置namesrv
		producer.setNamesrvAddr("192.168.95.57:9876;192.168.95.52:9876");
		//设置每个topic在单个broker上的队列数
		producer.setDefaultTopicQueueNums(4);
		//设置最大消息大小，128K
		producer.setMaxMessageSize(1024 * 128);
		//设置从namesrv拉取topic信息的间隔
		producer.setPollNameServerInteval(1000 * 30);
		//设置失败重试次数
		producer.setRetryTimesWhenSendFailed(2);
		//设置发送超时时间
		producer.setSendMsgTimeout(3000);
		
		producer.start();
		
		//可以根据一个topic获取它的所有队列，然后往指定队列里发送
		List<MessageQueue> mqs = producer.fetchPublishMessageQueues("topic");
		//有很多不同种类的send方法，可以查看源码
		for (int i = 0; i < 100; i++) {
			Message msg = new Message("Topic1", "Tag1", ("你好" + i).getBytes());
			SendResult sendResult = producer.send(msg);
			System.out.println(sendResult);
		}
		
		producer.shutdown();
	}

}
