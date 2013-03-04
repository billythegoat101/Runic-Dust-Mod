package dustmod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLLog;

public class VoidStorageManager {

    public static Properties propInv;
    public static File invFS;
    public static HashMap<String, ArrayList<ItemStack>> voidInventory;
    
    
    public static void updateVoidInventory()
    {
        try
        {
            propInv.load(new FileInputStream(invFS));

            if (propInv != null)
            {
                for (Object o: propInv.keySet())
                {
                    String key = (String)o;
//                    int i = Integer.valueOf(key);
//                    String inv = (String)propInv.get(o);
                    propInv.setProperty(key, "");
                }

                for (String key:voidInventory.keySet())
                {
                    ArrayList<ItemStack> items = voidInventory.get(key);
                    if(items != null)
                        for(ItemStack is:items){
                            propInv.setProperty(key, "[" + is.itemID + "," + is.stackSize + "," + is.getItemDamage() + "]");
                        }
                }


                //            ps.setProperty("" + id, inv.toNBTString());
                try
                {
                    propInv.store(new FileOutputStream(invFS), null);
                }
                catch (IOException ex)
                {
                    FMLLog.log(Level.SEVERE, null, ex);
                }
            }
        }
        catch (IOException ex)
        {
        	FMLLog.log(Level.SEVERE, null, ex);
        }
    }
    public static void addItemToVoidInventory(EntityDust e, ItemStack is){
        addItemToVoidInventory(e.summonerUN,is);
    }
    public static void addItemToVoidInventory(String player, ItemStack is){
        if(voidInventory.get(player) == null){
            voidInventory.put(player, new ArrayList<ItemStack>());
        }
        voidInventory.get(player).add(is);
                
    }
    public static ArrayList<ItemStack> getVoidInventory(EntityDust e){
        return voidInventory.get(e.summonerUN);
    }
    public static void clearVoidInventory(EntityDust e){
        voidInventory.put(e.summonerUN, null);
    }
    
    public static void load(String savePath){
        voidInventory = new HashMap<String, ArrayList<ItemStack>>();
        propInv = new Properties();
        
        if (invFS == null || !invFS.exists())
        {
            try
            {
                invFS = new File((new StringBuilder()).append(savePath).append("dustmodvoidinventory.dat").toString());

                if (invFS.createNewFile())
                {
                    if (propInv == null)
                    {
                        propInv = new Properties();
                    }

                    propInv.store(new FileOutputStream(invFS), null);
                }
            }
            catch (IOException ioexception)
            {
                FMLLog.log(Level.SEVERE, null, ioexception);
            }
        }
        try
        {
            propInv.load(new FileInputStream(invFS));

            for (Object o: propInv.keySet())
            {
                String key = (String)o;
//                int i = Integer.valueOf(key);
                try{
                    int i = Integer.valueOf(key);
                    //if it didn't throw an exception
                    if(propInv.size() == 1){
//                        key = ModLoader.getMinecraftInstance().thePlayer.username;
                    }
                }catch(Exception e){
                }
                String inv = (String)propInv.get(o);
                int c = 0;

                if (inv.length() > 0 && inv.charAt(c) == '[')
                {
                    c++;
                    //                            int pos;
                    int id;
                    int size;
                    int dam;
                    //                            pos = Integer.valueOf(inv.substring(c,inv.indexOf(':',c)));
                    //                            c = inv.indexOf(':',c)+1;
                    id = Integer.valueOf(inv.substring(c, inv.indexOf(',', c)));
                    c = inv.indexOf(',', c) + 1;
                    size = Integer.valueOf(inv.substring(c, inv.indexOf(',', c)));
                    c = inv.indexOf(',', c) + 1;
                    dam = Integer.valueOf(inv.substring(c, inv.indexOf(']', c)));
                    c = inv.indexOf(']', c);
                    if(voidInventory.get(key) == null){
                        voidInventory.put(key, new ArrayList<ItemStack>());
                    }
                    voidInventory.get(key).add(new ItemStack(id, size, dam));
                    DustMod.log(Level.FINER, "Void Inventory loading: " + key + "[" + id + "," + size + "," + dam + "]");
//                    System.out.println("[DustMod] Void Inventory loading: " + key + "[" + id + "," + size + "," + dam + "]");
                }
            }
        } catch (IOException e){
            FMLLog.log(Level.SEVERE, null, e);
        }
    }
}
