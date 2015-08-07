package quadtree;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.Vector;

import model.AbstractBody;
import model.Body;
import model.Node;
import util.FunMath;
import util.NodeIterator;

/**
 * @author andrei
 *
 * http://arborjs.org/docs/barnes-hut
 * http://en.wikipedia.org/wiki/Barnes%E2%80%93Hut_simulation
 * https://wiki.engr.illinois.edu/display/transformation/Barnes-Hut+N-Body+Simulation
 * 
 */

public class QuadTest implements Runnable{

	private static final int X = 1000;
	private static final int Y = 1000;
	private static final int BODIES = 1000;
	private static final double THETA = 1;
	
	
	
	private Thread th = null;
	private static QuadTest qt;
	private static QuadRenderer renderer;
	private static Node root;
	private static Vector<AbstractBody> bodies;
	private static Vector<AbstractBody> bodiesMoved;
	private int counter= 0;
	
	public static void main(String[] args) {
	
		qt = new QuadTest();
		qt.th = new Thread(qt);
		
		qt.init();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		qt.th.start();
	}

	private void init()
	{
		bodies = new Vector<AbstractBody>();
		bodiesMoved = new Vector<AbstractBody>();

		root = new Node(new Rectangle2D.Double(0, 0, X, Y), null, true);
		
		Random r = new Random();
		
		// put bodies in tree
		for (int i = 0; i < BODIES; i++) {
			Body b = new Body(r.nextInt(X-100)+50, r.nextInt(Y-100)+50);
			b.setId(i);
//			b.setMass(100);
			bodies.add(b);
			root.put(b);
			
			// make bodies move in circle motion
			Point2D dist = FunMath.subPoint(b.getLocation(), new Point2D.Double(X/2, Y/2));
			double angle = FunMath.getAngle(dist) + (0.5 * Math.PI);	// add 90 degrees
			b.setVector(FunMath.getForce(angle, 4));
			
		}
		
//		Body b = new Body(251, 251);
//		b.setId(101);
//		b.setMass(15000);
//		b.setMovable(true);
//		bodies.add(b);
//		root.put(b);
		
		System.out.println("Bodies: "+ bodies);
		System.out.println("number of bodies "+ root.countObjects(0));
		
		renderer = new QuadRenderer();
		renderer.render(root);
		
		renderer.repaint();
		System.out.println("size: "+ root.size());

	}
	
	private void runSimulation()
	{

		// calculate masses and center of masses
		double mass = root.calcMass();
		Point2D center = null;
		
		try {
			center = root.calcMassCenter();
		} catch (Exception e) {
			e.printStackTrace();
		}

		counter = 0;
		// calculate net force for each body
		for(AbstractBody body : bodies)
		{
			NodeIterator itr = new NodeIterator(root);
			Node node;
			while((node = itr.next()) != null)
			{
				// parent node.
				// check s / d, if less than theta, add force
				// else check next child node 

				if(node.hasChildren())
				{	
					double s = node.getW();
					double d = body.getLocation().distance(node.getMassCenter());

					// if less than THETA treat as single body
					if((s / d) < THETA ) {
						body.addGravity(node.getMassCenter(), node.getMass());
						counter ++;
					}
				}
				else if (node.hasObject()) {
//					if(body.getId() != node.getBody().getId())
//						body.addGravity(node.getBody());
				}
			}
		}

		// move bodies
		for(AbstractBody body : bodies)
		{
			body.Move();
		}
		
		// Check if body changed its rectangle
		NodeIterator itr = new NodeIterator(root);
		Node node;
		bodiesMoved.clear();
		while((node = itr.next()) != null)
		{
			if(node.hasObject())
			{
				if(false == node.getRect().contains(node.getBody().getLocation()) )
				{	
					bodiesMoved.add(node.getBody().clone());

					if(node.getBody() == bodiesMoved.lastElement())
						System.out.println("SAMA");
					// remove body from Node
					node.setBody(null);		
				}
			}
		}
		
		// remove empty nodes
		root.prune();

		// Now deal with lost bodies
		for(AbstractBody body : bodiesMoved)
		{
			root.put((Body)body);
		}	
		
		System.out.println("calculations: "+ counter);
	}
	
	@Override
	public void run() {
		
		while(true)
		try {
			
			runSimulation();
			renderer.repaint();
						
			th.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
