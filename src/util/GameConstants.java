/* Eric Whye
Student Number: 19336881
 */
package util;

public class GameConstants {
    private static final String res = "res/";//Resource Folder
    public static final String BACKGROUND_FILENAME = res+"background.png";
    //Audio
    private static final String audioRes = res+"audio/";
    //BackGround music by szegvari titled Mystic Forest Ambient found on Pixabay.com
    public static final String LEVEL_ONE_BACKGROUND_MUSIC_FILENAME = audioRes+"forest_music.wav";
    public static final String STAB_AUDIO_FILENAME = audioRes+"stab.wav";
    public static final String ARROW_IMPACT_AUDIO_FILENAME = audioRes+"arrow/arrow_impact.wav";
    public static final String ARROW_SHOT_AUDIO_FILENAME = audioRes+"arrow/arrow_shot.wav";
    public static final String FIREBALL_SHOT_AUDIO_FILENAME = audioRes+"druid_fireball/fireball.wav";
    public static final String FIREBALL_IMPACT_AUDIO_FILENAME = audioRes+"druid_fireball/fireball_impact.wav";
    public static final String WALKING_AUDIO_FILENAME = audioRes+"walking.wav";
    //Sprite Audio
    public static final String BOAR_APPEARANCE_AUDIO_FILENAME = audioRes+"boar/npc_boar_foot_01.wav";
    public static final String BOAR_INJURED_AUDIO_FILENAME = audioRes+"boar/npc_boar_injured_01.wav";
    public static final String BOAR_DEATH_AUDIO_FILENAME = audioRes+"boar/npc_boar_death_01.wav";
    public static final String WOLF_APPEARANCE_AUDIO_FILENAME = audioRes+"wolf/roar.wav";
    public static final String WOLF_INJURED_AUDIO_FILENAME = audioRes+"wolf/moan.wav";
    public static final String WOLF_DEATH_AUDIO_FILENAME = audioRes+"wolf/scream.wav";
    public static final String BAT_INJURED_AUDIO_FILENAME = audioRes+"bat/batHitA.wav";
    public static final String BAT_DEATH_AUDIO_FILENAME = audioRes+"bat/batDeathA.wav";
    public static final String ZOMBIE_APPEARANCE_AUDIO_FILENAME = audioRes+"zombie/alert1.wav";
    public static final String ZOMBIE_INJURED_AUDIO_FILENAME = audioRes+"zombie/pain1.wav";
    public static final String ZOMBIE_DEATH_AUDIO_FILENAME = audioRes+"zombie/die1.wav";
    public static final String ORC_APPEARANCE_AUDIO_FILENAME = audioRes+"orc/Orc_Roar1_Growl_Hard #109087_0.wav";
    public static final String ORC_INJURED_AUDIO_FILENAME = audioRes+"orc/Orc Wounded 2 #10008383_0.wav";
    public static final String ORC_DEATH_AUDIO_FILENAME = audioRes+"orc/Orc Death 1 #10008157_0.wav";
    public static final String TROLL_APPEARANCE_AUDIO_FILENAME = audioRes+"troll/Monster_Troll02_Agro_01 #10008622_0.wav";
    public static final String TROLL_INJURED_AUDIO_FILENAME = audioRes+"troll/Monster_Troll01_Hurt_02 #10007471_0.wav";
    public static final String TROLL_DEATH_AUDIO_FILENAME = audioRes+"troll/Monster_Troll01_Death_03 #10008141_0.wav";
    public static final String TITAN_APPEARANCE_AUDIO_FILENAME = audioRes+"titan/spawn.wav";
    public static final String TITAN_INJURED_AUDIO_FILENAME = audioRes+"titan/pain.wav";
    public static final String TITAN_DEATH_AUDIO_FILENAME = audioRes+"titan/death.wav";
    public static final String BOLT_SHOT_AUDIO_FILENAME = audioRes+"titan_bolt/launch_projectile.wav";
    public static final String BOLT_IMPACT_AUDIO_FILENAME = audioRes+"titan_bolt/atak0_hit.wav";
    public static final String _AUDIO_FILENAME = audioRes+"";
    public static final String[] DAMAGE_AUDIO_FILENAMES = new String[11];
    static {
        for (int i = 0 ; i < 11;i++)
            DAMAGE_AUDIO_FILENAMES[i] = audioRes+"damage/dth"+(i+1)+".wav";
    }


