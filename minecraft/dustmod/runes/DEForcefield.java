/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import dustmod.EntityDust;
import dustmod.PoweredEvent;
import dustmod.Sacrifice;

/**
 *
 * @author billythegoat101
 */
public class DEForcefield extends PoweredEvent
{
    public DEForcefield()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);
    	
    	e.setRenderStar(true);
        e.setColorStarOuter(0, 128, 62);
		
    }

    public void onInit(EntityDust e)
    {
        super.onInit(e);
        int a = e.dusts[5][5];
        int b = e.dusts[5][6];
        int c = e.dusts[6][5];
        int d = e.dusts[6][6];
        int dustStrength = a;

        ItemStack[] req = new ItemStack[] {new ItemStack(Item.monsterPlacer, 1, 120 /*testificate*/)};
        req = this.sacrifice(e, req);
        if (!checkSacrifice(req) || a != b || b != c || c != d || !takeXP(e, 15))
        {
            e.fizzle();
            return;
        }

        e.setColorStarOuter(0, 128, 62);
        e.setRenderStar(true);
        e.data[1] = dustStrength/100;
    }

    public void onTick(EntityDust e)
    {
    	if(e.bb != 0) e.data[1] = e.bb;
        super.onTick(e);
        float rad = e.data[1] * 8F;
        List<Entity> ents = this.getEntities(e, rad);
        double vel = 1.02D;
        double max = 24000 * 2;

//        e.posY = e.getY() + (double)e.data/(max) + e.yOffset;
//        System.out.println("Percent " + ((double)e.data/max));
        for (Entity i: ents)
        {
            if (i instanceof IMob)
            {
                float dist;

                if ((dist = i.getDistanceToEntity(e)) >= rad)
                {
                    continue;
                }

                double d = i.posX - e.posX;
                double d1 = i.posZ - e.posZ;
                float f2 = (float)((Math.atan2(d1, d) * 180D) / Math.PI) - 90F;
                float f3 = (rad / dist) * 0.5F;
                i.motionX -= MathHelper.cos((f2 + 270F) * 0.01745329F) * f3;
                i.motionZ -= MathHelper.sin((f2 + 270F) * 0.01745329F) * f3;
//                double dx = i.posX-e.posX;
//                double dz = i.posZ-e.posZ;
//                double mag = Math.sqrt(dx*dx+dz*dz);
//                dx/=mag;
//                dz/=mag;
//                i.setVelocity(dx*vel, 0.05D, dz*vel);
            }

//            if(i instanceof EntityItem && !i.isDead && i.getDistanceToEntity(e) <= 1.4D){
//                addFuel(e,((EntityItem)i).item.stackSize * 50);
//                i.setDead();
//            }
        }

        if (e.ticksExisted % 25 == 0)
        {
            e.shineRadiusSphere(rad, 0.0D, 0.5D, 0.5D);
        }
    }

    @Override
    public int getStartFuel()
    {
        return dayLength;
    }

    @Override
    public int getMaxFuel()
    {
        return dayLength * 2;
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
