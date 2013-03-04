/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import dustmod.DustItemManager.DustColor;

/**
 *
 * @author billythegoat101
 */
public class PacketHandler implements IPacketHandler, IConnectionHandler
{
    public static final String CHANNEL_TEDust = "TEdust";
    public static final String CHANNEL_TELexicon = "TElexicon";
    public static final String CHANNEL_TERut = "TERut";
    public static final String CHANNEL_DMRune = "DMRune";
    public static final String CHANNEL_DeclareInscription = "DecInsc";
    public static final String CHANNEL_DustItem = "DustItem";    
    public static final String CHANNEL_Mouse = "DustMouse";
    public static final String CHANNEL_UseInk = "DustUseInk";
    public static final String CHANNEL_SetInscription = "DustSetInsc";
    public static final String CHANNEL_SpawnParticles = "DustParticles"; 
    public static final String CHANNEL_SetEntVelocity = "DustModSetVel";
//    public static final String CHANNEL_UpdatePlayerInv = "DustModUpdInv";
    
    public static Packet getTEDPacket(TileEntityDust ted)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        int x = ted.xCoord;
        int y = ted.yCoord;
        int z = ted.zCoord;
        int entID = ted.dustEntID;
        
        boolean hasFlame = ted.hasFlame();
        int[] flamecolor = ted.getFlameColor();

        try
        {
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(z);
            dos.writeInt(entID);

            dos.writeBoolean(hasFlame);
            dos.writeInt(flamecolor[0]);
            dos.writeInt(flamecolor[1]);
            dos.writeInt(flamecolor[2]);

            for (int i = 0; i < ted.size; i++)
                for (int j = 0; j < ted.size; j++)
                {
                    dos.writeInt(ted.getDust(i, j));
                }
        }
        catch (IOException e)
        {
            // UNPOSSIBLE? -cpw
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = CHANNEL_TEDust;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet getTELPacket(TileEntityDustTable tel)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        int x = tel.xCoord;
        int y = tel.yCoord;
        int z = tel.zCoord;

        try
        {
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(z);
            dos.writeInt(tel.page);
        }
        catch (IOException e)
        {
            // UNPOSSIBLE? -cpw
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = CHANNEL_TELexicon;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet getTERPacket(TileEntityRut ter)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        int x = ter.xCoord;
        int y = ter.yCoord;
        int z = ter.zCoord;
        int entID = ter.dustEntID;
        
        boolean hasFlame = ter.hasFlame();
        int[] flamecolor = ter.getFlameColor();

        try
        {
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(z);
            dos.writeInt(ter.maskBlock);
            dos.writeInt(ter.maskMeta);
            dos.writeInt(ter.fluid);
            dos.writeInt(entID);

            dos.writeBoolean(hasFlame);
            dos.writeInt(flamecolor[0]);
            dos.writeInt(flamecolor[1]);
            dos.writeInt(flamecolor[2]);
            
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    for (int k = 0; k < 3; k++)
                    {
                        dos.writeInt(ter.ruts[i][j][k]);
                    }
                }
            }
        }
        catch (IOException e)
        {
            // UNPOSSIBLE? -cpw
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = CHANNEL_TERut;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }


    public static Packet getRuneDeclarationPacket(DustShape shape)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeInt(shape.width);
            dos.writeInt(shape.height);
            dos.writeInt(shape.length);
            dos.writeInt(shape.id);
            dos.writeInt(shape.ox);
            dos.writeInt(shape.oy);
            dos.writeInt(shape.cx);
            dos.writeInt(shape.cy);
            dos.writeInt(shape.pageNumber);
            dos.writeBoolean(shape.isPower);
            dos.writeBoolean(shape.solid);
            dos.writeInt(shape.name.length());
            dos.writeInt(shape.getRuneName().length());
            dos.writeInt(shape.getAuthor().length());
            dos.writeInt(shape.getNotes().length());
            dos.writeInt(shape.getDescription().length());
            dos.writeChars(shape.name);
            dos.writeChars(shape.getRuneName());
            dos.writeChars(shape.getAuthor());
            dos.writeChars(shape.getNotes());
            dos.writeChars(shape.getDescription());

