/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.DustModEntityBouncer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.EntityAIDustFollowBaitRune;
import dustmod.EntityDust;
import dustmod.PoweredEvent;

/**
 *
 * @author billythegoat101
 */
public class DEBait extends PoweredEvent
{
    public DEBait()
    {
        super();
    }

    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        e.setRenderStar(true);
        e.setStarScale(1.005F);
        e.setColorStarOuter(255, 1, 1);
    	
    	
    }
    
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        int entClass = -1;
        List l = getEntities(e);

        for (Object o: l)
        {
            if (o instanceof EntityItem)
            {
                EntityItem ei = (EntityItem)o;
                ItemStack item = ei.func_92014_d();

                if (item.itemID == Item.monsterPlacer.shiftedIndex)
                {
                    entClass = item.getItemDamage();
                    item.stackSize--;

                    if (item.stackSize <= 0)
                    {
                        ei.setDead();
                    }
                }
                ei.func_92013_a(item);
            }
        }

        ItemStack[] req = new ItemStack[] {new ItemStack(Block.blockGold, 1)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || entClass == -1 || !takeXP(e, 5))
        {
            e.fizzle();
            return;
        }

        e.data[0] = entClass;
        e.setStarScale(1.005F);
        e.setColorStarOuter(255, 1, 1);
    }

    public void onTick(EntityDust e)
    {
//        e.starScale += 0.001;
        super.onTick(e);
        List<Entity> bait = getEntities(e, 16D);

//        System.out.println("DATA " + e.data[0]);
        for (Entity k: bait)
        {
//                if(k instanceof EntityCreature) System.out.println("ENT " + EntityList.getEntityID(k));
            if (k instanceof EntityCreature && EntityList.getEntityID(k) == e.data[0])
            {
                EntityCreature el = (EntityCreature)k;

//                System.out.println("Found entity " + mod_DustMod.isAIEnabled(el));
//                if (!DustModBouncer.isAIEnabled(el))
//                {
//                    el.posY += 1;
//                    el.setPathToEntity(null);//e.worldObj.getPathToEntity(el, e, 16F));
//                    el.setTarget(e);
//                    el.setPathToEntity(e.worldObj.getEntityPathToXYZ(el, e.getX(), e.getY(), e.getZ(), 10F, true, false, false, true));
//                    DustModBouncer.updateState(el);
//                    el.motionY += 0.015;
//                    EntityLookHelper elh = el.getLookHelper();//func_46008_aG();
//                    elh.setLookPositionWithEntity(e, 0, 1);//func_46141_a(e, 1, 1);
//                    el.setMoveForward(16F);
//                    el.velocityChanged = true;
//                    DustModBouncer.setEntityToAttack(el, e);
//                    el.setHomeArea(e.getX(), e.getY(), e.getZ(), 0);
//                    DustModBouncer.setEntityToAttack(el, e);
//                    
//
//                    if(Math.random() < 0.2){
//                    	DustMod.spawnParticles(el.worldObj, "smoke", el.posX, el.posY+el.height/2, el.posZ,
//                    			0, Math.random() * 0.05, 0, (int)(Math.random()*20), 0.75, el.height/2, 0.75);
//                    }
//                    
//                }
//                else
//                {

                    if (!DustModEntityBouncer.hasAITask(el, new EntityAIDustFollowBaitRune(null, 0F)))
                    {
                    	DustModEntityBouncer.addAITask(el, new EntityAIDustFollowBaitRune(el, 0.22F), -1);
//                        System.out.println("Adding ai task");
                    }
//                }
            }
        }
    }

    public static int getEntity(ItemStack is)
    {
//        System.out.println("CHECK " + is.itemID + " " + is.stackSize + " " + is.getItemDamage());
        for (ItemStack i: entdrops.keySet())
        {
//            System.out.println("grr " + i.itemID + " " + i.stackSize + " " + i.getItemDamage());
            if (i.itemID == is.itemID && i.getItemDamage() == is.getItemDamage())
            {
//                System.out.println("ent found");
                return entdrops.get(i);
            }
        }

//        System.out.println("ent not found");
        return -1;
    }

    public static HashMap<ItemStack, Integer> entdrops = new HashMap<ItemStack, Integer>();

    static
    {
        entdrops.put(new ItemStack(Item.porkRaw.shiftedIndex, 0, 0), 90);
        entdrops.put(new ItemStack(Item.beefRaw.shiftedIndex, 0, 0), 92);
        entdrops.put(new ItemStack(Item.chickenRaw.shiftedIndex, 0, 0), 93);
        entdrops.put(new ItemStack(Item.dyePowder.shiftedIndex, 0, 0), 94);
        entdrops.put(new ItemStack(Item.leather.shiftedIndex, 0, 0), 95);
        entdrops.put(new ItemStack(Block.mushroomRed.blockID, 0, 0), 96);
        entdrops.put(new ItemStack(Block.pumpkin.blockID, 0, 0), 97);
        entdrops.put(new ItemStack(Item.porkCooked.shiftedIndex, 0, 0), 57);
        entdrops.put(new ItemStack(Item.gunpowder.shiftedIndex, 0, 0), 50);
        entdrops.put(new ItemStack(Item.bone.shiftedIndex, 0, 0), 51);
        entdrops.put(new ItemStack(Item.silk.shiftedIndex, 0, 0), 52);
        entdrops.put(new ItemStack(Item.rottenFlesh.shiftedIndex, 0, 0), 54);
        entdrops.put(new ItemStack(Item.slimeBall.shiftedIndex, 0, 0), 55);
        entdrops.put(new ItemStack(Item.ghastTear.shiftedIndex, 0, 0), 56);
        entdrops.put(new ItemStack(Item.enderPearl.shiftedIndex, 0, 0), 58);
        entdrops.put(new ItemStack(Item.spiderEye.shiftedIndex, 0, 0), 59);
        entdrops.put(new ItemStack(Block.stoneBrick.blockID, 0, 0), 60);
        entdrops.put(new ItemStack(Item.blazeRod.shiftedIndex, 0, 0), 61);
        entdrops.put(new ItemStack(Item.magmaCream.shiftedIndex, 0, 0), 62);
        //entdrops.put(new ItemStack(Item.eyeOfEnder.shiftedIndex, 0, 0), 63);
    }

    @Override
    public int getStartFuel()
    {
        return dayLength * 7;
    }

    @Override
    public int getMaxFuel()
    {
        return dayLength * 7;
    }

    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return dayLength * 5;
    }

    @Override
    public boolean isPaused(EntityDust e)
    {
        return false;
    }
}
