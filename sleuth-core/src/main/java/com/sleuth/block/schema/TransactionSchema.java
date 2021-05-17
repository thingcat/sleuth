package com.sleuth.block.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

/** 交易
 * 
 * @author Administrator
 *
 */
public abstract class TransactionSchema extends ProtostrffSchema implements Message<Transaction> {
	
	@Override
	public Schema<Transaction> cachedSchema() {
		return this.getSchema(Transaction.class);
	}
	
}
