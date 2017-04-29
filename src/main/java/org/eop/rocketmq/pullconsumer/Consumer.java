package org.eop.rocketmq.pullconsumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.PullResult;
import com.alibaba.rocketmq.client.consumer.PullStatus;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;

/**
 * @author lixinjie
 * @since 
 */
public class Consumer {

	private static final Map<MessageQueue, Long> OFFSE_TABLE = new HashMap<>();
	
	public static void main(String[] args) throws Exception {
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("ConsumerGroup7");
		consumer.setNamesrvAddr("");
        consumer.start();
        
        Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("Topic7");
        for (MessageQueue mq : mqs) {
        	PullResult pullResult = consumer.pullBlockIfNotFound(mq, "*", getMessageQueueOffset(mq), 32);
        	System.out.println(pullResult);
        	putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
        	if (PullStatus.FOUND == pullResult.getPullStatus()) {
        		List<MessageExt> msgs = pullResult.getMsgFoundList();
        		System.out.println(msgs);
        	} else if (PullStatus.NO_NEW_MSG == pullResult.getPullStatus()) {
        		
        	} else if (PullStatus.NO_MATCHED_MSG == pullResult.getPullStatus()) {
        		
        	} else if (PullStatus.OFFSET_ILLEGAL == pullResult.getPullStatus()) {
        		
        	}
        }
        consumer.shutdown();
	}
	
	private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = OFFSE_TABLE.get(mq);
        if (offset != null)
            return offset;

        return 0;
    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        OFFSE_TABLE.put(mq, offset);
    }

}