    //Sprites
    public static final String BULLET_BLANK_FILENAME = res+"blankSprite.png";
    public static final String HEALTH_POWERUP_FILENAME = res+"heart.png";
    //Sprites from https://www.spriters-resource.com/pc_computer/heroesofmightandmagic2/
    public static final String ELF_FILENAME = res+"elf.png";
    public static final String DRUID_FILENAME = res+"druid.png";
    public static final String BOAR_FILENAME = res+"boar.png";
    public static final String BAT_FILENAME = res+"vampire.png";
    public static final String ZOMBIE_FILENAME = res+"zombie.png";
    public static final String WOLF_FILENAME = res+"wolf.png";
    public static final String ORC_FILENAME = res+"orc.png";
    public static final String TROLL_FILENAME = res+"troll.png";
    public static final String GOLEM_FILENAME = res+"golem.png";
    public static final String TITAN_FILENAME = res+"titan.png";

    public static final int TARGET_FPS = 100;
    public static final int BACKGROUND_WIDTH = 700;
    public static final int BACKGROUND_HEIGHT = 400;

    public static final int FRAME_SIZE_WIDTH = 700;//Initial Frame Sizes
    public static final int FRAME_SIZE_HEIGHT = 400;



    //Game Constants
    //Hard Boundary that the player cannot pass
    public static final int PLAYER_BOUNDARY_X = FRAME_SIZE_WIDTH/2;
    public static final int PLAYER_BOUNDARY_Y = FRAME_SIZE_HEIGHT-75;
    public static final int OUTSIDE_BOUNDARY_X1 = -80;
    public static final int OUTSIDE_BOUNDARY_X2 = FRAME_SIZE_WIDTH+80;
    public static final int OUTSIDE_BOUNDARY_Y1 = -200;
    public static final int OUTSIDE_BOUNDARY_Y2 = FRAME_SIZE_HEIGHT+200;

    public static final int BULLET_SPD = 1;
    public static final Vector3f GRAVITY = new Vector3f( 0, 9.8f, 0);
    public static final int GROUND = FRAME_SIZE_HEIGHT-116;





    /***************************SPRITES*****************************/

    //POWERUP Sprites
    public static final int HEALTH_POWERUP_WIDTH = 27, HEALTH_POWERUP_HEIGHT = 27;
    public static final int[][] HEALTH_POWERUP_LOCATS = {{0,0}};
    //***********************ELF*****************************
    public static final float ELF_SPD = 2.0f;
    public static final int ELF_HITPOINTS = 5;
    public static final float ELF_KNOCKBACK_FORCE = 7;
    public static final int ELF_ATTACK_DELAY = 340;
    public static final int ELF_ATTACK_DURATION= 100;
    public static final int ELF_ATTACK_COOLDOWN = 200;
    //ELF Stationary Dimensions
    public static final int ELF_STATIONARY_WIDTH = 40, ELF_STATIONARY_HEIGHT = 75;
    //Sprite Pixel Locations from top left corner
    private static final int e1 = 4;
    public static final int[][] ELF_STATIONARY_LOCATS = {{16, e1}, {69, e1}, {123, e1}, {181, e1}, {229, e1}, {278, e1}, {326, e1}};
    //ELF Walking Dimensions
    public static final int ELF_WALKING_WIDTH = 50, ELF_WALKING_HEIGHT = 75;
    private static final int e2 = 109;
    public static final int[][] ELF_WALKING_LOCATS = {{0, e2}, {53, e2}, {112, e2}, {177, e2}, {243, e2}, {316, e2}, {385, e2}, {442, e2}, {497, e2}, {552, e2}};
    //ELF ATTACKING Dimensions
    public static final int ELF_ATTACKING_WIDTH = 72, ELF_ATTACKING_HEIGHT = 75;
    private static final int e3 = 450;
    public static final int[][] ELF_ATTACKING_LOCATS = {{178, 333}, {245, 333}, {316, e3}, {393, e3}, {465, e3}, {540, e3}};
    //ELF Dying Dimensions
    public static final int ELF_DYING_WIDTH = 60, ELF_DYING_HEIGHT = 75;
    private static final int e4 = 664;
    public static final int[][] ELF_DYING_LOCATS = {{2, e4}, {68, e4}, {130, e4}, {207, e4}, {272, e4}, {334, e4}};


