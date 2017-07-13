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
		//设置namesrv
		consumer.setNamesrvAddr("");
		//根据topic从消费者缓存中获取消息队列
		Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("topic");
		//根据topic获取消息队列
		mqs = consumer.fetchMessageQueuesInBalance("topic");
		
        consumer.start();
        //有很多种源码，具体查看源码（阻塞的，非阻塞的，带超时的，带回调的等）
        for (MessageQueue mq : mqs) {
        	//从broker获取偏移量（broker维护偏移量）
        	//long offset = consumer.fetchConsumeOffset(mq, false);
        	PullResult pullResult = consumer.pullBlockIfNotFound(mq, "*", getMessageQueueOffset(mq), 32);
        	System.out.println(pullResult);
        	//更新broker偏移量（broker维护偏移量）
        	//consumer.updateConsumeOffset(mq, pullResult.getNextBeginOffset());
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
	
	/** 自己维护偏移量 */
	private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = OFFSE_TABLE.get(mq);
        if (offset != null)
            return offset;

        return 0;
    }

	/** 自己维护偏移量 */
    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        OFFSE_TABLE.put(mq, offset);
    }

}
