/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustexample;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import dustexample.examplerunes.DEChestNom;
import dustexample.examplerunes.DEIceSprite;
import dustexample.examplerunes.DEMakeBlockFromDustLevel;
import dustexample.examplerunes.DEMakeBlockFromItem;
import dustexample.examplerunes.DEMakeItRain;
import dustexample.examplerunes.DEPain;
import dustmod.DustItemManager;
import dustmod.DustManager;
import dustmod.DustMod;
import dustmod.DustShape;

/**
 * 
 * These runes are meant only to be part of a demonstration of the rune system
 * to be used as a resource for modders looking to make custom runes of their
 * own. These runes are not meant to enhance gameplay or to be used as tools in
 * a real playthough of the Runic Dust mod, just as examples.
 * 
 * If you are a modder(which you probably are) then you will find that I list
 * off what each rune in here will provide a good example of at the top of the
 * DustEvent class in a similar comment block as this one. Good luck! And I hope
 * This helps!
 * 
 * 
 * @author billythegoat101
 */

@Mod(modid = "DustExample", name = "Dust Mod Example 1", version = "1.0", dependencies = "after:DustMod")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class DustExample {
	@Instance("DustExample")
	public static DustExample instance;

	@PostInit
	public void postInit(FMLPostInitializationEvent evt) {
		// registerDusts();
		// registerRunes();
		// registerInscriptions();
	}

	public void registerDusts() {
		DustItemManager.registerDust(350, "Glowing Runic Dust", "glowdust",
				0xEEEE00, 0xFFFF00, 0xFFFF00);

		// Register recipe for our dust (2xGlowstoneDust + 1xCoal)
		GameRegistry.addShapelessRecipe(new ItemStack(DustMod.getItemDust(), 4,
				350), new ItemStack(Item.lightStoneDust, 1), new ItemStack(
				Item.lightStoneDust, 1), new ItemStack(Item.coal, 1, -1));
	}

	/**
	 * Here is where you register all your runes with the DustManager.
	 */
	public void registerRunes() {
		// Note that the order in which these runes appear in the tome is
		// dependent
		// upon the order by which they are registered into the system.

		// Pre-set all these variables to make mass-rune-making easier.
		int N = -1; // Variable
		int P = 100; // Plant
		int G = 200; // Gunpowder
		int L = 300; // Lapis
		int g = 350; // Custom glow dust
		int B = 400; // Blaze
		DustShape s;
		int[][][] values;

		// This first line just helps to keep things organized
		// <editor-fold defaultstate="collapsed" desc="nomchest">
		// width, height, IDName, isOneColor, cx,cy,ox,oy, unique ID number
		s = new DustShape(8, 8, "nomchest", false, 2, 2, 2, 2, 100);
		// The design pattern for the rune
		values = new int[][][] { { { 0, 0, 0, G, G, 0, 0, 0 },
				{ 0, G, G, G, G, P, P, 0 }, { G, G, 0, 0, 0, 0, P, P },
				{ P, G, 0, 0, 0, 0, P, G }, { P, G, 0, 0, 0, 0, P, G },
				{ G, G, 0, 0, 0, 0, P, P }, { 0, G, G, P, P, P, P, 0 },
				{ 0, 0, 0, P, P, 0, 0, 0 } } };
		// Assigns the design to the DustShape
		s.setData(values);

		// Assigns the DustShape a proper name, rather than just the
		// identification one.
		// This one can be altered anytime, while the IDName should always
		// remain the same for
		s.setRuneName("Rune of OmNom");

		// This tells the shape how it should offset its position during
		// rotation.
		// Kinda just gonna have to go by guess,check,revise :-/
		s.setRotationMatrix(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });

		// Set the sacrifice entry information and any extra notes.
		// Doesn't actually set the sacrifice requirements, just what is said in
		// the tome GUI
		s.setNotes("Sacrifice:\n\n"
				+ "-1xChest, 1xGoldIngot, 1XP, 1/2xHungerBars");
		// Set the description of the rune for the entry in the tome GUI
		s.setDesc("Description:\n\n"
				+ "Creates a rune that sucks in items and puts them in a chest.\n\n"
				+ "Demonstrates: Item,XP,Hunger Sacrifices, and what happens after you die. ");
		// Set the author of this rune (and optionally the name of your rune
		// pack)
		// This should be you!
		s.setAuthor("billythegoat101: Demonstrations pack");

		// Finally register this rune into the sytem along with an instance of
		// the DustEvent that should be called for every instance of this rune.
		DustManager.registerLocalDustShape(s, new DEChestNom());
		// </editor-fold>

		// <editor-fold defaultstate="collapsed" desc="makeblock">
		s = new DustShape(4, 4, "makeblock", false, 0, 0, 0, 0, 101);
		values = new int[][][] { { { G, G, g, g }, { G, g, G, g },
				{ g, G, g, G }, { g, g, G, G } } };
		s.setData(values);
		s.setRuneName("First Rune of Cheating");
		s.setRotationMatrix(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		s.setNotes("Sacrifice:\n\n"
				+ "-1xIronIngot or 1xGoldIngot or 1xDiamond ");
		s.setDesc("Description:\n\n"
				+ "Creates a block of the sacrificed material\n\n"
				+ "Demonstrates: Variable sacrifices, Slowly altering the world.");
		s.setAuthor("billythegoat101: Demonstrations pack");
		DustManager.registerLocalDustShape(s, new DEMakeBlockFromItem());
		// </editor-fold>

		// <editor-fold defaultstate="collapsed" desc="makeitrain">
		s = new DustShape(4, 4, "makeitrain", false, 0, 0, 0, 0, 102);
		values = new int[][][] { { { g, g, g, L }, { g, g, L, g },
				{ g, L, g, g }, { L, g, g, g } } };
		s.setData(values);
		s.setRuneName("Rune of the Rain Maker");
		s.setRotationMatrix(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		s.setNotes("Sacrifice:\n\n" + "-2xHearts per rain change");
		s.setDesc("Description:\n\n"
				+ "Right click the rune to toggle rainfall.\n\n"
				+ "Demonstrates: Right-clicking, health sacrifices");
		s.setAuthor("billythegoat101: Demonstrations pack");
		DustManager.registerLocalDustShape(s, new DEMakeItRain());
		// </editor-fold>

		// <editor-fold defaultstate="collapsed" desc="constanthurt">
		s = new DustShape(8, 8, "constanthurt", false, 2, 2, 2, 2, 103);
		values = new int[][][] { { { 0, 0, 0, B, B, 0, 0, 0 },
				{ 0, 0, B, G, G, B, 0, 0 }, { 0, B, B, 0, 0, B, B, 0 },
				{ B, G, 0, N, N, 0, G, B }, { B, G, 0, N, N, 0, G, B },
				{ 0, B, B, 0, 0, B, B, 0 }, { 0, 0, B, G, G, B, 0, 0 },
				{ 0, 0, 0, B, B, 0, 0, 0 } } };
		s.setData(values);
		// Sets which types of dusts are allowed to replace the variable dust
		s.addAllowedVariable(100, 200, 300, 350, 400);
		s.setRuneName("Rune of Pain");
		s.setRotationMatrix(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		s.setNotes("Sacrifice:\n\n" + "-None\n\n" + "Notes:\n\n"
				+ "-Will last for a day unless fueled.");
		s.setDesc("Description:\n\n"
				+ "Constantly damages all entities (that aren't the summoning player) within range."
				+ "Amount of damage depends on the variable dust.\n\n"
				+ "Demonstrates: Identifying the rune creator, Powered runes, Pausing powered runes, Basic AOE effects.");
		s.setAuthor("billythegoat101: Demonstrations pack");
		DustManager.registerLocalDustShape(s, new DEPain());
		// </editor-fold>

		// <editor-fold defaultstate="collapsed" desc="icesprite">
		s = new DustShape(10, 10, "icesprite", false, 1, 1, 3, 3, 105);
		values = new int[][][] { { { 0, 0, 0, G, 0, 0, G, 0, 0, 0 },
				{ 0, G, G, 0, G, G, 0, G, G, 0 },
				{ 0, G, G, G, G, G, G, G, G, 0 },
				{ G, 0, G, G, 0, 0, G, G, 0, G },
				{ 0, G, G, 0, L, L, 0, G, G, 0 },
				{ 0, G, G, 0, L, L, 0, G, G, 0 },
				{ G, 0, G, G, 0, 0, G, G, 0, G },
				{ 0, G, G, G, G, G, G, G, G, 0 },
				{ 0, G, G, 0, G, G, 0, G, G, 0 },
				{ 0, 0, 0, G, 0, 0, G, 0, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Rune of the Ice Sprite");
		s.setRotationMatrix(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		s.setNotes("Sacrifice:\n\n" + "-4xSnowBlock + 1xGhastTear + 20XP\n\n"
				+ "Notes:\n\n" + "-Will last for a three days unless fueled.");
		s.setDesc("Description:\n\n"
				+ "Summons a sprite which follows the player and freezes the surface of liquids. Water turns to ice and lava turns to obsidian.\n\n"
				+ "Demonstrates: Item sacrifices, XP Sacrifices, Powered runes, Pausing, and Following sprites.");
		s.setAuthor("billythegoat101: Demonstrations pack");
		DustManager.registerLocalDustShape(s, new DEIceSprite());
		// </editor-fold>

		// <editor-fold defaultstate="collapsed" desc="NAME">
		// Solid flag is now changed to true because it is all variable and we
		// don't
		// want people cheating the rune by making some blaze and some plant
		s = new DustShape(6, 6, "changeblock", true, 3, 3, 1, 1, 106);
		values = new int[][][] { { { 0, 0, N, N, N, 0 }, { 0, N, N, N, N, N },
				{ N, N, N, 0, N, N }, { N, N, 0, N, N, N },
				{ N, N, N, N, N, 0 }, { 0, N, N, N, 0, 0 } } };
		s.setData(values);
		s.setRuneName("Second Rune of Cheating");
		// Sets which types of dusts are allowed to replace the variable dust
		s.addAllowedVariable(100, 200, 300, 350, 400);
		s.setRotationMatrix(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		s.setNotes("Sacrifice:\n\n" + "-");
		s.setDesc("Description:\n\n" + "");
		s.setAuthor("billythegoat101: Demonstrations pack");
		DustManager.registerLocalDustShape(s, new DEMakeBlockFromDustLevel());
		// </editor-fold>
		// ******Notes for reanimation*******
		// Adding numbers to the end of your rune's IDName (or just changing the
		// name in general) will force all existing runes of that type to
		// reanimate. Use this in case you make a major changes/updates to that
		// rune.
	}

	/*
	 * Template
	 * 
	 * //<editor-fold defaultstate="collapsed" desc="NAME"> s = new
	 * DustShape(8,8, "NAME", false,0,0,0,0, id); values = new int[][][]{ {
	 * {0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0},
	 * {0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0},
	 * {0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0} } }; s.setData(values);
	 * s.setRuneName("Name"); s.setManualRotationDerp(new int[]{0,0, 0,0, 0,0,
	 * 0,0}); s.setNotes("Sacrifice:\n\n" + "-"); s.setDesc("Description:\n\n" +
	 * ""); s.setAuthor(""); DustManager.registerLocalDustShape(s, new
	 * DENAME()); //</editor-fold>
	 */

	public void registerInscriptions() {
	}

	/**
	 * Tell forge that your mod content pack requires the Runic Dust mod to be
	 * installed and to let the Runic Dust mod to load and initialize first.
	 * (Because I am the king and I get to go first.)
	 * 
	 * @return
	 */
	public String getPriorities() {
		return "required-after:mod_DustMod";
	}

}
