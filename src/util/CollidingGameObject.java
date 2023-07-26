/* Eric Whye
Student Number: 19336881
 */
package util;

public class CollidingGameObject extends GameObject{
    private float knockbackForce;
    public CollidingGameObject(String textureLocation, Point3f centre, float knockbackForce,
                               int width, int height, int[][] pixel_locations){
        super(textureLocation, centre, width, height, pixel_locations);
        this.knockbackForce = knockbackForce;
    }

    public float getKnockbackForce() {return knockbackForce;}
}
