/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DELumberjack extends DustEvent
{
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        e.setRenderStar(true);
        e.setColorStarOuter(77, 65, 47);
        e.setColorStarInner(77, 65, 47);
        e.setStarScale(1.12F);
		
    }

    public void onInit(EntityDust e)
    {
        ItemStack[] sac = new ItemStack[]
        {
            new ItemStack(Item.stick, 2), new ItemStack(Block.wood, 3, -1)
        };
        sac = this.sacrifice(e, sac);

        if (sac[0].stackSize > 0 || sac[1].stackSize > 0)
        {
            e.fizzle();
            return;
        }

        e.data[0] = e.dustID;
        e.setRenderStar(true);
        e.setColorStarOuter(77, 65, 47);
        e.setColorStarInner(77, 65, 47);
        e.setStarScale(1.12F);
        int x = e.getX();
        int y = e.getY();
        int z = e.getZ();

        if (e.worldObj.getBlockId(x, y, z) != Block.wood.blockID)
        {
            e.fizzle();
        }
    }

    public void onTick(EntityDust e)
    {
        int x = e.getX();
        int y = e.getY();
        int z = e.getZ();
        World world = e.worldObj;
        int rad = 50;
        switch (e.data[0])
        {
            case 100:
                rad = 6;
                break;
            case 200:
                rad = 9;
                break;
            case 300:
                rad = 12;
                break;
            case 400:
                rad = 16;
                break;
        }
        if (Math.random() < 0.08)
        {
            for (int i = -rad; i <= rad; i++)
            {

                for (int j = -rad / 2; j <= rad / 2; j++)
                {
                    for (int k = -rad; k <= rad; k++)
                    {
                        if (isTree(world,x+i,y+j,z+k))
                        {
                            spawnExplosionParticle(world, x + i, y + j, z + k);
                        }

                    }
                }
            }
        }

        if (e.ticksExisted > 100)
        {

            for (int i = -rad; i <= rad; i++)
            {

                for (int j = -rad / 2; j <= rad / 2; j++)
                {
                    for (int k = -rad; k <= rad; k++)
                    {
                        if (isTree(world,x+i,y+j,z+k))
                        {
                            checkWood(world, x + i, y + j, z + k, e.data[0], x + i, y, z + k);
                        }

                    }
                }
            }

            e.fade();
        }
    }

    public void checkWood(World world, int x, int y, int z, int dustID, int sx, int sy, int sz)
    {
        double dx = Math.abs(x - sx);
        double dy = Math.abs(y - sy);
        double dz = Math.abs(z - sz);
        double tol = 16D;

        if (dx > tol || dz > tol || dy > tol)
        {
            return;
        }

        double doublechance = 0D;
        double stickchance = 0D;
        int maxdouble = 1;
        int maxstick = 1;

        switch (dustID)
        {
            case 100:
                doublechance = 0.12D;
                stickchance = 0.2D;
                maxdouble = 2;
                maxstick = 2;
                break;

            case 200:
                doublechance = 0.2D;
                stickchance = 0.35D;
                maxdouble = 2;
                maxstick = 3;
                break;

            case 300:
                doublechance = 0.35D;
                stickchance = 0.5D;
                maxdouble = 3;
                maxstick = 3;
                break;

            case 400:
                doublechance = 0.5D;
                stickchance = 0.65D;
                maxdouble = 3;
                maxstick = 4;
                break;
        }

        Random rand = new Random();

        if (Math.random() < doublechance)
        {
            for (int a = rand.nextInt(maxdouble); a > 0; a--)
            {
                Block.wood.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 1);
            }
        }

        if (Math.random() < stickchance)
        {
            for (int a = rand.nextInt(maxstick); a > 0; a--)
            {
                EntityItem ei = new EntityItem(world, x, y, z, new ItemStack(Item.stick.shiftedIndex, 1, 0));
                world.spawnEntityInWorld(ei);
            }
        }

        Block.wood.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 1);
        world.setBlockWithNotify(x, y, z, 0);

        for (int i = -2; i <= 2; i++)
        {
            for (int j = 0; j <= 1; j++)
            {
                for (int k = -2; k <= 2; k++)
                {
                    if (i == 0 || k == 0)
                    {
                        if (isTree(world,x+i,y+j,z+k))
                        {
                            checkWood(world, x + i, y + j, z + k, dustID, sx, sy, sz);
                        } else if(isLeaves(world,x+i,y+j,z+k)){
                            checkLeaves(world, x + i, y + j, z + k, dustID, sx, sy, sz);
                        }
                    }
                }
            }
        }
    }
    public void checkLeaves(World world, int x, int y, int z, int dustID, int sx, int sy, int sz)
    {
        double dx = Math.abs(x - sx);
        double dy = Math.abs(y - sy);
        double dz = Math.abs(z - sz);
        double tol = 16D;

        if (dx > tol || dz > tol || dy > tol)
        {
            return;
        }

        double dustchance = 0D;
        int maxDust = 0;

        switch (dustID)
        {
            case 100:
                dustchance = 0.02D;
                maxDust = 1;
                break;

            case 200:
                dustchance = 0.065D;
                maxDust = 1;
                break;

            case 300:
                dustchance = 0.1D;
                maxDust = 2;
                break;

            case 400:
                dustchance = 0.187D;
                maxDust = 2;
                break;
        }

        Random rand = new Random();

        if (Math.random() < dustchance)
        {
            for (int a = rand.nextInt(maxDust); a >= 0; a--)
            {
                EntityItem ei = new EntityItem(world, x, y, z, new ItemStack(DustMod.getItemDust().shiftedIndex, 1, 100));
                world.spawnEntityInWorld(ei);
            }
        }

//        Block.wood.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 1);
        world.setBlockWithNotify(x, y, z, 0);

        for (int i = -2; i <= 2; i++)
        {
            for (int j = 0; j <= 1; j++)
            {
                for (int k = -2; k <= 2; k++)
                {
                    if (i == 0 || k == 0)
                    {
//                        if (isTree(world,x+i,y+j,z+k))
//                        {
//                            checkWood(world, x + i, y + j, z + k, dustID, sx, sy, sz);
//                        } else
                    	if(isLeaves(world,x+i,y+j,z+k)){
                            checkLeaves(world, x + i, y + j, z + k, dustID, sx, sy, sz);
                        }
                    }
                }
            }
        }
    }

    public boolean isTree(World world, int i, int j, int k){
        int block = world.getBlockId(i,j,k);
        if(block != Block.wood.blockID){
            return false;
        }
        
//        int meta = world.getBlockMetadata(i,j,k);
//        if(meta >= 4)
//            return false;
        
        return true;
    }
    public boolean isLeaves(World world, int i, int j, int k){
        int block = world.getBlockId(i,j,k);
        if(block != Block.leaves.blockID){
            return false;
        }
        
//        int meta = world.getBlockMetadata(i,j,k);
//        if((meta&8) == 0){
//            return false;
//        }
        
        return true;
    }
    
    public void spawnExplosionParticle(World world, double x, double y, double z)
    {
        Random rand = new Random();
        double width = 1.3d;
        double height = 1.3d;
//        double var8 = 10.0D;
//        for (int var1 = 0; var1 < 20; ++var1)
//        {
//            double rx = rand.nextGaussian() * 0.02D;
//            double ry = rand.nextGaussian() * 0.02D;
//            double rz = rand.nextGaussian() * 0.02D;
//            world.spawnParticle("explode", x + (double) (y * 2.0F) - (double) width - rx * var8, y + (double) (rand.nextFloat() * height) - ry * var8, z + (double) (rand.nextFloat() * width * 2.0F) - (double) width - rz * var8, rx, ry, rz);
//        }
        
        DustMod.spawnParticles(world,"explode",x + width/2, y + height/2, z + width/2, 0, 0, 0, 5, width,height,width);
    }
}