    //********************DRUID********************************
    public static final float DRUID_SPD = 1.5f;
    public static final int DRUID_HITPOINTS = 3;
    public static final float DRUID_KNOCKBACK_FORCE = 8;
    public static final int DRUID_ATTACK_DELAY = 500;
    private static final int DRUID_ATTACK_DURATION = 0; //Druids don't have an attack_duration, they only make projectiles whose period of effectiveness is the length of their existence.
    public static final int DRUID_ATTACK_COOLDOWN = 500;
    //DRUID Stationary Dimensions
    public static final int DRUID_STATIONARY_WIDTH = 40, DRUID_STATIONARY_HEIGHT = 75;
    private static final int d1 = 11;
    public static final int[][] DRUID_STATIONARY_LOCATS = {{6, d1}, {67 ,d1}, {142, d1}, {209, d1}, {274, d1}};

    //DRUID WALKING Dimensions
    public static final int DRUID_WALKING_WIDTH = 40, DRUID_WALKING_HEIGHT = 75;
    private static final int d2 = 109;
    public static final int[][] DRUID_WALKING_LOCATS = {{12, d2}, {86 ,d2}, {158, d2}, {234, d2}, {304, d2}, {375, d2}, {450, d2}, {531, d2}, {616, d2}, {697, d2}, {765, d2}, {833, d2}};
    //DRUID ATTACKING Dimensions
    public static final int DRUID_ATTACKING_WIDTH = 50, DRUID_ATTACKING_HEIGHT = 75;
    private static final int d3 = 315;
    public static final int[][] DRUID_ATTACKING_LOCATS = {{12, d3}, {105, d3}, {188, d3}, {261, d3}, {328, d3}, {397, d3}, {461, d3}, {551, 404}, {610, 404}, {664, 404}, {723, 404}};
    //DRUID DYING Dimensions
    public static final int DRUID_DYING_WIDTH = 45, DRUID_DYING_HEIGHT = 75;
    private static final int d4 = 211;
    public static final int[][] DRUID_DYING_LOCATS = {{236, d4}, {322, d4}, {410, d4}, {480, d4}, {541, d4}, {607, d4}, {670, d4}, {725, d4}, {778, d4}};
    //DRUID FIREBALL Dimension
    public static final int DRUID_FIREBALL_SPEED = 6;
    public static final Vector3f DRUID_FIREBALL_GRAVITY = new Vector3f(0, 6, 0);
    public static final int DRUID_FIREBALL_WIDTH = 27, DRUID_FIREBALL_HEIGHT = 10;
    public static final int[][] DRUID_FIREBALL_LOCATS = {{146,428}};

    //*******************BOAR*********************************
    public static final float BOAR_SPD = 0.5f;
    public static final int BOAR_HITPOINTS = 2;
    public static final float BOAR_KNOCKBACK_FORCE = 8;
    //Boar Stationary Dimensions
    public static final int BOAR_STATIONARY_WIDTH = 85, BOAR_STATIONARY_HEIGHT = 55;
    private static final int b1 = 17;
    public static final int[][] BOAR_STATIONARY_LOCATS = {{4, b1}, {127, b1}, {256, b1}, {386, b1}};
    //Boar Walking Dimensions
    public static final int BOAR_WALKING_WIDTH = 85, BOAR_WALKING_HEIGHT = 55;
    private static final int b2 = 88;
    public static final int[][] BOAR_WALKING_LOCATS = {{5, b2}, {141, b2}, {275, b2}, {413, b2}, {547, b2}, {682, b2}, {818, b2}, {952, b2}};
    //Boar Dying Dimensions
    public static final int BOAR_DYING_WIDTH = 85, BOAR_DYING_HEIGHT = 60;
    private static final int b3 = 156;
    public static final int[][] BOAR_DYING_LOCATS = {{498, b3}, {620, b3}, {758, b3}, {888, b3}, {1021, b3}};

