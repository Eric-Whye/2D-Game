/* Eric Whye
Student Number: 19336881
 */
package util;

import Sprites.Mob;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;


public class GameObject {
	protected enum Direction {
		LEFT,
		RIGHT
	}

	private Point3f centre = new Point3f(0,0,0);// Centre of object, using 3D as objects may be scaled
	private Vector3f velocity = new Vector3f(0, 0, 0);//Current velocity of object to simulate kinematics
	private Vector3f accel = new Vector3f(0, 0, 0);//Current Acceleration to simulate kinematics
	private Direction dir = Direction.RIGHT;
	private int width = 10;
	private int height = 10;
	private int[][] pixel_locations;
	private boolean hasTextured = false;
	private String textureFilename;
	private Image textureImage;
	private Image blankTexture;

	public GameObject() {}

    public GameObject(String textureLocation, Point3f centre, int width, int height, int[][] pixel_locations) {
		this.textureFilename = textureLocation;
		try {
			blankTexture = ImageIO.read(new File(GameConstants.BULLET_BLANK_FILENAME));
			this.textureImage = ImageIO.read(new File(textureFilename));
			hasTextured = true;
		} catch (IOException e) {throw new RuntimeException(e);}
		this.width = width;
		this.height = height;
		this.pixel_locations = pixel_locations;
		this.centre = centre;
	}

	public void applyVector(Vector3f vector){
		centre.ApplyVector(vector);
	}

	public Vector3f getVelocity(){return velocity;}
	public void setVelocity(Vector3f vector){this.velocity = vector;}
	public Vector3f getAcceleration(){return accel;}
	public void setAcceleration(Vector3f accel){this.accel = accel;}
	public void applyAcceleration(float time){velocity = velocity.PlusVector(accel.byScalar(time));}

	public Point3f getCentre() {return centre;}

	public void setCentre(Point3f centre) {
		this.centre = centre;
		//make sure to put boundaries on the gameObject
	}

	public String getTextureFilename(){return textureFilename;}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {this.width = width;}
	public int getHeight() {return height;}
	public int[][] getPixelLocations(){return pixel_locations;}
	public Image getTexture() {
		if(hasTextured) {return textureImage;}
		return blankTexture;
	}

	public void setLeft(){dir = Direction.LEFT;}
	public void setRight(){dir = Direction.RIGHT;}
	public boolean isLeft(){return dir == Direction.LEFT;}
  	public boolean isRight(){return dir == Direction.RIGHT;}
}