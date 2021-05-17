package com.sleuth.core.storage.datastore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.rocksdb.BlockBasedTableConfig;
import org.rocksdb.BloomFilter;
import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.CompactionStyle;
import org.rocksdb.CompressionType;
import org.rocksdb.DBOptions;
import org.rocksdb.Filter;
import org.rocksdb.HashLinkedListMemTableConfig;
import org.rocksdb.HashSkipListMemTableConfig;
import org.rocksdb.InfoLogLevel;
import org.rocksdb.Logger;
import org.rocksdb.Options;
import org.rocksdb.PlainTableConfig;
import org.rocksdb.RateLimiter;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.SkipListMemTableConfig;
import org.rocksdb.Snapshot;
import org.rocksdb.Statistics;
import org.rocksdb.Transaction;
import org.rocksdb.TransactionDB;
import org.rocksdb.TransactionDBOptions;
import org.rocksdb.VectorMemTableConfig;
import org.rocksdb.WriteOptions;
import org.rocksdb.util.SizeUnit;
import org.slf4j.LoggerFactory;

import com.sleuth.core.storage.DataSource;
import com.sleuth.core.storage.RocksTransaction;
import com.sleuth.core.storage.exception.FamilyCreateException;
import com.sleuth.core.storage.exception.NotRocksTransactionException;

/** 共享配置
 * 
 * @author Jonse
 * @date 2020年1月13日
 */
public class DatastoreSource implements DataSource {
	
	final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	
	static final ThreadLocal<RocksTransaction> THREAD_LOCAL = new ThreadLocal<RocksTransaction>();
	
	//根路径
	private String root;
	//是否自动创建目录
	private boolean createIfMissing;
	//刷盘的数据大小，默认值：4 * 1024kb
	private Integer writeBufferSize;
	//在内存中建立的写入缓冲区的最大数目，默认值：2
	private Integer maxWriteBufferNumber;
	//指定提交到默认低优先级线程池的并发后台压缩作业的最大数量，默认值：1
	private Integer maxBackgroundCompactions;
	//用于分析数据库性能的统计信息
	private Statistics statistics;
	//数据库存储位置
	private String dbPath;
	
	private Options options;
	private DBOptions dbOptions;
	private TransactionDBOptions txnDbOptions;
	
	private TransactionDB rocksDB;//数据库操作
	private List<ColumnFamilyDescriptor> descriptors;
	private final List<ColumnFamilyHandle> familyHandles = new ArrayList<ColumnFamilyHandle>();
	
	public DatastoreSource() {
		
	}
	
	@Override
	@PostConstruct
	public void init() {
		logger.info("initialize database.");
		//存储的根目录
		File file = new File(this.getRoot());
		if (!file.exists()) {
			file.mkdirs();
		}
		this.dbPath = this.root;
		logger.info("store db at {}", this.dbPath);
		
		this.options = this.loadOptions();
		this.dbOptions = this.loadDBOptions();
		this.txnDbOptions = this.loadTransactionDBOptions();
		
		this.descriptors = this.openColumnFamilies(this.options);
		this.rocksDB = this.openRocksDB(this.dbOptions, this.txnDbOptions, this.descriptors, this.familyHandles);
	}
	
	@Override
	public RocksDB getRocksDB() {
		return this.rocksDB;
	}

	@Override
	public void close() {
		if (this.rocksDB != null) {
			this.rocksDB.close();
		}
	}
	
	@Override
	public ColumnFamilyHandle createFamily(String familyName) {
		//先判断是否存在重复的
		ColumnFamilyHandle familyHandle = getFamilyHandle(familyName);
		if (familyHandle == null) {
			ColumnFamilyDescriptor descriptor = new ColumnFamilyDescriptor(familyName.getBytes(), new ColumnFamilyOptions());
			try {
				familyHandle = this.getRocksDB().createColumnFamily(descriptor);
				this.descriptors.add(descriptor);
				this.familyHandles.add(familyHandle);
				logger.info("Successfully created ColumnFamily, name = {}", familyName);
			} catch (RocksDBException e) {
				logger.error("create columnfamily error, ", e);
				throw new FamilyCreateException(familyName);
			}
		} else {
			logger.warn("Duplicate ColumnFamily {} exists.", familyName);
		}
		return familyHandle;
	}
	
	@Override
	public ColumnFamilyHandle getFamilyHandle(String familyName) {
		try {
			for(ColumnFamilyHandle familyHandle : familyHandles) {
				if (new String(familyHandle.getName()).equals(familyName)) {
					return familyHandle;
				}
			}
		} catch (RocksDBException e) {
			logger.error("Error getting columnFamily of Handle.", e);
			return null;
		}
		return null;
	}
	
