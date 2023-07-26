/* Eric Whye
Student Number: 19336881
 */
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.*;

import Sprites.Mob;
import Sprites.Projectile;
import static util.GameConstants.*;
import util.GameObject;

import static Sprites.Mob.Status.*;


public class Viewer extends JPanel {
	JProgressBar progressBar = new JProgressBar(0, 100);
	//Contains various animation data and previous statuses about sprites
	private class SpriteData{
		Mob.Status status;
		int animationCycleTime;
		boolean isDead = false;
		JProgressBar healthBar = new JProgressBar(0, 100);
		SpriteData(Mob.Status status, int animationCycleTime){
			this.status = status;
			this.animationCycleTime = animationCycleTime;
			healthBar.setValue(50);
			healthBar.setStringPainted(true);
		}
	}
	private Model gameworld;
	//Loaded Images for drawing
	private Image backgroundImage;

	//Bodgy way to store previous Statuses of mobs
	private Hashtable<Mob, SpriteData> spriteDataTable = new Hashtable<>();
	private long CurrentAnimationTime = 0;

	public Viewer(Model World) {
		this.gameworld = World;
		try {
			this.backgroundImage = ImageIO.read(new File(BACKGROUND_FILENAME));
		} catch (IOException e) {throw new RuntimeException(e);}
	}
	public Viewer(LayoutManager layout) {super(layout);}
	public Viewer(boolean isDoubleBuffered) {super(isDoubleBuffered);}
	public Viewer(LayoutManager layout, boolean isDoubleBuffered) {super(layout, isDoubleBuffered);}
	public void updateview() {this.repaint();}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Draw background
		drawBackground(gameworld.getCamLeftCorner(), g);

		//Draw Title Announcers
		if (gameworld.getIsGameOver())
			fadeInTitle("Game Over", 1, g);
		else if (gameworld.getIsGameWon())
			fadeInTitle("You Win ;)", 1, g);

		//Draw player
		for (GameObject player : gameworld.getPlayers()){
			/*
			Index 0 - Status.Dead
			Index 1 - Status.Attacking
			Index 2 - Status.Jumping/Status.Walking
			Index 3 - Status.Standing
			Index 4 - default
			 */
			int[] tuners = {3, 17, 15, 4, 4};
			int[] pixelLocation = getPixelLocation((Mob) player, tuners);

			spriteDataTable.get(player).animationCycleTime += 1;
			drawMob((Mob) player, pixelLocation, g);
		}

		//Draw Enemies
		for (GameObject enemy : gameworld.getEnemies()) {
			int[] tuners = {5, 17, 15, 4, 4};
			int[] pixelLocation = getPixelLocation((Mob) enemy, tuners);

			spriteDataTable.get(enemy).animationCycleTime += 1;
			drawMob((Mob) enemy, pixelLocation, g);
		}

		//Draw Bullets
		for (GameObject projectile : gameworld.getProjectiles()) {
			//Projectiles only have one pixelLocation so there is no cycle
			drawProjectile((Projectile) projectile, projectile.getPixelLocations()[0], g);
		}

		//Draw Powerups
		for (GameObject powerup : gameworld.getPowerupsList()){
			//Powerups also don't have cycles so only one pixelLocation
			drawObject(powerup, powerup.getPixelLocations()[0], g);
			//System.out.println(powerup.getCentre());
		}

