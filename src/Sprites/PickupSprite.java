/* Eric Whye
Student Number: 19336881
 */
package Sprites;

import util.CollidingGameObject;
import util.GameConstants;
import util.Point3f;

public class PickupSprite extends CollidingGameObject {
    public enum PowerupType{
        Health
    }
    private PowerupType powerup;
    public PickupSprite(String textureLocation, Point3f centre, float knockbackForce, PowerupType powerup,
                        int width, int height, int[][] pixel_locations) {
        super(textureLocation, centre, knockbackForce, width, height, pixel_locations);
        this.powerup = powerup;

        centre.setBoundaries(GameConstants.OUTSIDE_BOUNDARY_X1, 0, GameConstants.OUTSIDE_BOUNDARY_X2, GameConstants.GROUND-(getHeight()/2));
        this.setAcceleration(GameConstants.GRAVITY);
    }
}
