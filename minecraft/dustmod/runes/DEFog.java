/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import dustmod.EntityDust;
import dustmod.PoweredEvent;
import dustmod.TileEntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEFog extends PoweredEvent
{
    public DEFog()
    {
        super();
    }

    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.bucketWater, 1), new ItemStack(Block.mushroomRed, 1)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || !takeXP(e, 6))
        {
            e.fizzle();
            return;
        }
    }

    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);

        if (e.ticksExisted % 5 == 0)
        {
            int amt = 40;
            int radius = 10;
            int cycle = 60;
            float maxAlpha = 1.5F;
            float minAlpha = 0.4F;
            float alpha = 0;
            float diff = maxAlpha - minAlpha;
            int flip = (e.ticksExisted % (cycle * 2) > cycle) ? 1 : -1;
            int stage = e.ticksExisted % cycle - cycle / 2;
            stage *= flip;
            float percent = ((float)stage / (float)(cycle));
            alpha = percent * diff + minAlpha + diff / 2F;
//            for(int i = 0; i < amt; i++){
//                int rx = (int)(Math.random()*radius*2)-radius;
//                int ry = (int)(Math.random()*radius+3)-3;
//                int rz = (int)(Math.random()*radius*2)-radius;
//                int x = e.getX() + rx;
//                int y = e.getY() + ry;
//                int z = e.getZ() + rz;
//
//                Block block = Block.blocksList[e.worldObj.getBlockId(x, y, z)];
//                int light = e.worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z);
//                if (light < 7 && (block == null || !block.isOpaqueCube()))
//                {
//                    mod_DustMod.spawnNewFogFX(e.worldObj, x, y, z, 0.85F + (float)Math.random() * 0.2F);
//                }
//            }
//            System.out.println("Alpha" + alpha + " \tPercent" + percent + " \tStage" + stage);

            if (e.ticksExisted % cycle == 0 && !e.worldObj.isRemote)
            {
//                System.out.println("Turn " + alpha);
                List ents = this.getEntities(e, radius);
                List ents2 = ents.subList(0, ents.size());
                EntityPlayer player = e.worldObj.getClosestPlayerToEntity(e, radius);

                for (Object o: ents)
                {
                    Entity i = (Entity)o;
                    int x = MathHelper.floor_double(i.posX);
                    int y = MathHelper.floor_double(i.posY);
                    int z = MathHelper.floor_double(i.posZ);
                    int light = e.worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z);
                    if (light >= 7)
                    {
                        System.out.println("Err light");
                        continue;
                    }

                    if (i instanceof EntityCreature)
                    {
                        EntityCreature ec = (EntityCreature)i;
//                        if(ec.getAttackTarget() != null){
//                            Entity target = (Entity)ents2.get((int)(Math.random()*(double)ents2.size()));
//                            if(target instanceof EntityLiving && target != ec && !(target instanceof EntityPlayer)){
//                                ec.field_704_R = 1.5F;
//                                ec.attackedAtYaw = 0;
                        ec.setRevengeTarget(ec);
                        ec.setAttackTarget(ec);
                        ec.setAttackTarget(ec);
                        ec.setPathToEntity(null);
//                        DustModBouncer.setHasAttacked(ec, true);
                        ec.attackTime = 30;

                        if (player != null)
                        {
//                        	DustModBouncer.setCantSee(ec, player);
                        }

//                                ec.worldObj.setEntityState(ec, (byte)2);
////                                ec.setBeenAttacked();
//                                ec.velocityChanged = true;
//                                System.out.println("SETTING");
//                                mod_DustMod.updateActionState(ec);
//                                ec.attackEntityFrom(DamageSource.causeMobDamage((EntityLiving)target),0);
//                            }
//                            System.out.println("Retarget " + ec.getAITarget());
//                        }
//                        }
                    }

                    if (i instanceof EntityLiving && Math.random() < 0.8D)
                    {
                        EntityLiving el = (EntityLiving)o;
//                        if(!(el instanceof EntityPlayer))
                        el.setPositionAndRotation(el.posX, el.posY, el.posZ, (float)Math.random() * 360F, el.rotationPitch);
//                        System.out.println("Spinning");
                    }
                }
            }
        }
    }

    @Override
    public void onRightClick(EntityDust e, TileEntityDust ted, EntityPlayer p)
    {
        super.onRightClick(e, ted, p);
    }

    @Override
    public void onUnload(EntityDust e)
    {
        super.onUnload(e);
    }

    @Override
    public int getStartFuel()
    {
        return dayLength;
    }

    @Override
    public int getMaxFuel()
    {
        return dayLength * 7;
    }

    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return dayLength;
    }

    @Override
    public boolean isPaused(EntityDust e)
    {
        return false;
    }
}
