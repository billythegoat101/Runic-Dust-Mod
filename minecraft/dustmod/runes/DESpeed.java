/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import dustmod.DustEvent;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DESpeed extends DustEvent
{
    public DESpeed()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        e.setStarScale(1.12F);
        e.setColorStarOuter(0, 255, 0);
        e.setRenderStar(true);
		
    }
    
    public void onInit(EntityDust e)
    {
        ItemStack[] req = new ItemStack[]
        {
            new ItemStack(Item.sugar, 3, -1),
            new ItemStack(Item.blazePowder, 1, -1),
        };
        sacrifice(e, req);

        if (req[0].stackSize > 0 || req[1].stackSize > 0)
        {
            e.fizzle();
            return;
        }

        int dustId = e.dusts[e.dusts.length - 1][e.dusts[0].length - 1];
        int p = 0;
        int d = 0;

        switch (dustId)
        {
            case 100:
                p = 1;
                d = 25 * 30;
                break;

            case 200:
                p = 1;
                d = 25 * 60;
                break;

            case 300:
                p = 2;
                d = 25 * 120;
                break;

            case 400:
                p = 4;
                d = 25 * 180;
                break;
        }

        List<Entity> ents = this.getEntities(e, 3D);

        for (Entity i: ents)
        {
            if (i instanceof EntityLiving)
            {
                ((EntityLiving)i).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, d, p));
            }
        }

        e.setStarScale(1.12F);
        e.setColorStarOuter(0, 255, 0);
        e.setRenderStar(true);
        e.fade();
    }

    public void onTick(EntityDust e)
    {
    }
}
