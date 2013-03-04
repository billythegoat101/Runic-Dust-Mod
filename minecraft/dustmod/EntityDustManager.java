package dustmod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

public class EntityDustManager {


	protected static HashMap<Long, EntityDust> entMap;
	protected static long nextDustEntID;
	protected static Properties propGeneral;
	protected static File generalFS;

	public static Long getNextDustEntityID() {
		nextDustEntID++;
		if (propGeneral == null) {
			DustMod.log(Level.WARNING, "General property file is null!");
//			System.out.println("[DustMod] General property file is null!");
		}
		propGeneral.setProperty("entDustNID", "" + nextDustEntID);

		try {
			propGeneral.store(new FileOutputStream(generalFS),
					null);
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e,
					"[DustMod] : Error loading world properties.");
		}

		return nextDustEntID - 1;
	}

	public static void registerEntityDust(EntityDust ent, long id) {
		entMap.put(id, ent);
	}

	public static EntityDust getDustAtID(long id) {
		if (entMap.containsKey(id)) {
			return entMap.get(id);
		} else {
			return null;
		}
	}
	
	public static void load(String savePath){
		entMap = new HashMap<Long, EntityDust>();
		
        propGeneral = new Properties();
        if (generalFS == null || !generalFS.exists())
        {
            try
            {
                generalFS = new File((new StringBuilder()).append(savePath).append("dustmodgeneral.dat").toString());

                if (generalFS.createNewFile())
                {
                    if (propGeneral == null)
                    {
                        propGeneral = new Properties();
                    }

                    propGeneral.store(new FileOutputStream(generalFS), null);
                }
            }
            catch (IOException ioexception)
            {
                FMLLog.log(Level.SEVERE, null, ioexception);
            }
        }

        try
        {
            propGeneral.load(new FileInputStream(generalFS));
            entMap = new HashMap<Long, EntityDust>();
            nextDustEntID = Long.valueOf(propGeneral.getProperty("entDustNID"));
        }
        catch (IOException ex)
        {
        	FMLLog.log(Level.SEVERE, null, ex);
        }
        catch (NumberFormatException ex)
        {
            nextDustEntID = 0;
            propGeneral.setProperty("entDustNID", "" + nextDustEntID);
        }
	}
}
