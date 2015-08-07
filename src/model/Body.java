package model;

import java.awt.Point;
import java.awt.geom.Point2D;


import util.FunMath;


public class Body extends AbstractBody implements Cloneable{

	private double mass;
	private Point2D vector;
	private Point2D location;
	private boolean movable = true;
	
	public boolean isMovable() {
		return movable;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public Body(int x, int y)
	{
		vector = new Point2D.Double(0, 0);
		location = new Point2D.Double(x, y);
		
		mass = 100 + Math.random() * 50;
	}
	
	public Body(Point point)
	{
		vector = new Point2D.Double(0, 0);
		location = point;

		mass = 100 + Math.random() * 500;
	}
	
	@Override
	public void Move()
	{				
		// Move object by force vector amount
		if(this.movable)
		location.setLocation(location.getX() + vector.getX(), location.getY() + vector.getY());	
	}
	
	public double getX() {
		return location.getX();
	}
	
	public double getY() {
		return location.getY();
	}
	 
	public String toString() {
		return "Body [location=" + location + " mass="+ mass + "vector="+ vector +"]";
	}

	@Override
	public Body clone() {
		 Body clone = null;
		try {
			clone = (Body)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}

	@Override
	public Point2D getLocation() {
		return location;
	}

	public Point2D getVector() {
		return vector;
	}

	public void setVector(Point2D vector) {
		this.vector = vector;
	}

	public void setLocation(Point2D location) {
		this.location = location;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	public double getMass() {
		return mass;
	}
	
	@Override
	public void addGravity(AbstractBody body) {
		double gravityForce = FunMath.calcGravity(this.mass, body.getMass(), this.location.distance(body.getLocation()));
		Point2D force = FunMath.getForce(FunMath.getAngle(FunMath.subPoint( body.getLocation(), this.location)), gravityForce);
		vector = FunMath.addForce(vector, force);
	}
	@Override
	public void addGravity(Point2D location, double mass) {
		
		double gravityForce = FunMath.calcGravity(this.mass, mass, this.location.distance(location));
		Point2D force = FunMath.getForce(FunMath.getAngle(FunMath.subPoint(location, this.location)), gravityForce);
		vector = FunMath.addForce(vector, force);
		
	}
}