	/** 加载DB
	 * 
	 * @param dbOptions
	 * @param descriptors
	 * @param familyHandles
	 * @return
	 */
	protected TransactionDB openRocksDB(DBOptions dbOptions, TransactionDBOptions txnOptions, List<ColumnFamilyDescriptor> descriptors, List<ColumnFamilyHandle> familyHandles) {
		TransactionDB rocksDB = null;
		try {
			//打开数据库
			String dbPath = this.getDbPath();
			rocksDB = TransactionDB.open(dbOptions, txnOptions, dbPath, descriptors, familyHandles);
			logger.info("OPEN DB SUCCESSFUL");
		} catch (RocksDBException e) {
			logger.error("open db error, ", e);
		} finally {
			/*
			for(ColumnFamilyHandle familyHandle : familyHandles) {
				familyHandle.close();
			}
			*/
		}
		return rocksDB;
	}
	
	@Override
	public void openTransaction() {
		RocksTransaction transaction = THREAD_LOCAL.get();
		if (transaction == null) {
			TransactionDB transactionDB = (TransactionDB) this.getRocksDB();
			transaction = new ThingRocksTransaction(transactionDB);
			THREAD_LOCAL.set(transaction);
		}
	}
	
	@Override
	public Transaction getTransaction() {
		RocksTransaction transaction = THREAD_LOCAL.get();
		if (transaction == null) {
			throw new NotRocksTransactionException();
		}
		return transaction.getTransaction();
	}
	
	@Override
	public boolean isTransaction() {
		RocksTransaction transaction = THREAD_LOCAL.get();
		if (transaction != null) {
			return THREAD_LOCAL.get().isTransaction();
		}
		return false;
	}

	@Override
	public void commit() {
		RocksTransaction transaction = THREAD_LOCAL.get();
		if (transaction == null) {
			throw new NotRocksTransactionException();
		}
		transaction.commit();
		THREAD_LOCAL.set(null);
	}
	
	@Override
	public ReadOptions getReadOptions(Snapshot snapshot) {
		ReadOptions readOptions = new ReadOptions();
		readOptions.setSnapshot(snapshot);
		return readOptions;
	}

	@Override
	public void rollback() {
		RocksTransaction transaction = THREAD_LOCAL.get();
		if (transaction == null) {
			throw new NotRocksTransactionException();
		}
		transaction.rollback();
		THREAD_LOCAL.set(null);
	}
	
