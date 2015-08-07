package quadtree;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import model.Node;

public class QuadRenderer extends JFrame{
	

	private ViewPanel panel;
	
	public QuadRenderer()
	{
		super("QuadTree");
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(1200, 1000));
		
		panel = new ViewPanel();
		JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.add(scrollPane);
		
		this.pack();
		this.setVisible(true);
		
	}
	

	public void render(Node root) {
		panel.render(root);
	}
	
}
