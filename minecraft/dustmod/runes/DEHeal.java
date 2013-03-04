/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
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
public class DEHeal extends DustEvent
{
    public DEHeal()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        e.setStarScale(1.12F);
        e.setColorStarInner(255, 255, 255);
        e.setColorStarOuter(255, -255, -255);
		e.setRenderBeam(true);
		
    }

    public void onInit(EntityDust e)
    {
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.coal.shiftedIndex, 2, -1)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || !takeXP(e, 2))
        {
            e.fizzle();
            return;
        }

        e.setStarScale(1.12F);
        e.setColorStarInner(255, 255, 255);
        e.setColorStarOuter(255, -255, -255);
		e.setRenderBeam(true);
        //entityplayersp.addPotionEffect(new PotionEffect(Potion.regeneration.id, 3, 2));
    }

    public void onTick(EntityDust e)
    {
    	if(e.ticksExisted == 0){


            int dustID = e.dustID;
            int healMul = 1;
            int healDurBase = 0;

            switch (dustID)
            {
                case 100:
                    healMul = 1;
                    healDurBase = 4; //3 hearts
                    break;

                case 200:
                    healMul = 2;
                    healDurBase = 5; //n-2 hearts
                    break;

                case 300:
                    healMul = 2;
                    healDurBase = 10;
                    break;

                case 400:
                    healMul = 5;
                    healDurBase = 32;
                    break;
            }

            List<Entity> ents = getEntities(e, 5D);

            for (Entity i: ents)
            {
                if (i instanceof EntityLiving)
                {
                    EntityLiving l = (EntityLiving)i;
//                    System.out.println("DURR heal");
                    l.addPotionEffect(new PotionEffect(Potion.regeneration.id, healDurBase * 20, healMul));
                }

                if (i instanceof EntityPlayer)
                {
                    EntityPlayer p = (EntityPlayer)i;

                    if (dustID == 3)
                    {
                        p.getFoodStats().addStats(5, 0.6F);
                    }
                    else if (dustID == 4)
                    {
                        p.getFoodStats().addStats(8, 0.8F);
                    }
                }
            }
    	}
        if (e.ticksExisted > 100)
        {
            e.fade();
        }
    }
}
