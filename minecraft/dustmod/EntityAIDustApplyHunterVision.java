/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 *
 * @author billythegoat101
 */
public class EntityAIDustApplyHunterVision extends EntityAIBase
{
    private EntityCreature theEntity;
    private EntityPlayer player;
//    private float field_48331_g;

    public EntityAIDustApplyHunterVision(EntityCreature par1EntityCreature)
    {
        theEntity = par1EntityCreature;
//        player = ModLoader.getMinecraftInstance().thePlayer;
//        field_48331_g = par3;
        setMutexBits(0);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
//        int ticks = theEntity.ticksExisted;
//        double dist = theEntity.getDistanceSqToEntity(theEntity);
//        double maxDist = 64D;
//       return (mod_DustMod.hunterVisionActive && ticks%15 <= 0/* && ticks%3 == 0*/ /*&& dist < maxDist*maxDist*/);
        return false;
    }

    @Override
    public void updateTask()
    {
        super.updateTask();
//        theEntity.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, speed);
//        int ticks = theEntity.ticksExisted;
//        if(mod_DustMod.hunterVisionActive && ticks%60 <= 21 && ticks%3 == 0/* &&
//            theEntity.worldObj.getClosestPlayerToEntity(theEntity, 64D) != null*/){
//            for(int i = 0; i < 2; i++)
//                mod_DustMod.spawnNewHunterFX(theEntity);
//        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
//        for(int i = 0; i < 5; i++)
//        Profiler.startSection("dust:hunter");
//            mod_DustMod.spawnNewHunterFX(theEntity,5);
//            Profiler.endSection();
        return false;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    }
}
