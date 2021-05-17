package com.sleuth.network.store.impl;

import java.util.List;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.springframework.stereotype.Repository;

import com.sleuth.core.storage.EachResult;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;
import com.sleuth.network.schema.NodeInfo;
import com.sleuth.network.store.NodeInfoStore;

@Repository
public class NodeInfoStoreImpl implements NodeInfoStore {
	
	final Serializer<NodeInfo> serializer = ProtostuffSerializer.of(new NodeInfo().cachedSchema());
	
	@Resource
	private RocksTemplate rocksTemplate;
	
	@Override
	public void set(NodeInfo nodeInfo) {
		NodeInfo nodeInfo2 = this.get(nodeInfo.toUnique());
		if (nodeInfo2 == null) {
			rocksTemplate.set(NodeInfo.class, new StoreParameterSetter() {
				@Override
				public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
					return new BytesObject(nodeInfo.toUnique().getBytes(), serializer.serialize(nodeInfo));
				}
			});
		} else {
			//否则，更新
			nodeInfo2.setUri(nodeInfo.getUri());
			rocksTemplate.set(NodeInfo.class, new StoreParameterSetter() {
				@Override
				public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
					return new BytesObject(nodeInfo2.toUnique().getBytes(), serializer.serialize(nodeInfo2));
				}
			});
		}
	}

	@Override
	public NodeInfo get(String key) {
		return rocksTemplate.get(NodeInfo.class, key, new StoreValueSetter<NodeInfo>() {
			@Override
			public NodeInfo setValue(ColumnFamilyHandle familyHandle, byte[] bytes) throws Exception {
				return serializer.deserialize(bytes);
			}
		});
	}

	@Override
	public List<NodeInfo> list() {
		return rocksTemplate.forEach(NodeInfo.class, new EachResult<NodeInfo>() {
			@Override
			public NodeInfo valueOf(byte[] key, byte[] value) {
				return serializer.deserialize(value);
			}
		});
	}

}
