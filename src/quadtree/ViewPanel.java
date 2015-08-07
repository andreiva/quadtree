package quadtree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import model.Node;

import util.FunMath;
import util.NodeIterator;

public class ViewPanel extends JPanel {

	private Node root;
	NodeIterator itr;

	public ViewPanel() {
		this.setPreferredSize(new Dimension(800, 600));

	}

	public void render(Node root) {
		this.root = root;
		this.repaint();
	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;


		itr = new NodeIterator(root);
		Node node;
		while((node = itr.next()) != null)
		{
			g2.setColor(Color.blue);
			g2.drawRect((int)node.getX(), (int)node.getY(), (int)node.getW(), (int)node.getH());

			if (node.hasObject()) {
				g2.setColor(Color.red);
				g2.fillOval((int)node.getObject().getX()-1, (int)node.getObject().getY()-1, 5, 5);
				
				// draw speed Vector
				Point2D v = node.getBody().getVector();
				v = FunMath.scale(v, 10);
				g2.setColor(Color.green);
				int bx = (int)node.getObject().getX()+1;
				int by = (int)node.getObject().getY()+1;
				int vx = (int)v.getX() + bx;
				int vy = (int)v.getY() + by;
				g2.drawLine(bx, by, vx, vy);
			}
			
		}
	}

}
