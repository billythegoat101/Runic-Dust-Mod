/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import dustmod.runes.DEPowerRelay;

/**
 *
 * @author billythegoat101
 */
public abstract class PoweredEvent extends DustEvent
{
    public static final int dayLength = 24000;
    public boolean consumeItems = true;
    public PoweredEvent()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);
    	
    	e.setRenderStar(true);
    	e.setStarScale(1.0F);
    	e.setColorStar(255,255,255);
		
    }

    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);

        if (this.getClass() != DEPowerRelay.class)
        {
            List<EntityDust> ents = DEPowerRelay.findDustEntities(e);

            for (EntityDust i: ents)
            {
            	if(i.event == null) continue;
                if (i.event.getClass() == DEPowerRelay.class)
                {
                    ((DEPowerRelay)i.event).registerSelfTo(i, e);
                }
            }
        }

        e.setFuel(this.getStartFuel());
        e.requiresFuel = true;
    	e.setRenderStar(true);
    	e.setStarScale(1.0F);
    	e.setColorStar(255,255,255);
    }

    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);

        if (e.getFuel() <= 0 && !this.isPaused(e))
        {
            e.fade();
            return;
        }

        if (!this.isPaused(e))
        {
            subtractFuel(e);
        }

        if (consumeItems)
        {
            List<Entity> ents = this.getEntities(e, 1.0D);

            for (Entity i: ents)
            {
                if (!i.isDead && i instanceof EntityItem)
                {
                    EntityItem ei = (EntityItem)i;
                    ei.attackEntityFrom(null, -20);
                    //                ei.delayBeforeCanPickup = 20;
                    ItemStack is = ei.getEntityItem();

                    if (TileEntityFurnace.getItemBurnTime(is) != 0)
                    {
                        addFuel(e, TileEntityFurnace.getItemBurnTime(is) * is.stackSize);
                        ei.setDead();
                        continue;
                    }
                }
            }
        }

        if (e.isFueledExternally())
        {
            e.setStarScale(1.04F);
        }
        else
        {
            e.setStarScale(1F);
        }

        if (!this.isPaused(e))
        {
            double powerPercent = (double)e.getFuel() / (double)this.getStableFuelAmount(e);
            int c = (int)(255D * powerPercent);

            if (c > 255)
            {
                c = 255;
            }

            e.setColorStar(255, c, c);
        }
        else
        {
            e.setColorStar(255, 255, 0);
        }

//        System.out.println("PowerPercent " + powerPercent + " Color " + c + " " + e.ri + ":" + e.gi + ":" + e.bi);
    }

    @Override
    public void onRightClick(EntityDust e, TileEntityDust ted, EntityPlayer p)
    {
        super.onRightClick(e, ted, p);
    }

    @Override
    public void onUnload(EntityDust e)
    {
        super.onUnload(e);

        if (this.getClass() != DEPowerRelay.class)
        {
            List<EntityDust> ents = DEPowerRelay.findDustEntities(e);

            for (EntityDust i: ents)
            {
            	if(i.event == null) continue;
                if (i.event.getClass() == DEPowerRelay.class)
                {
                    ((DEPowerRelay)i.event).removeSelfFrom(i, e);
                }
            }
        }
    }

    public void subtractFuel(EntityDust e)
    {
        e.setFuel(e.getFuel() - 1);
    }
    public void addFuel(EntityDust e, int amt)
    {
        if (e.getFuel() + amt > this.getMaxFuel())
        {
            return;
        }

        e.setFuel(e.getFuel() + amt);
    }

    public abstract int getStartFuel();     //amount of fuel to start with
    public abstract int getMaxFuel();       //Max amount of fuel
    public abstract int getStableFuelAmount(EntityDust e);    //amount of fuel requested
    public abstract boolean isPaused(EntityDust e); //pause the fuel depletion

    /**
     * Called to check if the rune is requesting anymore power or if it is 
     * currently stable where it is.
     * @param e EntityDust instance
     * @return The amount of power it wants, if needed
     */
    public int powerWanted(EntityDust e)
    {
        int cur = e.getFuel();
        int rtn = 0;
        int stable = this.getStableFuelAmount(e);

//        System.out.println("DICKS " + stable + " " + cur + " " + (stable-cur) + " " + this);
        if (stable > cur)
        {
            rtn = stable - cur;
        }

        return rtn;
    }
}
