package org.eop.rocketmq.transaction;

import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.client.producer.TransactionCheckListener;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author lixinjie
 * @since 
 */
public class TransactionCheckListenerImpl implements TransactionCheckListener {

	private AtomicInteger transactionIndex = new AtomicInteger(0);
	
	//事务回查，存在于MQ里的半消息，在无法得知本地事务是成功还是失败时
	//MQ会主动根据半消息来发送消息的客户端回查该事务，如果成功则把半消息变为正常消息
	//如果失败则删除半消息，如不知道则稍后再继续回查（从3.2.6移除了该回查机制）
	@Override
	public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
		int value = transactionIndex.getAndIncrement();
		
        if ((value % 6) == 0) {
            throw new RuntimeException("Could not find db");
        } else if ((value % 5) == 0) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else if ((value % 4) == 0) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }

        return LocalTransactionState.UNKNOW;
	}

}
