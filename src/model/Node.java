package model;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import util.NodeIterator;



public class Node extends AbstractNode{

	// Node stuff
	private Rectangle2D.Double rect;
	private Point center;
	private Node parent;
	private Node[] children;
	private boolean root;
	private String name;
	private int n = 0;

	// Barnes Hutt stuff
	private Body body;
	private double mass;
	private Point2D massCenter;
	
	public Node(Rectangle2D.Double rectangle, Node parent)
	{
		root = false;
		this.parent = parent;
		this.rect = rectangle;
		center = new Point((int)(rect.getWidth()/2), (int)(rect.getHeight()/2));
	}
	
	public Node(Rectangle2D.Double rectangle, Node parent, boolean isRoot)
	{
		root = isRoot;
		this.parent = parent;
		this.rect = rectangle;
		center = new Point((int)(rect.getWidth()/2), (int)(rect.getHeight()/2));
	}
	
	public boolean hasObject()
	{
		if(this.body == null)
			return false;
		else
			return true;
	}
	
	public Body getObject() {
		return body;
	}

	public boolean hasChildren()
	{
		if(this.children == null)
			return false;
		else
			return true;
	}
	
	/**
	 * Puts object into this node. If node already contains an object, 
	 * node will be divided in 4 node and objects will be put in own nodes.
	 * @param object
	 */
	public void put(Body object)
	{	
		if(!hasObject() && !hasChildren())
		{
			this.body = object; 
			return;
		}
		else if(this.hasChildren())
		{
			// put to minimum depth
			if(this.countObjects(0) == 0)
			{
				this.children = null;
				this.body = object;
				return;
			}
			else
			putIntoChild(object);
		}
		else if(this.hasObject())
		{
			divide();
			putIntoChild( this.body.clone() );
			this.body = null;
			
			putIntoChild(object);
		}
	}

	private void putIntoChild(Body object)
	{
		if (this.children[UPPER_RIGHT].getRect().contains(object.getLocation())) {
			children[UPPER_RIGHT].put(object);
			return;
		}

		else if (this.children[UPPER_LEFT].getRect().contains(
				object.getLocation())) {
			children[UPPER_LEFT].put(object);
			return;
		}

		else if (this.children[LOWER_RIGHT].getRect().contains(
				object.getLocation())) {
			children[LOWER_RIGHT].put(object);
			return;
		}

		else if (this.children[LOWER_LEFT].getRect().contains(
				object.getLocation())) {
			children[LOWER_LEFT].put(object);
			return;
		}
	}
	
	/**
	 * Will make this node parent, and assign 4 children nodes to it.
	 */
	private void divide()
	{
		if(children == null)
		{
			// divide into 4 segments
			children = new Node[4];
			double x = rect.x + rect.width/2;
			double y = rect.y;
			double w = rect.width/2;
			double h = rect.height/2;
			children[UPPER_RIGHT] = new Node(new Rectangle2D.Double(x, y, w, h), this);
//			System.out.println("UPPER_RIGHT "+ x +", "+y +", "+w +", "+h );
			
			x = rect.x;
			y = rect.y;
			children[UPPER_LEFT] = new Node(new Rectangle2D.Double(x, y, w, h), this);
//			System.out.println("UPPER_LEFT "+ x +", "+y +", "+w +", "+h );
			
			x = rect.x + rect.width/2;
			y = rect.y + rect.height/2;
			children[LOWER_RIGHT] = new Node(new Rectangle2D.Double(x, y, w, h), this);
//			System.out.println("LOWER_RIGHT "+ x +", "+y +", "+w +", "+h );

			x = rect.x;
			y = rect.y + rect.height/2;
			children[LOWER_LEFT] = new Node(new Rectangle2D.Double(x, y, w, h), this);
//			System.out.println("LOWER_LEFT "+ x +", "+y +", "+w +", "+h );
//			System.out.println("-------------------");
		}
	}
	
	/**
	 * Will remove empty parent nodes recursively.
	 */
	public void prune()
	{
		if(this.hasChildren())
		{
			// if none of children are parent AND none of children have objects
			if ((!children[0].hasChildren() && !children[1].hasChildren()
					&& !children[2].hasChildren() && !children[3].hasChildren())
					&&

					(!children[0].hasObject() && !children[1].hasObject()
							&& !children[2].hasObject() && !children[3]
								.hasObject())) {
				this.children = null;
			} else {
				if (children[0].hasChildren())
					children[0].prune();
				if (children[1].hasChildren())
					children[1].prune();
				if (children[2].hasChildren())
					children[2].prune();
				if (children[3].hasChildren())
					children[3].prune();
			}
		}			
	}
	
