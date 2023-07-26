/* Eric Whye
Student Number: 19336881
 */
package Sprites;

import util.Point3f;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Sprites.Mob.Status.*;

public class AttackingMob extends Mob{
    private int attacking_width;
    private int attacking_height;
    private int[][] pixel_attacking_locations;
    private Runnable attackSequenceRunnable;
    public AttackingMob(String textureLocation, Point3f centre, Identity identity, float speed, float knockbackForce, int hitPoints,
                        int width, int height, int[][] pixel_stationary_locations,
                        int walking_width, int walking_height, int[][] pixel_walking_locations,
                        int dying_width, int dying_height, int[][] pixel_dying_locations,
                        int attacking_width, int attacking_height, int[][] pixel_attacking_locations) {
        super(textureLocation, centre, identity, speed, knockbackForce, hitPoints,
                width, height, pixel_stationary_locations,
                walking_width, walking_height, pixel_walking_locations,
                dying_width, dying_height, pixel_dying_locations);

        this.attacking_width = attacking_width;
        this.attacking_height = attacking_height;
        this.pixel_attacking_locations = pixel_attacking_locations;
    }

    public void setAttackSequenceThread(Runnable attackSequenceRunnable){
        this.attackSequenceRunnable = attackSequenceRunnable;
    }

    @Override
    public void addStatus(Status status){
        if (status == Status.Attacking && !containsStatus(Attacking)) {
            super.addStatus(status);
            removeStatus(Status.Walking);
            Thread thread = new Thread(attackSequenceRunnable);
            thread.start();
        } else
            super.addStatus(status);

    }

    @Override
    public Status getStatus(){
        if (containsStatus(Status.Attacking)) return Status.Attacking;
        else return super.getStatus();
    }

    @Override
    public int getWidth() {
        if (containsStatus(Status.Dead))
            return super.getWidth();
        else if (containsStatus(Status.Attacking))
            return attacking_width;
        else
            return super.getWidth();
    }

    @Override
    public int getHeight() {
        if (containsStatus(Status.Dead))
            return super.getHeight();
        else if (containsStatus(Status.Attacking))
            return attacking_height;
        else
            return super.getHeight();
    }

    @Override
    public int[][] getPixelLocations() {
        if (containsStatus(Status.Dead))
            return super.getPixelLocations();
        else if (containsStatus(Status.Attacking))
            return pixel_attacking_locations;
        else
            return super.getPixelLocations();
    }
}
