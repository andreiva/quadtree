package util;

import java.awt.geom.Point2D;
import java.util.Vector;

import model.AbstractBody;


public class FunMath {

	public static double getMagnitude(Point2D point)
	{
		return Math.sqrt(point.getX() * point.getX() + point.getY() * point.getY() );
	}
	
	public static Point2D setMagnitude(Point2D point, double magnitude)
	{
		double angle = getAngle(point);
		double x = Math.cos(angle) * magnitude;
		double y = Math.sin(angle) * magnitude;
		point.setLocation(x, y);
		
		return point;
	}
	
	static public Point2D addForce(Point2D vector, Point2D force)
	{
		double x = vector.getX() + force.getX();
		double y = vector.getY() + force.getY();
		return new Point2D.Double(x, y);
	}
	
	static public Point2D getForce(double angle, double force)
	{
		double x = Math.cos(angle) * force;
		double y = Math.sin(angle) * force;
		return new Point2D.Double(x, y);
	}

	/**
	 * a unit vector = vector / abs(vector)
	 * @param point
	 * @return unit vector
	 */
	static public Point2D getUnitVector(Point2D point)
	{
//		double x = Math.acos(getAngle(point)) * 1;
//		double y = Math.asin(getAngle(point)) * 1;
		
		// about 7 x faster than trigonometric
		double x = point.getX() / getMagnitude(point);
		double y = point.getY() / getMagnitude(point);

		return new Point2D.Double(x, y);
	}
	
	/**
	 * 
	 * @param point
	 * @return
	 */
	static public double getAngle( Point2D point)
	{
		double cangle = 0;
		
		if(point.getX() < 0)
		{
			cangle = Math.atan( -point.getY() / -point.getX() );
			cangle += Math.PI;	
		}
		else		
		cangle = Math.atan( (point.getY()) / (point.getX()) );
		return cangle;
	}
	
	static public double distance(AbstractBody from, AbstractBody to)
	{
		return from.getLocation().distance(to.getLocation());
	}
	
	/**
	 * Calculates mass center of bodies
	 * @param bodies
	 * @return
	 */
	static public Point2D getMassCenter(Vector<AbstractBody> bodies)
	{
		Point2D center = new Point2D.Double(0, 0);
		double mass = 0;
		double x = 0;
		double y = 0;
		
		if(bodies.size()> 0)
		{
			for(AbstractBody body : bodies)
			{
				mass += body.getMass();
				x += body.getX() * body.getMass();
				y += body.getY() * body.getMass();
			}
			
			x = x / mass;
			y = y / mass;
			center.setLocation(x, y);
		}
		return center;
	}
	
	public static double calcGravity(double mass1, double mass2, double distance)
	{
		// Newtons law of gravity
		double force = 0.0001* (( mass1 * mass2) / (distance * distance));
		return force;
	}
	
	public static Point2D scale(Point2D vector, double factor) {
		
		return new Point2D.Double(vector.getX()*factor, vector.getY()*factor);
	}
	
	public static Point2D summPoint(Point2D a, Point2D b)
	{
		return new Point2D.Double(a.getX() + b.getX(), a.getY() + b.getY());
	}
	
	public static Point2D subPoint(Point2D a, Point2D b)
	{
		return new Point2D.Double(a.getX() - b.getX(), a.getY() - b.getY());
	}
	
}