		CurrentAnimationTime++; // runs animation time step

	}
	//Get cyclic pixelLocations from mobs with variability in animation speed according to their statuses
	private int[] getPixelLocation(Mob mob, int[] animation_speed_tuners){
		//Start up the table with basic data
		if (!spriteDataTable.containsKey(mob))
			spriteDataTable.put(mob, new SpriteData(Standing, 0));

		//If mobs current status isn't the current status:
		//it means status has changed so animationCycle is set to 0
		if (spriteDataTable.get(mob).status != (mob).getStatus())
			spriteDataTable.replace(mob, new SpriteData((mob).getStatus(), 0));
		int[] pixelLocation;
		int[][] pixelLocations = mob.getPixelLocations();
		int animationCycleTime = spriteDataTable.get(mob).animationCycleTime;
		int currCycle;
		switch (spriteDataTable.get(mob).status){
			case Dead ->{
				//If the pixelLocation is the last sprite image, then stay on it
				currCycle = ((animation_speed_tuners[0]*animationCycleTime)/100) % pixelLocations.length;
				if (currCycle == pixelLocations.length-1)
					spriteDataTable.get(mob).isDead = true;
			}
			case Attacking -> currCycle = ((animation_speed_tuners[1]*animationCycleTime)/100) % pixelLocations.length;
			case Walking, Jumping -> currCycle = ((animation_speed_tuners[2]*animationCycleTime)/100) % pixelLocations.length;
			case Standing -> currCycle = ((animation_speed_tuners[3]*animationCycleTime)/100) % pixelLocations.length;
			default -> currCycle = ((animation_speed_tuners[4]*animationCycleTime)/100) % pixelLocations.length;
		}

		if (spriteDataTable.get(mob).isDead)
			pixelLocation = pixelLocations[pixelLocations.length-1];
		else
			pixelLocation = pixelLocations[currCycle];

		return pixelLocation;
	}


	private void drawBackground(int cam_x, Graphics g) {
		//BackGround From the start
		if (cam_x == 0){
			g.drawImage(backgroundImage,
					0, 0, FRAME_SIZE_WIDTH, FRAME_SIZE_HEIGHT,
					0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, null);

			//When Player starts moving and attempts to cross boundary,
		} else {//Draw background to repeat on itself and scroll across frame
			int leftCorner = cam_x%FRAME_SIZE_WIDTH;
			g.drawImage(backgroundImage,
					0, 0,
					FRAME_SIZE_WIDTH-leftCorner, FRAME_SIZE_HEIGHT,
					leftCorner, 0,
					BACKGROUND_WIDTH, BACKGROUND_HEIGHT, null);
			g.drawImage(backgroundImage,
					FRAME_SIZE_WIDTH-leftCorner, 0,
					FRAME_SIZE_WIDTH, FRAME_SIZE_HEIGHT,
					0, 0,
					leftCorner, BACKGROUND_HEIGHT, null);
			}
	}

	private void drawObject(GameObject object, int[] pixelLocation, Graphics g){
		//TODO Offset player when changing stances that have varying widths
		Image texture = object.getTexture();
		int width = object.getWidth();
		int height = object.getHeight();
		int x = (int) object.getCentre().getX() - (width/2);
		int y = (int) object.getCentre().getY() - (height/2);

		//Draw from different source pixels to reverse image when direction is changed
		if (object.isRight())
			g.drawImage(texture,
					x, y, x + width, y + height,
					pixelLocation[0],
					pixelLocation[1],
					pixelLocation[0] + width,
					pixelLocation[1] + height, null);
		else {
			g.drawImage(texture,
					x, y, x + width, y + height,
					pixelLocation[0] + width,
					pixelLocation[1],
					pixelLocation[0],
					pixelLocation[1] + height, null);
		}
	}

	//synchronized as graphics context is a shared context
	private synchronized void drawProjectile(Projectile projectile, int[] pixelLocation, Graphics g) {
		Image texture = projectile.getTexture();
		int width = projectile.getWidth();
		int height = projectile.getHeight();
		int x = (int) projectile.getVelocity().getX();
		int y = (int) projectile.getVelocity().getY();

		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(projectile.getCentre().getX(), projectile.getCentre().getY());//Translate based on projectile position
		g2d.rotate(Math.toRadians(projectile.getRotation()));//Rotate projectile

		//Draw image based on velocity as origin is projectile position
		g2d.drawImage(texture, x, y, x + width, y + height,
				pixelLocation[0],
				pixelLocation[1],
				pixelLocation[0] + width,
				pixelLocation[1] + height, null);
		g2d.rotate(-Math.toRadians(projectile.getRotation()));
		g2d.translate(-projectile.getCentre().getX(), -projectile.getCentre().getY());

	}


	private void drawMob(Mob mob, int[] pixelLocation, Graphics g) {
		drawObject(mob, pixelLocation, g);
		int height = mob.getHeight();
		int y = (int) mob.getCentre().getY() - (height/2);

		//Draw HealthBar
		g.setColor(Color.BLACK);
		g.drawRect((int)mob.getCentre().getX()-20, y+height+10, 40, 1);
		g.setColor(new Color(204, 0, 0));
		g.fillRect((int)mob.getCentre().getX()-20, y+height+10,(int)(40*(mob.getHitPoints()/(float)mob.getMaxHitPoints())), 1);

		//g.drawRect(x, y, width, height);
	}

	private int alpha = 0;
	private int alphaChanger = 5;
	private Timer timer = new Timer(20, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			alpha += alphaChanger;
			if (alpha > 255) {
				alpha = 0;
				timer.stop();
			}
		}
	});
	private void fadeInTitle(String title, int alphaChanger, Graphics g){
		this.alphaChanger = alphaChanger;
		// Set the font and color for the text
		g.setFont(new Font("Arial", Font.BOLD, 48));
		g.setColor(new Color(255, 0, 0, alpha));

		// Draw the title in the center of the panel
		FontMetrics fm = g.getFontMetrics();
		int x = (getWidth() - fm.stringWidth(title)) / 2;
		int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
		g.drawString(title, x, y);
		timer.setRepeats(false);
		if (!timer.isRunning())
			timer.start();
	}

	//Elf and Vampire Sprites by Cyrus Annihilator
	//solid.cyrus@gmail.com
}