    //************************Bat****************************
    public static final float BAT_SPD = 0.7f;
    public static final int BAT_HITPOINTS = 3;
    public static final float BAT_KNOCKBACK_FORCE = 8;
    //Bat Stationary Dimensions
    public static final int BAT_STATIONARY_WIDTH = 60, BAT_STATIONARY_HEIGHT = 51;
    private static final int bat1 = 108;
    public static final int[][] BAT_STATIONARY_LOCATS = {{503, bat1}, {588, bat1}};
    //Bat Walking Dimensions
    public static final int BAT_WALKING_WIDTH = 60, BAT_WALKING_HEIGHT = 51;
    private static final int bat2 = 108;
    public static final int[][] BAT_WALKING_LOCATS = {{341, bat2}, {415, bat2}, {503, bat2}, {588, bat2}};
    //Bat Dying Dimensions
    public static final int BAT_DYING_WIDTH = 72, BAT_DYING_HEIGHT = 95;
    private static final int bat3 = 95, bat4 = 506;
    public static final int[][] BAT_DYING_LOCATS = {{245, bat3}, {160, bat3}, {80, bat3}, {2, bat3}, {519, 410}, {605, 410}, {4, bat4}, {91, bat4}, {179, bat4}, {267, bat4}, {352, bat4}, {439, bat4}, {525, bat4}, {608, bat4}};
    //************************ZOMBIE****************************
    public static final float ZOMBIE_SPD = 0.7f;
    public static final int ZOMBIE_HITPOINTS = 2;
    public static final float ZOMBIE_KNOCKBACK_FORCE = 8;
    //Zombie Stationary Dimensions
    public static final int ZOMBIE_STATIONARY_WIDTH = 41, ZOMBIE_STATIONARY_HEIGHT = 77;
    public static final int z1 = 159;
    public static final int[][] ZOMBIE_STATIONARY_LOCATS = {{8, z1}, {57, z1}, {109, z1}, {156, z1}, {201, z1}, {246, z1}};
    //Zombie Walking Dimensions
    public static final int ZOMBIE_WALKING_WIDTH = 34, ZOMBIE_WALKING_HEIGHT = 71;
    private static final int z2 = 85;
    public static final int[][] ZOMBIE_WALKING_LOCATS = {{12, z2}, {63, z2}, {124, z2}, {239, z2}, {291, z2}, {349, z2}};
    //Zombie Dying Dimensions
    public static final int ZOMBIE_DYING_WIDTH = 48, ZOMBIE_DYING_HEIGHT = 65;
    private static final int z3 = 397;
    public static final int[][] ZOMBIE_DYING_LOCATS = {{53, z3}, {105, z3}, {161, z3}, {212, z3}, {264, z3}, {318, z3}, {369, z3}};

