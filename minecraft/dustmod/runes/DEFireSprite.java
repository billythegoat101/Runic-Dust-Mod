/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.EntityDust;
import dustmod.PoweredEvent;

/**
 *
 * @author billythegoat101
 */
public class DEFireSprite extends PoweredEvent
{
    public DEFireSprite()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderStar(true);
        e.setFollow(true);
        e.setColorStarInner(255, 0, 0);
		
    }

    public void onInit(EntityDust e)
    {
        super.onInit(e);
		e.setRenderStar(true);
        e.setFollow(true);
        e.setColorStarInner(255, 0, 0);
        ItemStack[] sacrifice = new ItemStack[] {new ItemStack(Item.ghastTear, 1), new ItemStack(Item.fireballCharge, 2)};
        this.sacrifice(e, sacrifice);

        if (!checkSacrifice(sacrifice) || !takeXP(e, 22))
        {
            e.fizzle();
            return;
        }
    }

    public void onTick(EntityDust e)
    {
        super.onTick(e);
        e.setColorStarOuter(255, 0, 0);
        
        EntityPlayer player = e.worldObj.getPlayerEntityByName(e.summonerUN);

//        if (true/*!e.worldObj.multiplayerWorld*/)
//        {
//            player = ModLoader.getMinecraftInstance().thePlayer;
//        }

        if (player == null)
        {
            e.data[0] = 1;
            return;
        }else {
            e.data[0] = 0;
        }
            

        e.setFire(0);
        int rad = 3;
        List<Entity> kill = getEntities(e, rad);

        for (Entity k: kill)
        {
            if (k == player || k == e)
            {
                continue;
            }

            if (k instanceof EntityLiving)
            {
                k.setFire(2 + (int)(Math.random() * 5));
            }
        }

        if (e.ticksExisted % 100 == 0 && Math.random() < 0.5)
        {
//            System.out.println("ignite");
            int ex = e.getX();
            int ey = e.getY();
            int ez = e.getZ();
            boolean ignited = false;

            for (int x = -rad; x <= rad && !ignited; x++)
            {
                for (int y = -rad; y <= rad && !ignited; y++)
                {
                    for (int z = -rad; z <= rad && !ignited; z++)
                    {
                        if (e.worldObj.getBlockId(ex + x, ey + y - 1, ez + z) != 0 && e.worldObj.getBlockId(ex + x, ey + y, ez + z) == 0 && Math.random() < 0.05D)
                        {
                            e.worldObj.setBlockWithNotify(ex + x, ey + y, ez + z, Block.fire.blockID);
                            ignited = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getStartFuel()
    {
        return dayLength * 3;
    }

    @Override
    public int getMaxFuel()
    {
        return dayLength * 7;
    }

    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return dayLength + dayLength / 2;
    }

    @Override
    public boolean isPaused(EntityDust e)
    {
        return e.data[0] == 1;
    }
}
