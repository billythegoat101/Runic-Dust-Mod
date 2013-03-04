/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import dustmod.DustEvent;
import dustmod.EntityBlock;
import dustmod.EntityDust;
import dustmod.VoidTeleManager;

/**
 *
 * @author billythegoat101
 */
public class DEMiniTele extends DustEvent
{
    public DEMiniTele()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        e.setRenderStar(true);
		
    }

    public void onInit(EntityDust e)
    {
        World world = e.worldObj;

//        ItemStack[] req = this.sacrifice(e, new ItemStack[]{new ItemStack(Block.obsidian, 2)});
//        if (req[0].stackSize != 0 ) {
//            e.fizzle();
//            return;
//        }

        if (!takeXP(e, 5))
        {
            e.fizzle();
            return;
        }

//        Integer[] fnd = null;
//
//        for (Integer[] i : e.dustPoints)
//        {
//            TileEntity te = world.getBlockTileEntity(i[0], i[1], i[2]);
//
//            if (te != null && te instanceof TileEntityDust)
//            {
//                TileEntityDust ted = (TileEntityDust) te;
//                int gamt = 8;
//                int bamt = 6;
//
////                System.out.println("CHECKING");
//                for (int x = 0; x < 4; x++)
//                {
//                    for (int y = 0; y < 4; y++)
//                    {
////                        System.out.print(ted.getDust(x, y) + ",");
//                        if (ted.getDust(x, y) == 2)
//                        {
//                            gamt--;
//                        }
//
//                        if (ted.getDust(x, y) == 4)
//                        {
//                            bamt--;
//                        }
//                    }
//
////                    System.out.println();
//                }
//
//                if (gamt == 0 && bamt == 0)
//                {
//                    fnd = i;
//                    e.data[0] = world.getBlockId(i[0], i[1] - 1, i[2]);
//                    world.setBlockWithNotify(i[0],i[1]-1, i[2], Block.brick.blockID);
//                    System.out.println("Warp ID set to " + e.data[0] + " " + Block.blocksList[e.data[0]].getBlockName());
//                }
//            }
//            else
//            {
////                System.out.println("dewrp");
//            }
//        }
        
        int cx, cy, cz;
        cx = (int)e.posX - (e.posX<0?1:0);
        cy = (int)e.posY - 1;
        cz = (int)e.posZ - (e.posZ<0?1:0);
//        world.setBlockWithNotify(cx,cy,cz, Block.brick.blockID);
        e.data[0] = world.getBlockId(cx,cy,cz);
//        for (int x = -1; x <= 1 && fnd != null; x++) {
//            for (int z = -1; z <= 1; z++) {
//                if (x == 0 || z == 0) {
//                    if (world.getBlockId(fnd[0] + x, fnd[1], fnd[2] + z) == mod_DustMod.dust.blockID) {
//                        TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(x + fnd[0], fnd[1], fnd[2] + z);
//                        int gamt = 4;
//                        int bamt = 4;
//                        for (int i = 0; i < 4; i++) {
//                            for (int j = 0; j < 4; j++) {
//                                if (ted.getDust(i, j) == 2) {
//                                    gamt--;
//                                }
//                                if (ted.getDust(i, j) == 4) {
//                                    bamt--;
//                                }
//                            }
//                        }
//                        if (gamt == 0 && bamt == 0) {
//                            e.posX = (fnd[0] + x) + 0.5D;
//                            e.posY = (fnd[1]) + 1.5D + EntityDust.yOffset;
//                            e.posZ = (fnd[2] + z) + 0.5D;
//                            if (x == -1) {
//                                e.rotationYaw = 270;
//                            } else if (x == 1) {
//                                e.rotationYaw = 90;
//                            } else if (z == -1) {
//                                e.rotationYaw = 0;
//                            } else if (z == 1) {
//                                e.rotationYaw = 180;
//                            }
//                        }
//                    }
//                }
//            }
//        }
        e.setRenderStar(true);
//        e.starScaleY = 2F;
    }

    public void onTick(EntityDust e)
    {
//        System.out.println("ENTITY ID " + e.entityId);
//        System.out.println("wtf ram:" + e.ram);
        int[] warp = VoidTeleManager.toWarp(e);
//        mod_DustMod.addWarp(warp);
//        if (e.ram == 0) {
//            mod_DustMod.addWarp(warp);
//            e.ram = 1;
//        } else if (e.ram > 1) {
//            e.ram--;
//            e.setColorOuter(255, 0, 0);
//            e.setColorInner(255, 0, 0);
//        } else {
//            e.setColorInner(255, 255, 255);
//            e.setColorOuter(255, 255, 255);
//        }
        List<Entity> ents = this.getEntities(e, 10D);

//        System.out.println("DURR " + e.worldObj.worldProvider.worldType + " " + ents.size());
//        if(e.ram > 1 && mod_DustMod.skipWarpTick > 0) {
//            mod_DustMod.skipWarpTick --;
//        }
//        if (e.ram == 1) {
//            if (ents.size() > 1) {
////                System.out.println("potato " + ents.size());
//                mod_DustMod.skipWarpTick--;
//            }
        for (Object o : ents.toArray())
        {
            Entity i = (Entity) o;

            if (i instanceof EntityBlock)
            {
                if (((EntityBlock)i).hasParent())
                {
                    continue;
                }
            }

            double dx = i.posX - e.getX();
            double dy = i.posY - e.getY();
            double dz = i.posZ - e.getZ();
//                dx *= (dx < 0) ? -1 : 1;
//                dz *= (dz < 0) ? -1 : 1;
//                dy *= (dy < 0) ? -1 : 1;
            double tol = 3.2D;

            if (!(i instanceof EntityDust) && Math.abs(dx) < tol && Math.abs(dz) < tol && Math.abs(dy) < 3D/*i instanceof EntityLiving && e.getDistanceToEntity(i) <= 0.5F*/)
            {
//                    System.out.println("Entity found " + e.getDistanceToEntity(i));
//                    EntityLiving ei = (EntityLiving)i;
                int index = 0;//mod_DustMod.getVoidNetworkIndex(warp);

//                    for(Object o:(ArrayList<EntityDust>)(warps.clone())){
//                        if(((EntityDust)o).isDead) warps.remove(o);
//                    }
                if (i instanceof EntityPlayer && ((EntityPlayer) i).timeUntilPortal < 300)
                {
                    e.ram = 100;
                }

                stopWarp:

                for (int temp = index + 1; temp != index && index != -1; temp++)
                {
                    if (temp >= VoidTeleManager.voidNetwork.size())
                    {
                        temp = 0;
                    }

//                        System.out.println("Dicks :" + temp + " " + index);
                    if (temp == index)
                    {
                        break stopWarp;
                    }

                    int[] iwarp = VoidTeleManager.voidNetwork.get(temp);

//                        System.out.println("Found warp: " + warp[3] + ":" + warp[4] + " " + iwarp[3] + ":" + iwarp[4] + " dim:" + iwarp[6] + " ver:" + iwarp[7]);
                    if ((Math.abs(warp[0] - iwarp[0]) < 0.5D && Math.abs(warp[1] - iwarp[1]) < 0.5D && Math.abs(warp[2] - iwarp[2]) < 0.5D) || iwarp[6] != i.worldObj.provider.dimensionId || iwarp[7] != warp[7])
                    {
//                            System.out.println("Skipping dead:" + ed.isDead);
                        continue;
                    }

                    //if(ed != null && ed.data == e.data[0] && !ed.equals(e) && ed != e && ed.ram == 1){
                    if (warp[3] == iwarp[3] && warp[4] == iwarp[4])
                    {
//                            System.out.println("Found warp location " + Arrays.toString(iwarp) + " " + Arrays.toString(warp));
                        if (VoidTeleManager.skipWarpTick > 0)
                        {
//                                mod_DustMod.skipWarpTick -- ;
//                                e.ram = 100;
//                                System.out.println("Skipping due to recent tele " + mod_DustMod.skipWarpTick);
                            break stopWarp;
                        }

                        if (i instanceof EntityLiving)
                        {
                            ((EntityLiving) i).setPositionAndUpdate(iwarp[0] + 0.5D, iwarp[1] + 0.6D, iwarp[2]  + 0.5D);
                        }
                        else
                        {
                            i.setPosition(iwarp[0] + 0.5D, iwarp[1] + 0.6D, iwarp[2] + 0.5D);
                        }

                        i.posX = iwarp[0] + 0.5D;
                        i.posY = iwarp[1] + 0.6D;
                        i.posZ = iwarp[2] + 0.5D;
                        i.rotationYaw = iwarp[5];
//                            System.out.println("Sending to dimension " + i.worldObj.worldProvider.worldType);
//                            System.out.println("new loc " + i.posX + " " + i.posY + " " + i.posZ);
//                            System.out.println("DELTA " + dx + " " + dy + " " + dz);
//                            e.ram = 100;
                        EntityDust enWarp = VoidTeleManager.getWarpEntity(iwarp, e.worldObj);

//                            if (enWarp != null) {
//                                enWarp.ram = 100;
//                            } else {
//                                System.out.println("Bad ram");
//                            }
                        if (i instanceof EntityPlayer)
                        {
                            ((EntityPlayer) i).timeUntilPortal = 200;
                        }

                        VoidTeleManager.skipWarpTick = 10;
                        break stopWarp;
                    }
                }
            }
            else
            {
//                    System.out.println("Derp? " + (!(i instanceof EntityDust)) + " " + e.getDistanceToEntity(i) + " " + dx + " " + dz + " " + dy);
            }
        }

        if (e.ticksExisted > 100)
        {
            e.setColorStarOuter(255, 0, 0);
            e.setColorStarInner(255, 0, 0);
            e.fade();
        }

//        }
    }

    public void onUnload(EntityDust e)
    {
//        System.out.println("KILL");
//        mod_DustMod.removeWarp(mod_DustMod.toWarp(e));
    }
}
