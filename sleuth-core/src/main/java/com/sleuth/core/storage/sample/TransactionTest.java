package com.sleuth.core.storage.sample;

import org.rocksdb.Options;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksIterator;
import org.rocksdb.Transaction;
import org.rocksdb.TransactionDB;
import org.rocksdb.TransactionDBOptions;
import org.rocksdb.WriteOptions;

public class TransactionTest {
	
	private static final String dbPath = "G:\\rockets";
	
	public static void main(String[] args) throws Exception {
		
		final Options options = new Options();
		options.setCreateIfMissing(true);
		final TransactionDBOptions txnDbOptions = new TransactionDBOptions();
		final TransactionDB transactionDB = TransactionDB.open(options, txnDbOptions, dbPath);
//		final RocksDB rocksDB = RocksDB.open(dbPath);
		
		final WriteOptions writeOptions = new WriteOptions();
		
		//开启事务
		final Transaction transaction = transactionDB.beginTransaction(writeOptions);
		for(int i=0; i<100; i++) {
			byte[] key = ("test_" + i).getBytes();
			byte[] value = ("张三_" + i).getBytes();
			transaction.putUntracked(key, value);
		}
		transaction.commit();
		
		ReadOptions readOptions = new ReadOptions();
//		readOptions.setSnapshot(transaction.getSnapshot());
		RocksIterator iterator = transaction.getIterator(readOptions);
		iterator.seekForPrev("test_20".getBytes());
		for(; iterator.isValid(); iterator.next()) {
			String value = new String(iterator.value());
			System.out.println(value);
		}
	}

}