	/**
	 * Will recursively count number of objects in tree
	 * @param n Pass 0 to root node
	 * @return number of objets in tree
	 */
	public int countObjects(int n)
	{
		if(this.hasChildren())
		{
			if (children[0].hasChildren())
				n = children[0].countObjects(n);
			else if (children[0].hasObject())
				n++;
			if (children[1].hasChildren())
				n = children[1].countObjects(n);
			else if (children[1].hasObject())
				n++;
			if (children[2].hasChildren())
				n = children[2].countObjects(n);
			else if (children[2].hasObject())
				n++;
			if (children[3].hasChildren())
				n = children[3].countObjects(n);
			else if (children[3].hasObject())
				n++;
		}
		else if(this.hasObject())
			return n + 1;
		
		return n;
	}
	
	/**
	 * Will traverse through all nodes recursively, and count how many nodes are there.
	 * @return amount of nodes for this parent.
	 */
	public int size()
	{
		if(children == null)
			return 1;
		else
			return 1+ children[0].size() + children[1].size() + children[2].size() + children[3].size();
	}
	
	/**
	 * Will traverse through all nodes recursively, and calculate total mass of all children.
	 * @return total mass of all children.
	 */
	public double calcMass()
	{	
		if(hasChildren() )
		{
			mass = children[0].calcMass() + children[1].calcMass() + children[2].calcMass() + children[3].calcMass();
			return mass;
		}
		else
		{
			mass = body == null ? 0 : body.getMass();
			return mass;
		}
	}
	
	/**
	 * Will traverse through all children nodes, and calculate mass center point of this node.
	 * @return mass center point.
	 * @throws NodeException 
	 */
	public Point2D calcMassCenter() throws NodeException
	{
		double mass = 0;
		double x = 0;
		double y = 0;

		// FIXME tämä metodi kusee, senhän pitäisi olla rekursiivinen
		
		if (this.hasChildren()) {
			
			
			// FIXME toimiiko tämä näin??
			
			if(children[0].hasChildren())
				children[0].calcMassCenter();
			if(children[1].hasChildren())
				children[1].calcMassCenter();
			if(children[2].hasChildren())
				children[2].calcMassCenter();
			if(children[3].hasChildren())
				children[3].calcMassCenter();
			
			
			// start iterating from this node, all the way down
			NodeIterator itr = new NodeIterator(this);
			Node node;
			while((node = itr.next()) != null)
			{
				if (node.hasObject()) {
					mass += node.getBody().getMass();
					x += node.getBody().getX() * node.getBody().getMass();
					y += node.getBody().getY() * node.getBody().getMass();
				}
			}
			x = x / mass;
			y = y / mass;
			massCenter = new Point2D.Double(x, y);
			return massCenter;
		}
		else if(this.hasObject())
		{
			massCenter = new Point2D.Double(body.getX(), body.getY());
			return massCenter;
		}
		else
			throw new NodeException("Node is empty.");
	}
	
	class NodeException extends Exception {
		public NodeException(String msg) {
			super(msg);
		}
	}
	
	public Node getChild(int n)
	{
		return children[n];
	}
	
	public Node nextChild()
	{
		n++;
		if(n < 5)
		{
			return children[n-1];
		}
		return null;
	}
	
	public boolean hasNext()
	{
		if(this.hasChildren())
		{
			if(n < 4)
				return true;
			else 
			{
				n = 0;
				return false;
			}
		}
		else
			return false;
	}
	
	public double getX()
	{
		return rect.x;
	}

	public double getY()
	{
		return rect.y;
	}

	public double getW()
	{
		return rect.width;
	}

	public double getH()
	{
		return rect.height;
	}

	public Rectangle2D.Double getRect() {
		return rect;
	}

	public Node[] getChildren() {
		return children;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Point2D getMassCenter() {
		return massCenter;
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public double getMass() {
		return mass;
	}

	@Override
	public String toString() {
		String str = "";
		if(this.hasChildren())
			str = "Node [rect=" + rect +"] "+ body + "\n"+
					"\n\t"+ children[0]+ 
					"\n\t"+ children[1]+ 
					"\n\t"+ children[2]+ 
					"\n\t"+ children[3];
		else if(this.hasObject())
			str = "Node [rect=" + rect +"] "+ body;
		else
			str = "Node [rect=" + rect +"] ";
				
		return str;
	}
	

}
