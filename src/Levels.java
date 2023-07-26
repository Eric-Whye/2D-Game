/* Eric Whye
Student Number: 19336881
 */
import Sprites.AttackingMob;
import Sprites.Mob;
import util.GameObject;
import util.Point3f;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static Sprites.Mob.Status.*;
import static util.GameConstants.*;
import static util.GameConstants.OUTSIDE_BOUNDARY_X2;

//An extension of gameworld where bulk monotonous code for level data is
public class Levels{
    Model gameworld;

    //Audio related constants
    private final String[] enemyAppearanceAudoFilenames = {BOAR_APPEARANCE_AUDIO_FILENAME, WOLF_APPEARANCE_AUDIO_FILENAME, ZOMBIE_APPEARANCE_AUDIO_FILENAME, "",ORC_APPEARANCE_AUDIO_FILENAME, TROLL_APPEARANCE_AUDIO_FILENAME, TITAN_APPEARANCE_AUDIO_FILENAME};
    private final String[] enemyInjuredAudoFilenames = {BOAR_INJURED_AUDIO_FILENAME, WOLF_INJURED_AUDIO_FILENAME, ZOMBIE_INJURED_AUDIO_FILENAME, BAT_INJURED_AUDIO_FILENAME, ORC_INJURED_AUDIO_FILENAME, TROLL_INJURED_AUDIO_FILENAME, TITAN_INJURED_AUDIO_FILENAME};
    private final String[] enemyDeathAudioFilesnames = {BOAR_DEATH_AUDIO_FILENAME, WOLF_DEATH_AUDIO_FILENAME, ZOMBIE_DEATH_AUDIO_FILENAME, BAT_DEATH_AUDIO_FILENAME, ORC_DEATH_AUDIO_FILENAME, TROLL_DEATH_AUDIO_FILENAME, TITAN_DEATH_AUDIO_FILENAME};

    public String[] getEnemyAppearanceAudoFilenames() {return enemyAppearanceAudoFilenames;}
    public String[] getEnemyInjuredAudoFilenames() {return enemyInjuredAudoFilenames;}
    public String[] getEnemyDeathAudioFilesnames() {return enemyDeathAudioFilesnames;}

    //Audio
    private final AudioManager audioManager = AudioManager.getInstance();

    private class Level{
        int levelNumber;
        int maxSpawnCount;
        int levelLimit;
        LevelData levelData = new LevelData();
        Level(int levelNumber){this.levelNumber = levelNumber;}
        private class LevelData{
            CopyOnWriteArrayList<Integer> spawnThresholds = new CopyOnWriteArrayList<>();
            CopyOnWriteArrayList<GameObject> spawnObjects = new CopyOnWriteArrayList<>();
            CopyOnWriteArrayList<Boolean> hasObjectsSpawned = new CopyOnWriteArrayList<>();
        }
    }
    //Getters for all the important lists related to spawning enemies
    public CopyOnWriteArrayList<Integer> requestSpawnThresholds(int levelNumber){
        if (levels.size() <= levelNumber)
            return levels.get(levelNumber-1).levelData.spawnThresholds;
        else return null;
    }
    public CopyOnWriteArrayList<GameObject> requestSpawnObjects(int levelNumber){
        if (levels.size() <= levelNumber)
            return levels.get(levelNumber-1).levelData.spawnObjects;
        else return null;
    }
    public CopyOnWriteArrayList<Boolean> requestHasObjectsSpawned(int levelNumber){
        if (levels.size() <= levelNumber)
            return levels.get(levelNumber-1).levelData.hasObjectsSpawned;
        else return null;
    }
    public int RequestMaxSpawnCount(int levelNumber){
        if (levels.size() <= levelNumber)
            return levels.get(levelNumber-1).maxSpawnCount;
        else return 0;
    }
    public int RequestLevelLimit(int levelNumber){
        if (levels.size() <= levelNumber)
            return levels.get(levelNumber-1).levelLimit;
        else return 0;
    }

