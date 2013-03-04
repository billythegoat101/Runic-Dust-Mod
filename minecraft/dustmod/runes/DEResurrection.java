/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEResurrection extends DustEvent
{
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderStar(true);
		e.setRenderBeam(true);
		
    }
	
    public void onInit(EntityDust e)
    {
		e.setRenderStar(true);
		e.setRenderBeam(true);
        ItemStack[] sac = new ItemStack[] {new ItemStack(Block.slowSand, 4)};
        sac = this.sacrifice(e, sac);

        if (!checkSacrifice(sac))
        {
            e.fizzle();
            return;
        }

        //get sacrifice
        ArrayList<EntityItem> itemstacks = new ArrayList<EntityItem>();
        List l = getEntities(e);

        for (Object o: l)
        {
            if (o instanceof EntityItem)
            {
                EntityItem ei = (EntityItem)o;
                itemstacks.add(ei);
            }
        }

        if (itemstacks.size() == 0)
        {
            e.kill();
            return;
        }

        int entClass = -1;

        for (EntityItem ei: itemstacks)
        {
            if (entClass != -1)
            {
                break;
            }

            int id = ei.getEntityItem().itemID;
            int m = ei.getEntityItem().getItemDamage();
            int amount;
            int amt = amount = 2;

            for (EntityItem ent: itemstacks)
            {
                if (ent.getEntityItem().itemID == id && ent.getEntityItem().getItemDamage() == m)
                {
                    amount -= ent.getEntityItem().stackSize;
                }
            }

            if (amount <= 0 && DustMod.getEntityIDFromDrop(new ItemStack(id, 0, m), 0) != -1)
            {
                for (EntityItem ent: itemstacks)
                {
                    if (ent.getEntityItem().itemID == id && ent.getEntityItem().getItemDamage() == m)
                    {
                        while (amt > 0 && ent.getEntityItem().stackSize > 0)
                        {
                            amt--;
                            ItemStack item = ent.getEntityItem();
                            item.stackSize--;
                            
                            if (item.stackSize <= 0)
                            {
                                ent.setDead();
                            }else{
                            	ent.func_92058_a(item);
                            }
                        }
                    }
                }

                entClass = DustMod.getEntityIDFromDrop(new ItemStack(id, 0, m), 0);

                if (entClass != -1)
                {
                    break;
                }
            }
        }

        if (entClass == -1)
        {
            e.fizzle();
            return;
        }

        e.data[0] = (byte)entClass;
        EntitySkeleton test;
    }

    public void onTick(EntityDust e)
    {
        e.setStarScale(e.getStarScale() + 0.001F);

        if (e.ticksExisted > 120)
        {
            Entity en = null;
            en = EntityList.createEntityByID((int)e.data[0], e.worldObj);

            if (en != null)
            {
                en.setPosition(e.posX, e.posY - EntityDust.yOffset, e.posZ);
                boolean blah = e.worldObj.spawnEntityInWorld(en);
            }

            e.fade();
        }
    }
}