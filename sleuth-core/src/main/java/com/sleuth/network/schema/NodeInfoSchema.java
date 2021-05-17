package com.sleuth.network.schema;

import com.sleuth.core.storage.serialize.protostuff.ProtostrffSchema;

import io.protostuff.Message;
import io.protostuff.Schema;

/**
 * 节点信息
 * 
 * @author Jonse
 * @date 2019年1月24日
 */
public abstract class NodeInfoSchema extends ProtostrffSchema implements Message<NodeInfo> {

	@Override
	public Schema<NodeInfo> cachedSchema() {
		return this.getSchema(NodeInfo.class);
	}
	
}
