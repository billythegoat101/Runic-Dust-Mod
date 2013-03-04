package dustmod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;

public class VoidTeleManager {

    public static ArrayList<int[]> voidNetwork;
    public static int prevVoidSize;
    public static Properties propNet;
    public static File netFS;
    public static int skipWarpTick = 0;
    
    public static boolean containsWarp(int[] pos)
    {
        for (int[] iter: voidNetwork)
        {
            if (iter[0] == pos[0] && iter[1] == pos[1] && iter[2] == pos[2])
            {
                return true;
            }
        }

        return false;
    }

    public static void addWarp(int[] warp)
    {
        if (!containsWarp(warp))
        {
            voidNetwork.add(warp);
            updateVoidNetwork();
        }
    }

    public static void removeWarp(int[] warp)
    {
        for (int i = 0; i < voidNetwork.size(); i++) // iter:voidNetwork){
        {
            int[] iter = voidNetwork.get(i);

            if (iter[0] == warp[0] && iter[1] == warp[1] && iter[2] == warp[2])
            {
                voidNetwork.remove(i);
                updateVoidNetwork();
                return;
            }
        }
    }

    public static int getVoidNetworkIndex(int[] warp)
    {
        for (int i = 0; i < voidNetwork.size(); i++) // iter:voidNetwork){
        {
            int[] iter = voidNetwork.get(i);

            if (iter[0] == warp[0] && iter[1] == warp[1] && iter[2] == warp[2])
            {
                return i;
            }
        }

        return -1;
    }

    public static int[] toWarp(EntityDust e)
    {
        return new int[] {e.getX(), e.dustPoints.get(0)[1], e.getZ(), e.data[0], e.worldObj.getBlockMetadata(e.getX(), e.dustPoints.get(0)[1] - 1, e.getZ()), (int)e.rotationYaw, e.worldObj.provider.dimensionId, DustMod.warpVer};
    }
    
    public static EntityDust getWarpEntity(int[] warp, World world)
    {
        double x = warp[0];
        double y = warp[1];
        double z = warp[2];
        double radius = 2D;
        List list = world.getEntitiesWithinAABBExcludingEntity(null,  AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D).expand(radius, radius, radius));

        for (Object o: list)
        {
        	Entity i = (Entity)o;
            if (i instanceof EntityDust)
            {
                EntityDust ed = (EntityDust)i;
//                if(ed.eventName.equals("tele")){
                return ed;
//                }
            }
        }

        return null;
    }
    
    public synchronized static void updateVoidNetwork()
    {
        if (propNet != null)
        {
            try
            {
                propNet.load(new FileInputStream(netFS));
                Object[] set = propNet.keySet().toArray();

                for (Object o: set)
                {
                    String key = (String)o;
                    int i = Integer.valueOf(key);
                    String inv = (String)propNet.get(o);
                    propNet.setProperty(key, "");
                }

                ArrayList<int[]> clone = (ArrayList<int[]>)voidNetwork.clone();

                for (int i = 0; i < clone.size(); i++)
                {
                    int[] warp = clone.get(i);
                    String prp = "[";

                    for (int s: warp)
                    {
                        prp += s + ",";
                    }

                    prp = prp.substring(0, prp.length() - 1) + "]";
                    propNet.setProperty(i + "", "[" + warp[0] + "," + warp[1] + "," + warp[2] + "," + warp[3] + "," + warp[4] + "," + warp[5] + "," + warp[6] + "," + warp[7] + "]");
                }

                //            ps.setProperty("" + id, inv.toNBTString());
                try
                {
                    propNet.store(new FileOutputStream(netFS), null);
                }
                catch (IOException ex)
                {
                	FMLLog.log(Level.SEVERE, null, ex);
                }
            }
            catch (IOException ex)
            {
            	FMLLog.log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void load(String savePath){

    	voidNetwork = new ArrayList<int[]>();
        propNet = new Properties();
        if (netFS == null || !netFS.exists())
        {
            try
            {
                netFS = new File((new StringBuilder()).append(savePath).append("dustmodvoidnetwork.dat").toString());

                if (netFS.createNewFile())
                {
                    if (propNet == null)
                    {
                        propNet = new Properties();
                    }

                    propNet.store(new FileOutputStream(netFS), null);
                }
            }
            catch (IOException ioexception)
            {
                FMLLog.log(Level.SEVERE, null, ioexception);
            }
        }
        
        try
        {

            propNet.load(new FileInputStream(netFS));

            for (Object o: propNet.keySet())
            {
                try
                {
                    String key = (String)o;
                    int i = Integer.valueOf(key);
                    String data = (String)propNet.get(o);
                    int c = 0;

                    if (data.length() > 0 && data.charAt(c) == '[')
                    {
                        c++;
                        //                            int pos;
                        int x;
                        int y;
                        int z;
                        int block;
                        int meta;
                        int rot;
                        int dim;
                        int ver;
                        //                            pos = Integer.valueOf(inv.substring(c,inv.indexOf(':',c)));
                        //                            c = inv.indexOf(':',c)+1;
                        x = Integer.valueOf(data.substring(c, data.indexOf(',', c)));
                        c = data.indexOf(',', c) + 1;
                        y = Integer.valueOf(data.substring(c, data.indexOf(',', c)));
                        c = data.indexOf(',', c) + 1;
                        z = Integer.valueOf(data.substring(c, data.indexOf(',', c)));
                        c = data.indexOf(',', c) + 1;
                        block = Integer.valueOf(data.substring(c, data.indexOf(',', c)));
                        c = data.indexOf(',', c) + 1;
                        meta = Integer.valueOf(data.substring(c, data.indexOf(',', c)));
                        c = data.indexOf(',', c) + 1;
                        rot = Integer.valueOf(data.substring(c, data.indexOf(',', c)));
                        c = data.indexOf(',', c) + 1;
                        dim = Integer.valueOf(data.substring(c, data.indexOf(',', c)));
                        c = data.indexOf(',', c) + 1;
                        ver = Integer.valueOf(data.substring(c, data.indexOf(']', c)));
                        c = data.indexOf(']', c);
                        voidNetwork.add(new int[] {x, y, z, block, meta, rot, dim, ver});
//                        System.out.println("Void Network loading: [" + x + "," + y + "," + z + "] " + block + ":" + meta + " dim:" + dim + " ver:" + ver);
                    }
                }
                catch (Exception e) {}
            }
        }
        catch (IOException ex)
        {
            FMLLog.log(Level.SEVERE, null, ex);
        }
    }

}
