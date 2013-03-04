package dustmod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import dustmod.runes.DEBait;
import dustmod.runes.DEBomb;
import dustmod.runes.DEBounce;
import dustmod.runes.DECage;
import dustmod.runes.DECampFire;
import dustmod.runes.DEChargeInscription;
import dustmod.runes.DECompression;
import dustmod.runes.DEDawn;
import dustmod.runes.DEEarthSprite;
import dustmod.runes.DEEggifier;
import dustmod.runes.DEFarm;
import dustmod.runes.DEFireBowEnch;
import dustmod.runes.DEFireRain;
import dustmod.runes.DEFireSprite;
import dustmod.runes.DEFireTrap;
import dustmod.runes.DEFlatten;
import dustmod.runes.DEFog;
import dustmod.runes.DEForcefield;
import dustmod.runes.DEFortuneEnch;
import dustmod.runes.DEHeal;
import dustmod.runes.DEHideout;
import dustmod.runes.DEHunterVision;
import dustmod.runes.DELiftTerrain;
import dustmod.runes.DELightning;
import dustmod.runes.DELillyBridge;
import dustmod.runes.DELoyaltySprite;
import dustmod.runes.DELumberjack;
import dustmod.runes.DELunar;
import dustmod.runes.DEMiniTele;
import dustmod.runes.DEObelisk;
import dustmod.runes.DEPit;
import dustmod.runes.DEPoisonTrap;
import dustmod.runes.DEPowerRelay;
import dustmod.runes.DEResurrection;
import dustmod.runes.DESilkTouchEnch;
import dustmod.runes.DESpawnRecord;
import dustmod.runes.DESpawnTorch;
import dustmod.runes.DESpawnerCollector;
import dustmod.runes.DESpawnerReprog;
import dustmod.runes.DESpeed;
import dustmod.runes.DESpiritTool;
import dustmod.runes.DETeleportation;
import dustmod.runes.DETimeLock;
import dustmod.runes.DEVoid;
import dustmod.runes.DEWall;
import dustmod.runes.DEXP;
import dustmod.runes.DEXPStore;

/**
 * This pack is meant for testing runes & inscriptions as a separate download to
 * make sure that the added content is balanced and fair.
 * 
 * @author billythegoat101
 * 
 */
