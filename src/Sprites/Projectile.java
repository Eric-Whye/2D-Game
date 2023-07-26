/* Eric Whye
Student Number: 19336881
 */
package Sprites;

import util.CollidingGameObject;
import util.GameConstants;
import util.Point3f;
import util.Vector3f;

public class Projectile extends CollidingGameObject {
    private Mob.Identity recipient;//Who this projectile is intended to hit
    private Point3f origin;
    private Point3f destination;
    private float speed;
    private float angle;
    private Vector3f gravity;

    public Projectile(String textureLocation, Point3f centre, Point3f destination, Mob.Identity recipient, float speed, float knockBackForce,
                      Vector3f gravity,
                      int width, int height, int[][] pixel_locations) {
        super(textureLocation, centre, knockBackForce, width, height, pixel_locations);
        this.recipient = recipient;
        this.origin = new Point3f(centre);
        this.destination = destination;
        this.speed = speed;
        this.gravity = gravity;

        centre.setBoundaries(GameConstants.OUTSIDE_BOUNDARY_X1, GameConstants.OUTSIDE_BOUNDARY_Y1, GameConstants.OUTSIDE_BOUNDARY_X2, GameConstants.OUTSIDE_BOUNDARY_Y2);
        angle = (float) Math.atan2(destination.getY()-origin.getY(), destination.getX()-origin.getX());
        this.setVelocity(new Vector3f((float) (speed*Math.cos(angle)), (float) (speed * Math.sin(angle)), 0));
        this.setAcceleration(gravity);
    }

    public Vector3f getSpeed() {
        return new Vector3f((float) (speed*Math.cos(angle)), (float) (speed * Math.sin(angle)), 0);
    }

    public double getRotation(){return Math.atan2(getVelocity().getY(), getSpeed().getX()) * 180/Math.PI;}

    public Mob.Identity getRecipient() {return recipient;}
}
