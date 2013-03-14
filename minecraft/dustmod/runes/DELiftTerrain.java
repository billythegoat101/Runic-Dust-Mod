/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dustmod.DustEvent;
import dustmod.EntityDust;
import dustmod.Sacrifice;
import dustmod.TileEntityRut;

/**
 *
 * @author billythegoat101
 */
public class DELiftTerrain extends DustEvent
{
    public static final int ticksperblock = 32;

    public DELiftTerrain()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderBeam(true);
        e.setColorStar(255, 255, 0);
        e.setColorFire(0,0,255);
		e.setRenderFireOnRuts(true);
		
    }

    public void onInit(EntityDust e)
    {
        ItemStack[] req = new ItemStack[] {new ItemStack(Block.plantRed, 1)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || !takeXP(e, 10))
        {
            e.fizzle();
            return;
        }

		e.setRenderBeam(true);
        e.setColorStar(255, 255, 0);
        int a, b, c, d;

        if (e.dusts.length > e.dusts[0].length)
        {
            a = e.dusts[7][3];
            b = e.dusts[8][3];
            c = e.dusts[8][4];
            d = e.dusts[7][4];
        }
        else
        {
            a = e.dusts[3][7];
            b = e.dusts[3][8];
            c = e.dusts[4][8];
            d = e.dusts[4][7];
        }

        int dustStrength = a;

        if (a != b || b != c || c != d)
        {
            e.fizzle();
            return;
        }

        switch (dustStrength)
        {
            case 100:
                e.data[1] = 12;
                break;

            case 200:
                e.data[1] = 16;
                break;

            case 300:
                e.data[1] = 22;
                break;

            case 400:
                e.data[1] = 32;
                break;
        }
//        List<Entity> ents = this.getEntities(e, 3D);
//        boolean found = false;
//        for (Entity i : ents) {
//            if (i instanceof EntityPig || i instanceof EntitySheep || i instanceof EntityCow) {
//                found = true;
//                ((EntityLiving) i).attackEntityFrom(DamageSource.magic, 5000);
//                mod_DustMod.killEntity(i);
//                break;
//            }
//        }
//        if (!found) {
//            e.fizzle();
//            return;
//        }
        e.sacrificeWaiting = 600;
        this.addSacrificeList(new Sacrifice(99));
        loadArea(e);

		e.setRenderFireOnRuts(true);
        e.setColorFire(0,0,255);
//        e.fade();
    }

    public void onTick(EntityDust e)
    {
//        e.fade();
        e.setColorStar(255, 255, 255);

        if (e.ticksExisted < ticksperblock * 2)
        {
            return;
        }

        if (e.rutAreaPoints == null)
        {
            e.fade();
        }

        World world = e.worldObj;
        int x = (int) e.getX();
        int y = (int) e.getY();
        int z = (int) e.getZ();
        int height = e.data[1];

        if (e.ticksExisted % ticksperblock == 0 && e.data[2] <= height)
        {
//            int c = e.bb - e.gb;
            for (Integer[] i : e.rutAreaPoints)
            {
                x = i[0];
                y = i[1];
                z = i[2];
//                List<Entity> ents = getEntities(e.worldObj, (double) x + 0.5D, (double) y + (double) e.gb + 2D, (double) z + 0.5D, 1D);
//                for (Entity ie : ents) {
//                    if(ie instanceof EntityItem) System.out.println("dicks, things have been dropped");;
//                    double dx = ie.posX - ((double) x + 0.5D);
//                    double dz = ie.posZ - ((double) z + 0.5D);
////                    System.out.println("delta " + dx + " " + dz + " " + e.posX + " " + e.posZ + " " + (((double)x)+0.5D) + " " + (((double)z)+0.5D));
//                    if (Math.abs(dx) < 0.5D && Math.abs(dz) < 0.5D) {
//                        ie.setPosition(Math.floor(ie.posX) + 0.5D, (double) y + (double)e.gb + 3D, Math.floor(ie.posZ) + 0.5D);
//                    }
//                }

                for (int t = -height; t <= height; t++)
                {
                    int c = -t + e.data[2] - 1;

                    if (y + c <= 0)
                    {
                        e.fade();
                        return;
                    }

                    if (t != height)
                    {
                        int b = world.getBlockId(x, y + c, z);
                        int m = world.getBlockMetadata(x, y + c, z);
                        int nb = world.getBlockId(x, y + c + 1, z);

//                        System.out.println("fuck it all " + nb + " " + b + " " + world.getBlockId(x,y+c+2,z));;
                        if (world.getBlockId(x, y + c + 2, z) == 0 && b != 0)
                        {
//                            System.out.println("GOOOOOOO");
                            List<Entity> ents = getEntities(e.worldObj, (double) x + 0.5D, (double) y + (double)c + 1D, (double) z + 0.5D, 1D);

                            for (Entity ie : ents)
                            {
                                if (ie == e)
                                {
                                    continue;
                                }

//                                System.out.println("DICKS " + ie);
                                //                        if(ie instanceof EntityItem) System.out.println("dicks, things have been dropped");;
                                //                        double dx = ie.posX - ((double) x + 0.5D);
                                //                        double dz = ie.posZ - ((double) z + 0.5D);
                                //                    System.out.println("delta " + dx + " " + dz + " " + e.posX + " " + e.posZ + " " + (((double)x)+0.5D) + " " + (((double)z)+0.5D));
                                //                        if (Math.abs(dx) < 0.5D && Math.abs(dz) < 0.5D) {
                                ie.setPosition(Math.floor(ie.posX) + 0.5D, (double) y + (double)c + 2D + ie.yOffset, Math.floor(ie.posZ) + 0.5D);
                                //                        }
                            }
                        }

                        Block B = Block.blocksList[b];
                        Block nB = Block.blocksList[nb];
                        boolean isContainer = false;
                        TileEntity te = null;
                        NBTTagCompound tag = null;

                        //                    System.out.println("lift " + b + " " + (B != null && B instanceof BlockContainer) + " " + (nB == null || !(nB instanceof BlockContainer)));
                        if ((B != null && B instanceof BlockContainer)/* && (nB == null || !(nB instanceof BlockContainer))*/)
                        {
//                            System.out.println("IS CONTAINER************************");
                            isContainer = true;
                            te = world.getBlockTileEntity(x, y + c, z);
                            tag = new NBTTagCompound();
                            te.writeToNBT(tag);
//                            world.removeBlockTileEntity(x, y+c, z);
                            te.invalidate();
                            //                        world.setBlockTileEntity(x,y+c,z,null);
                            //                        Chunk chunk = e.worldObj.getChunkFromBlockCoords(e.getX(), e.getZ());
                            //                        chunk.setChunkBlockTileEntity(x & 0xf, y-t+e.gb, z & 0xf, null);
                            //                        chunk.chunkTileEntityMap.put(new ChunkPosition(x&0xf,y+c,z&0xf), null);
//                            System.out.println("Rawr " + world.getBlockTileEntity(x,y+c,z));
                        }

                        //                    else if(nB != null && (nB instanceof BlockContainer)){
                        //                        System.out.println("Failure... " + b + " " + nb);
                        //                        e.fade();
                        //                        return;
                        //                    }
                        world.setBlockAndMetadataWithNotify(x, y + c + 1, z, 0, 0,3);
                        world.setBlockAndMetadataWithNotify(x, y + c + 1, z, b, m,3);
                        world.setBlockAndMetadataWithNotify(x, y + c, z, Block.stone.blockID,0,3);

                        if (isContainer)
                        {
                            TileEntity tet = world.getBlockTileEntity(x, y + c + 1, z);

//                            System.out.println("Fucker " + world.getBlockId(x, y + c + 1, z));
//                            System.out.println("grah " + Block.blocksList[world.getBlockId(x,y+c+1,z)].getBlockName());
                            if (tet != null)
                            {
                                tet.readFromNBT(tag);
                                tet.xCoord = x;
                                tet.yCoord = y + c + 1;
                                tet.zCoord = z;
                                tet.blockMetadata = m;
                            }

                            //                        te.validate();
//                            Chunk chunk = e.worldObj.getChunkFromBlockCoords(e.getX(), e.getZ());
//                            te.xCoord = x;
//                            te.yCoord = y+c+1;
//                            te.zCoord = z;
//
//    //                        world.removeBlockTileEntity(x,y+c+1,z);
//                            te.validate();
//                            world.setBlockTileEntity(x,y+c+1,z,te);
//                            chunk.setChunkBlockTileEntity(x & 0xf, y+c+1, z & 0xf, te);
//                            chunk.chunkTileEntityMap.put(new ChunkPosition(x&0xf,y+c+1,z&0xf), te);
//                            System.out.println("Validating " + (world.getBlockTileEntity(x,y+c+1,z)==te));
                        }

                        world.setBlockMetadataWithNotify(x, y + c + 1, z, m,3);
                    }
                    else
                    {
//                        if(world.getBlockTileEntity(x, y+c, z) != null)
//                            world.getBlockTileEntity(x, y+c, z).validate();
//                        world.notifyBlockChange(x, y+c, z, 0);
                    }

//                    TileEntity FUCKER = world.getBlockTileEntity(x,y+c,z);
//                    int BITCH = world.getBlockId(x,y+c,z);
//                    if(FUCKER != null){
//                        if(FUCKER.blockType.blockID == BITCH)
//                            System.out.println("DIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIICKS");
//                    }
                }
            }

            e.data[2]++;

//            System.out.println("Data " + e.gb + " " + height);
            if (e.data[2] >= height)
            {
                e.fade();
            }
        }
    }

    private void loadArea(EntityDust e)
    {
        this.findRutAreaFlat(e, Block.blockClay.blockID);
        int min = e.getY();
        int max = 0;

        for (int iter = 0; iter < e.rutAreaPoints.size(); iter++)
        {
            Integer[] i = e.rutAreaPoints.get(iter);
            int x = i[0];
            int z = i[1];
            int h = e.worldObj.getHeightValue(x, z);

            if (h < min)
            {
                min = h;
            }

            if (h > max)
            {
                max = h;
            }

//            i[1] = e.worldObj.getHeightValue(i[0], i[2]);
//            if(i[1] > max) max = i[1];
            e.rutAreaPoints.set(iter, new Integer[] {x, h, z});
//            e.worldObj.setBlockWithNotify(i[0], e.worldObj.getHeightValue(i[0], i[1]), i[1], Block.glass.blockID);
        }

        e.data[2] = max - min;
//        System.out.println("offset " + e.gb);
    }

    @Override
    public void onUnload(EntityDust e)
    {
        if (e.rutPoints == null)
        {
            this.findRuts(e, Block.blockClay.blockID);
        }

        super.onUnload(e);

        if (e.rutPoints != null)
        {
            for (Integer[] i : e.rutPoints)
            {
                int rand = e.worldObj.rand.nextInt(100);

                if (rand > 15)
                {
//                world.setBlockWithNotify(i[0], i[1], i[2], Block.melon.blockID);
                    TileEntityRut ter = (TileEntityRut) e.worldObj.getBlockTileEntity(i[0], i[1], i[2]);

                    if (ter != null)
                    {
//                        ter.fluid = Block.sand.blockID;
                    }
                }
            }
        }
    }
}
