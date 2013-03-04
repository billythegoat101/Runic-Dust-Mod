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
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/torch.xml", new DESpawnTorch());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/rabbit.xml", new DEHideout());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/healing.xml", new DEHeal());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/lumber.xml", new DELumberjack());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/campfire.xml", new DECampFire());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/depths.xml", new DEPit());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/heights.xml", new DEObelisk());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/farm.xml", new DEFarm());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/leapfrog.xml", new DELillyBridge());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/dawn.xml", new DEDawn());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/dusk.xml", new DELunar());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/trap.fire.xml", new DEFireTrap());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/trap.lightning.xml", new DELightning());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/trap.poison.xml", new DEPoisonTrap());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/trap.detonation.xml", new DEBomb());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/trap.entrap.xml", new DECage());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/timelock.xml", new DETimeLock());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/void.xml", new DEVoid());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/wall.xml", new DEWall());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/wisdom.xml", new DEXPStore());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/speed.xml", new DESpeed());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/compression.xml", new DECompression());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/firerain.xml", new DEFireRain());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/eggifier.xml", new DEEggifier());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/resurrection.xml", new DEResurrection());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/power_relay.xml", new DEPowerRelay());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/charge_inscriptions.xml", new DEChargeInscription());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/spawner_collection.xml", new DESpawnerCollector());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/spawner_reassignment.xml", new DESpawnerReprog());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/teleport.xml", new DETeleportation());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/minitele.xml", new DEMiniTele());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/sprite.fire.xml", new DEFireSprite());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/sprite.earth.xml", new DEEarthSprite());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/bounce.xml", new DEBounce());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/spawn_record.xml", new DESpawnRecord());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/spirit_tools.xml", new DESpiritTool());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/ench.firebow.xml", new DEFireBowEnch());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/ench.silktouch.xml", new DESilkTouchEnch());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/ench.Fortune.xml", new DEFortuneEnch());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/protection.xml", new DEForcefield());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/level_earth.xml", new DEFlatten());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/lift_terrain.xml", new DELiftTerrain());
		
		XMLDustShapeReader.readAndRegiterShape("/dustmod/runes/data/sarlacc.xml", new DEXP());

		// last id used: 46
		// notes for reanimation:
		// all numbers are cut off at the end of the name to preserve lexicon
		// page picture names

	}

	public void registerInscriptions() {
	}

}
