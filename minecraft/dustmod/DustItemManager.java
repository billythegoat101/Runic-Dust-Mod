package dustmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringTranslate;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

public class DustItemManager {
	
	public static DustColor[] colors = new DustColor[1000];
	public static String[] names = new String[1000];
	public static String[] ids = new String[1000];
	public static DustColor[] colorsRemote = new DustColor[1000];
	public static String[] namesRemote = new String[1000];
	public static String[] idsRemote = new String[1000];
	
	/**
	 * Register a new dust type to the system.
	 * You'll have to manually set the recipe/method of getting the dust.
	 * The item will just be DustMod.idust with the damage value equal to this passed value parameter. 
	 * 
	 * @param value	Worth of the dust. Bigger number means more worth (999 max)
	 * @param primaryColor	Color of the base of the item dust
	 * @param secondaryColor	Color of the sparkles on the item dust 
	 * @param floorColor	Color of the dust when placed on the ground.
	 */
	public static void registerDust(int value, String name, String idName,  int primaryColor, int secondaryColor, int floorColor){
		if(colors[value] != null){
			throw new IllegalArgumentException("[DustMod] Dust value already taken! " + value);
		}
		
		colors[value] = colorsRemote[value] = new DustColor(primaryColor, secondaryColor, floorColor);
		names[value] = namesRemote[value] = name;
		ids[value] = idsRemote[value] = idName;

		LanguageRegistry.instance().addStringLocalization("tile.dust." + idName + ".name", "en_US", name + " Runic Dust");
		LanguageRegistry.instance().addStringLocalization("tile.ink." + idName + ".name", "en_US", name + " Runic Ink");
		LanguageRegistry.instance().addStringLocalization("pouch." + idName + ".name", "en_US", name + " Dust Pouch");
		reloadLanguage();
		

    	GameRegistry.addShapelessRecipe(DustMod.ink.getInk(value),new Object[] {new ItemStack(Item.potion.itemID, 1, 0), new ItemStack(DustMod.idust, 1, value), Item.ghastTear});
    	GameRegistry.addShapelessRecipe(DustMod.ink.getInk(value),new Object[] {new ItemStack(Item.potion.itemID, 1, 0), new ItemStack(DustMod.pouch, 1, value*2+1), Item.ghastTear});
    	
		ItemStack craft = new ItemStack(DustMod.pouch, 1, value*2);
		GameRegistry.addRecipe(craft, new Object[] {" s ", "ldl", " l ", 's', new ItemStack(Item.silk, 1), 'd', new ItemStack(DustMod.idust, 1, value), 'l', new ItemStack(Item.leather, 1)});
//		GameRegistry.addShapelessRecipe(craft, new Object[]{craft, new ItemStack(DustMod.idust,1,value)});
		GameRegistry.addShapelessRecipe(new ItemStack(DustMod.idust,1,value),new ItemStack(DustMod.pouch, 1, value*2+1));
	}
	protected static void registerRemoteDust(int value, String name, String idName,  int primaryColor, int secondaryColor, int floorColor){
		if(colorsRemote[value] != null){
			throw new IllegalArgumentException("[DustMod] Remote error! Dust value already taken! " + value);
		}
		
//		System.out.println("Register new remote dust " + primaryColor + " " + secondaryColor + " " + floorColor);
		
		colorsRemote[value] = new DustColor(primaryColor, secondaryColor, floorColor);
		namesRemote[value] = name;
		idsRemote[value] = idName;
		
		LanguageRegistry.instance().addStringLocalization("tile.dust." + idName + ".name", "en_US", name + " Runic Dust");
		LanguageRegistry.instance().addStringLocalization("tile.ink." + idName + ".name", "en_US", name + " Runic Ink");
		LanguageRegistry.instance().addStringLocalization("pouch." + idName + ".name", "en_US", name + " Dust Pouch");

    	GameRegistry.addShapelessRecipe(DustMod.ink.getInk(value),new Object[] {new ItemStack(Item.potion.itemID, 1, 0), new ItemStack(DustMod.idust, 1, value), Item.ghastTear});
    	GameRegistry.addShapelessRecipe(DustMod.ink.getInk(value),new Object[] {new ItemStack(Item.potion.itemID, 1, 0), new ItemStack(DustMod.pouch, 1, value*2+1), Item.ghastTear});
    	
		ItemStack craft = new ItemStack(DustMod.pouch, 1, value*2);
		GameRegistry.addRecipe(craft, new Object[] {" s ", "ldl", " l ", 's', new ItemStack(Item.silk, 1), 'd', new ItemStack(DustMod.idust, 1, value), 'l', new ItemStack(Item.leather, 1)});
//		GameRegistry.addShapelessRecipe(craft, new Object[]{craft, new ItemStack(DustMod.idust,1,value)});
		GameRegistry.addShapelessRecipe(new ItemStack(DustMod.idust,1,value),new ItemStack(DustMod.pouch, 1, value*2+1));
		reloadLanguage();
	}
	
