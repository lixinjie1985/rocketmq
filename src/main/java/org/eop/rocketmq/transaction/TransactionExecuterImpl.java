package org.eop.rocketmq.transaction;

import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.rocketmq.client.producer.LocalTransactionExecuter;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.common.message.Message;

/**
 * @author lixinjie
 * @since 
 */
public class TransactionExecuterImpl implements LocalTransactionExecuter {

	private AtomicInteger transactionIndex = new AtomicInteger(1);
	
	//本地事务执行器，在MQ成功发送一条半消息后，开始执行本地事务操作
	//如果返回成功半消息将变成正常消息，如果返回失败则删除半消息
	//如果返回不知道，MQ会根据半消息回查客户端，来确定事务是成功还是失败（3.2.6版本回查机制被去掉了）
	@Override
	public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
		int value = transactionIndex.getAndIncrement();

        if (value == 0) {
            throw new RuntimeException("Could not find db");
        } else if ((value % 5) == 0) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else if ((value % 4) == 0) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }

        return LocalTransactionState.UNKNOW;
	}

}
