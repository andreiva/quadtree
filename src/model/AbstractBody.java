package model;

import java.awt.geom.Point2D;

public abstract class AbstractBody {

	private int id;

	public abstract void Move();
	
	public abstract Point2D getLocation();
	public abstract double getMass();
	public abstract double getX();
	public abstract double getY();
	public abstract void addGravity(AbstractBody body);
	public abstract void addGravity(Point2D location, double mass); 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
