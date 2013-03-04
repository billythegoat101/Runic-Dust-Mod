/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import dustmod.DustEvent;
import dustmod.EntityDust;
import dustmod.VoidStorageManager;

/**
 *
 * @author billythegoat101
 */
public class DEVoid extends DustEvent
{
    public DEVoid()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderStar(true);
        e.setColorStarInner(255, 0, 255);
        e.setColorStarOuter(255, 0, 255);
		
    }

    public void onInit(EntityDust e)
    {
        if (!this.takeXP(e, 3))
        {
            e.fizzle();
            return;
        }

		e.setRenderStar(true);
        e.setColorStarInner(255, 0, 255);
        e.setColorStarOuter(255, 0, 255);
        List<EntityItem> sacrifice = this.getItems(e);

        if (sacrifice == null || sacrifice.isEmpty())
        {
        	e.setStarScale(1.02F);
            e.data[0] = 1;
        }
        else
        {
            for (EntityItem i: sacrifice)
            {
            	VoidStorageManager.addItemToVoidInventory(e, i.func_92014_d());
                i.setDead();
            }

            VoidStorageManager.updateVoidInventory();
            e.data[0] = 0;
        }
    }

    public void onTick(EntityDust e)
    {
        if (e.data[0] == 1)
        {
            if (e.ticksExisted > 100)
            {
                e.fade();
                ArrayList<ItemStack> list = VoidStorageManager.getVoidInventory(e);
                if(list == null) return;
                for (ItemStack i: list)
                {
                    Entity en = null;
                    en = new EntityItem(e.worldObj, e.posX, e.posY - EntityDust.yOffset, e.posZ, i);

                    if (en != null)
                    {
                        en.setPosition(e.posX, e.posY, e.posZ);
                        e.worldObj.spawnEntityInWorld(en);
                    }
                }

                VoidStorageManager.clearVoidInventory(e);
                VoidStorageManager.updateVoidInventory();
            }
        }
        else
        {
            if (e.ticksExisted > 35)
            {
                e.ticksExisted += 3;
                e.setStarScale(e.getStarScale() - 0.001F);
            }

            if (e.ticksExisted > 100)
            {
                e.kill();
            }
        }
    }
}
