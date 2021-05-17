package com.sleuth.block.store.impl;

import java.util.List;

import javax.annotation.Resource;

import org.rocksdb.ColumnFamilyHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sleuth.block.schema.TxOutput;
import com.sleuth.block.schema.UTXO;
import com.sleuth.block.store.UTXOStore;
import com.sleuth.core.storage.EachResult;
import com.sleuth.core.storage.RocksTemplate;
import com.sleuth.core.storage.datastore.setter.BytesObject;
import com.sleuth.core.storage.datastore.setter.StoreParameterSetter;
import com.sleuth.core.storage.datastore.setter.StoreValueSetter;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

@Repository
public class UTXOStoreImpl implements UTXOStore {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	final Serializer<UTXO> serializer = ProtostuffSerializer.of(new UTXO().cachedSchema());
	
	@Resource
	private RocksTemplate rocksTemplate;
	
	@Override
	public void add(String txId, TxOutput[] txOutputs) {
		UTXO utxo = this.get(txId);
		if (utxo == null) {
			UTXO newUTXO = new UTXO(txId, txOutputs);
			this.rocksTemplate.set(UTXO.class, new StoreParameterSetter() {
				@Override
				public BytesObject setValue(ColumnFamilyHandle familyHandle) throws Exception {
					return new BytesObject(newUTXO.toUnique().getBytes(), serializer.serialize(newUTXO));
				}
			});
		}
	}
	
	@Override
	public UTXO get(String txId) {
		return this.rocksTemplate.get(UTXO.class, txId, new StoreValueSetter<UTXO>() {
			@Override
			public UTXO setValue(ColumnFamilyHandle familyHandle, byte[] value) throws Exception {
				return serializer.deserialize(value);
			}
		});
	}

	@Override
	public void remove(String txId) {
		this.rocksTemplate.delete(UTXO.class, txId);
	}

	@Override
	public List<UTXO> getTxOutputs(byte[] pubKeyHash) {
		this.rocksTemplate.forEach(UTXO.class, new EachResult<UTXO>() {
			@Override
			public UTXO valueOf(byte[] key, byte[] value) {
				UTXO utxo = serializer.deserialize(value);
				utxo.getTxOutputs();
				return null;
			}
		});
		
		return null;
	}
	
}
