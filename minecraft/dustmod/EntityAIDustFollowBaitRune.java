/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

/**
 *
 * @author billythegoat101
 */
public class EntityAIDustFollowBaitRune extends EntityAIBase
{
    private EntityCreature theEntity;
    private EntityDust dust;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private float speed;
//    private float field_48331_g;

    public EntityAIDustFollowBaitRune(EntityCreature par1EntityCreature, float speed)
    {
        theEntity = par1EntityCreature;
        this.speed = speed;
//        field_48331_g = par3;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        Entity target = theEntity.getAttackTarget();

        if (target == null)
        {
            return false;
        }

        if (target instanceof EntityDust)
        {
            dust = (EntityDust)target;
            movePosX = dust.getX();
            movePosY = dust.getY();
            movePosZ = dust.getZ();
            return true;
        }

        return false;
//        if (targetEntity.getDistanceSqToEntity(theEntity) > (double)(field_48331_g * field_48331_g))
//        {
//            return false;
//        }
//
//        Vec3D vec3d = RandomPositionGenerator.func_48620_a(theEntity, 16, 7, Vec3D.createVector(targetEntity.posX, targetEntity.posY, targetEntity.posZ));
//
//        if (vec3d == null)
//        {
//            return false;
//        }
//        else
//        {
//            movePosX = vec3d.xCoord;
//            movePosY = vec3d.yCoord;
//            movePosZ = vec3d.zCoord;
//            return true;
//        }
    }

    @Override
    public void updateTask()
    {
        super.updateTask();
        
        if(!continueExecuting()){
        	theEntity.getNavigator().clearPathEntity();
        	theEntity.tasks.taskEntries.remove(this);
        	return;
        }
        
        theEntity.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, speed);
        
        if(Math.random() < 0.2){
        	DustMod.spawnParticles(theEntity.worldObj, "smoke", theEntity.posX, theEntity.posY+theEntity.height/2, theEntity.posZ,
        			0, Math.random() * 0.05, 0, (int)(Math.random()*20), 0.75, theEntity.height/2, 0.75);
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return (dust != null && !dust.isDead);
//    	return false;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        dust = null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        theEntity.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, speed);
    }
}
