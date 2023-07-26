/* Eric Whye
Student Number: 19336881
 */
package Sprites;

import util.*;

import java.util.HashSet;
import java.util.Set;


public class Mob extends CollidingGameObject{
	//***************IMPORTANT*******************//
	//Statuses are listed in order of priority. e.g. Stabbing information is more important than standing
	public enum Status{
		KnockedBacked,//Mob is being knocked back
		Attacking,//Mob is conducting an attack
		Walking,
		Jumping,
		Standing,
		Shooting,
		AttackCooldown,//Attack is on cooldown
		Dead
	}
	private final Set<Status> status = new HashSet<>();
	{status.add(Status.Standing);}

	//This is mainly implemented for different projectiles to be able to only hit certain groups of sprites, but is also generally useful
	public enum Identity{
		Player,
		Enemy
	}
	private final Identity identity;

	private final int walking_width;
	private final int walking_height;
	private final int[][] pixel_walking_locations;
	private final int dying_width;
	private final int dying_height;
	private final int[][] pixel_dying_locations;

	private final float speed;
	private int hitPoints;
	private final int maxHitPoints;

	public Mob(String textureLocation, Point3f centre, Identity identity, float speed, float knockbackForce, int hitPoints,
			   int width, int height, int[][] pixel_stationary_locations,
			   int walking_width, int walking_height, int[][] pixel_walking_locations,
			   int dying_width, int dying_height, int[][]pixel_dying_locations) {
		super(textureLocation, centre, knockbackForce, width, height, pixel_stationary_locations);
		this.identity = identity;
		this.speed = speed;
		this.maxHitPoints = hitPoints;
		this.hitPoints = hitPoints;
		this.walking_width = walking_width;
		this.walking_height = walking_height;
		this.pixel_walking_locations = pixel_walking_locations;
		this.dying_width = dying_width;
		this.dying_height = dying_height;
		this.pixel_dying_locations = pixel_dying_locations;

		centre.setBoundaries(0, 0, GameConstants.FRAME_SIZE_WIDTH, GameConstants.GROUND-(getHeight()/2));
		this.setAcceleration(GameConstants.GRAVITY);
	}

	//Mobs that can can Stab (have attack animations)
	public Identity getIdentity() {return identity;}
	public float getSpeed() {return speed;}
	public int getMaxHitPoints() {return maxHitPoints;}
	public int getHitPoints(){return hitPoints;}
	public void reduceHitPoints(int reduceBy){hitPoints -= reduceBy;}
	public void increaseHitPoints(int increaseBy){
		if (hitPoints+increaseBy >= maxHitPoints)
			hitPoints = maxHitPoints;
		else
		 	hitPoints += increaseBy;
	}

	public boolean containsStatus(Status status){ return this.status.contains(status);}
	public void addStatus(Status status){
		if (!this.status.contains(status)) {
			this.status.add(status);
			//Some statuses can coexist at the same time while others cannot. These Cases are resolved here
			if (status == Status.Walking) removeStatus(Status.Standing);
			if (status == Status.Standing) removeStatus(Status.Walking);
			if (status == Status.KnockedBacked) removeStatus(Status.Attacking);
		}
	}
	public void removeStatus(Status status){this.status.remove(status);}

	/**
	 * Some Status are more important than others
	 * @return Most important status this object contains
	 */
	public Status getStatus(){
		if (status.contains(Status.Dead)) return Status.Dead;
		else if (status.contains(Status.Jumping)) return Status.Jumping;
		else if (status.contains(Status.Walking)) return Status.Walking;
		else return Status.Standing;
	}

	@Override
	public int getWidth() {
		if (status.contains(Status.Dead))
			return dying_width;
		else if (status.contains(Status.Walking) || status.contains(Status.Jumping))
			return walking_width;
		else if (status.contains(Status.Standing))
			return super.getWidth();
		else
			throw new IllegalArgumentException("Erroneous Status Assigned");
	}

	@Override
	public int getHeight() {
		if (status.contains(Status.Dead))
			return dying_height;
		else if (status.contains(Status.Walking) || status.contains(Status.Jumping))
			return walking_height;
		else if (status.contains(Status.Standing))
			return super.getHeight();
		else
			throw new IllegalArgumentException("Erroneous Status Assigned");
	}

	@Override
	public int[][] getPixelLocations(){
		if (status.contains(Status.Dead))
			return pixel_dying_locations;
		else if (status.contains(Status.Walking) || status.contains(Status.Jumping))
			return pixel_walking_locations;
		else if (status.contains(Status.Standing))
			return super.getPixelLocations();
		else throw new IllegalArgumentException("Erroneous Status Assigned");
	}
}
