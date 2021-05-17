package com.sleuth.block.transaction;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.collect.Lists;
import com.sleuth.core.utils.ByteUtils;

/**
 * 希树的跟节点称为Merkle根 Merkle树可以仅用log2(N)的时间复杂度检查任何一个数据元素是否包含在树中
 * 
 * @author Jonse
 * @date 2018年10月31日
 */
public class MerkleBuilder  {
	
	private Node root;//根节点
    private byte[][] leafHashes;//叶子节点Hash

    public MerkleBuilder(byte[][] leafHashes) {
        constructTree(leafHashes);
    }

    /**
     * 从底部叶子节点开始往上构建整个Merkle Tree
     *
     * @param leafHashes
     */
    private void constructTree(byte[][] leafHashes) {
        if (leafHashes == null || leafHashes.length < 1) {
            throw new RuntimeException("ERROR:Fail to construct merkle tree ! leafHashes data invalid ! ");
        }
        this.leafHashes = leafHashes;
        List<Node> parents = bottomLevel(leafHashes);
        while (parents.size() > 1) {
            parents = internalLevel(parents);
        }
        root = parents.get(0);
    }

    /**
     * 构建一个层级节点
     *
     * @param children
     * @return
     */
    private List<Node> internalLevel(List<Node> children) {
        List<Node> parents = Lists.newArrayListWithCapacity(children.size() / 2);
        for (int i = 0; i < children.size() - 1; i += 2) {
            Node child1 = children.get(i);
            Node child2 = children.get(i + 1);

            Node parent = constructInternalNode(child1, child2);
            parents.add(parent);
        }

        // 内部节点奇数个，只对left节点进行计算
        if (children.size() % 2 != 0) {
            Node child = children.get(children.size() - 1);
            Node parent = constructInternalNode(child, null);
            parents.add(parent);
        }

        return parents;
    }

    /**
     * 底部节点构建
     *
     * @param hashes
     * @return
     */
    private List<Node> bottomLevel(byte[][] hashes) {
        List<Node> parents = Lists.newArrayListWithCapacity(hashes.length / 2);

        for (int i = 0; i < hashes.length - 1; i += 2) {
            Node leaf1 = constructLeafNode(hashes[i]);
            Node leaf2 = constructLeafNode(hashes[i + 1]);

            Node parent = constructInternalNode(leaf1, leaf2);
            parents.add(parent);
        }

        if (hashes.length % 2 != 0) {
            Node leaf = constructLeafNode(hashes[hashes.length - 1]);
            // 奇数个节点的情况，复制最后一个节点
            Node parent = constructInternalNode(leaf, leaf);
            parents.add(parent);
        }

        return parents;
    }

    /**
     * 构建叶子节点
     *
     * @param hash
     * @return
     */
    private static Node constructLeafNode(byte[] hash) {
        Node leaf = new Node();
        leaf.hash = hash;
        return leaf;
    }

    /**
     * 构建内部节点
     *
     * @param leftChild
     * @param rightChild
     * @return
     */
    private Node constructInternalNode(Node leftChild, Node rightChild) {
        Node parent = new Node();
        if (rightChild == null) {
            parent.hash = leftChild.hash;
        } else {
            parent.hash = internalHash(leftChild.hash, rightChild.hash);
        }
        parent.left = leftChild;
        parent.right = rightChild;
        return parent;
    }

    /**
     * 计算内部节点Hash
     *
     * @param leftChildHash
     * @param rightChildHash
     * @return
     */
    private byte[] internalHash(byte[] leftChildHash, byte[] rightChildHash) {
        byte[] mergedBytes = ByteUtils.merge(leftChildHash, rightChildHash);
        return DigestUtils.sha256(mergedBytes);
    }
    
    public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public byte[][] getLeafHashes() {
		return leafHashes;
	}

	public void setLeafHashes(byte[][] leafHashes) {
		this.leafHashes = leafHashes;
	}

	/** 默克尔树节点
     * 
     * @author Jonse
     * @date 2018年11月10日
     */
    public static class Node {
        private byte[] hash;
        private Node left;
        private Node right;
		public byte[] getHash() {
			return hash;
		}
		public void setHash(byte[] hash) {
			this.hash = hash;
		}
		public Node getLeft() {
			return left;
		}
		public void setLeft(Node left) {
			this.left = left;
		}
		public Node getRight() {
			return right;
		}
		public void setRight(Node right) {
			this.right = right;
		}
    }

}
