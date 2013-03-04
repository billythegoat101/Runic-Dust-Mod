/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.Sacrifice;
import dustmod.TileEntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEEggifier extends DustEvent
{
    public DEEggifier()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        e.setRenderStar(true);
    	
    }

    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.egg, 1)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || !takeXP(e, 5))
        {
            e.fizzle();
            return;
        }

        e.setRenderStar(true);
        e.setColorStar(255, 2555, 255);
        e.sacrificeWaiting = 600;

        for (Object o: EntityList.IDtoClassMapping.keySet())
        {
        	int i = (Integer)o;
            this.addSacrificeList(new Sacrifice(i));
        }
    }

    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);
        e.setStarScale(e.getStarScale() + 0.001F);

    	if(e.ticksExisted > 40 && !EntityList.entityEggs.containsKey(e.data[15])){
    		e.fizzle();
    		return;
    	} else if (e.ticksExisted > 120)
        {
            EntityItem en = null;
            en = new EntityItem(e.worldObj, e.posX, e.posY, e.posZ, new ItemStack(Item.monsterPlacer, 1, e.data[15]));

            if (en != null)
            {
                en.setPosition(e.posX, e.posY - EntityDust.yOffset, e.posZ);
                boolean blah = e.worldObj.spawnEntityInWorld(en);
            }

            e.fade();
        }
    }
}
