/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *
 * @author billythegoat101
 */
public class ItemDust extends ItemReed 
{
    private int blockID;

    public ItemDust(int i, Block block)
    {
        super(i, block);
        blockID = block.blockID;
        setMaxDamage(0);
        setHasSubtypes(true);
        
        //[non-forge]
//        plantTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/plantdust.png");
//        gunTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/gundust.png");
//        lapisTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/lapisdust.png");
//        blazeTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/blazedust.png");
        
        //[forge]
        this.setTextureFile(DustMod.path + "/dustItems.png");
    }
   
    
    
    public boolean onItemUse(ItemStack item, EntityPlayer p, World world, int i, int j, int k, int face, float x, float y, float z)
    {
		if(!world.canMineBlock(p, i, j, k)) return false;
		
        int var11 = world.getBlockId(i, j, k);

        if(var11 == DustMod.dust.blockID && world.getBlockTileEntity(i, j, k) != null){
            DustMod.dust.onBlockActivated(world, i, j, k, p, face, x, y, z);
            return false;
        }
        
        if (var11 == Block.snow.blockID)
        {
            face = 1;
        }
        else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID)
        {
            if (face == 0)
            {
                --j;
            }

            if (face == 1)
            {
                ++j;
            }

            if (face == 2)
            {
                --k;
            }

            if (face == 3)
            {
                ++k;
            }

            if (face == 4)
            {
                --i;
            }

            if (face == 5)
            {
                ++i;
            }
        }

        if (!p.canPlayerEdit(i, j, k, 7, item))
        {
            return false;
        }
        else if (item.stackSize == 0)
        {
            return false;
        }
        else
        {
            if (world.canPlaceEntityOnSide(this.blockID, i, j, k, false, face, (Entity)null))
            {
                Block var12 = Block.blocksList[this.blockID];
                int var13 = var12.func_85104_a(world, i, j, k, face, x, y, z, 0);


                if (world.setBlockWithNotify(i, j, k, this.blockID))
                {
                    if (world.getBlockId(i, j, k) == this.blockID)
                    {
                        Block.blocksList[this.blockID].onBlockPlacedBy(world, i, j, k, p);
                        Block.blocksList[this.blockID].func_85105_g(world, i, j, k, var13);
                        
                    }
                    DustMod.dust.onBlockActivated(world, i, j, k, p, face, x, y, z);

                    world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), var12.stepSound.getStepSound(), (var12.stepSound.getVolume() + 1.0F) / 6.0F, var12.stepSound.getPitch() * 0.99F);
                    --item.stackSize;
                }
            }

            return true;
        }
    }
    

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
    	String id = DustItemManager.getIDS()[itemstack.getItemDamage()];
    	if(id != null) return "tile.dust." + DustItemManager.idsRemote[itemstack.getItemDamage()];

        return "tile.dust";
    }

    @Override
    public String getLocalItemName(ItemStack itemstack)
    {
    	return getItemNameIS(itemstack);
    }

    @Override
    public int getIconFromDamage(int i)
    {
        return i-1;
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
                par3List.add(new ItemStack(par1, 1, i));
        	}
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
    	int meta = stack.getItemDamage();
    	return pass == 0 ? DustItemManager.getPrimaryColor(meta) : DustItemManager.getSecondaryColor(meta);
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
        return rend > 0 ? 5 : 4;
    }
}