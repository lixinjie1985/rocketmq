package org.eop.rocketmq.querymessage;

import com.alibaba.rocketmq.client.QueryResult;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author lixinjie
 * @since 
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		 DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup6");
		 producer.setNamesrvAddr("");
	     producer.start();
	     
	     for (int i = 0; i < 100; i++) {
	    	 Message msg = new Message("Topic6", "Tag6", "key", ("world" + i).getBytes());
	    	 SendResult sendResult = producer.send(msg);
	    	 System.out.println(sendResult);
	    	 
	    	 QueryResult queryResult = producer.queryMessage("Topic6", "key", 1, System.currentTimeMillis() - 2000, System.currentTimeMillis());
	    	 for (MessageExt msge : queryResult.getMessageList()) {
	    		 System.out.println(msge);
	    	 }
	     }
	     
	     producer.shutdown();
	}

}
