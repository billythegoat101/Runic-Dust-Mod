package dustmod;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemInk extends Item {
	
	public static final int maxAmount = 32;
	
	public ItemInk(int i)
    {
        super(i);
        setHasSubtypes(true);
        
        //[non-forge]
//        plantTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/plantdust.png");
//        gunTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/gundust.png");
//        lapisTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/lapisdust.png");
//        blazeTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/blazedust.png");
        
        //[forge]
        this.setMaxStackSize(1);
        this.setTextureFile(DustMod.path + "/dustItems.png");
        this.setCreativeTab(DustMod.creativeTab);
    }
	

    

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 5; i < 1000; ++i) //i > 4 for migration from old system
        {
        	if(DustItemManager.getColors()[i] != null){
                par3List.add(getInk(i));
        	}
        }
    }
    

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
    	int dustID = getDustID(itemstack);
    	String id = DustItemManager.getIDS()[dustID];
    	if(id != null) return "tile.ink." + DustItemManager.idsRemote[dustID];

        return "tile.ink";
    }

    @Override
    public String getLocalItemName(ItemStack itemstack)
    {
    	int dustID = getDustID(itemstack);
    	String id = DustItemManager.getIDS()[dustID];
    	if(id != null) return "ink." + DustItemManager.idsRemote[dustID];

        return "ink";
    }

    @Override
    public int getIconFromDamage(int i)
    {
        return i-1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
    	int meta = stack.getItemDamage();
    	int id = getDustID(meta);
    	if(pass == 0) return 16777215;
    	return pass == 1 ? DustItemManager.getPrimaryColor(id) : DustItemManager.getSecondaryColor(id);
    }


    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public int getIconFromDamageForRenderPass(int meta, int rend)
    {
    	if(rend == 0) return 48;
    	
    	int off = (maxAmount-1)-meta%maxAmount;
    	
    	off /= (maxAmount/8);
    	
    	if(rend == 1){
    		return 49 + off * 16;
    	}else if(rend == 2){
    		return 50 + off * 16;
    	}
        return 0;
    }
    
    public static ItemStack getInk(int dustID){
    	return new ItemStack(DustMod.ink.itemID, 1, dustID*maxAmount + maxAmount-1);
    }
    
    public static int getDustID(ItemStack item){
    	return getDustID(item.getItemDamage());
    }
    public static int getDustID(int meta){
    	return (meta - (meta%maxAmount)) / maxAmount; 
    }
    
    public static boolean reduce(EntityPlayer p, ItemStack item, int amt){
    	if(p.capabilities.isCreativeMode) return true;
    	int fill = item.getItemDamage()%maxAmount;
    	int level = item.getItemDamage() - fill;
    	if(fill < amt) return false;
    	fill -= amt;
    	if(fill == 0) {
    		item.itemID = Item.glassBottle.itemID;
    		item.setItemDamage(0);
    	}else
    		item.setItemDamage(level + fill);
    	return true;
    }

}
