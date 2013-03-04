/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.DustEvent;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEDawn extends DustEvent
{
    public static boolean oneActive = false;
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderStar(true);
		e.setRenderBeam(false);
    	
    }
	
    public void onInit(EntityDust e)
    {
		e.setRenderStar(true);
		e.setRenderBeam(false);
        ItemStack[] req = this.sacrifice(e, new ItemStack[] {new ItemStack(Item.redstone, 4), new ItemStack(Item.dyePowder, 1, 4)});

        if (req[0].stackSize != 0 || req[1].stackSize != 0)
        {
            e.fizzle();
            return;
        }

        e.data[0] = (byte)0;
        e.data[0] = 0;
    }

    public void onTick(EntityDust e)
    {
        if (e.data[0] == 1 && !oneActive)
        {
            oneActive = false;
            e.data[0] = 0;
    		e.setRenderStar(true);
    		e.setRenderBeam(false);
        }

        long time = e.worldObj.getWorldTime() + 1000;

        if (e.data[0] == 1 && !e.worldObj.isDaytime())
        {
            e.worldObj.setWorldTime(e.worldObj.getWorldTime() + 25);
        }
        else if (e.data[0] == 0 && !e.worldObj.isDaytime() && !oneActive)
        {
            oneActive = true;
            e.data[0] = 1;
        }

        if (e.data[0] == 1)
        {
    		e.setRenderStar(false);
    		e.setRenderBeam(true);
        }

        if (e.data[0] == 1 && e.worldObj.isDaytime())
        {
            e.kill();
        }
    }

    @Override
    protected void onUnload(EntityDust e)
    {
        super.onUnload(e);

        if (e.data[0] == 1)
        {
            oneActive = false;
        }
    }
}
