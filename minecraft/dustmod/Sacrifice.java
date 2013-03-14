/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

/**
 *
 * @author billythegoat101
 */
public class Sacrifice
{
    public ItemStack itemType;
    public int entityType = -1;
    public boolean isComplete = false;
    public Entity entity;
    public Sacrifice(ItemStack item)
    {
        this.itemType = item;
    }
    public Sacrifice(EntityLiving entity)
    {
        this.entityType = EntityList.getEntityID(entity);
    }
    public Sacrifice(int ent)
    {
        this.entityType = ent;
    }
    public Sacrifice(String entityName)
    {
        Entity ent = EntityList.createEntityByName(entityName, null);
        this.entityType = EntityList.getEntityID(ent);
    }

    public boolean handleObject(EntityDust e, Entity ent)
    {
        if (ent == null)
        {
            return isComplete;
        }

        if (ent instanceof EntityItem)
        {
            ItemStack in = ((EntityItem)ent).getEntityItem();

            if (in.itemID == itemType.itemID && itemType.stackSize > 0 &&
                    (in.getItemDamage() == itemType.getItemDamage() || itemType.getItemDamage() == -1))
            {
                int amt = in.stackSize;
                in.stackSize -= itemType.stackSize;

                if (in.stackSize <= 0)
                {
                    ent.setDead();
                }

                itemType.stackSize -= amt;

                if (itemType.stackSize < 0)
                {
                    itemType.stackSize = 0;
                }

                e.data[15] = in.itemID;
                ((EntityItem)ent).setEntityItemStack(in);
                return true;
            }
        }
        else if (ent instanceof EntityLiving)
        {
            int id = EntityList.getEntityID(ent);

            if (id == entityType)
            {
                e.data[15] = id;
//                System.out.println("Got entity " + id);
//                mod_DustMod.killEntity(ent);
//                ent.setDead();
                EntityLiving el = (EntityLiving)ent;
                el.attackEntityFrom(DamageSource.magic, el.getHealth() * 10);
                return true;
            }
        }

        return false;
    }

    public boolean matchObject(Entity ent)
    {
        if (ent == null)
        {
            return false;
        }

        if (ent instanceof EntityItem && itemType != null)
        {
            ItemStack in = ((EntityItem)ent).getEntityItem();

            if (in.itemID == itemType.itemID &&
                    (in.getItemDamage() == itemType.getItemDamage() || itemType.getItemDamage() == -1))
            {
                return true;
            }
        }
        else if (ent instanceof EntityLiving && entityType != -1)
        {
//            System.out.println("dicks " + ent);
            int id = EntityList.getEntityID(ent);

            if (id == entityType)
            {
//                System.out.println("Golden");
                return true;
            }
        }

        return false;
    }

    public Sacrifice clone()
    {
        Sacrifice rtn = null;

        if (itemType != null)
        {
            rtn = new Sacrifice(new ItemStack(itemType.itemID, itemType.stackSize, itemType.getItemDamage()));
        }
        else
        {
            rtn = new Sacrifice(entityType);
        }

        return rtn;
    }
}
