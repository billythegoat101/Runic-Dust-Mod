/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.DustEvent;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DESpawnRecord extends DustEvent
{
    public DESpawnRecord()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderStar(true);
		e.setRenderBeam(true);
        e.setColorStarOuter(0, 255, 0);
        e.setColorBeam(0, 255, 0);
		
    }

    public void onInit(EntityDust e)
    {
		e.setRenderStar(true);
		e.setRenderBeam(true);
        e.setColorStarOuter(0, 255, 0);
        e.setColorBeam(0, 255, 0);
        ItemStack[] sacrifice = new ItemStack[] {new ItemStack(Item.diamond, 1)};
        this.sacrifice(e, sacrifice);

        if (sacrifice[0].stackSize > 0)
        {
            e.fizzle();
            return;
        }
    }

    public void onTick(EntityDust e)
    {
        e.setStarScale(e.getStarScale() + 0.001F);

        if (e.ticksExisted > 120)
        {
            Random r = new Random();
            EntityItem en = new EntityItem(e.worldObj, e.posX, e.posY - EntityDust.yOffset - 1, e.posZ, new ItemStack(2000 + r.nextInt(11) + 256, 1, 0));
            e.worldObj.spawnEntityInWorld(en);
            e.fade();
        }
    }
}
