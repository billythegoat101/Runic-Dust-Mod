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
		
		XMLDustShapeReader.ReadAndRegiterShape("torch.xml", new DESpawnTorch());
		
		XMLDustShapeReader.ReadAndRegiterShape("rabbit.xml", new DEHideout());
		
		XMLDustShapeReader.ReadAndRegiterShape("healing.xml", new DEHeal());
		
		XMLDustShapeReader.ReadAndRegiterShape("lumber.xml", new DELumberjack());
		
		XMLDustShapeReader.ReadAndRegiterShape("campfire.xml", new DECampFire());
		
		XMLDustShapeReader.ReadAndRegiterShape("depths.xml", new DEPit());
		
		XMLDustShapeReader.ReadAndRegiterShape("heights.xml", new DEObelisk());
		
		XMLDustShapeReader.ReadAndRegiterShape("farm.xml", new DEFarm());
		
		XMLDustShapeReader.ReadAndRegiterShape("leapfrog.xml", new DELillyBridge());
		
		XMLDustShapeReader.ReadAndRegiterShape("dawn.xml", new DEDawn());
		
		XMLDustShapeReader.ReadAndRegiterShape("dusk.xml", new DELunar());
		
		XMLDustShapeReader.ReadAndRegiterShape("trap.fire.xml", new DEFireTrap());
		
		XMLDustShapeReader.ReadAndRegiterShape("trap.lightning.xml", new DELightning());
		
		XMLDustShapeReader.ReadAndRegiterShape("trap.poison.xml", new DEPoisonTrap());
		
		XMLDustShapeReader.ReadAndRegiterShape("trap.detonation.xml", new DEBomb());
		
		XMLDustShapeReader.ReadAndRegiterShape("trap.entrap.xml", new DECage());
		
		XMLDustShapeReader.ReadAndRegiterShape("timelock.xml", new DETimeLock());
		
		XMLDustShapeReader.ReadAndRegiterShape("void.xml", new DEVoid());
		
		XMLDustShapeReader.ReadAndRegiterShape("wall.xml", new DEWall());
		
		XMLDustShapeReader.ReadAndRegiterShape("wisdom.xml", new DEXPStore());
		
		XMLDustShapeReader.ReadAndRegiterShape("speed.xml", new DESpeed());
		
		XMLDustShapeReader.ReadAndRegiterShape("compression.xml", new DECompression());
		
		XMLDustShapeReader.ReadAndRegiterShape("firerain.xml", new DEFireRain());
		
		XMLDustShapeReader.ReadAndRegiterShape("eggifier.xml", new DEEggifier());
		
		XMLDustShapeReader.ReadAndRegiterShape("resurrection.xml", new DEResurrection());
		
		XMLDustShapeReader.ReadAndRegiterShape("power_relay.xml", new DEPowerRelay());
		
		XMLDustShapeReader.ReadAndRegiterShape("charge_inscriptions.xml", new DEChargeInscription());
		
		XMLDustShapeReader.ReadAndRegiterShape("spawner_collection.xml", new DESpawnerCollector());
		
		XMLDustShapeReader.ReadAndRegiterShape("spawner_reassignment.xml", new DESpawnerReprog());
		
		XMLDustShapeReader.ReadAndRegiterShape("teleport.xml", new DETeleportation());
		
		XMLDustShapeReader.ReadAndRegiterShape("minitele.xml", new DEMiniTele());
		
		XMLDustShapeReader.ReadAndRegiterShape("sprite.fire.xml", new DEFireSprite());
		
		XMLDustShapeReader.ReadAndRegiterShape("sprite.earth.xml", new DEEarthSprite());
		
		XMLDustShapeReader.ReadAndRegiterShape("bounce.xml", new DEBounce());
		
		XMLDustShapeReader.ReadAndRegiterShape("spawn_record.xml", new DESpawnRecord());
		
		XMLDustShapeReader.ReadAndRegiterShape("spirit_tools.xml", new DESpiritTool());
		
		XMLDustShapeReader.ReadAndRegiterShape("ench.firebow.xml", new DEFireBowEnch());
		
		XMLDustShapeReader.ReadAndRegiterShape("ench.silktouch.xml", new DESilkTouchEnch());
		
		XMLDustShapeReader.ReadAndRegiterShape("ench.Fortune.xml", new DEFortuneEnch());
		
		XMLDustShapeReader.ReadAndRegiterShape("protection.xml", new DEForcefield());
		
		XMLDustShapeReader.ReadAndRegiterShape("level_earth.xml", new DEFlatten());
		
		XMLDustShapeReader.ReadAndRegiterShape("lift_terrain.xml", new DELiftTerrain());
		
		XMLDustShapeReader.ReadAndRegiterShape("sarlacc.xml", new DEXP());

		// last id used: 46
		// notes for reanimation:
		// all numbers are cut off at the end of the name to preserve lexicon
		// page picture names

	}

	public void registerInscriptions() {
	}

}
