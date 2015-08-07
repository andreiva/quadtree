package util;

import model.Node;

public class NodeIterator {

	private int n = 0;
	private Node leaf;
	private Node root;
	
	public NodeIterator(Node node)
	{
		this.root = node;
	}
	
	/**
	 * Depth first search. Will go through the tree and return next available node.
	 * @return node or null when top is reached.
	 */
	public Node next()
	{
		if (root.hasNext()) 
		{
			leaf = findLeaf(root.nextChild());
			root = leaf.getParent();
			return leaf;
		}
		root = root.getParent();
		return root;
	}
	
	/**
	 * Recursive search, will return first occurrence of leaf in node.
	 * @param node to search
	 * @return leaf node
	 */
	private Node findLeaf(Node node)
	{
		if (node.hasChildren()) 
			node = findLeaf(node.nextChild());
		return node;
	}
	
	public boolean hasNext()
	{
		if(!root.hasNext() && root.isRoot())
			return false;
		else
			return true;
	}
}