    protected CopyOnWriteArrayList<GameObject> spawnGameEnder(){
        CopyOnWriteArrayList<GameObject> list = new CopyOnWriteArrayList<>();
        for (int i = 0 ; i < 60; i+=20) {
            GameObject titan = LeftTitan();
            titan.getCentre().setX(titan.getCentre().getX()-i);
            list.add(titan);
        }
        for (int i =0 ; i < 60; i+=20) {
            GameObject titan = RightTitan();
            titan.getCentre().setX(titan.getCentre().getX()+i);
            list.add(titan);
        }
        return list;
    }

    private CopyOnWriteArrayList<Level> levels = new CopyOnWriteArrayList<>();
    protected Levels(Model gameworld){
        this.gameworld = gameworld;
        Level one = new Level(1);
        //Scripted level objects
        int[] arr = {0, 300, 500, 540, 800, 820,
                1000, 1000, 1000, 1000, 1000, //wall of left bats
                1300, 1300, 1300, 1300, 1300,//wall of right bats
                1450, 1480, 1520, 1540, 1580,
                1800, 2100, 2105, 2200, 2220,
                2500, 2520, 2540, 2550, 2700,
                3000, 3020, 3040, 3060, 3100,
                3500};
        one.maxSpawnCount = arr.length;
        one.levelLimit = 10000;
        one.levelData.spawnThresholds.addAll(Arrays.asList(Arrays.stream(arr).boxed().toArray(Integer[]::new)));
        one.levelData.hasObjectsSpawned = new CopyOnWriteArrayList<>(Collections.nCopies(arr.length, false));


        one.levelData.spawnObjects.add(LeftBoar());
        one.levelData.spawnObjects.add(LeftBoar());
        one.levelData.spawnObjects.add(LeftZombie());
        one.levelData.spawnObjects.add(LeftZombie());
        one.levelData.spawnObjects.add(LeftWolf());
        one.levelData.spawnObjects.add(RightWolf());

        for (int i=0; i < 5;i++)
            one.levelData.spawnObjects.add(LeftBat());

        for (int i=0; i < 5;i++)
            one.levelData.spawnObjects.add(RightBat());

        for (int i=0; i < 5;i++)
            one.levelData.spawnObjects.add(LeftZombie());

        one.levelData.spawnObjects.add(LeftTroll());
        one.levelData.spawnObjects.add(LeftWolf());
        one.levelData.spawnObjects.add(LeftOrc());
        one.levelData.spawnObjects.add(LeftOrc());
        one.levelData.spawnObjects.add(LeftOrc());

        one.levelData.spawnObjects.add(LeftTroll());
        one.levelData.spawnObjects.add(LeftTroll());
        one.levelData.spawnObjects.add(LeftOrc());
        one.levelData.spawnObjects.add(LeftOrc());
        one.levelData.spawnObjects.add(RightWolf());

        one.levelData.spawnObjects.add(RightOrc());
        one.levelData.spawnObjects.add(LeftZombie());
        one.levelData.spawnObjects.add(LeftZombie());
        one.levelData.spawnObjects.add(LeftZombie());
        one.levelData.spawnObjects.add(LeftWolf());

        one.levelData.spawnObjects.add(LeftTitan());
        levels.add(one);
    }

    //Methods to spawn all variations of implemented sprites
    private GameObject LeftEnemy(GameObject enemy){
        enemy.getCentre().setBoundaries(OUTSIDE_BOUNDARY_X1,0, OUTSIDE_BOUNDARY_X2, enemy.getCentre().getYBoundary());
        enemy.getCentre().setX(OUTSIDE_BOUNDARY_X2-1);
        enemy.setLeft();
        return enemy;
    }
    private GameObject RightEnemy(GameObject enemy){
        enemy.getCentre().setBoundaries(OUTSIDE_BOUNDARY_X1,0, OUTSIDE_BOUNDARY_X2, enemy.getCentre().getYBoundary());
        enemy.getCentre().setX(OUTSIDE_BOUNDARY_X1+1);
        enemy.setRight();
        return enemy;
    }

