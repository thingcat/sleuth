package com.sleuth.network.peer.hash;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.sleuth.network.peer.PeerNetwork;
import com.sleuth.network.schema.NodeInfo;
import com.sleuth.network.service.NodeInfoService;

/** 基于DHT的P2P网络模型
 * <p>DHT(Distributed Hash Table，分布式哈希表)</p>
 * @author Jonse
 * @date 2020年9月26日
 */
public class DHTPeerNetwork implements PeerNetwork {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	//桶集合
    final List<Kbucket> buckets = Lists.newArrayList();
    final List<NodeInfo> nodesConnected = Lists.newArrayList();
    
    @Resource
    private NodeInfoService nodeInfoService;
    
    private static NodeInfo localNode;
    
    private String uri;
    private String lable;
    
	@Override
    public void init() {
    	String lable = this.getLable();
    	if (lable == null) {
    		try {
				lable = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				logger.warn("failed to get host name!");
			}
		}
    	
    	//初始化本机节点信息
    	localNode = new NodeInfo();
    	localNode.setLable(lable);
		localNode.setUri(this.getUri());
		localNode.setWeight(-1);
		BigInteger nid = new NodeId().getNid();
		localNode.setNid(nid);
    	
		logger.info("Local node: {}", localNode.toString());
		
		// 创建初始化的桶, 范围为0到2的160次方, 添加我们自身的节点到桶中
        Kbucket kbucket = new Kbucket(new BigInteger("0"), new BigInteger("2").pow(160));
        kbucket.addNode(localNode);
        Kbucket[] kbuckets;
        while ((kbuckets = kbucket.fissionKbucket()) != null){
            buckets.remove(kbucket);
            buckets.add(kbuckets[0]);
            buckets.add(kbuckets[1]);
            kbucket = kbuckets[0];
        }
        kbucket.clear();
        
        //从数据库中装载节点信息
        List<NodeInfo> nodeInfos = nodeInfoService.list();
        if (nodeInfos != null && nodeInfos.size() > 0) {
        	for (NodeInfo node : nodeInfos) {
				node.setNid(new NodeId().getNid());
				this.addNode(node);
			}
		}
        
    }
	
	@Override
	public List<NodeInfo> findNearNodes(NodeId nodeId) {
		int x = 0;
        for(int i=0; i<buckets.size(); i++){
           if (buckets.contains(nodeId.getNid())){ 
        	   x = i; 
        	   break;
           }
        }
        //获取自身节点所在桶中的所有节点
        final Collection<NodeInfo> kbuckets = buckets.get(x).getNodes();
        final List<NodeInfo> nodes = new ArrayList<NodeInfo>(kbuckets);
        //节点左侧所有节点集合
        final List<NodeInfo> leftNodes = new ArrayList<NodeInfo>();
        //节点右侧所有节点集合
        final List<NodeInfo> rightNodes = new ArrayList<NodeInfo>();

        // 获取目标id所在的桶的左边的桶的节点直到获取够8个以上
        for(int i = x - 1; i >= 0; i--) {
            leftNodes.addAll(buckets.get(i).getNodes());
            if(leftNodes.size() >= 8) break;
        }

        // 获取目标id所在的桶的右边的桶的节点直到获取够8个以上
        for(int i = x + 1; i < buckets.size(); i++) {
            rightNodes.addAll(buckets.get(i).getNodes());
            if(rightNodes.size() >= 8) break;
        }

        nodes.addAll(leftNodes);
        nodes.addAll(rightNodes);
        // 所有的距离保存在TreeSet中, 利用TreeSet维护自然顺序的特性, 获取的第一个就是最近的距离, 最后一个就是最远的距离
        Set<BigInteger> distances = new TreeSet<>();
        // 距离对应的节点列表, 因为同样的距离可能有左右两个节点
        Map<BigInteger, List<NodeInfo>> distance2Node = new HashMap<>();
        for(NodeInfo n : nodes) {
            // 两个id的距离为他们id转化为无符号数后的差值的绝对值
            BigInteger distance = n.getNid().subtract(nodeId.getNid()).abs();
            distances.add(distance);

            // 设置距离对应的节点
            List<NodeInfo> value;
            if(null == (value = distance2Node.get(distance))) {
                value = new ArrayList<>();
                distance2Node.put(distance, value);
            }
            value.add(n);
        }

        // 将要返回的节点, 从TreeSet中依次取出最近距离的节点, 满8个后结束
        List<NodeInfo> result = new ArrayList<>();
        for(BigInteger distance : distances) {
            result.addAll(distance2Node.get(distance));
            if(result.size() >= 8) break;
        }

        // 只返回8个节点
        return result.size() > 8 ? result.subList(0, 8) : result;
	}

	@Override
	public void addNode(NodeInfo node) {
		BigInteger id = node.getNid();
        for(Kbucket bucket : buckets) {
            // 如果id在当前桶范围内, 则添加到当前桶
            if(bucket.contains(id)) {
                bucket.addNode(node);
                break;
            }
        }
	}

	@Override
	public void addNodes(Collection<NodeInfo> nodes) {
		for (NodeInfo nodeInfo : nodes) {
    		this.addNode(nodeInfo);
		}
	}

	@Override
	public List<Kbucket> getBuckets() {
		return buckets;
	}

	@Override
	public NodeInfo getLocalNodeInfo() {
		return localNode;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}
	
}
