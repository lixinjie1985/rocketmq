package org.eop.rocketmq.transaction;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.TransactionCheckListener;
import com.alibaba.rocketmq.client.producer.TransactionMQProducer;
import com.alibaba.rocketmq.common.message.Message;

/**
 * @author lixinjie
 * @since 
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		 TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();
	     TransactionMQProducer producer = new TransactionMQProducer("ProducerGroup4");
	     producer.setTransactionCheckListener(transactionCheckListener);
	     producer.start();
	     
	     String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
	     TransactionExecuterImpl tranExecuter = new TransactionExecuterImpl();
	     for (int i = 0; i < 100; i++) {
	    	 Message msg = new Message("TopicTest", tags[i % tags.length], "KEY" + i, ("Hello RocketMQ " + i).getBytes());
	    	 SendResult sendResult = producer.sendMessageInTransaction(msg, tranExecuter, null);//半消息发送成功后执行事务
             System.out.println(sendResult);
	     }
	     
	     producer.shutdown();
	}

}