@Mod(modid = "DustModDefaults", name = "Dust mod default Rune Pack", version = "1.5.1", dependencies = "after:DustMod")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class DustModDefaults {

	@Instance("DustModDefaults")
	public static DustModDefaults instance;

	@PostInit
	public void postInit(FMLPostInitializationEvent evt) {
		registerDusts();
		registerRunes();
		registerInscriptions();
	}

	public void registerDusts() {
		// Default dusts come with the actual mod to start
	}

	public void registerRunes() {

		// System.out.println("Registering Shapes");
		int N = -1;
		int P = 100;
		int G = 200;
		int L = 300;
		int B = 400;
		DustShape s;
		int[][][] values;
		// test shape
		// s = new DustShape(2,2,1, "testshape");
		// DustManager.registerLocalDustShape(s, new DETestEvent());
		// s.setDataAt(0, 0, 0, 1);
		// s.setDataAt(0, 0, 1, 1);
		// s.setDataAt(1, 0, 1, 1);
		// s.setDataAt(1, 0, 0, 1);
		// DustManager.shapes.add(s);
		// <editor-fold defaultstate="collapsed" desc="torch">
		s = new DustShape(4, 4, "torch2", false, 0, 0, 0, 0, 0);
		values = new int[][][] { { { 0, 0, 0, 0 }, { 0, N, N, 0 },
				{ 0, N, N, 0 }, { 0, 0, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Torch Rune");
		s.setNotes("Sacrifice:\n\n"
				+ "-None: Normal torch spawn.\n"
				+ "-1xFlint: Beacon rune.\n"
				+ "\nNotes:\n\n"
				+ "=Sacrificing a dye to an existing beacon will change its color.");
		s.setDesc("Description:\n\n"
				+ "Spawns a torch or, if a piece of flint is sacrficed, a beacon.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 00, 00, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DESpawnTorch());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="rabbit">
		s = new DustShape(4, 4, "rabbit", true, 0, 0, 0, 0, 44);
		values = new int[][][] { { { 0, 0, N, N }, { 0, 0, 0, N },
				{ N, 0, 0, 0 }, { N, N, 0, 0 } } };
		s.setData(values);
		s.addAllowedVariable(100, 200, 300, 400);
		s.setRuneName("Rune of the Rabbit Hole");
		s.setNotes("Sacrifice:\n\n"
				+ "-2xHunger.\n\n"
				+ "Notes:\n\n"
				+ "-Variable determines the volume of the den.\n"
				+ "-Stepping on the rune will send you down.\n"
				+ "-Standing directly below and crouching will send you back up.");
		s.setDesc("Description:\n\n"
				+ "Creates a small hole beneath the rune and allows you to jump "
				+ "inside for safety. Walking over the top will send you down to "
				+ "the next solid block below the rune. Pressing [crouch] while "
				+ "directly beneath the rune will bring you back up.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 00, 00, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEHideout());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="heal">
		s = new DustShape(4, 4, "heal", true, 0, 0, 0, 0, 1);
		values = new int[][][] { { { 0, N, N, 0 }, { N, N, N, N },
				{ N, N, N, N }, { 0, N, N, 0 }, } };
		s.setData(values);
		s.addAllowedVariable(100, 200, 300, 400);
		s.setRuneName("Rune of Healing");
		s.setNotes("Sacrifice:\n\n" + "-2xCoal + 2XP");
		s.setDesc("Description:\n\n"
				+ "Heals any nearby entities' hearts with regeneration.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEHeal());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="lumberjack">
		s = new DustShape(8, 8, "lumberjack", true, 2, 2, 2, 2, 4);
		values = new int[][][] { { { 0, 0, N, 0, 0, N, 0, 0 },
				{ 0, 0, N, N, N, N, 0, 0 },

				{ N, N, 0, 0, 0, 0, N, N }, { 0, N, 0, 0, 0, 0, N, 0 },
				{ 0, N, 0, 0, 0, 0, N, 0 }, { N, N, 0, 0, 0, 0, N, N },

				{ 0, 0, N, N, N, N, 0, 0 }, { 0, 0, N, 0, 0, N, 0, 0 } } };
		s.setData(values);
		s.addAllowedVariable(100, 200, 300, 400);
		s.setRuneName("Rune of Lumber");
		s.setNotes("Sacrifice:\n\n" + "-3xLog + 2xStick");
		s.setDesc("Description:\n\n"
				+ "Chops down all trees within an area. Has a chance to drop more than 1 log and some sticks.\n"
				+ "The area of effect and the chances of doubling increase with dust value. "
				+ "Also destroys leaves with a small chance of dropping plant runic dust.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 1, -1, 0, 0, -1, 1, 0 });
		DustManager.registerLocalDustShape(s, new DELumberjack());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="campfire">
		s = new DustShape(8, 8, "campfire6", true, 2, 2, 2, 2, 40);
		values = new int[][][] { { { 0, P, 0, P, P, 0, P, 0 },
				{ P, 0, 0, P, P, 0, 0, P },

				{ 0, 0, 0, 0, 0, 0, 0, 0 }, { P, P, 0, 0, 0, 0, P, P },
				{ P, P, 0, 0, 0, 0, P, P }, { 0, 0, 0, 0, 0, 0, 0, 0 },

				{ P, 0, 0, P, P, 0, 0, P }, { 0, P, 0, P, P, 0, P, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of Fire");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		s.setAuthor("billythegoat101");
		s.setNotes("Sacrifice:\n\n" + "-8xLog + 1xZombieFlesh\n\n"
				+ "Notes:\n\n" + "-Lasts a fourth of a day unless fueled.\n");
		s.setDesc("Description:\n\n"
				+ "Creates a flame that allows you to smelt items instantly. "
				+ "There is a small chance of getting double of what you throw in. "
				+ "\n-\nItems must be thrown in 1 at a time or else they will burn.");
		DustManager.registerLocalDustShape(s, new DECampFire());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="pit">
		s = new DustShape(8, 8, "pit", true, 2, 2, 2, 2, 2);
		values = new int[][][] { { { 0, 0, N, 0, 0, N, 0, 0 },
				{ 0, N, N, 0, 0, N, N, 0 },

				{ N, N, 0, 0, 0, 0, N, N }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, { N, N, 0, 0, 0, 0, N, N },

				{ 0, N, N, 0, 0, N, N, 0 }, { 0, 0, N, 0, 0, N, 0, 0 } } };
		s.setData(values);
		s.addAllowedVariable(100, 200, 300, 400);
		s.setRuneName("Rune of Depths");
		s.setAuthor("billythegoat101");
		s.setNotes("Sacrifice:\n\n" + "-2xLog at Plant & Gunpowder levels\n"
				+ "-2xCoal at Lapis & Blaze levels.\n\n" + "Notes:\n\n"
				+ "-Requires hole in the center.");
		s.setDesc("Description:\n\n"
				+ "Digs a pit down into the earth. Requires a hole at the center of the rune (1 block down).");
		DustManager.registerLocalDustShape(s, new DEPit());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="obelisk">
		s = new DustShape(8, 8, "obelisk", true, 2, 2, 2, 2, 15);
		values = new int[][][] { { { 0, 0, 0, P, P, 0, 0, 0 },
				{ 0, P, P, P, P, P, P, 0 },

				{ 0, P, 0, 0, 0, 0, P, 0 }, { P, P, 0, 0, 0, 0, P, P },
				{ P, P, 0, 0, 0, 0, P, P }, { 0, P, 0, 0, 0, 0, P, 0 },

				{ 0, P, P, P, P, P, P, 0 }, { 0, 0, 0, P, P, 0, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of Heights");
		s.setNotes("Sacrifice:\n\n"
				+ "-1xIronOre\n\n"
				+ "Notes:\n\n"
				+ "-The obelisk will stay standing for one day waiting for you.\n"
				+ "-Destroying the top block will cause it to go back down.\n"
				+ "-If you do not destroy the top block within a day, it will simply remain standing.");
		s.setDesc("Description:\n\n"
				+ "Creates a ride-able pillar to the sky. When it reaches the top, you can destroy the top block to send it back down.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEObelisk());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="farm">
		s = new DustShape(8, 6, "farm", false, 2, 3, 2, 1, 3);
		values = new int[][][] { { { 0, P, P, 0, 0, P, P, 0 },

		{ P, 0, P, N, N, P, 0, P }, { P, 0, P, N, N, P, 0, P },
				{ P, 0, P, N, N, P, 0, P }, { P, 0, P, N, N, P, 0, P },

				{ 0, P, P, 0, 0, P, P, 0 } } };
		s.setData(values);
		s.addAllowedVariable(100, 200, 300, 400);
		s.setRuneName("Rune of the Farm");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { -1, 0, 0, 0, -1, 0, 0, 0 });
		s.setNotes("Sacrifice:\n\n" + "-8xIronIngot + 4XP");
		s.setDesc("Description:\n\n" + "Instantly spawns a farm.");
		DustManager.registerLocalDustShape(s, new DEFarm());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="lilypad">
		s = new DustShape(6, 9, "lilypad", false, 3, 0, 1, 4, 7);
		values = new int[][][] { { { 0, 0, P, G, 0, 0 }, { 0, 0, P, G, 0, 0 },
				{ 0, P, P, G, G, 0 }, { 0, P, P, G, G, 0 },

				{ P, P, 0, 0, G, G }, { P, P, P, G, G, G },
				{ P, P, G, P, G, G }, { P, P, P, G, G, G },

				{ 0, 0, P, G, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of the Leaping Frog");
		s.setNotes("Sacrifice:\n\n" + "-4xLeaves");
		s.setDesc("Description:\n\n"
				+ "Spawns a bridge of lily pads over a body of water in front of it.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, -1, 0, 0 });
		DustManager.registerLocalDustShape(s, new DELillyBridge());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="dawn">
		s = new DustShape(8, 8, "dawn", true, 2, 2, 2, 2, 8);
		values = new int[][][] { { { 0, 0, G, 0, 0, G, 0, 0 },
				{ 0, 0, 0, G, G, 0, 0, 0 },

				{ G, 0, G, 0, 0, G, 0, G }, { 0, G, 0, G, G, 0, G, 0 },
				{ 0, G, 0, G, G, 0, G, 0 }, { G, 0, G, 0, 0, G, 0, G },

				{ 0, 0, 0, G, G, 0, 0, 0 }, { 0, 0, G, 0, 0, G, 0, 0 } } };
		s.setRuneName("Rune of Dawn");
		s.setData(values);
		s.setNotes("Sacrifice:\n\n"
				+ "-4xRedstoneDust + 1LapisLazuli\n\n"
				+ "Notes:\n"
				+ "-If it is already day, it will last through to the next night night to do anything.");
		s.setDesc("Description:\n\n"
				+ "Turns night into day. If it is already day, it will wait until the next night to activate.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEDawn());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="lunar">
		s = new DustShape(4, 4, "lunar", true, 0, 0, 0, 0, 9);
		values = new int[][][] { { { G, G, G, G }, { G, 0, 0, G },
				{ G, 0, 0, 0 }, { G, G, 0, G } } };
		s.setRuneName("Rune of Dusk");
		s.setData(values);
		s.setNotes("Sacrifice:\n\n" + "-4xNetherwart + 1xLapisLazuli");
		s.setDesc("Description:\n\n"
				+ "Turns day into night. If it is already night, it will wait until the next day to activate.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DELunar());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="firetrap">
		s = new DustShape(4, 4, "firetrap", true, 0, 0, 0, 0, 10);
		values = new int[][][] { { { 0, 0, 0, N }, { N, 0, N, N },
				{ N, N, 0, 0 }, { N, N, N, 0 } } };
		s.setData(values);
		s.addAllowedVariable(200, 300, 400);
		s.setRuneName("Fire Trap Rune");
		s.setNotes("Sacrifice:\n\n" + "-3xFlint\n\n" + "Notes:\n\n"
				+ "-Dust must be gunpowder or better.");
		s.setDesc("Description:\n\n"
				+ "Sets entities and landscape on fire when an entity comes near.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEFireTrap());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="lightning">
		s = new DustShape(4, 4, "lightning", true, 0, 0, 0, 0, 12);
		values = new int[][][] { { { 0, N, N, N }, { 0, N, 0, 0 },
				{ 0, 0, N, 0 }, { N, N, N, 0 } } };
		s.setData(values);
		s.addAllowedVariable(200, 300, 400);
		s.setRuneName("Lightning Trap Rune");
		s.setNotes("Sacrifice:\n\n" + "-3xIronIngot\n\n" + "Notes:\n\n"
				+ "-Dust must be gunpowder or better.");
		s.setDesc("Description:\n\n"
				+ "Zaps entities with lightning when an entity comes near.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DELightning());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="poison">
		s = new DustShape(6, 6, "poison", true, 3, 3, 1, 1, 11);
		values = new int[][][] { { { 0, 0, N, 0, N, 0 },

		{ 0, N, N, N, N, N }, { N, N, N, 0, N, 0 }, { N, 0, N, N, N, N },
				{ 0, N, 0, N, N, 0 },

				{ 0, 0, N, N, 0, 0 } } };
		s.setData(values);
		s.addAllowedVariable(200, 300, 400);
		s.setRuneName("Poison Trap Rune");
		s.setNotes("Sacrifice:\n\n" + "-1xSpiderEye\n\n" + "Notes:\n\n"
				+ "-Dust must be gunpowder or better.");
		s.setDesc("Description:\n\n"
				+ "Poisons entities when an entity comes near.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEPoisonTrap());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="bomb">
		s = new DustShape(5, 6, "bomb", false, 0, 2, 0, 2, 13);
		values = new int[][][] { { { 0, 0, 0, 0, N }, { 0, 0, 0, N, N },

		{ G, G, G, N, 0 }, { G, N, N, G, 0 }, { G, N, N, G, 0 },
				{ G, G, G, G, 0 } } };
		s.setData(values);
		s.addAllowedVariable(100, 200, 300, 400);
		s.setRuneName("Rune of Detonation");
		s.setNotes("Sacrifice:\n\n"
				+ "-2xGunpowder\n\n"
				+ "Notes:\n\n"
				+ "-Center determines strength.\n"
				+ "-Fuse determines triggering.\n"
				+ "-If fuse is made out of plant runic dust, it will wait for a mob to trigger.\n"
				+ "-Otherwise, the time until deonation will depend on the dust strength.");
		s.setDesc("Description:\n\n"
				+ "Creates a variable-sized explosion when triggered by an entity or by time. "
				+ "Center determines the strength, fuse deteremines the triggering. "
				+ "If fuse is made out of plant runic dust, it will wait for a mob to trigger. "
				+ "Otherwise, the time until deonation will depend on the dust strength.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, -1, 0, -1 });
		DustManager.registerLocalDustShape(s, new DEBomb());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="cage">
		s = new DustShape(6, 6, "cage", false, 3, 3, 1, 1, 18);
		values = new int[][][] { { { 0, G, G, G, G, 0 },

		{ G, G, 0, 0, G, G }, { G, L, L, L, L, G }, { G, L, L, L, L, G },
				{ G, G, 0, 0, G, G },

				{ 0, G, G, G, G, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of Entrapment");
		s.setNotes("Sacrifice:\n\n" + "-6xIron + 2xRedstone");
		s.setDesc("Description:\n\n"
				+ "Entraps a single mob or player that walks nearby. Will not entrap the summoner (if not activated by redstone).");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DECage());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="timelock">
		s = new DustShape(6, 6, "timelock6", false, 3, 2, 1, 2, 19);
		values = new int[][][] { { { 0, G, G, G, G, 0 }, { 0, G, 0, 0, G, 0 },

		{ G, G, G, G, G, G }, { G, G, L, L, G, G }, { G, G, L, L, G, G },
				{ G, G, G, G, G, G } } };
		s.setData(values);
		s.setRuneName("Rune of Locked Time");
		s.setNotes("Sacrifice:\n\n" + "-4xObsidian + 4xSlime + 1xLapisLaz\n\n"
				+ "Notes:\n\n" + "-Expect bugs.\n"
				+ "-Lasts for a day's time unless fueled.");
		s.setDesc("Description:\n\n"
				+ "Locks day/night time, sand falling, and water flowing for as long as it is fueled. BEWARE: Very high chance of bugs especially with other mods.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, -1, 0, 0 });
		DustManager.registerLocalDustShape(s, new DETimeLock());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="void">
		s = new DustShape(8, 6, "void", true, 2, 3, 2, 1, 29);
		values = new int[][][] { { { 0, L, 0, L, L, 0, L, 0 },

		{ L, L, L, 0, 0, L, L, L }, { 0, L, 0, L, L, 0, L, 0 },
				{ 0, L, 0, L, L, 0, L, 0 }, { L, L, L, 0, 0, L, L, L },

				{ 0, L, 0, L, L, 0, L, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of the Void");
		s.setNotes("Sacrifice:\n\n"
				+ "-3XP + Items to store\n\n"
				+ "Notes:\n\n"
				+ "-If there is a sacrifice, it will be stored.\n"
				+ "-If there is not, all items in storage will be dropped.\n"
				+ "-3XP will be taken either way.\n"
				+ "-Inventories are separated by user. So you will not get yours mixed with someone elses.");
		s.setDesc("Description:\n\n"
				+ "Stores sacrificed items in a void. When activated without a sacrifice, the items are returned.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { -1, 0, 0, 0, -1, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEVoid());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="wall">
		s = new DustShape(14, 6, "wall", false, 3, 3, 5, 1, 16);
		values = new int[][][] { {
				{ 0, 0, 0, 0, 0, 0, G, G, G, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, G, P, G, G, G, 0, 0, 0 },
				{ 0, G, G, G, G, G, G, G, P, P, G, G, G, G },
				{ P, P, P, P, G, G, P, P, P, P, P, P, P, 0 },
				{ 0, 0, 0, P, P, P, G, P, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, P, P, P, 0, 0, 0, 0, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of the Barrier");
		s.setNotes("Sacrifice:\n\n" + "-1xIronOre + 3XP");
		s.setDesc("Description:\n\n" + "Lifts a wall out of the earth.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { -1, 1, 0, 0, -1, 1, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEWall());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="xpstore">
		s = new DustShape(6, 6, "xpstore", false, 3, 3, 1, 1, 37);
		values = new int[][][] { { { 0, 0, 0, P, P, 0 },

		{ 0, G, G, 0, P, P }, { 0, G, L, L, 0, P }, { P, 0, L, L, G, 0 },
				{ P, P, 0, G, G, 0 },

				{ 0, P, P, 0, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of Wisdom");
		s.setNotes("Sacrifice:\n\n"
				+ "-16xIronIngot + 6XP\n\n"
				+ "Notes:\n\n"
				+ "-Right-clicking will give you back your XP and pause the rune for a short time.\n"
				+ "-While the rune is paused, it will glow yellow and not absorb any XP.\n"
				+ "-Not accessible by other players.");
		s.setDesc("Description:\n\n"
				+ "Stores all XP. When you walk over it it will take all your levels. It will also store any XP orbs dropped onto it. Not useable by other players.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEXPStore());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="speed">
		s = new DustShape(5, 4, "speed", false, 0, 0, 0, 0, 6);
		values = new int[][][] { { { P, P, P, P, N }, { 0, L, L, L, N },
				{ 0, 0, P, P, N }, { 0, 0, 0, L, N } } };
		s.setData(values);
		s.addAllowedVariable(100, 200, 300, 400);
		s.setRuneName("Rune of Speed");
		s.setNotes("Sacrifice:\n\n" + "-3xSugar + 1xBlazePowder");
		s.setDesc("Description:\n\n" + "Gives you a variable speed boost.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, -1, 0, 0, -1 });
		DustManager.registerLocalDustShape(s, new DESpeed());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="compression">
		s = new DustShape(6, 6, "compression", false, 3, 3, 1, 1, 24);
		values = new int[][][] { { { 0, G, B, G, B, 0 },

		{ G, G, G, B, B, B }, { B, B, G, B, G, G }, { G, G, B, G, B, B },
				{ B, B, B, G, G, G },

				{ 0, B, G, B, G, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of Compression");
		s.setNotes("Sacrifice:\n\n" + "-NxCoal + 1xIronBlock\n\n"
				+ "Notes:\n\n" + "-Every 32Coal will yield a diamond.");
		s.setDesc("Description:\n\n"
				+ "Turns coal into diamond at a ratio of 32Coal=1Diamond.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DECompression());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="fog">
		s = new DustShape(8, 6, "fog", false, 2, 3, 2, 1, 41);
		values = new int[][][] { { { 0, 0, G, G, G, G, G, 0 },
				{ 0, L, G, L, L, L, G, G }, { L, 0, G, G, L, L, G, G },
				{ L, L, G, G, L, L, 0, G }, { L, L, G, G, G, L, G, 0 },
				{ 0, L, L, L, L, L, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of the Blinding Fog");
		s.setNotes("Sacrifice:\n\n" + "-1xWaterBucket + 1xRedshroom + 6XP\n\n"
				+ "Notes:\n\n" + "-Will last a day unless fueled.");
		s.setDesc("Description:\n\n"
				+ "Creates a fog that blinds and confuses anyone inside.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { -1, 0, 0, 0, -1, 0, 0, 0 });
		DustManager.registerLocalDustShape(s,
				new DEFog().setPermaAllowed(false));
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="firerain">
		s = new DustShape(6, 7, "firerain6", false, 3, 3, 1, 1, 25);
		values = new int[][][] { { { B, G, G, G, G, B }, { B, B, G, G, B, B },
				{ 0, G, B, B, G, 0 }, { B, G, B, B, G, B },
				{ B, B, G, G, B, B }, { 0, B, G, G, B, 0 },
				{ 0, G, G, G, G, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of the Hellstorm");
		s.setNotes("Sacrifice:\n\n" + "-2xBlazeRod\n\n" + "Notes:\n\n"
				+ "-Will last half a day unless fueled.\n"
				+ "-Will cause major lag.");
		s.setDesc("Description:\n\n"
				+ "Summons a storm of ignited arrows for a duration of time. WARNING:Large chance of lag if left running. Break rune to stop.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, -1, 0, 0, 0, 0, -1, 0 });
		DustManager.registerLocalDustShape(s, new DEFireRain());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="egg">
		s = new DustShape(6, 7, "egg", false, 3, 2, 1, 2, 45);
		values = new int[][][] { { { 0, 0, P, P, 0, 0 }, { 0, P, P, P, P, 0 },

		{ P, P, P, P, P, P }, { P, P, G, G, P, P }, { P, P, G, G, P, P },
				{ G, G, G, G, G, G },

				{ 0, P, P, P, P, 0 } } };
		s.setData(values);
		s.setRuneName("Rune Rebirth");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, -1, 0, 0 });
		s.setNotes("Sacrifice:\n\n"
				+ "-1xLiveEntity + 1xEgg + 1xDiamond + 10XP\n\n");
		s.setDesc("Description:\n\n"
				+ "Drops an egg containing the mob sacrificed.");
		DustManager.registerLocalDustShape(s, new DEEggifier());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="bait">
		s = new DustShape(6, 6, "bait6", false, 3, 3, 1, 1, 5);
		values = new int[][][] { { { P, P, P, 0, 0, 0 },

		{ P, P, P, P, P, 0 }, { P, P, L, L, P, 0 }, { 0, P, L, L, P, P },
				{ 0, P, P, P, P, P },

				{ 0, 0, 0, P, P, P } } };
		s.setData(values);
		s.setRuneName("Rune of Baiting");
		s.setNotes("Sacrifice:\n\n" + "-1xMobEgg + 1xGoldBlock + 5XP\n\n"
				+ "Notes:\n\n" + "-Will last seven days unless fueled.\n\n"
				+ "Will not attract flying mobs, swimming mobs, or slimes.");
		s.setDesc("Description:\n\n"
				+ "Attracts any mobs with the specified drop type.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEBait());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="resurrect">
		s = new DustShape(6, 6, "resurrect", true, 3, 3, 1, 1, 17);
		values = new int[][][] { { { L, 0, 0, 0, L, L },

		{ L, 0, L, 0, 0, L }, { L, L, L, L, 0, L }, { L, 0, L, L, L, L },
				{ L, 0, 0, L, 0, L },

				{ L, L, 0, 0, 0, L } } };
		s.setData(values);
		s.setRuneName("Rune of Resurrection");
		s.setNotes("Sacrifice:\n\n" + "-4xSoulSand +2xMobDrop");
		s.setDesc("Description:\n\n"
				+ "Spawns a mob of the specified drop type.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEResurrection());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="powerRelay">
		s = new DustShape(10, 10, "powerRelay", false, 1, 1, 3, 3, 42);
		values = new int[][][] { { { 0, L, L, L, L, 0, 0, 0, 0, 0 },
				{ 0, L, G, G, L, 0, G, G, 0, 0 },
				{ 0, L, G, G, L, L, G, G, 0, 0 },

				{ 0, L, L, 0, L, L, L, 0, 0, 0 },
				{ G, G, 0, 0, B, B, L, L, G, G },
				{ G, G, L, L, B, B, 0, 0, G, G },
				{ 0, 0, 0, L, L, L, 0, L, L, 0 },

				{ 0, 0, G, G, L, L, G, G, L, 0 },
				{ 0, 0, G, G, 0, L, G, G, L, 0 },
				{ 0, 0, 0, 0, 0, L, L, L, L, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of Power Distribution");
		s.setNotes("Sacrifice:\n\n"
				+ "-3xIronIngot + 5XP\n\n"
				+ "Notes:\n\n"
				+ "-Does not exhaust any fuel itself. All fuel is redirected to runes within a "
				+ DEPowerRelay.distance
				+ " block radius.\n"
				+ "-Fueled runes will have a bigger spark than normal.\n"
				+ "-Sprites cannot be powered by powering the location at which their runes are summoned, but by the sprites themselves.");
		s.setDesc("Description:\n\n"
				+ "Acts like a battery storing an infinite amount of fuel and distributing it to nearby runes who need it as they need it. Takes no fuel to sustain itself. Runes being powered will display a spark twice as big as normal.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEPowerRelay());
		// </editor-fold>
		s = new DustShape(10, 10, "charchInsc", false, 1, 1, 3, 3, 46);
		values = new int[][][] { { { 0, 0, 0, 0, 0, 0, G, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, P, G, P, 0, 0 },
				{ 0, P, G, P, P, P, G, P, 0, 0 },

				{ P, G, G, G, G, P, P, G, G, 0 },
				{ G, G, P, P, G, 0, P, P, G, 0 },
				{ 0, G, P, P, 0, G, P, P, G, G },
				{ 0, G, G, P, P, G, G, G, G, P },

				{ 0, 0, P, G, P, P, P, G, P, 0 },
				{ 0, 0, P, G, P, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, G, 0, 0, 0, 0, 0, 0 }, } };
		s.setData(values);
		s.setRuneName("Inscription Enchanting Rune");
		s.setNotes("Sacrifice:\n\n" + "Defined by the inscription.");
		s.setDesc("Description:\n\n"
				+ "Placing a dried inscription on this rune along with its required sacrifices will imbue it with energy.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEChargeInscription());

		// <editor-fold defaultstate="collapsed" desc="scollect">
		s = new DustShape(9, 6, "scollect", false, 2, 3, 2, 3, 35);
		values = new int[][][] { { { L, G, G, G, G, G, G, L },
				{ L, G, 0, 0, 0, 0, G, L }, { G, G, 0, 0, 0, 0, G, G },
				{ L, L, 0, 0, 0, 0, L, L }, { G, L, 0, 0, 0, 0, L, G },
				{ G, L, L, L, L, L, L, G }, { 0, 0, L, L, L, L, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of Spawner Collection");
		s.setNotes("Sacrifice:\n\n" + "-6xGoldIngot + 10XP");
		s.setDesc("Description:\n\n"
				+ "Collects a spawner that it is placed around.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 1, -1, 0, 0, -1, 1, 0 });
		DustManager.registerLocalDustShape(s, new DESpawnerCollector());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="reprog">
		s = new DustShape(8, 10, "reprog", true, 2, 2, 2, 2, 33);
		values = new int[][][] { { { 0, 0, L, 0, 0, L, 0, 0 },
				{ 0, L, L, 0, 0, L, L, 0 },

				{ 0, L, 0, 0, 0, 0, L, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ L, L, 0, 0, 0, 0, L, L }, { L, L, 0, 0, 0, 0, L, L },

				{ 0, L, L, L, L, L, L, 0 }, { L, L, L, L, L, L, L, L },
				{ L, L, 0, L, L, 0, L, L }, { 0, L, 0, L, L, 0, L, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of Spawner Reassignment");
		s.setNotes("Sacrifice:\n\n" + "-1xMobEgg + 2xEnderPearl + 10XP");
		s.setDesc("Description:\n\n"
				+ "Reassigns a placed spawner to spawn mobs of the specified type. Note:The entity inside won't update visually until you re-log.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, -1, 0, 0, -1, 0, 0 });
		DustManager.registerLocalDustShape(s, new DESpawnerReprog());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="tele">
		s = new DustShape(12, 10, "tele7", false, 1, 0, 3, 8, 30);
		values = new int[][][] { { { 0, 0, 0, 0, 0, B, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, B, B, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, B, G, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, G, G, 0, 0, 0, 0 },

				{ 0, 0, 0, 0, G, B, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, B, B, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, B, G, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, G, G, 0, 0, 0, 0 },

				{ G, G, G, G, G, G, G, G, G, G },
				{ 0, B, B, G, B, B, G, B, B, 0 },
				{ 0, 0, G, G, B, B, G, G, 0, 0 },
				{ 0, 0, 0, G, 0, 0, G, 0, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of Teleportation");
		s.setNotes("Sacrifice:\n\n" + "-1xEnderEye + 5XP\n\n" + "Notes:\n\n"
				+ "-Takes away 3 hearts upon every teleportation.");
		s.setDesc("Description:\n\n"
				+ "Creates a teleporation network location for other teleportation runes to teleport to. Will cost 4 hearts every teleportation. The teleportation network frequency on which to send you depends on the block beneath the blaze square in the rune design.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 1, 0, 0, 0, 1, -2, 2, 0 });
		DustManager.registerLocalDustShape(s, new DETeleportation());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="minitele">
		s = new DustShape(10, 4, "minitele", false, 1, 0, 3, 0, 34);
		values = new int[][][] { { { B, B, B, B, G, G, B, B, B, B },
				{ 0, G, G, G, B, B, G, G, G, 0 },
				{ 0, B, B, G, B, B, G, B, B, 0 },
				{ 0, 0, G, G, 0, 0, G, G, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of Transport");
		s.setNotes("Sacrifice:\n\n" + "-5XP");
		s.setDesc("Description:\n\n"
				+ "Teleports you to a teleporation network rune location. "
				+ "The teleportation network frequency on which to send you "
				+ "depends on the block beneath the blaze square in the rune design.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { -1, 0, 0, 0, -1, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEMiniTele());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="Fire Sprite">
		s = new DustShape(12, 12, "Fire Sprite6", false, 0, 0, 4, 4, 20);
		values = new int[][][] { { { 0, 0, 0, G, G, 0, 0, G, G, 0, 0, 0 },
				{ 0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0 },
				{ 0, 0, 0, G, B, G, G, B, G, 0, 0, 0 },
				{ G, 0, G, B, B, G, G, B, B, G, 0, G },

				{ G, G, B, B, 0, 0, 0, 0, B, B, G, G },
				{ 0, 0, G, G, 0, B, B, 0, G, G, 0, 0 },
				{ 0, 0, G, G, 0, B, B, 0, G, G, 0, 0 },
				{ G, G, B, B, 0, 0, 0, 0, B, B, G, G },

				{ G, 0, G, B, B, G, G, B, B, G, 0, G },
				{ 0, 0, 0, G, B, G, G, B, G, 0, 0, 0 },
				{ 0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0 },
				{ 0, 0, 0, G, G, 0, 0, G, G, 0, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of the Fire Sprite");
		s.setNotes("Sacrifice:\n\n" + "-1xGhastTear + 2xFireCharge + 22XP\n\n"
				+ "-Notes:\n\n" + "-Will last for three days unless fueled.");
		s.setDesc("Description:\n\n"
				+ "Creates a sprite that will follow you and set the world and mobs on fire. ");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEFireSprite());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="Earth Sprite">
		s = new DustShape(10, 8, "earthsprite6", false, 1, 2, 3, 2, 39);
		values = new int[][][] { { { 0, 0, 0, 0, G, G, 0, 0, 0, 0 },
				{ 0, 0, G, P, B, G, P, G, 0, 0 },

				{ 0, B, B, P, B, B, P, B, B, 0 },
				{ G, G, G, P, G, B, P, G, G, G },
				{ G, G, G, P, B, G, P, G, G, G },
				{ 0, B, B, P, B, B, P, B, B, 0 },

				{ 0, 0, G, P, G, B, P, G, 0, 0 },
				{ 0, 0, 0, 0, G, G, 0, 0, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of the Earth Sprite");
		s.setNotes("Sacrifice:\n\n" + "-1xGhastTear + 16xGlass + 20XP\n\n"
				+ "Notes:\n\n"
				+ "-Stop and crouch to call the sprite to protect you.\n"
				+ "-Will last for three days unless fueled.\n");
		s.setDesc("Description:\n\n"
				+ "Summons a sprite that will encircle you with earth. Pressing [crouch] while standing still will call the sprite to protect you.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { -1, 0, 0, 0, -1, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEEarthSprite());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="loyaltysprite">
		s = new DustShape(6, 6, "loyaltysprite6", false, 3, 3, 1, 1, 21);
		values = new int[][][] { { { L, L, G, L, G, G },

		{ G, G, G, L, L, L }, { L, G, L, G, L, G }, { L, G, L, G, L, G },
				{ G, G, G, L, L, L },

				{ L, L, G, L, G, G } } };
		s.setData(values);
		s.setRuneName("Rune of the Loyalty Sprite");
		s.setNotes("Sacrifice:\n\n" + "-2xGhastTear + 10XP\n\n" + "Notes:\n\n"
				+ "-Will last for three days unless fueled.\n"
				+ "-CURRENTLY BROKEN.");
		s.setDesc("Description:\n\n"
				+ "Takes over the mind of a mob to have them fight for you. CURRENTLY BROKEN: Any attempt to summon this rune will automatically fail.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s,
				new DELoyaltySprite().setPermaAllowed(false));
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="hunter">
		s = new DustShape(12, 8, "hunter6", false, 0, 2, 4, 2, 43);
		values = new int[][][] { { { 0, 0, G, G, G, 0, 0, G, G, G, 0, 0 },
				{ G, G, G, B, G, G, G, G, B, G, G, G },

				{ 0, 0, B, G, G, G, G, G, G, B, 0, 0 },
				{ 0, 0, G, G, B, B, B, B, G, G, 0, 0 },
				{ 0, 0, G, G, B, B, B, B, G, G, 0, 0 },
				{ 0, 0, B, G, G, G, G, G, G, B, 0, 0 },

				{ G, G, G, B, G, G, G, G, B, G, G, G },
				{ 0, 0, G, G, G, 0, 0, G, G, G, 0, 0 }, } };
		s.setData(values);
		s.setRuneName("Rune of the Hunter");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { -1, -1, -1, -1, -1, -1, -1, -1 });
		s.setNotes("Sacrifice:\n\n"
				+ "-3xBlazePowder + 1xEnderEye + 12XP\n\n"
				+ "Notes:\n\n"
				+ "-Will last a day unless fueled.\n"
				+ "-Will cause lag: Right clicking will pause the rune (and its fuel consumption.)\n\n"
				+ "Current bug: Does not detect entities who do not utilize the new AI system.");
		s.setDesc("Description:\n\n"
				+ "Allows you to see the location and health of any mob nearby. WARNING: Possiblitity for lag on lower-quality machines. Right-click the rune to disable. ");

		DustManager.registerLocalDustShape(s,
				new DEHunterVision().setPermaAllowed(false));
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="bounce">
		s = new DustShape(4, 4, "bounce", false, 0, 0, 0, 0, 22);
		values = new int[][][] { { { G, G, P, P }, { G, P, G, P },
				{ P, G, P, G }, { P, P, G, G } } };
		s.setData(values);
		s.setRuneName("Rune of Bouncing");
		s.setNotes("Sacrifice:\n\n" + "-4xSlimeBall");
		s.setDesc("Description:\n\n"
				+ "Creates a rune that will help you jump much higher.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEBounce());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="record">
		s = new DustShape(4, 4, "record", false, 0, 0, 0, 0, 23);
		values = new int[][][] { { { G, G, G, G }, { G, L, G, G },
				{ G, G, L, G }, { G, G, G, G } } };
		s.setData(values);
		s.setRuneName("Rune of Music");
		s.setNotes("Sacrifice:\n\n" + "-1xDiamond");
		s.setDesc("Description:\n\n" + "Spawns a random record.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DESpawnRecord());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="spirittools">
		s = new DustShape(4, 4, "spirittools", true, 0, 0, 0, 0, 14);
		values = new int[][][] { { { L, L, L, L }, { L, L, L, L },
				{ L, L, L, L }, { L, L, L, L } } };
		s.setRuneName("Rune of the Spirit Tools");
		s.setNotes("Sacrifice:\n\n"
				+ "-Spirit Pickaxe: 1xGoldPickaxe + 4xTNT + 18XP\n"
				+ "-Spirit Sword: 1xGoldSword + 1xGlowstoneBlock + 18XP");
		s.setDesc("Description:\n\n"
				+ "Spawns either a spirit sword or spirit pickaxe.\n-\n"
				+ "The spirit pick on right-click breaks a bigger area and has a chance to drop Lapis dust depending on your XP level. \n-\n"
				+ "The spirit sword comes with high-enchantments and may drop Gunpowder dust in the same way.");
		s.setData(values);
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DESpiritTool());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="ench.firebow">
		s = new DustShape(6, 6, "ench.firebow", false, 3, 3, 1, 1, 28);
		values = new int[][][] { { { 0, B, G, G, 0, 0 }, { B, B, G, B, B, 0 },
				{ G, G, G, G, B, G }, { G, B, G, G, G, G },
				{ 0, B, B, G, B, B }, { 0, 0, G, G, B, 0 } } };
		s.setData(values);
		s.setRuneName("Enchanting Rune of the Fire Bow");
		s.setNotes("Sacrifice:\n\n"
				+ "-9xFireCharge + 1xBow + 1xGoldBlock + 5XP");
		s.setDesc("Description:\n\n"
				+ "Enchants and repairs your bow with Fire Aspect I");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEFireBowEnch());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="ench.silktouch">
		s = new DustShape(8, 8, "ench.silktouch", false, 2, 2, 2, 2, 26);
		values = new int[][][] { { { 0, 0, 0, G, 0, 0, 0, 0 },
				{ 0, G, G, G, P, P, P, 0 },

				{ 0, G, G, B, G, P, P, 0 }, { G, G, B, G, B, G, P, 0 },
				{ 0, P, G, B, G, B, G, G }, { 0, P, P, G, B, G, G, 0 },

				{ 0, P, P, P, G, G, G, 0 }, { 0, 0, 0, 0, G, 0, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Enchanting Rune of Silk Touch");
		s.setNotes("Sacrifice:\n\n"
				+ "-1xDiamondPickaxe + 1xGoldBlock + 10XP\n" + "OR\n"
				+ "-1xDiamondShovel + 1xGoldBlock + 10XP");
		s.setDesc("Description:\n\n"
				+ "Enchants and repairs your pick or shovel with Silk Touch I.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DESilkTouchEnch());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="ench.fortune">
		s = new DustShape(10, 10, "ench.fortune", false, 1, 1, 3, 3, 27);
		values = new int[][][] { { { 0, 0, G, G, G, G, G, G, 0, 0 },
				{ 0, 0, G, L, G, G, L, G, 0, 0 },
				{ G, G, L, L, P, P, L, L, G, G },
				{ G, L, L, P, 0, 0, P, L, L, G },
				{ G, G, P, 0, 0, 0, 0, P, G, G },
				{ G, G, P, 0, 0, 0, 0, P, G, G },
				{ G, L, L, P, 0, 0, P, L, L, G },
				{ G, G, L, L, P, P, L, L, G, G },
				{ 0, 0, G, L, G, G, L, G, 0, 0 },
				{ 0, 0, G, G, G, G, G, G, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Enchanting Rune of Fortune");
		s.setNotes("Sacrifice:\n\n"
				+ "-1xDiamondOre + 1xLapisOre + 1xRedstoneOre + 1xDiamondPickaxe + 15XP\n"
				+ "OR\n"
				+ "-1xDiamondOre + 1xLapisOre + 1xRedstoneOre + 1xDiamondSword + 15XP\n");
		s.setDesc("Description:\n\n"
				+ "Enchants and repairs your pick or sword with Fortune IV or Looting IV respectively.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEFortuneEnch());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="forcefield">
		s = new DustShape(12, 12, "forcefield6", false, 0, 0, 4, 4, 36);
		values = new int[][][] { { { 0, 0, 0, 0, L, 0, 0, L, 0, 0, 0, 0 },
				{ 0, 0, L, L, 0, L, L, 0, L, L, 0, 0 },
				{ 0, L, L, G, L, B, B, L, G, L, L, 0 },
				{ 0, L, G, G, B, G, G, B, G, G, L, 0 },

				{ L, 0, L, B, B, 0, 0, B, B, L, 0, L },
				{ 0, L, B, G, 0, N, N, 0, G, B, L, 0 },
				{ 0, L, B, G, 0, N, N, 0, G, B, L, 0 },
				{ L, 0, L, B, B, 0, 0, B, B, L, 0, L },

				{ 0, L, G, G, B, G, G, B, G, G, L, 0 },
				{ 0, L, L, G, L, B, B, L, G, L, L, 0 },
				{ 0, 0, L, L, 0, L, L, 0, L, L, 0, 0 },
				{ 0, 0, 0, 0, L, 0, 0, L, 0, 0, 0, 0 } } };
		s.setData(values);
		s.addAllowedVariable(100, 200, 300, 400);
		s.setRuneName("Rune of Protection");
		s.setNotes("Sacrifice:\n\n" + "-1xVillagerEgg + 15XP\n\n"
				+ "Notes:\n\n" + "-Lasts for a day unless fueled.");
		s.setDesc("Description:\n\n"
				+ "Creates a forcefield that will push away all hostile mobs.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEForcefield());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="flatten">
		s = new DustShape(20, 20, "flatten", false, 0, 0, 8, 8, 32);
		values = new int[][][] { {
				{ 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, G, G, G, G, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, G, G, G, G, 0, 0, 0, 0, 0, 0, 0, 0 },

				{ 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, P, P, G, G, G, G, P, P, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, P, P, L, G, 0, 0, G, L, P, P, 0, 0, 0, 0, 0 },
				{ 0, G, G, G, 0, P, L, G, G, L, L, G, G, L, P, 0, G, G, G, 0 },

				{ G, G, G, G, G, G, G, G, 0, 0, 0, 0, G, G, G, G, G, G, G, G },
				{ 0, G, 0, G, 0, G, 0, L, 0, N, N, 0, L, 0, G, 0, G, 0, G, 0 },
				{ 0, G, 0, G, 0, G, 0, L, 0, N, N, 0, L, 0, G, 0, G, 0, G, 0 },
				{ G, G, G, G, G, G, G, G, 0, 0, 0, 0, G, G, G, G, G, G, G, G },

				{ 0, G, G, G, 0, P, L, G, G, L, L, G, G, L, P, 0, G, G, G, 0 },
				{ 0, 0, 0, 0, 0, P, P, L, G, 0, 0, G, L, P, P, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, P, P, G, G, G, G, P, P, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0, 0, 0, 0, 0 },

				{ 0, 0, 0, 0, 0, 0, 0, 0, G, G, G, G, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, G, G, G, G, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0, 0, 0, 0, 0 }, } };
		s.setData(values);
		s.addAllowedVariable(100, 200, 300, 400);
		s.setRuneName("Rune of Level Earth");
		s.setNotes("Sacrifice:\n\n" + "-Plant Dust: 5XP + 5xIronOre\n"
				+ "-Gunpowder Dust: 6XP + 5xIronOre\n"
				+ "-Lapis Dust: 7XP + 5xIronOre\n"
				+ "-Blaze Dust: 8XP + 5xIronOre");
		s.setDesc("Description:\n\n" + "Flattens the terrain around it.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEFlatten());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="liftterrain">
		s = new DustShape(16, 8, "liftterrain", false, 2, 2, 6, 2, 38);
		values = new int[][][] { {
				{ 0, 0, 0, 0, 0, L, G, G, G, G, L, 0, 0, 0, 0, 0 },
				{ L, L, L, L, L, L, G, P, P, G, L, L, L, L, L, L },

				{ 0, 0, G, G, G, G, P, P, P, P, G, G, G, G, 0, 0 },
				{ 0, 0, 0, 0, G, P, P, N, N, P, P, G, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, G, P, P, N, N, P, P, G, 0, 0, 0, 0 },
				{ 0, 0, G, G, G, G, P, P, P, P, G, G, G, G, 0, 0 },

				{ L, L, L, L, L, L, G, P, P, G, L, L, L, L, L, L },
				{ 0, 0, 0, 0, 0, L, G, G, G, G, L, 0, 0, 0, 0, 0 } } };
		s.setData(values);
		s.addAllowedVariable(100, 200, 300, 400);
		s.setRuneName("Rune of the Mountain");
		s.setNotes("Sacrifice:\n\n"
				+ "-1xLiveIronGolem + 1xRose + 10XP\n\n"
				+ "Notes:\n\n"
				+ "-The area you want to lift should be outline with etchings filled with clay blocks.");
		s.setDesc("Description:\n\n"
				+ "Lifts the earth specified by the clay-filled etchings up high into the sky."
				+ "The ruts must be connected to the rune.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { -1, 1, 0, 0, -1, 1, 0, 0 });

		DustManager.registerLocalDustShape(s, new DELiftTerrain());
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="sarlacc">
		s = new DustShape(22, 32, "xp6", false, 2, 3, 14, 9, 31);
		values = new int[][][] { {
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, G, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, 0, G, G, G, G, 0, 0,
						0, G, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, G, G, G, G, 0, 0, 0, 0, G, G, 0, 0, 0,
						0, G, G, G, G, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, L, L, 0, 0, 0, 0, 0, 0, L, 0, L, 0, 0, L, 0, L,
						0, 0, 0, 0, 0, 0, L, L, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L,
						L, L, L, L, L, L, L, L, 0, 0, 0, 0 },

				{ 0, 0, G, G, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0,
						0, 0, 0, 0, 0, 0, L, 0, G, G, 0, 0 },
				{ 0, 0, G, 0, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0,
						0, 0, 0, 0, 0, 0, L, 0, 0, G, 0, 0 },
				{ 0, 0, 0, 0, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0,
						0, 0, 0, 0, 0, 0, L, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, L, L, L, 0, 0, 0, 0, 0, 0, 0, 0, L, L, L, L, 0, 0,
						0, 0, 0, 0, 0, 0, L, L, L, 0, 0, 0 },

				{ 0, L, L, L, 0, L, 0, 0, 0, 0, G, G, G, G, L, G, G, L, G, G,
						G, G, 0, 0, 0, 0, L, 0, L, L, L, 0 },
				{ L, L, L, 0, L, L, 0, 0, 0, 0, 0, L, L, G, G, L, L, G, G, L,
						L, 0, 0, 0, 0, 0, L, L, 0, L, L, L },
				{ L, L, L, 0, L, L, 0, 0, 0, 0, 0, L, L, G, G, L, L, G, G, L,
						L, 0, 0, 0, 0, 0, L, L, 0, L, L, L },
				{ 0, L, L, L, 0, L, 0, 0, 0, 0, G, G, G, G, L, G, G, L, G, G,
						G, G, 0, 0, 0, 0, L, 0, L, L, L, 0 },

				{ 0, 0, 0, L, L, L, 0, 0, 0, 0, 0, 0, 0, 0, L, L, L, L, 0, 0,
						0, 0, 0, 0, 0, 0, L, L, L, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0,
						0, 0, 0, 0, 0, 0, L, 0, 0, 0, 0, 0 },
				{ 0, 0, G, 0, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0,
						0, 0, 0, 0, 0, 0, L, 0, 0, G, 0, 0 },
				{ 0, 0, G, G, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0,
						0, 0, 0, 0, 0, 0, L, 0, G, G, 0, 0 },

				{ 0, 0, 0, 0, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L,
						L, L, L, L, L, L, L, L, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, L, L, 0, 0, 0, 0, 0, 0, L, 0, L, 0, 0, L, 0, L,
						0, 0, 0, 0, 0, 0, L, L, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, G, G, G, G, 0, 0, 0, 0, G, G, 0, 0, 0,
						0, G, G, G, G, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, 0, G, G, G, G, 0, 0,
						0, G, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },

				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, G, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, } };
		s.setData(values);
		s.setRuneName("Rune of Sarlacc");
		s.setNotes("Sacrifice:\n\n"
				+ "-1xNetherStar\n\n"
				+ "Notes:\n\n"
				+ "-Lasts for a day unless fueled. \n"
				+ "-Every mob sacrificed will prolonge it\' life for an eighth of a day.\n"
				+ "-Every item (smeltable or otherwise) will prolonge it\'s life for (1/2 a second)*stackSize.");
		s.setDesc("Description:\n\n"
				+ "Kills any mobs dropped onto it and destroys their drops. However, will drop 2 times as much XP into the holes around it. Will not damage anyone underneath.");
		s.setAuthor("billythegoat101");
		s.setManualRotationDerp(new int[] { -2, 1, 0, 0, -2, 1, 0, 0 });
		DustManager.registerLocalDustShape(s, new DEXP());
		// </editor-fold>
		// System.out.println("Loaded " + DustManager.shapes.size() +
		// " runes.");
		// last id used: 46
		// notes for reanimation:
		// all numbers are cut off at the end of the name to preserve lexicon
		// page picture names

	}

	public void registerInscriptions() {
	}

}
