/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 *
 * @author billythegoat101
 */
public class ItemRunicTome  extends DustModItem
{
    private int blockID;
    private int tex;

    public ItemRunicTome(int i)
    {
        super(i);
        blockID = DustMod.dust.blockID;
        
        //[non-forge]
//        this.setIconIndex(ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/notebook.png"));
        //[forge]
        this.setMaxStackSize(1);
    }

//    @Override
//    public boolean onItemUse(ItemStack itemstack, EntityPlayer p, World world, int i, int j, int k, int l) {
//        ModLoader.openGUI(p, new GuiTome());
//        return true;
//    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer p)
    {
        if (p.isSneaking())
        {
//        	p.openGui(DustMod.instance, 0, world, 0, 0, 0);
            DustMod.proxy.openTomeGUI(itemstack, p);
        }

        return super.onItemRightClick(itemstack, world, p);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer p, World world, int i, int j, int k, int l, float x, float y, float z)
    {
    	

    	int page = itemstack.getItemDamage();

        if (world.isRemote || page == 0 || p.isSneaking() || !p.capabilities.isCreativeMode)
        {
            return false;
        }

        DustShape ds = DustManager.getShape(page - 1);
        int r = (MathHelper.floor_double((double)(p.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);

        if (DustMod.isDust(world.getBlockId(i, j, k)))
        {
            if (world.getBlockMetadata(i, j, k) == DustMod.DustMetaUsed)
            {
                world.setBlockAndMetadataWithNotify(i, j, k, 0,0,3);
                j--;
            }
            else
            {
                return false;
            }
        }

        if (ds.drawOnWorldWhole(world, i, j, k, p, r))
        {
//                itemstack.stackSize--;
        }
    	
        return true;//DustMod.proxy.placeDustWithTome(itemstack, p, world, i, j, k, l);
    }

//    @Override
//    public String getItemName() {
//        return "dustchisel";
//    }
}