    private GameObject LeftBoar(){
        Mob enemy = new Mob(BOAR_FILENAME, new Point3f(FRAME_SIZE_WIDTH+BOAR_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, BOAR_SPD, BOAR_KNOCKBACK_FORCE, BOAR_HITPOINTS,
                BOAR_STATIONARY_WIDTH, BOAR_STATIONARY_HEIGHT, BOAR_STATIONARY_LOCATS,
                BOAR_WALKING_WIDTH, BOAR_WALKING_HEIGHT, BOAR_WALKING_LOCATS,
                BOAR_DYING_WIDTH, BOAR_DYING_HEIGHT, BOAR_DYING_LOCATS);
        return LeftEnemy(enemy);
    }
    private GameObject RightBoar(){
        Mob enemy = new Mob(BOAR_FILENAME, new Point3f(FRAME_SIZE_WIDTH+BOAR_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, BOAR_SPD, BOAR_KNOCKBACK_FORCE, BOAR_HITPOINTS,
                BOAR_STATIONARY_WIDTH, BOAR_STATIONARY_HEIGHT, BOAR_STATIONARY_LOCATS,
                BOAR_WALKING_WIDTH, BOAR_WALKING_HEIGHT, BOAR_WALKING_LOCATS,
                BOAR_DYING_WIDTH, BOAR_DYING_HEIGHT, BOAR_DYING_LOCATS);
        return RightEnemy(enemy);
    }

    private GameObject LeftZombie() {
        Mob enemy = new Mob(ZOMBIE_FILENAME, new Point3f(FRAME_SIZE_WIDTH + ZOMBIE_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, ZOMBIE_SPD, ZOMBIE_KNOCKBACK_FORCE, ZOMBIE_HITPOINTS,
                ZOMBIE_STATIONARY_WIDTH, ZOMBIE_STATIONARY_HEIGHT, ZOMBIE_STATIONARY_LOCATS,
                ZOMBIE_WALKING_WIDTH, ZOMBIE_WALKING_HEIGHT, ZOMBIE_WALKING_LOCATS,
                ZOMBIE_DYING_WIDTH, ZOMBIE_DYING_HEIGHT, ZOMBIE_DYING_LOCATS);
        return LeftEnemy(enemy);
    }
    private GameObject RightZombie() {
        Mob enemy = new Mob(ZOMBIE_FILENAME, new Point3f(FRAME_SIZE_WIDTH + ZOMBIE_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, ZOMBIE_SPD, ZOMBIE_KNOCKBACK_FORCE, ZOMBIE_HITPOINTS,
                ZOMBIE_STATIONARY_WIDTH, ZOMBIE_STATIONARY_HEIGHT, ZOMBIE_STATIONARY_LOCATS,
                ZOMBIE_WALKING_WIDTH, ZOMBIE_WALKING_HEIGHT, ZOMBIE_WALKING_LOCATS,
                ZOMBIE_DYING_WIDTH, ZOMBIE_DYING_HEIGHT, ZOMBIE_DYING_LOCATS);
        return RightEnemy(enemy);
    }
    private GameObject LeftWolf() {
        Mob enemy = new Mob(WOLF_FILENAME, new Point3f(FRAME_SIZE_WIDTH + WOLF_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, WOLF_SPD, WOLF_KNOCKBACK_FORCE, WOLF_HITPOINTS,
                WOLF_STATIONARY_WIDTH, WOLF_STATIONARY_HEIGHT, WOLF_STATIONARY_LOCATS,
                WOLF_WALKING_WIDTH, WOLF_WALKING_HEIGHT, WOLF_WALKING_LOCATS,
                WOLF_DYING_WIDTH, WOLF_DYING_HEIGHT, WOLF_DYING_LOCATS);
        return LeftEnemy(enemy);
    }
    private GameObject RightWolf() {
        Mob enemy = new Mob(WOLF_FILENAME, new Point3f(FRAME_SIZE_WIDTH + WOLF_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, WOLF_SPD, WOLF_KNOCKBACK_FORCE, WOLF_HITPOINTS,
                WOLF_STATIONARY_WIDTH, WOLF_STATIONARY_HEIGHT, WOLF_STATIONARY_LOCATS,
                WOLF_WALKING_WIDTH, WOLF_WALKING_HEIGHT, WOLF_WALKING_LOCATS,
                WOLF_DYING_WIDTH, WOLF_DYING_HEIGHT, WOLF_DYING_LOCATS);
        return RightEnemy(enemy);
    }
    private GameObject LeftBat(){
        Mob enemy = new Mob(BAT_FILENAME, new Point3f(FRAME_SIZE_WIDTH + BAT_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, BAT_SPD, BAT_KNOCKBACK_FORCE, BAT_HITPOINTS,
                BAT_STATIONARY_WIDTH, BAT_STATIONARY_HEIGHT, BAT_STATIONARY_LOCATS,
                BAT_WALKING_WIDTH, BAT_WALKING_HEIGHT, BAT_WALKING_LOCATS,
                BAT_DYING_WIDTH, BAT_DYING_HEIGHT, BAT_DYING_LOCATS);
        LeftEnemy(enemy);
        int min = 0;
        int max = (int)enemy.getCentre().getYBoundary();
        enemy.getCentre().setBoundaries(OUTSIDE_BOUNDARY_X1,0, OUTSIDE_BOUNDARY_X2, new Random().nextInt(max - min + 1) + min);
        return enemy;
    }
    private GameObject RightBat(){
        Mob enemy = new Mob(BAT_FILENAME, new Point3f(FRAME_SIZE_WIDTH + BAT_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, BAT_SPD, BAT_KNOCKBACK_FORCE, BAT_HITPOINTS,
                BAT_STATIONARY_WIDTH, BAT_STATIONARY_HEIGHT, BAT_STATIONARY_LOCATS,
                BAT_WALKING_WIDTH, BAT_WALKING_HEIGHT, BAT_WALKING_LOCATS,
                BAT_DYING_WIDTH, BAT_DYING_HEIGHT, BAT_DYING_LOCATS);
        RightEnemy(enemy);
        int min = 0;
        int max = (int)enemy.getCentre().getYBoundary();
        enemy.getCentre().setBoundaries(OUTSIDE_BOUNDARY_X1,0, OUTSIDE_BOUNDARY_X2, new Random().nextInt(max - min + 1) + min);
        return enemy;
    }
    private GameObject LeftOrc(){
        AttackingMob enemy = new AttackingMob(ORC_FILENAME, new Point3f(FRAME_SIZE_WIDTH+ORC_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, ORC_SPD, ORC_KNOCKBACK_FORCE, ORC_HITPOINTS,
                ORC_STATIONARY_WIDTH, ORC_STATIONARY_HEIGHT, ORC_STATIONARY_LOCATS,
                ORC_WALKING_WIDTH, ORC_WALKING_HEIGHT, ORC_WALKING_LOCATS,
                ORC_DYING_WIDTH, ORC_DYING_HEIGHT, ORC_DYING_LOCATS,
                ORC_ATTACKING_WIDTH, ORC_ATTACKING_HEIGHT, ORC_ATTACKING_LOCATS);
        enemy.setAttackSequenceThread(() -> {
            try {
                Thread.sleep(ORC_ATTACK_DELAY);
                audioManager.playSound(ARROW_SHOT_AUDIO_FILENAME, -20);
                enemy.removeStatus(Attacking);
                gameworld.createProjectile(ORC_FILENAME,
                        new Point3f(enemy.getCentre().getX(), enemy.getCentre().getY()-15, enemy.getCentre().getZ()),
                        new Point3f(enemy.getCentre().getX() - FRAME_SIZE_WIDTH, enemy.getCentre().getY()-500, 0),
                        Mob.Identity.Player, ORC_ARROW_SPEED, ORC_KNOCKBACK_FORCE, ORC_ARROW_GRAVITY,
                        ORC_ARROW_WIDTH, ORC_ARROW_HEIGHT, ORC_ARROW_LOCATS);
                enemy.addStatus(AttackCooldown);
                Thread.sleep(ORC_ATTACK_COOLDOWN);
                enemy.removeStatus(AttackCooldown);
                if (!enemy.containsStatus(Dead))
                    enemy.addStatus(Attacking);
            } catch (InterruptedException e) {throw new RuntimeException(e);}
        });
        return LeftEnemy(enemy);
    }
    private GameObject RightOrc(){
        AttackingMob enemy = new AttackingMob(ORC_FILENAME, new Point3f(FRAME_SIZE_WIDTH+ORC_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, ORC_SPD, ORC_KNOCKBACK_FORCE, ORC_HITPOINTS,
                ORC_STATIONARY_WIDTH, ORC_STATIONARY_HEIGHT, ORC_STATIONARY_LOCATS,
                ORC_WALKING_WIDTH, ORC_WALKING_HEIGHT, ORC_WALKING_LOCATS,
                ORC_DYING_WIDTH, ORC_DYING_HEIGHT, ORC_DYING_LOCATS,
                ORC_ATTACKING_WIDTH, ORC_ATTACKING_HEIGHT, ORC_ATTACKING_LOCATS);
        enemy.setAttackSequenceThread(() -> {
            try {
                Thread.sleep(ORC_ATTACK_DELAY);
                audioManager.playSound(ARROW_SHOT_AUDIO_FILENAME, -20);
                enemy.removeStatus(Attacking);
                gameworld.createProjectile(ORC_FILENAME,
                        new Point3f(enemy.getCentre().getX(), enemy.getCentre().getY()-15, enemy.getCentre().getZ()),
                        new Point3f(enemy.getCentre().getX() + FRAME_SIZE_WIDTH, enemy.getCentre().getY()-500, 0),
                        Mob.Identity.Player, ORC_ARROW_SPEED, ORC_KNOCKBACK_FORCE, ORC_ARROW_GRAVITY,
                        ORC_ARROW_WIDTH, ORC_ARROW_HEIGHT, ORC_ARROW_LOCATS);
                enemy.addStatus(AttackCooldown);
                Thread.sleep(ORC_ATTACK_COOLDOWN);
                enemy.removeStatus(AttackCooldown);
                if (!enemy.containsStatus(Dead))
                    enemy.addStatus(Attacking);
            } catch (InterruptedException e) {throw new RuntimeException(e);}
        });
        return RightEnemy(enemy);
    }
    private GameObject LeftTroll(){
        Mob enemy = new Mob(TROLL_FILENAME, new Point3f(FRAME_SIZE_WIDTH+TROLL_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, TROLL_SPD, TROLL_KNOCKBACK_FORCE, TROLL_HITPOINTS,
                TROLL_STATIONARY_WIDTH, TROLL_STATIONARY_HEIGHT, TROLL_STATIONARY_LOCATS,
                TROLL_WALKING_WIDTH, TROLL_WALKING_HEIGHT, TROLL_WALKING_LOCATS,
                TROLL_DYING_WIDTH, TROLL_DYING_HEIGHT, TROLL_DYING_LOCATS);
        return LeftEnemy(enemy);
    }
    private GameObject RightTroll(){
        Mob enemy = new Mob(TROLL_FILENAME, new Point3f(FRAME_SIZE_WIDTH+TROLL_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, TROLL_SPD, TROLL_KNOCKBACK_FORCE, TROLL_HITPOINTS,
                TROLL_STATIONARY_WIDTH, TROLL_STATIONARY_HEIGHT, TROLL_STATIONARY_LOCATS,
                TROLL_WALKING_WIDTH, TROLL_WALKING_HEIGHT, TROLL_WALKING_LOCATS,
                TROLL_DYING_WIDTH, TROLL_DYING_HEIGHT, TROLL_DYING_LOCATS);
        return RightEnemy(enemy);
    }
    private GameObject LeftTitan(){
        AttackingMob enemy = new AttackingMob(TITAN_FILENAME, new Point3f(FRAME_SIZE_WIDTH+TITAN_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, TITAN_SPD, TITAN_KNOCKBACK_FORCE, TITAN_HITPOINTS,
                TITAN_STATIONARY_WIDTH, TITAN_STATIONARY_HEIGHT, TITAN_STATIONARY_LOCATS,
                TITAN_WALKING_WIDTH, TITAN_WALKING_HEIGHT, TITAN_WALKING_LOCATS,
                TITAN_DYING_WIDTH, TITAN_DYING_HEIGHT, TITAN_DYING_LOCATS,
                TITAN_ATTACKING_WIDTH, TITAN_ATTACKING_HEIGHT, TITAN_ATTACKING_LOCATS);
        enemy.setAttackSequenceThread(() -> {
            try {
                Thread.sleep(TITAN_ATTACK_DELAY);
                audioManager.playSound(BOLT_SHOT_AUDIO_FILENAME, -15);
                for (int i = 0; i <= 400; i+=200)//Shoot 3 projectiles
                    gameworld.createProjectile(TITAN_FILENAME,
                            new Point3f(enemy.getCentre().getX(), enemy.getCentre().getY()-15, enemy.getCentre().getZ()),
                            new Point3f(enemy.getCentre().getX() - (FRAME_SIZE_WIDTH/5)-i,enemy.getCentre().getY()-800, 0),
                            Mob.Identity.Player, TITAN_BOLT_SPEED, TITAN_KNOCKBACK_FORCE, TITAN_BOLT_GRAVITY,
                            TITAN_BOLT_WIDTH, TITAN_BOLT_HEIGHT, TITAN_BOLT_LOCATS);
                Thread.sleep(TITAN_ATTACK_DELAY);
                enemy.removeStatus(Attacking);
                enemy.addStatus(AttackCooldown);
                Thread.sleep(TITAN_ATTACK_COOLDOWN);
                enemy.removeStatus(AttackCooldown);
                if (!enemy.containsStatus(Dead))
                    enemy.addStatus(Attacking);
            } catch (InterruptedException e) {throw new RuntimeException(e);}
        });
        return LeftEnemy(enemy);
    }
    private GameObject RightTitan(){
        AttackingMob enemy = new AttackingMob(TITAN_FILENAME, new Point3f(FRAME_SIZE_WIDTH+TITAN_STATIONARY_WIDTH, GROUND, 0), Mob.Identity.Enemy, TITAN_SPD, TITAN_KNOCKBACK_FORCE, TITAN_HITPOINTS,
                TITAN_STATIONARY_WIDTH, TITAN_STATIONARY_HEIGHT, TITAN_STATIONARY_LOCATS,
                TITAN_WALKING_WIDTH, TITAN_WALKING_HEIGHT, TITAN_WALKING_LOCATS,
                TITAN_DYING_WIDTH, TITAN_DYING_HEIGHT, TITAN_DYING_LOCATS,
                TITAN_ATTACKING_WIDTH, TITAN_ATTACKING_HEIGHT, TITAN_ATTACKING_LOCATS);
        enemy.setAttackSequenceThread(() -> {
            try {
                Thread.sleep(TITAN_ATTACK_DELAY);
                audioManager.playSound(BOLT_SHOT_AUDIO_FILENAME, -15);
                for (int i = 0; i <= 400; i+=200)
                    gameworld.createProjectile(TITAN_FILENAME,
                            new Point3f(enemy.getCentre().getX(), enemy.getCentre().getY()-15, enemy.getCentre().getZ()),
                            new Point3f(enemy.getCentre().getX() + (FRAME_SIZE_WIDTH/5)+i,enemy.getCentre().getY()-800, 0),
                            Mob.Identity.Player, TITAN_BOLT_SPEED, TITAN_KNOCKBACK_FORCE, TITAN_BOLT_GRAVITY,
                            TITAN_BOLT_WIDTH, TITAN_BOLT_HEIGHT, TITAN_BOLT_LOCATS);
                Thread.sleep(TITAN_ATTACK_DELAY);
                enemy.removeStatus(Attacking);
                enemy.addStatus(AttackCooldown);
                Thread.sleep(TITAN_ATTACK_COOLDOWN);
                enemy.removeStatus(AttackCooldown);
                if (!enemy.containsStatus(Dead))
                    enemy.addStatus(Attacking);
            } catch (InterruptedException e) {throw new RuntimeException(e);}
        });
        return RightEnemy(enemy);
    }
}