	public static void reloadLanguage(){
		if(FMLCommonHandler.instance().getSide() != Side.CLIENT) return;
		try{
			StringTranslate st = StringTranslate.getInstance();
			String curLan = st.currentLanguage;
//			String trick = "ar_SA"; //I pick this one because its the second one I see a hard-coded reference to in StringTranslate >_>
//			if(curLan.equals(trick)){
//				trick = "zh_TW"; //in case someone is using ar_SA. Not even 100% sure what that is.
//			}
//			st.setLanguage(trick);
//			st.setLanguage(curLan);
			
			Properties var2 = new Properties();

            try
            {
                loadLanguage(st,var2, "en_US");
            }
            catch (IOException var8)
            {
                ;
            }

//            st.isUnicode = false;

//            if (!"en_US".equals(par1Str))
//            {
                try
                {
                    loadLanguage(st,var2, curLan);
                    Enumeration var3 = var2.propertyNames();

                    while (var3.hasMoreElements() && !st.isUnicode())
                    {
                        Object var4 = var3.nextElement();
                        Object var5 = var2.get(var4);

                        if (var5 != null)
                        {
                            String var6 = var5.toString();

                            for (int var7 = 0; var7 < var6.length(); ++var7)
                            {
                                if (var6.charAt(var7) >= 256)
                                {
//                                    st.isUnicode = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                catch (IOException var9)
                {
                    var9.printStackTrace();
                    return;
                }
//            }

//            st.currentLanguage = par1Str;
            st.translateTable = var2;
		}catch(Exception e){
			
		}
	}
	
	private static void loadLanguage(StringTranslate st, Properties par1Properties, String par2Str) throws IOException
    {
        BufferedReader var3 = new BufferedReader(new InputStreamReader(StringTranslate.class.getResourceAsStream("/lang/" + par2Str + ".lang"), "UTF-8"));

        for (String var4 = var3.readLine(); var4 != null; var4 = var3.readLine())
        {
            var4 = var4.trim();

            if (!var4.startsWith("#"))
            {
                String[] var5 = var4.split("=");

                if (var5 != null && var5.length == 2)
                {
                    par1Properties.setProperty(var5[0], var5[1]);
                }
            }
        }
        LanguageRegistry.instance().loadLanguageTable(par1Properties, par2Str);
    }
	
	public static String[] getNames(){
		return (DustMod.proxy.isClient() ? namesRemote:names);
	}
	public static String[] getIDS(){
		return (DustMod.proxy.isClient() ? idsRemote:ids);
	}
	public static DustColor[] getColors(){
		return (DustMod.proxy.isClient() ? colorsRemote:colors);
	}
	
	public static int getPrimaryColor(int value){
		if(value <= 0) return 0x8F25A2;
		if(value > colorsRemote.length || colorsRemote[value] == null) return 0;
		return colorsRemote[value].primaryColor;
	}
	
	public static int getSecondaryColor(int value){
		if(value <= 0) return 0xDB73ED1;
		if(value > colorsRemote.length || colorsRemote[value] == null) return 0;
		return colorsRemote[value].secondaryColor;
	}
	
	public static int getFloorColor(int value){
		if(value <= 0) return 0xCE00E0;
		if(value > colorsRemote.length || colorsRemote[value] == null) return 0;
		return colorsRemote[value].floorColor;
	}
	
	public static int[] getFloorColorRGB(int value){
		if(value <= 0) return new int[] {206, 0, 224}; //00CE00E0 variable
		
		if(value > colorsRemote.length || colorsRemote[value] == null) return new int[]{0,0,0};
		
		int[] rtn = new int[3];
		
		int col = colorsRemote[value].floorColor;
		
		rtn[0] = (col & 0xFF0000) >> 16;
		rtn[1] = (col & 0xFF00) >> 8;
		rtn[2] = (col & 0xFF);
		
		return rtn;
	}
	
	public static void reset(){
		DustMod.log(Level.FINE, "Reseting remote dusts.");
//    	System.out.println("[DustMod] Reseting remote dusts.");
		colorsRemote = new DustColor[1000];
		namesRemote = new String[1000];
		idsRemote = new String[1000];
	}
	
	public static void registerDefaultDusts(){
		registerDust(1,"(old, place or craft to update)", "plantdustold", 0x629B26, 0x8AD041, 0xC2E300);
		registerDust(100,"Plant", "plantdust", 0x629B26, 0x8AD041, 0xC2E300); //Migrating to space out
		
		registerDust(2,"(old, place or craft to update)", "gundustold",0x696969, 0x979797, 0x666464);
		registerDust(200,"Gunpowder", "gundust",0x696969, 0x979797, 0x666464); //Migrating to space out
		
		registerDust(3,"(old, place or craft to update)", "lapisdustold",0x345EC3, 0x5A82E2, 0x0087FF);
		registerDust(300,"Lapis", "lapisdust",0x345EC3, 0x5A82E2, 0x0087FF); //Migrating to space out
		
		registerDust(4,"(old, place or craft to update)", "blazedustold",0xEA8A00, 0xFFFE31, 0xFF6E1E);
		registerDust(400,"Blaze", "blazedust",0xEA8A00, 0xFFFE31, 0xFF6E1E); //Migrating to space out
	}

	public static class DustColor{
		public int primaryColor;
		public int secondaryColor;
		public int floorColor;
		public DustColor(int primaryColor, int secondaryColor, int floorColor){
			this.primaryColor = primaryColor;
			this.secondaryColor = secondaryColor;
			this.floorColor = floorColor;
		}
	}
}
