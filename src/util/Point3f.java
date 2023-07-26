/* Eric Whye
Student Number: 19336881
 */
package util;

public class Point3f {

	private float x;
	private float y;
	private float z;

	//Rectangular boundary.
	private float boundary_x1 = 0;
	private float boundary_y1 = 0;
	private float boundary_x2 = GameConstants.FRAME_SIZE_WIDTH;
	private float boundary_y2 = GameConstants.FRAME_SIZE_HEIGHT;
	
	
	// default constructor
	public Point3f() { 
		setX(0.0f);
		setY(0.0f);
		setZ(0.0f);
	}

	public Point3f(Point3f point) {
		setX(point.getX());
		setY(point.getY());
		setZ(point.getZ());
	}
	
	//initializing constructor
	public Point3f(float x, float y, float z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z); 
	}

	public float getYBoundary(){return boundary_y2;}
	public void setBoundaries(float x1, float y1, float x2, float y2) {
		boundary_x1 = x1;
		boundary_y1 = y1;
		boundary_x2 = x2;
		boundary_y2 = y2;
	}

	// sometimes for different algorithms we will need to address the point using positions 0 1 2 
	public float getPostion(int postion)
	{
		switch(postion)
		{
		case 0: return getX();
		case 1: return getY();
		case 2: return getZ(); 
		default: return Float.NaN;  
		} 
	}
	
	public String toString()
	{
		return ("(" + getX() +"," + getY() +"," + getZ() +")");
    }



	 //implement Point plus a Vector and comment what the method does 
	public Point3f PlusVector(Vector3f Additonal) { 
		return new Point3f(this.getX()+Additonal.getX(), this.getY()+Additonal.getY(), this.getZ()+Additonal.getZ());
	} 
	
	 //implement Point minus a Vector and comment what the method does 
	public Point3f MinusVector(Vector3f Minus) { 
		return new Point3f(this.getX()-Minus.getX(), this.getY()-Minus.getY(), this.getZ()-Minus.getZ());
	}

	//Vector Between two points
	public Vector3f MinusPoint(Point3f Minus) {
		return new Vector3f(this.getX()-Minus.getX(), this.getY()-Minus.getY(), this.getZ()-Minus.getZ());
	}
	 
	
	
	 //Use for direct application of a Vector 
	public void ApplyVector(Vector3f vector) {
		 setX(CheckBoundary(this.getX()+vector.getX(), boundary_x1, boundary_x2));
		 setY(CheckBoundary(this.getY()+vector.getY(), boundary_y1, boundary_y2));
		 setZ(CheckBoundary(this.getZ()+vector.getZ(), 0, 0));
	}

	private float CheckBoundary(float f, float b1, float b2) {
		if (f < b1)
			return b1;
		else if (f > b2)
			return b2;
		else
			return f;
	}


	public float getX() {return x;}
	public void setX(float x) {this.x = x;}
	public float getY() {return y;}
	public void setY(float y) {this.y = y;}
	public float getZ() {return z;}
	public void setZ(float z) {this.z = z;}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Point3f temp){
			return this.x == temp.getX() && this.y == temp.getY() && this.z == temp.getZ();
		}else return false;
	}


	// Remember point + point  is not defined so we not write a method for it.
}