/* Eric Whye
Student Number: 19336881
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import Sprites.PickupSprite;
import Sprites.Projectile;
import Sprites.Mob;
import Sprites.AttackingMob;
import static util.GameConstants.*;

import util.CollidingGameObject;
import util.GameObject;
import util.Point3f;
import util.Vector3f;

import javax.swing.*;

import static Sprites.Mob.Status.*;

public class Model {
	//Variables to handle when the game is lost or won
	private Mob currentBoss = null;//When boss is spawned, this variable is initialised and when it dies, Levels.spawnGameEnder() is called
	private boolean isGameOver = false;
	private boolean isGameWon = false;
	private boolean isGameEnderSpawned = false;

	//Audio
	private final AudioManager audioManager = AudioManager.getInstance();

	//Level Data
	private int currentLevel = 1; //To track what the current level the player is on
	private Levels levelsData = new Levels(this);

	//Coordinates of where the camera should be pointing at in the world.
	private Point3f camLeftCorner = new Point3f(0, 0, 0);//Left Corner
	private Vector3f camSpeed = new Vector3f(0,0,0);
	private Point3f mouseCoords;

	//Players and their inputs
	private AttackingMob Player1;
	private AttackingMob Player2;
	private final KeyController keyController = KeyController.getInstance();
	private final MouseListener player2MouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!Player2.containsStatus(AttackCooldown) && !Player2.containsStatus(KnockedBacked) && !Player2.containsStatus(Dead)) {
				mouseCoords = new Point3f(e.getX(), e.getY(), 0);
				Player2.addStatus(Standing);
				Player2.addStatus(Attacking);
			}
		}@Override public void mousePressed(MouseEvent e) {}@Override public void mouseReleased(MouseEvent e) {}@Override public void mouseEntered(MouseEvent e) {}@Override public void mouseExited(MouseEvent e) {}
	};

	private CopyOnWriteArrayList<GameObject> PlayersList  = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<GameObject> EnemiesList  = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<GameObject> ProjectileList  = new CopyOnWriteArrayList<GameObject>();
	private CopyOnWriteArrayList<GameObject> PowerupsList = new CopyOnWriteArrayList<>();

	public Model() {
		//Setup game world
		camLeftCorner.setBoundaries(0, 0, levelsData.RequestLevelLimit(1), FRAME_SIZE_HEIGHT);

		//Players
		Player1 = new AttackingMob(ELF_FILENAME, new Point3f(80,100,0), Mob.Identity.Player, ELF_SPD, ELF_KNOCKBACK_FORCE, ELF_HITPOINTS,
				ELF_STATIONARY_WIDTH, ELF_STATIONARY_HEIGHT, ELF_STATIONARY_LOCATS,
				ELF_WALKING_WIDTH, ELF_WALKING_HEIGHT, ELF_WALKING_LOCATS,
				ELF_DYING_WIDTH, ELF_DYING_HEIGHT, ELF_DYING_LOCATS,
				ELF_ATTACKING_WIDTH, ELF_ATTACKING_HEIGHT, ELF_ATTACKING_LOCATS);
		Player1.setAttackSequenceThread(() -> {try {
			audioManager.playSound(STAB_AUDIO_FILENAME, -20);
			Thread.sleep(ELF_ATTACK_DELAY);//Period of time when player is in attacking animation
			Player1.removeStatus(Attacking);
			Player1.addStatus(AttackCooldown);
			Thread.sleep(ELF_ATTACK_COOLDOWN);//Period of time when player is on cooldown
			Player1.removeStatus(AttackCooldown);
			} catch (InterruptedException e) {throw new RuntimeException(e);}
		});

		Player2 = new AttackingMob(DRUID_FILENAME, new Point3f(130,100,0), Mob.Identity.Player, DRUID_SPD, DRUID_KNOCKBACK_FORCE, DRUID_HITPOINTS,
				DRUID_STATIONARY_WIDTH, DRUID_STATIONARY_HEIGHT, DRUID_STATIONARY_LOCATS,
				DRUID_WALKING_WIDTH, DRUID_WALKING_HEIGHT, DRUID_WALKING_LOCATS,
				DRUID_DYING_WIDTH, DRUID_DYING_HEIGHT, DRUID_DYING_LOCATS,
				DRUID_ATTACKING_WIDTH, DRUID_ATTACKING_HEIGHT, DRUID_ATTACKING_LOCATS);
		Player2.setAttackSequenceThread(() -> {try {
			Thread.sleep(DRUID_ATTACK_DELAY);//Period of time when player is attacking/charging up attack
			audioManager.playSound(FIREBALL_SHOT_AUDIO_FILENAME, -20);
			createProjectile(DRUID_FILENAME, new Point3f(Player2.getCentre().getX(), Player2.getCentre().getY()-15, Player2.getCentre().getZ()), mouseCoords,
					Mob.Identity.Enemy, DRUID_FIREBALL_SPEED, DRUID_KNOCKBACK_FORCE, DRUID_FIREBALL_GRAVITY,
					DRUID_FIREBALL_WIDTH, DRUID_FIREBALL_HEIGHT, DRUID_FIREBALL_LOCATS);
			Player2.removeStatus(Attacking);
			Player2.addStatus(AttackCooldown);
			Thread.sleep(DRUID_ATTACK_COOLDOWN);//Cooldown time
			Player2.removeStatus(AttackCooldown);
		} catch (InterruptedException e) {throw new RuntimeException(e);}
		});
		PlayersList.add(Player1);
		PlayersList.add(Player2);
	}

	// This is the heart of the game , where the model takes in all the inputs, decides the outcomes and then changes the model accordingly.
	public void gamelogic() {
		// Player Logic first 
		playerLogic(); 
		// Enemy Logic next
		enemyLogic();
		// Bullets move next 
		projectileLogic();
		//Powerups logic next
		PowerUpLogic();
		// interactions between objects 
		gameLogic();
	}

	private int spawnCounter = 0;
	private void gameLogic() {
		//Spawn Enemies
		//If the next enemy to spawn has not spawned yet and if the camera has reached certain pixel thresholds
		if (levelsData.requestHasObjectsSpawned(currentLevel).get(spawnCounter) == false &&
				camLeftCorner.getX() >= levelsData.requestSpawnThresholds(currentLevel).get(spawnCounter) ){
			GameObject enemy = levelsData.requestSpawnObjects(currentLevel).get(spawnCounter);
			if (enemy instanceof AttackingMob) ((AttackingMob) enemy).addStatus(Attacking);
			EnemiesList.add(enemy);
			levelsData.requestHasObjectsSpawned(currentLevel).set(spawnCounter, true);

			if (spawnCounter+1 < levelsData.RequestMaxSpawnCount(currentLevel))
				spawnCounter++;

			if (enemy.getTextureFilename().equals(TITAN_FILENAME))
				currentBoss = (Mob) enemy;
			//Audio
			playEnemySounds(enemy, levelsData.getEnemyAppearanceAudoFilenames());
		}

		//Checking for game end
		if (currentBoss != null)
			if (currentBoss.containsStatus(Dead))
				PronouceGameWon();


		//Checking for dead mobs
		for (GameObject enemy : EnemiesList)
			if (((Mob)enemy).getHitPoints() <= 0)
				pronouceDead((Mob) enemy);
		//Checking for dead players
		for (GameObject player : PlayersList)
			if (((Mob) player).getHitPoints() <= 0)
				pronouceDead((Mob) player);


		//Do collision between enemies and players
		for (GameObject enemy: EnemiesList) {
			for (GameObject player : PlayersList) {
				if (!(enemy instanceof Mob) || !(player instanceof Mob)) continue;
				if (((Mob) enemy).containsStatus(Dead) || ((Mob) player).containsStatus(Dead)) continue;//No collision if one dies
				if (collision(enemy, player)){
					//Deciding if Player attacked the enemy or got hit by enemy
					if (((Mob) player).containsStatus(Attacking) && ((player.isRight() && enemy.getCentre().getX() > player.getCentre().getX()) || //Player is facing right and enemy is on right or
										(player.isLeft() && enemy.getCentre().getX() < player.getCentre().getX()))){ //Player is facing left and enemy is on left

						//enemy is knockedBacked then knockback enemy
						if (!((Mob) enemy).containsStatus(KnockedBacked)) {
							knockback((Mob) player, (Mob) enemy);//Player is knocking the enemy back
							((Mob) enemy).reduceHitPoints(1);

							//Audio
							playEnemySounds(enemy, levelsData.getEnemyInjuredAudoFilenames());
						}
					} else {
						//player isn't knocked back then knockback player
						if (!((Mob) player).containsStatus(KnockedBacked)) {
							knockback((Mob) enemy, (Mob) player);//Enemy is knocking the player back
							((Mob) player).reduceHitPoints(1);

							//Audio
							audioManager.playRandomSound(DAMAGE_AUDIO_FILENAMES, -15);
						}
					}
				}
			}
		}
		//Do collision between enemies and projectiles
		for (GameObject enemy : EnemiesList) {
			for (GameObject projectile : ProjectileList) {
				if (!(enemy instanceof Mob) || !(projectile instanceof Projectile)) continue;
				if (((Mob)enemy).containsStatus(Dead) || ((Projectile)projectile).getRecipient() != Mob.Identity.Enemy) continue;//No collision if one dies
				if (collision(enemy, projectile)){
					knockback((CollidingGameObject) projectile, (Mob) enemy);
					((Mob) enemy).reduceHitPoints(1);
					ProjectileList.remove(projectile);

					//Audio
					playEnemySounds(enemy, levelsData.getEnemyInjuredAudoFilenames());

					if (projectile.getTextureFilename().equals(DRUID_FILENAME))
						audioManager.playSound(FIREBALL_IMPACT_AUDIO_FILENAME, -20);
				}
			}
		}
		//Do collision between players and projectiles
		for (GameObject player : PlayersList) {
			for (GameObject projectile : ProjectileList) {
				if (!(player instanceof Mob) || !(projectile instanceof Projectile)) continue;
				if (((Mob)player).containsStatus(Dead) || ((Projectile)projectile).getRecipient() != Mob.Identity.Player) continue;//No collision if they're dead and can only target the spcified target
				if (collision(player, projectile)){
					knockback((CollidingGameObject) projectile, (Mob) player);
					((Mob) player).reduceHitPoints(1);
					ProjectileList.remove(projectile);

					//Audio
					audioManager.playRandomSound(DAMAGE_AUDIO_FILENAMES, -15);

					if (projectile.getTextureFilename().equals(TITAN_FILENAME))
						audioManager.playSound(BOLT_IMPACT_AUDIO_FILENAME, -15);
					else if (projectile.getTextureFilename().equals(ORC_FILENAME))
						audioManager.playSound(ARROW_IMPACT_AUDIO_FILENAME, -15);
				}
			}
		}
		//Do Collision between Powerups and Players
		for (GameObject powerup : PowerupsList){
			for (GameObject player : PlayersList){
				if (((Mob) player).containsStatus(Dead)) continue;//No collision if one dies
				if (collision(powerup, player)){
					((Mob)player).increaseHitPoints(1);
					PowerupsList.remove(powerup);
				}
			}
		}
	}

	//A bit of a janky collision. Better fix would to have relevant collision widths and heights for each sprite
	private boolean collision(GameObject object1, GameObject object2){
		//AABB Bounding Box detection
		int offset1 = 0;
		int offset2 = 0;
		if (object1 instanceof Mob)
			if (((Mob) object1).getIdentity() == Mob.Identity.Player && !(object2 instanceof Projectile))
					offset1 = 20;
		if (object2 instanceof Mob)
			if (((Mob) object2).getIdentity() == Mob.Identity.Player && !(object1 instanceof Projectile))
					offset1 = 20;
		if (!(object2.getCentre().getY()-(object2.getHeight()/2)> object1.getCentre().getY()+(object1.getHeight()/2) - offset1|| //top of player is below the bottom of enemy
				object1.getCentre().getY()-(object1.getHeight()/2)> object2.getCentre().getY()+(object2.getHeight()/2) - offset2|| //top of enemy is below bottom of player
				object2.getCentre().getX()-(object2.getWidth()/2)+ offset2> object1.getCentre().getX()+(object1.getWidth()/2) - offset1|| //Left of player is right of rightside of enemy
				object1.getCentre().getX()-(object1.getWidth()/2) + offset1> object2.getCentre().getX()+(object2.getWidth()/2) - offset2)){ //Left of enemy is right of rightside of player
			//Collision!
			return true;
		} else
			return false;
	}


	private void enemyLogic() {
		//Remove enemies that are off screen
		for (GameObject enemy : EnemiesList) {
			if (!(enemy instanceof Mob)) continue;
			if (enemy.getCentre().getX() <= OUTSIDE_BOUNDARY_X1-150 || enemy.getCentre().getX() >= OUTSIDE_BOUNDARY_X2+150) {
				EnemiesList.remove(enemy);
				continue;
			}
			//If enemy is dead, keep it from moving left or right. If Camera moves, keep it steady from the player's perspective
			if (((Mob) enemy).containsStatus(Dead)) {
				if (camSpeed.getX()!=0)
					enemy.setVelocity(new Vector3f(-camSpeed.getX(), enemy.getVelocity().getY(), enemy.getVelocity().getZ()));
				else
					enemy.setVelocity(new Vector3f(0, enemy.getVelocity().getY(), enemy.getVelocity().getZ()));
			}

			//Apply physics
			Point3f prevLocation = enemy.getCentre();
			Vector3f prevVelocity = enemy.getVelocity();
			applyKinematicMotions(enemy);
			validatePhysics(enemy, prevLocation, prevVelocity);

			//If enemy is knocked backed, no movement is allowed
			if (((Mob) enemy).containsStatus(KnockedBacked))
				return;
			// Move enemies
			//If enemy is facing left
			if (enemy.isLeft()){
				((Mob) enemy).addStatus(Walking);
				//Change Speed if the background/camera is moving
				if (camSpeed.getX()!=0)
					enemy.setVelocity(new Vector3f(-(((Mob) enemy).getSpeed() + camSpeed.getX()), enemy.getVelocity().getY(), enemy.getVelocity().getZ()));
				else
					enemy.setVelocity(new Vector3f(-((Mob) enemy).getSpeed(), enemy.getVelocity().getY(), enemy.getVelocity().getZ()));

				//Else if enemy is facing right
			}else if (enemy.isRight()){
				enemy.setRight();
				((Mob) enemy).addStatus(Walking);
				if (camSpeed.getX()!=0) {
					enemy.setVelocity(new Vector3f(((Mob) enemy).getSpeed() - camSpeed.getX(), enemy.getVelocity().getY(), enemy.getVelocity().getZ()));
				}else
					enemy.setVelocity(new Vector3f(((Mob) enemy).getSpeed(), enemy.getVelocity().getY(), enemy.getVelocity().getZ()));
			}
		}
	}

	private void projectileLogic() {
		// move bullets
		for (GameObject projectile : ProjectileList) {
		    //check to move them
			if (projectile instanceof Projectile) {
				//Adjust Projectile speed according to camera speed
				if (camSpeed.getX()!=0)
					projectile.setVelocity(new Vector3f(((Projectile)projectile).getSpeed().getX() - camSpeed.getX(), projectile.getVelocity().getY(), projectile.getVelocity().getZ()));
				else
					projectile.setVelocity(new Vector3f(((Projectile)projectile).getSpeed().getX(), projectile.getVelocity().getY(), projectile.getVelocity().getZ()));
				applyKinematicMotions(projectile);
				if (projectile.getCentre().getX() == OUTSIDE_BOUNDARY_X1 || projectile.getCentre().getX() == OUTSIDE_BOUNDARY_X2||
				projectile.getCentre().getY() == OUTSIDE_BOUNDARY_Y1 || projectile.getCentre().getY() == OUTSIDE_BOUNDARY_Y2)
					ProjectileList.remove(projectile);
			}
		}
	}

	private void PowerUpLogic(){
		for (GameObject powerup : PowerupsList){
			if (powerup.getCentre().getX() <= OUTSIDE_BOUNDARY_X1 || powerup.getCentre().getX() >= OUTSIDE_BOUNDARY_X2) {
				PowerupsList.remove(powerup);
				continue;
			}
			if (camSpeed.getX()!=0)
				powerup.setVelocity(new Vector3f(-camSpeed.getX(), powerup.getVelocity().getY(), powerup.getVelocity().getZ()));
			else
				powerup.setVelocity(new Vector3f(0, powerup.getVelocity().getY(), powerup.getVelocity().getZ()));
			//Apply physics to powerup
			Point3f prevLocation = powerup.getCentre();
			Vector3f prevVelocity = powerup.getVelocity();
			applyKinematicMotions(powerup);
			validatePhysics(powerup, prevLocation, prevVelocity);
		}
	}


	private void playerLogic() {
		/*******************Physics and camera checks for both players***********************/
		camSpeed.setX(0);
		boolean boundaryBreached = false;//If the boundary is breached by any player
		Point3f prevLocation1 = Player1.getCentre();
		Vector3f prevVelocity1 = Player1.getVelocity();
		Point3f prevLocation2 = Player2.getCentre();
		Vector3f prevVelocity2 = Player2.getVelocity();

		//Check for a player who is breaching the boundary
		for (int i = 0;i < 2; i++) {
			GameObject player = PlayersList.get(i);
			GameObject otherPlayer = PlayersList.get((i + 1) % 2);

			if (((Mob)player).containsStatus(Dead))//If player is dead, don't move them left or right
				player.setVelocity(new Vector3f(0, player.getVelocity().getY(), player.getVelocity().getZ()));

			//If current player is moving into right boundary, then move camera and stop current player
			if (player.getCentre().getX() >= PLAYER_BOUNDARY_X && player.getVelocity().getX() > 0) {
				boundaryBreached = true;
				//If the other player is o the left of the screen, then don't move the camera
				if (otherPlayer.getCentre().getX() != 0 && !isGameOver) {
					camSpeed.setX(player.getVelocity().getX());
					camLeftCorner.ApplyVector(camSpeed);
				}
				//If a player's velocity has acceleration affecting it,
				//then keep calculating new velocities but keep player from moving in x axis
				player.applyAcceleration((float) TARGET_FPS / 10000);
				player.applyVector(new Vector3f(0, player.getVelocity().getY(), player.getVelocity().getZ()));

				//Apply relative velocity to other player
				otherPlayer.setVelocity(new Vector3f(otherPlayer.getVelocity().getX() - camSpeed.getX(), otherPlayer.getVelocity().getY(), otherPlayer.getVelocity().getZ()));
				applyKinematicMotions(otherPlayer);
				break;
			}

		}
		for (int i = 0; i < 2; i++){
			GameObject player = PlayersList.get(i);
			//If no player is breaching the boundary, then apply normal physics
			if (!boundaryBreached) {
				applyKinematicMotions(player);
			}
		}
		validatePhysics(Player1, prevLocation1, prevVelocity1);
		validatePhysics(Player2, prevLocation2, prevVelocity2);


		/*********************PLAYER INPUTS*************************/
		//System.out.println(Player1.status);
		//****************Attacking**********************
		/////////////PLAYER 1
		//When Player presses space, then attack,
		//If player is not pressing space but is attacking, then reset to standing
		if (keyController.isKeyPressed(32) && !Player1.containsStatus(AttackCooldown) && !Player1.containsStatus(KnockedBacked) && !Player1.containsStatus(Dead)) {
			Player1.addStatus(Standing);
			Player1.addStatus(Attacking);
			Player1.setVelocity(new Vector3f(0, Player1.getVelocity().getY(), Player1.getVelocity().getZ()));
		}
		///////////PLAYER 2
		//Most of player 2 attacking is handled in the constructor
		if (Player2.containsStatus(Attacking) && !Player2.containsStatus(KnockedBacked) && !Player2.containsStatus(Dead)){
			//If player is shooting right, then face right. If shooting left, then face left
			if (mouseCoords.getX() > Player2.getCentre().getX())
				Player2.setRight();
			else if (mouseCoords.getX() < Player2.getCentre().getX())
				Player2.setLeft();
		}

		//***********Movement in the x axis*********************
		/////////////PLAYER 1
		boolean moveLeft = false;
		boolean moveRight = false;
		//TODO Deal with simultaneous key presses
		//If Player isn't attacking and isn't knocked back, then move player if they want to move
		if (!Player1.containsStatus(Attacking) && !Player1.containsStatus(KnockedBacked) && !Player1.containsStatus(Dead)) {
			moveLeft = applyPlayerInputs(Player1, 'A', new Vector3f(-Player1.getSpeed(), Player1.getVelocity().getY(), Player1.getVelocity().getZ()), Walking);
			moveRight = applyPlayerInputs(Player1, 'D', new Vector3f(Player1.getSpeed(), Player1.getVelocity().getY(), Player1.getVelocity().getZ()), Walking);
		}
		if (moveLeft)Player1.setLeft();
		if (moveRight)Player1. setRight();
		//If Player is not moving and not being knocked backed, then reset velocity
		if ((!moveLeft && !moveRight) && !Player1.containsStatus(KnockedBacked)){
			Player1.removeStatus(Walking);
			Player1.addStatus(Standing);
			Player1.setVelocity(new Vector3f(0, Player1.getVelocity().getY(), Player1.getVelocity().getZ()));
		}

		///////////////PLAYER 2
		moveLeft = false;
		moveRight = false;
		//If player isn't jumping or getting knocked back
		if (!Player2.containsStatus(Attacking) && !Player2.containsStatus(KnockedBacked) && !Player2.containsStatus(Dead)) {
			moveLeft = applyPlayerInputs(Player2, 37, new Vector3f(-Player2.getSpeed(), Player2.getVelocity().getY(), Player2.getVelocity().getZ()), Walking);
			moveRight = applyPlayerInputs(Player2, 39, new Vector3f(Player2.getSpeed(), Player2.getVelocity().getY(), Player2.getVelocity().getZ()), Walking);
		}
		if (moveLeft) Player2.setLeft();
		if (moveRight) Player2. setRight();
		//If Player is not moving and not being knocked backed, then reset velocity
		if ((!moveLeft && !moveRight) && !Player2.containsStatus(KnockedBacked)) {
			Player2.removeStatus(Walking);
			Player2.addStatus(Standing);
			Player2.setVelocity(new Vector3f(0, Player2.getVelocity().getY(), Player2.getVelocity().getZ()));
		}
		//****************Jumping*******************
		//If player isn't jumping or getting knocked back
		if (!Player1.containsStatus(Jumping) && !Player1.containsStatus(KnockedBacked) && !Player1.containsStatus(Dead))
			applyPlayerInputs(Player1, 'W', new Vector3f(Player1.getVelocity().getX(), -5, Player1.getVelocity().getZ()), Jumping);

		//When Player velocity is zero, they are not jumping
		if (Player1.getVelocity().getY()==0)
			Player1.removeStatus(Jumping);

		//If player isn't jumping or getting knocked back
		if (!Player2.containsStatus(Jumping) && !Player2.containsStatus(KnockedBacked) && !Player2.containsStatus(Dead))
			applyPlayerInputs(Player2, 38, new Vector3f(Player2.getVelocity().getX(), -5, Player2.getVelocity().getZ()), Jumping);

		//When Player velocity is zero, they are not jumping
		if (Player2.getVelocity().getY()==0)
			Player2.removeStatus(Jumping);
	}

	private boolean applyPlayerInputs(Mob player, int keyCodeInput, Vector3f outputVector, Mob.Status statusToAdd){
		if (keyController.isKeyPressed(keyCodeInput)){
			player.addStatus(statusToAdd);
			player.setVelocity(outputVector);
			return true;
		} else
			return false;
	}

	//Apply object's kinematic parameters
	private void applyKinematicMotions(GameObject object){
		object.applyAcceleration((float)TARGET_FPS/10000);
		object.applyVector(object.getVelocity());
	}

	/**Correct any implemented physics of GameObjects when called up
	 * Current Physics:
	 * Correction of velocity due to gravity when object hits the ground
	 * Correction of velocity and acceleration due to knockback when object finishes being knockedbacked
	 * @param object object to check
	 * @param prevLocation previous frame location
	 * @param prevVelocity prevuiys frame velocity
	 */
	private void validatePhysics(GameObject object, Point3f prevLocation, Vector3f prevVelocity){
		////////////////////////Gravity///////////////////////////
		//If the mob was affected by gravity then its velocity changes else its velocity goes back to zero
		if (object.getCentre().getY() == object.getCentre().getYBoundary())//TODO Bug where Velocity is not reset on elevated places
			object.setVelocity(new Vector3f(object.getVelocity().getX(), 0, object.getVelocity().getZ()));

		/////////////////////////KnockBack////////////////////////////
		if (object instanceof Mob){
			if (((Mob) object).containsStatus(KnockedBacked)){
				//If velocity changes from positive to negative and vice versa, it means mob has stopped moving, therefore knockback is finished
				if (prevVelocity.getX() <= 0 && object.getVelocity().getX() >= 0){
					object.setVelocity(new Vector3f(0, object.getVelocity().getY(), object.getVelocity().getZ()));
					object.setAcceleration(new Vector3f(0, object.getAcceleration().getY(), object.getAcceleration().getZ()));
					((Mob) object).removeStatus(KnockedBacked);
				} else if (prevVelocity.getX() >= 0 && object.getVelocity().getX() <= 0){
					object.setVelocity(new Vector3f(0, object.getVelocity().getY(), object.getVelocity().getZ()));
					object.setAcceleration(new Vector3f(0, object.getAcceleration().getY(), object.getAcceleration().getZ()));
					((Mob) object).removeStatus(KnockedBacked);
				}
			}
		}
	}

	private void knockback(CollidingGameObject knockbacker, Mob mob){
		if (mob.containsStatus(KnockedBacked))return;
		//If knockbackee is left of knockbacker then knockback to the left
		if (mob.getCentre().getX() < knockbacker.getCentre().getX()){
			mob.setVelocity(mob.getVelocity().PlusVector(new Vector3f(-(knockbacker).getKnockbackForce(), 0, 0)));
			mob.setAcceleration(mob.getAcceleration().PlusVector(new Vector3f(20, 0, 0)));
		} else {
			mob.setVelocity(mob.getVelocity().PlusVector(new Vector3f((knockbacker).getKnockbackForce(), 0, 0)));
			mob.setAcceleration(mob.getAcceleration().PlusVector(new Vector3f(-20, 0, 0)));
		}
		mob.addStatus(KnockedBacked);
		mob.addStatus(Walking);
	}

	//Creates a projectile in the world
	protected void createProjectile(String textureLocation, Point3f centre, Point3f destination, Mob.Identity recipient, float speed, float knockBackForce, Vector3f gravity, int width, int height, int[][] pixel_locations){
		ProjectileList.add(new Projectile(textureLocation, centre, destination, recipient, speed, knockBackForce, gravity, width, height, pixel_locations));
	}

	//Handle when a mob dies
	private void pronouceDead(Mob mob){
		if (!mob.containsStatus(Dead)) {
			mob.addStatus(Dead);

			Timer timer = new Timer(5000, e -> {
				EnemiesList.remove(mob);
			});
			timer.setRepeats(false);
			//chance to spawn health powerup if enemy died
			if (mob.getIdentity() == Mob.Identity.Enemy) {
				spawnPowerRandomly(30, PickupSprite.PowerupType.Health, mob.getCentre());
				timer.start();

				//Audio
				playEnemySounds(mob, levelsData.getEnemyDeathAudioFilesnames());
			} else if (mob.getIdentity() == Mob.Identity.Player)//If played is dead, game is over
				pronouceGameOver();
		}
	}

	private void spawnPowerRandomly(double percentChance, PickupSprite.PowerupType powerupType, Point3f location){
		if (new Random().nextDouble() < percentChance)
			PowerupsList.add(new PickupSprite(HEALTH_POWERUP_FILENAME, new Point3f(location), 0, powerupType,
					HEALTH_POWERUP_WIDTH, HEALTH_POWERUP_HEIGHT, HEALTH_POWERUP_LOCATS));
	}

	private void playEnemySounds(GameObject enemy, String[] enemyAudioFilenames) {
		switch (enemy.getTextureFilename()) {
			case BOAR_FILENAME -> audioManager.playSound(enemyAudioFilenames[0], -20);
			case WOLF_FILENAME -> audioManager.playSound(enemyAudioFilenames[1], -20);
			case ZOMBIE_FILENAME -> audioManager.playSound(enemyAudioFilenames[2], -20);
			case BAT_FILENAME -> audioManager.playSound(enemyAudioFilenames[3], -20);
			case ORC_FILENAME -> audioManager.playSound(enemyAudioFilenames[4], -20);
			case TROLL_FILENAME -> audioManager.playSound(enemyAudioFilenames[5], -20);
		}
	}

	private void pronouceGameOver(){
		isGameOver = true;
	}

	//When game is won, add game enders to enemieslist and set them to attack
	private void PronouceGameWon(){
		isGameWon = true;
		if (!isGameEnderSpawned) {
			for (GameObject object : levelsData.spawnGameEnder()) {
				if (object instanceof AttackingMob) ((AttackingMob) object).addStatus(Attacking);
				EnemiesList.add(object);
			}
			isGameEnderSpawned = true;
		}
	}

	public boolean getIsGameOver(){return isGameOver;}
	public boolean getIsGameWon(){return isGameWon;}
	public int getCamLeftCorner() {return (int) camLeftCorner.getX();}

	public CopyOnWriteArrayList<GameObject> getPlayers() {return PlayersList;}
	public CopyOnWriteArrayList<GameObject> getEnemies() {return EnemiesList;}
	public CopyOnWriteArrayList<GameObject> getProjectiles() {return ProjectileList;}
	public CopyOnWriteArrayList<GameObject> getPowerupsList() {return PowerupsList;}

	public MouseListener getPlayer2MouseListener() {return player2MouseListener;}
}