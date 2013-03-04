/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.ForgeDirection;
import dustmod.EntityDust;
import dustmod.PoweredEvent;
import dustmod.TileEntityDust;

/**
 *
 * @author billythegoat101
 */
public class DECampFire extends PoweredEvent
{
    
    public DECampFire()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);
    	
    	
    }

    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        ItemStack[] sac = new ItemStack[] {new ItemStack(Item.rottenFlesh, 1), new ItemStack(Block.wood, 8, -1)};
        sac = this.sacrifice(e, sac);

        if (sac[0].stackSize > 0 || sac[1].stackSize > 0)
        {
            e.fizzle();
            return;
        }

        e.data[0] = 2400;
        int block = e.worldObj.getBlockId(e.getX(), e.getY(), e.getZ());

        if (block != Block.fire.blockID)
        {
            int under = e.worldObj.getBlockId(e.getX(), e.getY() - 1, e.getZ());

            if (block == 0 && under != 0 && Block.blocksList[under].isBlockSolidOnSide(e.worldObj, e.getX(), e.getY() - 1, e.getZ(), ForgeDirection.UP))
            {
                e.worldObj.setBlockWithNotify(e.getX(), e.getY(), e.getZ(), Block.fire.blockID);
            }
        }
        e.posY += 0.65d;

//        e.worldObj.setBlockWithNotify(e.getX(), e.getY(), e.getZ(), Block.fire.blockID);
    }

    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);
//        System.out.println("POWAH: " + e.getFuel() + " " + e.isFueledExternally());
//        if(e.isFueledExternally()/* && !e.renderFlamesDust*/){
//            e.renderFlamesDust = true;
////            System.out.println("FUELED!!");
//            e.setColorFire(255, 128, 0);
//        }
//        else if(!e.isFueledExternally()){
//            e.renderFlamesDust = false;
//        }
        int block = e.worldObj.getBlockId(e.getX(), e.getY(), e.getZ());

        if (block != Block.fire.blockID)
        {
            if (e.worldObj.isRaining() && e.worldObj.canBlockSeeTheSky(e.getX(), e.getY(), e.getZ()))
            {
                e.kill();
//                System.out.println("die1");
                return;
            }

            int under = e.worldObj.getBlockId(e.getX(), e.getY() - 1, e.getZ());

            if (block == 0 && under != 0 && Block.blocksList[under].isBlockSolidOnSide(e.worldObj, e.getX(), e.getY() - 1, e.getZ(), ForgeDirection.UP))
            {
                e.worldObj.setBlockWithNotify(e.getX(), e.getY(), e.getZ(), Block.fire.blockID);
            }
            else
            {
//                System.out.println("die2");
                e.kill();
                return;
            }

//            System.out.println("Meta " + e.worldObj.getBlockMetadata(e.getX(),e.getY(),e.getZ()));
        }

        List<Entity> ents = this.getEntities(e, 0.85D);

        for (Entity i: ents)
        {
            if (!i.isDead && i instanceof EntityItem)
            {
                EntityItem ei = (EntityItem)i;
                ei.attackEntityFrom(null, -20);
//                ei.delayBeforeCanPickup = 20;
                ItemStack is = ei.getEntityItem();
                ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(is);

                if (e.ticksExisted % 3 == 0)
                {
//                    double burnChance = 0.1D * (double)ei.item.stackSize;

//                    System.out.println("Chance " + burnChance + " " + rand);
                    if (is.stackSize > 1 && result != null)
                    {
                    	System.out.println("Stack size too big");
                        ei.setDead();
                    }
                    else 
                    {
                        if (result != null)
                        {
                            is.itemID = result.itemID;
                            is.stackSize *= result.stackSize *  + ((Math.random() > 0.85) ? 2:1);
                            is.setItemDamage(result.getItemDamage());
                            
                            EntityItem spawn = new EntityItem(e.worldObj, e.posX, e.posY, e.posZ, is);
                            e.worldObj.spawnEntityInWorld(spawn);
                            shoot(spawn);
                            ei.setDead();
//                            while (ei.item.stackSize > ei.item.getMaxStackSize())
//                            {
//                            	ei.setPosition(ei.posX, ei.posY, ei.posZ);
//                                EntityItem spawn = new EntityItem(ei.worldObj,
//                                        ei.posX, ei.posY, ei.posZ,
//                                        new ItemStack(ei.item.itemID,
//                                                ei.item.stackSize,
//                                                ei.item.getItemDamage()));
//                                ei.worldObj.spawnEntityInWorld(spawn);
//                                shoot(spawn);
//                            }
                        }

                        if(!ei.isDead)
                        	shoot(ei);
//                        System.out.println("Motion " + ei.motionX + " " + e.motionZ);
                    }
                }
            }
        }
    }

    public void shoot(EntityItem ei)
    {
        float ran = 0.12F;
        ei.motionX = (double)((float)ei.worldObj.rand.nextGaussian() * ran);
        ei.motionY = (double)((float)ei.worldObj.rand.nextGaussian() * ran + 0.2F);
        ei.motionZ = (double)((float)ei.worldObj.rand.nextGaussian() * ran);
    }

    @Override
    public void onRightClick(EntityDust e, TileEntityDust ted, EntityPlayer p)
    {
        super.onRightClick(e, ted, p);
    }

    @Override
    public void onUnload(EntityDust e)
    {
    }

    @Override
    public int getStartFuel()
    {
        return dayLength / 4;
    }

    @Override
    public int getMaxFuel()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return dayLength;
    }

    @Override
    public boolean isPaused(EntityDust e)
    {
        return false;
    }
}