	/** 加载目录下的簇列
	 * 
	 * @param dbPath
	 * @param configurable
	 * @return
	 */
	protected List<ColumnFamilyDescriptor> openColumnFamilies(Options options) {
		List<ColumnFamilyDescriptor> descriptors = new ArrayList<ColumnFamilyDescriptor>();
		try {
			logger.debug("find ColumnFamilyDescriptor at {} directory.", this.getDbPath());
			List<byte[]> families = TransactionDB.listColumnFamilies(options, this.getDbPath());
			if (families.size() > 0) {
				for (byte[] familie : families) {
					ColumnFamilyDescriptor descriptor = new ColumnFamilyDescriptor(familie, new ColumnFamilyOptions());
					descriptors.add(descriptor);
					logger.info("Find family = {}", new String(descriptor.getName()));
				}
			} else {
				//为空，则添加一个默认的
				descriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, new ColumnFamilyOptions()));
				logger.info("Add family = {}", new String(RocksDB.DEFAULT_COLUMN_FAMILY));
			}
		} catch (Exception e) {
			logger.error("get families error, ", e);
		}
		return descriptors;
	}
	
	protected List<ColumnFamilyDescriptor> getColumnFamilies(String dbPath) {
		List<ColumnFamilyDescriptor> columnFamilyDescriptors = new ArrayList<>();
		try {
			Options options = this.loadOptions();
			logger.debug("find ColumnFamilyDescriptor at {} directory.", dbPath);
			List<byte[]> families = RocksDB.listColumnFamilies(options, dbPath);
			if (families.size() > 0) {
				for (byte[] familie : families) {
					columnFamilyDescriptors.add(new ColumnFamilyDescriptor(familie, new ColumnFamilyOptions()));
				}
			} else {
				//为空，则添加一个默认的
				columnFamilyDescriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, new ColumnFamilyOptions()));
			}
		} catch (Exception e) {
			logger.error("get families error, ", e);
		}
		return columnFamilyDescriptors;
	}
	
	/** 根据外部配置文件来组装配置属性
	 * 
	 * @param configurable
	 * @return
	 */
	protected DBOptions loadDBOptions() {
		DBOptions dbOptions = new DBOptions();
		dbOptions.setCreateIfMissing(this.getCreateIfMissing());
		return dbOptions;
	}
	
	/** 加载配置
	 * 
	 * @param configurable
	 * @return
	 */
	protected Options loadOptions() {
		final Options options = new Options();
		final Filter bloomFilter = new BloomFilter(10);
//		final ReadOptions readOptions = new ReadOptions().setFillCache(false);
		final RateLimiter rateLimiter = new RateLimiter(10000000, 10000, 10);
		
		options.setCreateIfMissing(this.getCreateIfMissing())
			.setStatistics(this.getStatistics())
			.setWriteBufferSize(this.getWriteBufferSize())
			.setMaxWriteBufferNumber(this.getMaxWriteBufferNumber())
			.setMaxBackgroundCompactions(this.getMaxBackgroundCompactions())
//			.setCompressionType(CompressionType.SNAPPY_COMPRESSION)暂时不启用压缩，以后再来研究
			.setCompressionType(CompressionType.NO_COMPRESSION)
			.setCompactionStyle(CompactionStyle.UNIVERSAL);
		
		//日志配置
		Logger dbLogger = new Logger(options) {
			@Override
			protected void log(InfoLogLevel log, String msg) {
				logger.debug(log.name() + ">>" + msg);
			}
		};
		options.setLogger(dbLogger);
		
		options.setMemTableConfig(new HashSkipListMemTableConfig().setHeight(4).setBranchingFactor(4).setBucketCount(2000000));
		options.setMemTableConfig(new HashLinkedListMemTableConfig().setBucketCount(100000));
		options.setMemTableConfig(new VectorMemTableConfig().setReservedSize(10000));
		options.setMemTableConfig(new SkipListMemTableConfig());
		options.setTableFormatConfig(new PlainTableConfig());
		// Plain-Table requires mmap read
		options.setAllowMmapReads(true);
		options.setRateLimiter(rateLimiter);
		
		final BlockBasedTableConfig tableOptions = new BlockBasedTableConfig();
		
		tableOptions.setBlockCacheSize(64 * SizeUnit.KB)
				.setFilter(bloomFilter)
				.setCacheNumShardBits(6)
				.setBlockSizeDeviation(5)
				.setBlockRestartInterval(10)
				.setCacheIndexAndFilterBlocks(true)
				.setHashIndexAllowCollision(false)
				.setBlockCacheCompressedSize(64 * SizeUnit.KB)
				.setBlockCacheCompressedNumShardBits(10);
		
		options.setTableFormatConfig(tableOptions);
		
		return options;
	}
	
	/** 加载事务提交策略配置
	 * 
	 * @return
	 */
	protected TransactionDBOptions loadTransactionDBOptions() {
		final TransactionDBOptions txnDbOptions = new TransactionDBOptions();
		//何时将数据写入数据库的策略。默认策略是只写提交的数据
//		txnDbOptions.setWritePolicy(writePolicy);
		return txnDbOptions;
	}
	
	/** 加载事务写策略配置
	 * 
	 * @return
	 */
	protected WriteOptions loadWriteOptions() {
		final WriteOptions writeOptions = new WriteOptions();
		//如果为true，写入操作将不会首先进入预写日志，并且在崩溃后写入操作可能会丢失。
		writeOptions.setDisableWAL(false);
		//如果为true，并且用户试图写入不存在的列族（它们已被删除），则忽略写入（不返回错误）。如果一个WriteBatch中有多个写操作，则其他写操作将成功。默认值：false
		writeOptions.setIgnoreMissingColumnFamilies(false);
		//如果为true，我们需要等待或休眠写入请求，则会立即失败
		writeOptions.setNoSlowdown(false);
		//是否同步写入，同步效率低，异步效率高
		writeOptions.setSync(false);
		return writeOptions;
	}
	
	/** 加载事务读策略配置
	 * 
	 * @return
	 */
	protected ReadOptions loadReadOptions(final Snapshot snapshot) {
		final ReadOptions readOptions = new ReadOptions();
		readOptions.setSnapshot(snapshot);
//		readOptions.setReadaheadSize(readaheadSize)
		return readOptions;
	}
	
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public boolean getCreateIfMissing() {
		return createIfMissing;
	}
	public void setCreateIfMissing(boolean createIfMissing) {
		this.createIfMissing = createIfMissing;
	}
	public Statistics getStatistics() {
		if (this.statistics == null) {
			this.statistics = new Statistics();
		}
		return statistics;
	}
	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}
	public Integer getWriteBufferSize() {
		return writeBufferSize;
	}
	public void setWriteBufferSize(Integer writeBufferSize) {
		this.writeBufferSize = writeBufferSize;
	}
	public Integer getMaxWriteBufferNumber() {
		return maxWriteBufferNumber;
	}
	public void setMaxWriteBufferNumber(Integer maxWriteBufferNumber) {
		this.maxWriteBufferNumber = maxWriteBufferNumber;
	}
	public Integer getMaxBackgroundCompactions() {
		return maxBackgroundCompactions;
	}
	public void setMaxBackgroundCompactions(Integer maxBackgroundCompactions) {
		this.maxBackgroundCompactions = maxBackgroundCompactions;
	}
	@Override
	public String getDbPath() {
		return this.dbPath;
	}
	/** 获得目录下的簇列
	 * 
	 */
	@Override
	public List<ColumnFamilyDescriptor> getFamilyDescriptors() {
		return this.descriptors;
	}
	/** 获得目录下簇列的句柄语句
	 * 
	 */
	@Override
	public List<ColumnFamilyHandle> getFamilyHandles() {
		return this.familyHandles;
	}

}
