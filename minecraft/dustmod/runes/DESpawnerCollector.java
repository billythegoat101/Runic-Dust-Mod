/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import dustmod.DustEvent;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DESpawnerCollector extends DustEvent
{
    public DESpawnerCollector()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderBeam(true);
		e.setStarScale(1.05F);
		
    }
    public void onInit(EntityDust e)
    {
		e.setRenderBeam(true);
		e.setStarScale(1.05F);
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.ingotGold, 6)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || !takeXP(e, 10))
        {
            e.fizzle();
            return;
        }
        

//        EntityItem ei = new EntityItem(e.worldObj);
//        ei.setPosition(e.posX, e.posY - e.yOffset, e.posZ);
//        ei.item = new ItemStack(Block.mobSpawner, 1);
//        e.worldObj.spawnEntityInWorld(ei);
    }

    public void onTick(EntityDust e)
    {
        int[] fin = new int[3];

        for (Integer[] i: e.dustPoints)
        {
            fin[0] += i[0];
            fin[1] += i[1];
            fin[2] += i[2];
        }

        fin[0] /= 8;
        fin[1] /= 8;
        fin[2] /= 8;

        if (e.worldObj.getBlockId(fin[0], fin[1], fin[2]) == Block.mobSpawner.blockID)
        {
        	TileEntityMobSpawner tems =(TileEntityMobSpawner)e.worldObj.getBlockTileEntity(fin[0], fin[1], fin[2]);
//        	String entID = tems.func_92015_a();
            tems.invalidate();

            if (e.ticksExisted > 100)
            {
                e.worldObj.setBlockWithNotify(fin[0], fin[1], fin[2], 0);
                EntityItem ei = new EntityItem(e.worldObj);
                ei.setPosition(e.posX, e.posY - e.yOffset, e.posZ);
                ItemStack item = new ItemStack(Block.mobSpawner, 1);
//                NBTTagCompound nbt = new NBTTagCompound();
//                item.setTagCompound(nbt);
//                nbt.setString("EntityID", entID); 
                ei.func_92058_a(item); 
                e.worldObj.spawnEntityInWorld(ei);
                e.worldObj.markBlockForUpdate(fin[0], fin[1], fin[2]);
            }
        }

        if (e.ticksExisted > 100)
        {
            e.fade();
//            e.worldObj.setBlock(fin[0], fin[1]-1, fin[2], Block.brick.blockID);
        }
    }
}
