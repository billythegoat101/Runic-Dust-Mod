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
import dustmod.DustEvent;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEBounce extends DustEvent
{
    public DEBounce()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);
    	
    	
    }

    public void onInit(EntityDust e)
    {
        ItemStack[] req = new ItemStack[]
        {
            new ItemStack(Item.slimeBall, 4, -1)
        };
        sacrifice(e, req);

        if (req[0].stackSize > 0)
        {
            e.fizzle();
            return;
        }

//        e.renderFlamesDust = true;
    }

    public void onTick(EntityDust e)
    {
        List<Entity> entities = this.getEntities(e, 0.35D);
//        System.out.println("Dicks " + entities.size());
        for (Entity i: entities)
        {
            if (i instanceof EntityLiving)
            {
                EntityLiving el = (EntityLiving)i;
                double cons = 0;//0.0784000015258789;
                double yVel = i.motionY + cons;
                double diff = e.posY - el.prevPosY;
//                System.out.println("i.motion " + mod_DustMod.getMoveForward(el) + " " + mod_DustMod.getMoveSpeed(el));
                if (!el.onGround && !el.isJumping && yVel < 0.7D)
                {
                    el.getJumpHelper().setJumping();
                    el.getJumpHelper().doJump();
//                    el.setMoveForward(mod_DustMod.getMoveSpeed(el)*2);
//                    i.motionY = 1D;
//                    i.motionX *= 2;
//                    i.motionZ *= 2;
                    i.addVelocity(0, 1.27D, 0);
//                    System.out.println("Launch " + " " + (i.motionY + cons) + " " + i.motionX + " " + i.motionZ);
                    i.velocityChanged = true;
                }
                if (!el.onGround /*i.motionY < 0*/)
                {
                    i.fallDistance = 0;
                }else{
                    el.setJumping(false);
                }
            }
        }
        entities = this.getEntities(e, 3D);

        for (Entity i: entities)
        {
            if (i instanceof EntityLiving)
            {
                EntityLiving el = (EntityLiving)i;
                if (!el.onGround /*i.motionY < 0*/)
                {
                    i.fallDistance = 0;
                }else{
                    el.setJumping(false);
                }
            }
        }
    }
}