    //************************WOLF****************************
    public static final float WOLF_SPD = 1.8f;
    public static final int WOLF_HITPOINTS = 1;
    public static final float WOLF_KNOCKBACK_FORCE = 10;
    //Wolf Stationary Dimensions
    public static final int WOLF_STATIONARY_WIDTH = 86, WOLF_STATIONARY_HEIGHT = 64;
    private static final int w1 = 403;
    public static final int[][] WOLF_STATIONARY_LOCATS = {{26, w1}, {106, w1}, {191, w1}, {279, w1}, {371, w1}, {458, w1}};
    //Wolf Walking Dimensions
    public static final int WOLF_WALKING_WIDTH = 110, WOLF_WALKING_HEIGHT = 64;
    private static final int w2 = 91;
    public static final int[][] WOLF_WALKING_LOCATS = {{19, w2}, {128, w2}, {241, w2}, {365, w2}, {479, w2}};
    //Wolf Dying Dimensions
    public static final int WOLF_DYING_WIDTH = 94, WOLF_DYING_HEIGHT = 88;
    private static final int w3 = 472;
    public static final int[][] WOLF_DYING_LOCATS = {{7, w3}, {220, w3}, {318, w3}, {419, w3}, {518, w3}};
    //************************ORC****************************
    public static final float ORC_SPD = 0.5f;
    public static final int ORC_HITPOINTS = 2;
    public static final float ORC_KNOCKBACK_FORCE = 5;
    public static final int ORC_ATTACK_DELAY = 500;
    public static final int ORC_ATTACK_DURATION= 100;
    public static final int ORC_ATTACK_COOLDOWN = 3500;
    //Orc Stationary Dimensions
    public static final int ORC_STATIONARY_WIDTH = 41, ORC_STATIONARY_HEIGHT = 70;
    private static final int o1 = 2;
    public static final int[][] ORC_STATIONARY_LOCATS = {{3, o1}, {102, o1}, {199, o1}, {300, o1}};
    //Orc Walking Dimensions
    public static final int ORC_WALKING_WIDTH = 49, ORC_WALKING_HEIGHT = 70;
    private static final int o2 = 78;
    public static final int[][] ORC_WALKING_LOCATS = {{0, o2}, {102, o2}, {201, o2}, {307, o2}, {411, o2}, {512, o2}, {616, o2}, {728, o2}};
    //Orc Attacking Dimensions
    public static final int ORC_ATTACKING_WIDTH = 52, ORC_ATTACKING_HEIGHT = 70;
    private static final int o3 = 150;
    public static final int[][] ORC_ATTACKING_LOCATS = {{90, o3}, {190, o3}, {288, o3}, {387, o3}, {483, o3}, {585, o3}, {683, o3}, {785, o3}, {890, o3}, {987, o3}};
    //Orc Arrow Dimensions
    public static final int ORC_ARROW_SPEED = 6;
    public static final Vector3f ORC_ARROW_GRAVITY = new Vector3f(0, 6, 0);
    public static final int ORC_ARROW_WIDTH = 26, ORC_ARROW_HEIGHT = 8;
    public static final int[][] ORC_ARROW_LOCATS = {{993,338}};
    //Orc Dying Dimensions
    public static final int ORC_DYING_WIDTH = 64, ORC_DYING_HEIGHT = 70;
    private static final int o4 = 306;
    public static final int[][] ORC_DYING_LOCATS = {{5, o4}, {95, o4}, {187, o4}, {312, o4}, {412, o4}, {525, o4}, {615, o4}};
    //************************TROLL****************************
    public static final float TROLL_SPD = 0.3f;
    public static final int TROLL_HITPOINTS = 5;
    public static final float TROLL_KNOCKBACK_FORCE = 10;
    //Troll Stationary Dimensions
    public static final int TROLL_STATIONARY_WIDTH = 45, TROLL_STATIONARY_HEIGHT = 80;
    private static final int t1 = 11;
    public static final int[][] TROLL_STATIONARY_LOCATS = {{9, t1}, {129, t1}, {224, t1}, {361, t1}, {476, t1}, {594, t1}, {708, t1}, {824, t1}};
    //Troll Walking Dimensions
    public static final int TROLL_WALKING_WIDTH = 45, TROLL_WALKING_HEIGHT = 80;
    private static final int t2 = 102, t3 = 187;
    public static final int[][] TROLL_WALKING_LOCATS = {{8, t2}, {127,t2}, {245, t2}, {364, t2}, {484, t2}, {603, t2}, {727, t2}, {845, t2}, {8, t3}, {127, t3}, {245, t3}, {364, t3}, {484, t3}, {603, t3}};
    //Troll Dying Dimensions
    public static final int TROLL_DYING_WIDTH = 70, TROLL_DYING_HEIGHT = 83;
    private static final int t5 = 799;
    public static final int[][] TROLL_DYING_LOCATS = {{20, t5},{132, t5}, {246, t5}, {368, t5}, {480, t5},{587, t5},{705, t5},{818, t5}};
    //************************GOLEM****************************
    //************************TITAN****************************
    public static final float TITAN_SPD = 0.2f;
    public static final int TITAN_HITPOINTS = 10;
    public static final float TITAN_KNOCKBACK_FORCE = 13;
    public static final int TITAN_ATTACK_DELAY = 200;
    public static final int TITAN_ATTACK_COOLDOWN = 5000;
    //Titan Stationary Dimensions
    public static final int TITAN_STATIONARY_WIDTH = 45, TITAN_STATIONARY_HEIGHT = 108;
    private static final int tt1 = 3;
    public static final int[][] TITAN_STATIONARY_LOCATS = {{4, tt1}, {117, tt1}, {228, tt1}, {340, tt1}, {452, tt1}, {564, tt1}};
    //Titan Walking Dimensions
    public static final int TITAN_WALKING_WIDTH = 60, TITAN_WALKING_HEIGHT = 108;
    private static final int tt2 = 115;
    public static final int[][] TITAN_WALKING_LOCATS = {{3, tt2}, {123, tt2}, {239, tt2}, {356, tt2}, {480, tt2}, {593, tt2}, {708, tt2}};
    //Titan Attacking Dimensions
    public static final int TITAN_ATTACKING_WIDTH = 80, TITAN_ATTACKING_HEIGHT = 108;
    private static final int tt3 = 345;
    public static final int[][] TITAN_ATTACKING_LOCATS = {{603, tt3}, {706, tt3}, {831, tt3}, {945, tt3}, {1059, tt3}, {1170, tt3}};
    //Titan Bolt Dimensions
    public static final int TITAN_BOLT_SPEED = 2;
    public static final Vector3f TITAN_BOLT_GRAVITY = new Vector3f(0, 1, 0);
    public static final int TITAN_BOLT_WIDTH = 50, TITAN_BOLT_HEIGHT = 13;
    public static final int[][] TITAN_BOLT_LOCATS = {{927, 136}};
    //Titan Dying Dimensions
    public static final int TITAN_DYING_WIDTH = 65, TITAN_DYING_HEIGHT = 108;
    private static final int tt4 = 597;
    public static final int[][] TITAN_DYING_LOCATS = {{7, tt4}, {117, tt4}, {235, tt4}, {346, tt4}, {456, tt4}, {565, tt4}, {676, tt4}, {789, tt4}, {901, tt4}};
}