            for (int y = 0; y < shape.height; y++)
            {
                for (int x = 0; x < shape.width; x++)
                {
                    for (int z = 0; z < shape.length; z++)
                    {
                        dos.writeInt(shape.getDataAt(x, y, z));
                    }
                }
            }
            
            for(int i = 0; i < 8; i++){
            	dos.writeInt(shape.manRot[i]);
            }
        }
        catch (IOException e)
        {
            // UNPOSSIBLE? -cpw
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = CHANNEL_DMRune;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
//        pkt.isChunkDataPacket = true;
        return pkt;
    }
    
    public static Packet getInscriptionDeclarationPacket(InscriptionEvent evt){
    	ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
        	dos.writeInt(evt.referenceDesign.length);
        	dos.writeInt(evt.referenceDesign[0].length);
            dos.writeInt(evt.id);
            dos.writeInt(evt.getIDName().length());
            dos.writeInt(evt.getInscriptionName().length());
            dos.writeInt(evt.getDescription().length());
            dos.writeInt(evt.getNotes().length());
            dos.writeInt(evt.getAuthor().length());
            dos.writeChars(evt.getIDName());
            dos.writeChars(evt.getInscriptionName());
            dos.writeChars(evt.getDescription());
            dos.writeChars(evt.getNotes());
            dos.writeChars(evt.getAuthor());

            int w = evt.referenceDesign.length;
            int h = evt.referenceDesign[0].length;
            for (int x = 0; x < w; x++)
            {
                for (int y = 0; y < h; y++)
                { 
                        dos.writeInt(evt.referenceDesign[x][y]); 
                }
            }
        }
        catch (IOException e)
        {
            // UNPOSSIBLE? -cpw
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = CHANNEL_DeclareInscription;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
//        pkt.isChunkDataPacket = true;
        return pkt;
    }
    
    public static Packet getDustDeclarationPacket(int value){

        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeInt(value);
            int primaryColor, secondaryColor, floorColor;
            String idName, name;
            
            DustColor color = DustItemManager.colors[value];
            primaryColor = color.primaryColor;
            secondaryColor = color.secondaryColor;
            floorColor = color.floorColor;
            
            idName = DustItemManager.ids[value];
            name = DustItemManager.names[value];
            
            dos.writeInt(primaryColor);
            dos.writeInt(secondaryColor);
            dos.writeInt(floorColor);
            
            dos.writeInt(idName.length());
            dos.writeInt(name.length());
            dos.writeChars(idName);
            dos.writeChars(name);
        }
        catch (IOException e)
        {
            // UNPOSSIBLE? -cpw
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = CHANNEL_DustItem;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        return pkt;
    }

    public static Packet getMousePacket(int keyID, boolean pressed){
    	 ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
         DataOutputStream dos = new DataOutputStream(bos);

         try
         {
             dos.writeInt(keyID);
             dos.writeBoolean(pressed);
         }
         catch (IOException e)
         {
             // UNPOSSIBLE? -cpw
         }

         Packet250CustomPayload pkt = new Packet250CustomPayload();
         pkt.channel = CHANNEL_Mouse;
         pkt.data = bos.toByteArray();
         pkt.length = bos.size();
         pkt.isChunkDataPacket = false;
         return pkt;
    }
    
    public static Packet getUseInkPacket(int slot, int amt){
    	ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            dos.writeInt(slot);
            dos.writeInt(amt);
        }
        catch (IOException e)
        {
            // UNPOSSIBLE? -cpw
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = CHANNEL_UseInk;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = false;
        return pkt;
    }
    
    public static Packet getSetInscriptionPacket(int[] design){
    	ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
        	for(int i = 0; i < design.length; i++){
    			dos.writeInt(design[i]);
        	}
        }
        catch (IOException e)
        {
            // UNPOSSIBLE? -cpw
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = CHANNEL_SetInscription;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = false;
        return pkt;
    }
    
    public static Packet getParticlePacket(String type, short formation, double[] loc, double velx, double vely, double velz, int amt, double rx, double ry, double rz){

    	ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
        	dos.writeInt(loc.length);
        	for(double i:loc){
        		dos.writeFloat((float)i);
        	}
        	dos.writeShort(formation);
        	dos.writeFloat((float)velx);
        	dos.writeFloat((float)vely);
        	dos.writeFloat((float)velz);
        	dos.writeInt(amt);
        	dos.writeFloat((float)rx);
        	dos.writeFloat((float)ry);
        	dos.writeFloat((float)rz);
        	
        	dos.writeInt(type.length());
        	dos.writeChars(type);
        }
        catch (IOException e)
        {
            // UNPOSSIBLE? -cpw
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = CHANNEL_SpawnParticles;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = false;
        return pkt;
    }
    
    public static Packet getSetVelocityPacket(EntityLiving ent){
    	ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
        	dos.writeInt(ent.entityId);
        	dos.writeFloat((float)ent.motionX);
        	dos.writeFloat((float)ent.motionY);
        	dos.writeFloat((float)ent.motionZ);
        	dos.writeFloat((float)ent.jumpMovementFactor);
        }
        catch (IOException e)
        {
            // UNPOSSIBLE? -cpw
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = CHANNEL_SetEntVelocity;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = false;
        return pkt;
    }
    
    private static void onTEDPacket(byte[] data, Player player){
    	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        int x, y, z;
        int entID;
        boolean hasFlame;
        int r,g,b;
        TileEntityDust ted;
        World world = DustMod.proxy.getClientWorld();

        try
        {
            x = dis.readInt();
            y = dis.readInt();
            z = dis.readInt();
            entID = dis.readInt();
            
            hasFlame = dis.readBoolean();
            r = dis.readInt();
            g = dis.readInt();
            b = dis.readInt();
            
            ted = (TileEntityDust)world.getBlockTileEntity(x, y, z);

            if (ted == null)
            {
                return;
            }

            ted.dustEntID = entID;
            ted.setRenderFlame(hasFlame, r, g, b);
            int[][] pattern = ted.getPattern();

            for (int i = 0; i < TileEntityDust.size; i++)
            {
                for (int j = 0; j < TileEntityDust.size; j++)
                {
                	EntityPlayer p = null;
                	if(player instanceof EntityPlayer) p = (EntityPlayer)player;
                	ted.setDust(p, i,j,dis.readInt());
                }
            }

            world.notifyBlockChange(x,y,z,0);
        }
        catch (IOException e)
        {
            return;
        }
    }
    private static void onTELPacket(byte[] data, Player player){
    	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        int x, y, z;
        TileEntityDustTable tel;
        World world = DustMod.proxy.getClientWorld();

        try
        {
            x = dis.readInt();
            y = dis.readInt();
            z = dis.readInt();
            tel = (TileEntityDustTable)world.getBlockTileEntity(x, y, z);

            if (tel == null)
            {
                return;
            }

            tel.page = dis.readInt();
            world.notifyBlockChange(x,y,z,0);
        }
        catch (IOException e)
        {
            return;
        }
    }
    private static void onTERPacket(byte[] data, Player player){
    	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        int x, y, z;
        int entID;
        boolean hasFlame;
        int r,g,b;
        TileEntityRut ter;
        World world = DustMod.proxy.getClientWorld();

        try
        {
            x = dis.readInt();
            y = dis.readInt();
            z = dis.readInt();
            ter = (TileEntityRut)world.getBlockTileEntity(x, y, z);

            if (ter == null)
            {
                return;
            }

            ter.maskBlock = dis.readInt();
            ter.maskMeta = dis.readInt();
            ter.fluid = dis.readInt();
            entID = dis.readInt();
            
            hasFlame = dis.readBoolean();
            r = dis.readInt();
            g = dis.readInt();
            b = dis.readInt();

            ter.dustEntID = entID;
            ter.setRenderFlame(hasFlame, r, g, b);
            
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    for (int k = 0; k < 3; k++)
                    {
                        ter.ruts[i][j][k] = dis.readInt();
                    }
                }
            }

            world.notifyBlockChange(x,y,z,0);
        }
        catch (IOException e)
        {
            return;
        }
    }
    private static void onRuneDeclarationPacket(byte[] data, Player player){

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

        int w,h,l,id,ox,oy,cx,cy,pageNumber,nameLen, pNameLen,authorLen, notesLen, descLen;
        String name,pName,author,notes,desc;
        boolean powered,solid;
        try
        {
            w = dis.readInt();//dos.writeInt(shape.width);
            h = dis.readInt();//dos.writeInt(shape.height);
            l = dis.readInt();//dos.writeInt(shape.length);
            id = dis.readInt();//dos.writeInt(shape.id);
            ox = dis.readInt();//dos.writeInt(shape.ox);
            oy = dis.readInt();//dos.writeInt(shape.oy);
            cx = dis.readInt();//dos.writeInt(shape.cx);
            cy = dis.readInt();//dos.writeInt(shape.cy);
            pageNumber = dis.readInt();
            powered = dis.readBoolean();
            solid = dis.readBoolean();
            nameLen = dis.readInt();
            pNameLen = dis.readInt();
            authorLen = dis.readInt();
            notesLen = dis.readInt();
            descLen = dis.readInt();
            name = "";
            for(int i = 0; i < nameLen; i++)
                name += dis.readChar();
            pName = "";
            for(int i = 0; i < pNameLen; i++)
                pName += dis.readChar();
            author = "";
            for(int i = 0; i < authorLen; i++)
                author += dis.readChar();
            notes = "";
            for(int i = 0; i < notesLen; i++)
                notes += dis.readChar();
            desc = "";
            for(int i = 0; i < descLen; i++)
                desc += dis.readChar();

            int[][][] design = new int[h][w][l];
            
            for (int y = 0; y < h; y++)
            {
                for (int x = 0; x < w; x++)
                {
                    for (int z = 0; z < l; z++)
                    {
                        design[y][x][z] = dis.readInt();//dos.writeInt(shape.getDataAt(x, y, z));
                    }
                }
            }
            
            int[] manRot = new int[8];
            for(int i = 0; i < 8; i++){
            	manRot[i] = dis.readInt();
            }
            
            DustShape shape = new DustShape(w,l,name,solid,ox,oy,cx,cy,pageNumber,id);
            shape.setData(design);
            shape.setRuneName(pName);
            shape.setNotes(notes);
            shape.setDesc(desc);
            shape.setAuthor(author);
            shape.isPower = powered;
            shape.manRot = manRot;
            
            shape.isRemote = true;
            
            DustManager.registerRemoteDustShape(shape);
        }
        catch (IOException e)
        {
            return;
        }
    }
    private static void onInscriptionDeclarationPacket(byte[] data, Player player){
//    	System.out.println("[DustMod] Inscription recieved");
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

        int w,h,id,idNameLen, nameLen,authorLen, notesLen, descLen;
        String idname,name,author,notes,desc;
        try
        {
        	w = dis.readInt();
        	h = dis.readInt();
            id = dis.readInt();//dos.writeInt(shape.id);
            idNameLen = dis.readInt();
            nameLen = dis.readInt();
            descLen = dis.readInt();
            notesLen = dis.readInt();
            authorLen = dis.readInt();
            idname = "";
            for(int i = 0; i < idNameLen; i++)
                idname += dis.readChar();
            name = "";
            for(int i = 0; i < nameLen; i++)
                name += dis.readChar();
            desc = "";
            for(int i = 0; i < descLen; i++)
                desc += dis.readChar();
            notes = "";
            for(int i = 0; i < notesLen; i++)
                notes += dis.readChar();
            author = "";
            for(int i = 0; i < authorLen; i++)
                author += dis.readChar();

            int[][] design = new int[w][h];
            
            for (int x = 0; x < w; x++)
            {
                for (int y = 0; y < h; y++)
                {
                    design[x][y] = dis.readInt();//dos.writeInt(shape.getDataAt(x, y, z));
                }
            }
            
            InscriptionEvent event = new InscriptionEvent(design, idname, name, id);
            event.setAuthor(author);
            event.setDescription(desc);
            event.setNotes(notes);
            InscriptionManager.registerRemoteInscriptionEvent(event);
        }
        catch (IOException e)
        {
            return;
        }
    }
    private static void onSetInscriptionPacket(byte[] data, Player player){
    	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

    	int[] design = new int[16*16];
        try
        {
        	for(int i = 0; i < design.length;i++){
        		design[i] = dis.readInt();
        	}
        	
        	EntityPlayer ep = (EntityPlayer)player;
        	ItemStack stack = ep.getCurrentEquippedItem();
        	if(stack != null){
        		if(stack.getItemDamage() == 0)
        			stack.setItemDamage(1);
        		NBTTagCompound tag = stack.getTagCompound();
        		if(tag == null){
        			tag = new NBTTagCompound();
        			stack.setTagCompound(tag);
        		}
            	for(int i = 0; i < 16; i++){
            		for(int j = 0; j < 16; j++){
            			tag.setInteger(i + "," + j, design[i*16 + j]);
            		}
            	}
            	EntityPlayerMP mp = (EntityPlayerMP)player;
            	//mp.upsendInventoryToPlayer(); //TEST find replacement
        	}
        }
        catch (IOException e)
        {
            return;
        }
    }
    private static void onDustDeclarationPacket(byte[] data, Player player){
    	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

        int value, primaryColor, secondaryColor, floorColor;
        int idNameLen, nameLen;
        String idName, name;
        try
        {
        	value = dis.readInt();
        	primaryColor= dis.readInt();
        	secondaryColor = dis.readInt();
        	floorColor = dis.readInt();
        	
        	idNameLen = dis.readInt();
        	nameLen = dis.readInt();

        	idName = "";
            for(int i = 0; i < idNameLen; i++)
                idName += dis.readChar();
            name = "";
            for(int i = 0; i < nameLen; i++)
                name += dis.readChar();
            
            DustItemManager.registerRemoteDust(value, name, idName, primaryColor, secondaryColor, floorColor);
        }
        catch (IOException e)
        {
            return;
        }
    }
    private static void onMousePacket(byte[] data, Player player){

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

    	int keyID;
    	boolean pressed;
        try
        {
        	keyID = dis.readInt();
        	pressed = dis.readBoolean();

        	DustMod.keyHandler.setKey(player, keyID, pressed);
        	
        }
        catch (IOException e)
        {
            return;
        }
    }
    private static void onUseInkPacket(byte[] data, Player player){

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

    	int slot, amt;
        try
        {
        	slot = dis.readInt();
        	amt = dis.readInt();
        	
        	EntityPlayer ep = (EntityPlayer)player;
        	ItemStack stack = ep.inventory.getStackInSlot(slot);
        	ItemInk.reduce((EntityPlayer)player, stack, amt);
        	ep.inventory.setInventorySlotContents(slot, stack);
        }
        catch (IOException e)
        {
            return;
        }
    }
    private static void onParticlePacket(byte[] data, Player player){

    	if(data == null || data.length == 0) return;
    	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
    	int locLength;
    	double[] locations;
    	short formation;
        double vx,vy,vz;
        double rx,ry,rz;
        int amt;
        String type = "";
        int typeLen;

        World world = null;
        try
        {
        	locLength = dis.readInt();
        	locations = new double[locLength];
        	for(int i = 0; i < locLength; i++){
        		locations[i] = dis.readFloat();
        	}
        	
        	formation = dis.readShort();
        	
            vx = dis.readFloat();
            vy = dis.readFloat();
            vz = dis.readFloat();
            
            amt = dis.readInt();
            rx = dis.readFloat();
            ry = dis.readFloat();
            rz = dis.readFloat();
            
            typeLen = dis.readInt();
            for(int i = 0; i < typeLen; i++){
            	type += dis.readChar();
            }
            
            
            world = ((EntityPlayer)player).worldObj;//MinecraftServer.getServer().worldServerForDimension(dimension);
            
            for(int l = 0; l < locLength/3; l++){
            	double x = locations[l*3];
            	double y = locations[l*3+1];
            	double z = locations[l*3+2];
	            Random rand = new Random((long)(x+y+z+world.getWorldTime()));
	            for(int i = 0; i < amt; i++){
	            	double nx = x + rand.nextDouble()*(rx*2) - rx;
	            	double ny = y + rand.nextDouble()*(ry*2) - ry;
	            	double nz = z + rand.nextDouble()*(rz*2) - rz;
	            	world.spawnParticle(type, nx, ny, nz, vx, vy, vz);
	            }
            }
        }
        catch (IOException e)
        {
            return;
        }
    }
    private static void onSetVelocityPacket(byte[] data, Player player){

    	if(data == null || data.length == 0) return;
    	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
    	int entID;
    	double mx,my,mz;
    	float jumpMotion;
    	double[] locations;
        try
        {
        	entID = dis.readInt();
        	mx = (double)dis.readFloat();
        	my = (double)dis.readFloat();
        	mz = (double)dis.readFloat();
        	jumpMotion = dis.readFloat();
        	
        	EntityLiving ent = (EntityLiving)((EntityPlayer)player).worldObj.getEntityByID(entID);
        	
        	if(ent != null){
//        		ent.setVelocity(mx, my, mz);
        		ent.addVelocity(-ent.motionX + mx, -ent.motionY + my, -ent.motionZ + mz);
        		ent.jumpMovementFactor = jumpMotion;
        	}
        }
        catch (IOException e)
        {
            return;
        }
    }
    
    
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
    	
    	
//    	System.out.println("Got packet " + packet.channel);
    	byte[] data = packet.data;
    	String channel = packet.channel;
        if (channel.equals(CHANNEL_TEDust)){
            onTEDPacket(data,player);
        }else if (channel.equals(CHANNEL_TELexicon)){
            onTELPacket(data, player);
        }else if (channel.equals(CHANNEL_TERut)){
            onTERPacket(data,player);
        }else if(channel.equals(CHANNEL_DMRune)){
        	onRuneDeclarationPacket(data,player);
        }else if(channel.equals(CHANNEL_DustItem)){
            onDustDeclarationPacket(data,player);
        }else if(channel.equals(CHANNEL_Mouse)){
        	onMousePacket(data,player);
        }else if(channel.equals(CHANNEL_UseInk)){
        	onUseInkPacket(data,player);
        }else if(channel.equals(CHANNEL_SetInscription)){
        	onSetInscriptionPacket(data,player);
        }else if(channel.equals(CHANNEL_DeclareInscription)){
        	onInscriptionDeclarationPacket(data,player);
        }else if(channel.equals(CHANNEL_SpawnParticles)){
        	onParticlePacket(data,player);
        }else if(channel.equals(CHANNEL_SetEntVelocity)){
        	onSetVelocityPacket(data,player);
        }
    }

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler,
			INetworkManager manager) {
		
//		System.out.println("Player logged in " + DustMod.proxy.isClient() );
		DustMod.keyHandler.checkPlayer(player);
		for(int i = 0; i < DustItemManager.ids.length; i++){
			if(DustItemManager.ids[i] != null){
				manager.addToSendQueue(getDustDeclarationPacket(i));
			}
		}
        for(DustShape shape:DustManager.shapes){
        	DustEvent e = DustManager.getEvents().get(shape.name); 
        	if(e.canPlayerKnowRune(((EntityPlayer)player).username) && !e.secret)
        		manager.addToSendQueue(getRuneDeclarationPacket(shape));
        }

        for(InscriptionEvent evt:InscriptionManager.events){
        	if(evt.canPlayerKnowInscription((EntityPlayer)player) && !evt.secret)
        		manager.addToSendQueue(getInscriptionDeclarationPacket(evt));
        }
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler,
			INetworkManager manager) {
		return null;
	}

	//Remote server connection established
	@Override
	public void connectionOpened(NetHandler netClientHandler, String server,
			int port, INetworkManager manager) {
		DustMod.log(Level.FINER, "Resetting due to opened connection.1");
		DustManager.resetMultiplayerRunes();
		DustItemManager.reset();
		InscriptionManager.resetRemoteInscriptions();
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler,
			MinecraftServer server, INetworkManager manager) {
		DustMod.log(Level.FINER, "Resetting due to opened connection.2");
		DustManager.resetMultiplayerRunes();
		DustItemManager.reset();
		InscriptionManager.resetRemoteInscriptions();
	}

	@Override
	public void connectionClosed(INetworkManager manager) {
//		System.out.println("[DustMod] Resetting due to closed connection.");
//		DustManager.resetMultiplayerRunes();
//		DustItemManager.reset();
//		InscriptionManager.resetRemoteInscriptions();
//		DustManager.registerDefaultShapes();
//		DustItemManager.registerDefaultDusts();
//		System.out.println("Connection closed");
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler,
			INetworkManager manager, Packet1Login login) {
//		System.out.println("Logged in");
	}
    
}
