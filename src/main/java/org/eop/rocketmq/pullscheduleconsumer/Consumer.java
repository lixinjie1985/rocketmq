package org.eop.rocketmq.pullscheduleconsumer;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.MQPullConsumer;
import com.alibaba.rocketmq.client.consumer.MQPullConsumerScheduleService;
import com.alibaba.rocketmq.client.consumer.PullResult;
import com.alibaba.rocketmq.client.consumer.PullTaskCallback;
import com.alibaba.rocketmq.client.consumer.PullTaskContext;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

/**
 * @author lixinjie
 * @since 
 */
public class Consumer {

	public static void main(String[] args) throws Exception {
		MQPullConsumerScheduleService scheduleService = new MQPullConsumerScheduleService("ConsumerGroup8");
		scheduleService.setMessageModel(MessageModel.CLUSTERING);
		scheduleService.registerPullTaskCallback("Topic8", new PullTaskCallback() {
			
			@Override
			public void doPullTask(MessageQueue mq, PullTaskContext context) {
				MQPullConsumer consumer = context.getPullConsumer();
				try {
					long offset = consumer.fetchConsumeOffset(mq, false);
					if (offset < 0) {
                        offset = 0;
					}
					PullResult pullResult = consumer.pull(mq, "*", offset, 32);
					System.out.println(pullResult);
					switch (pullResult.getPullStatus()) {
						case FOUND :
							List<MessageExt> msgs = pullResult.getMsgFoundList();
							System.out.println(msgs);
							break;
						case NO_NEW_MSG :
							break;
						case NO_MATCHED_MSG :
							break;
						case OFFSET_ILLEGAL :
							break;
						default :
							break;
					}
					
					consumer.updateConsumeOffset(mq, pullResult.getNextBeginOffset());
					context.setPullNextDelayTimeMillis(100);
					
				} catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		scheduleService.start();
	}

}
