/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author billythegoat101
 */
public class EntityDust extends Entity
{

	public static final int birthLength = 20*5;
	
    public static int DW_ri = 10;
    public static int DW_gi = 11;
    public static int DW_bi = 12;
    public static int DW_ro = 13;
    public static int DW_go = 14;
    public static int DW_bo = 15;
    public static int DW_rb = 16;
    public static int DW_gb = 17;
    public static int DW_bb = 18;
    public static int DW_rf = 19;
    public static int DW_gf = 20;
    public static int DW_bf = 21;
    public static int DW_starScale = 22;
    public static int DW_starScaleY = 23;
    public static int DW_renderBeam = 24;
    public static int DW_renderStar = 25;
    public static int DW_eventName = 26;
    public static int DW_beamType = 27;
    public static int DW_justBorn = 28;
    
    public boolean justBorn = true;
    public boolean doFizzle = false;
    
    public boolean reanimate = false;
    public  boolean renderBeam = false;
    public int beamType = 0;
    public boolean renderStar = false;
    public boolean ignoreRune = false;
    public boolean renderFlamesDust = false;
    public boolean renderFlamesRut = false;
    public boolean fade = false;
    public boolean follow = false;
    public Entity toFollow = null;
    public int ri, gi, bi; //inside star
    public int ro, go, bo; //outside star
    public int rb, gb, bb; //beam
    public int rf, gf, bf; //fire
    public float starScale = 1F;
    public float starScaleY = 1F;
    public int sacrificeWaiting = -1;
    public int fuelAmount = 0;
    public boolean requiresFuel = false;
    private boolean hasSetTileEntities = false;
    public String eventName = "";
    public DustEvent event;
    public HashMap<Integer, Integer[]> flameRenderHelperDust;
    public HashMap<Integer, Integer[]> flameRenderHelperRut;
    public List<Integer[]> dustPoints;
    public List<Integer[]> rutPoints;
    public List<Integer[]> rutAreaPoints;
    public int[][] dusts;
    public int rot;
    public int dustID; //only if rune is solid
    public int ram = 0;
    public boolean givenFuelThisTick = false;
    public List genericList;
    public long entityDustID;
    public int[] data;
    public String summonerUN;
    public static final double yOffset = 0;//-1.5D;
    protected int runeWidth,runeLength;
//    public int lifetime = -1;

    public EntityDust(World world)
    {
        super(world);
        this.ignoreFrustumCheck = true;
        ri = gi = bi = 255;
        ro = 255;
        go = 236;
        bo = 128;
        this.width = 0.2F;
        this.height = 0.01F;
        this.noClip = true;
        renderDistanceWeight = 50.0D;
        data = new int[16];
    }
    
    public EntityDust(World world, List<Integer[]> dustPoints){
    	this(world);
    	this.dustPoints = dustPoints;
    }
    

    /**
     * The datawatcher is 100% dedicated to the visual aspects of the dust entity.
     * This is because only the server needs to know anything involving the logic 
     * (the data array is not synced) and the client just needs to know what the 
     * player needs to see.
     */
    @Override
    protected void entityInit()
    {
//    	System.out.println("Inint datawatcher");
        dataWatcher.addObject(DW_ri, new Integer(ri));
        dataWatcher.addObject(DW_gi, new Integer(gi));
        dataWatcher.addObject(DW_bi, new Integer(bi));
        dataWatcher.addObject(DW_ro, new Integer(ro));
        dataWatcher.addObject(DW_go, new Integer(go));
        dataWatcher.addObject(DW_bo, new Integer(bo));
        dataWatcher.addObject(DW_rb, new Integer(rb));
        dataWatcher.addObject(DW_gb, new Integer(gb));
        dataWatcher.addObject(DW_bb, new Integer(bb));
        dataWatcher.addObject(DW_rf, new Integer(rf));
        dataWatcher.addObject(DW_gf, new Integer(gf));
        dataWatcher.addObject(DW_bf, new Integer(bf));
        dataWatcher.addObject(DW_starScale, new Float(starScale).byteValue());
        dataWatcher.addObject(DW_starScaleY, new Float(starScaleY).byteValue());
        dataWatcher.addObject(DW_renderBeam, renderBeam ? (byte) 1 : (byte) 0);
        dataWatcher.addObject(DW_beamType,new Integer(beamType));
        dataWatcher.addObject(DW_renderStar, renderStar ? (byte) 1 : (byte) 0);
        dataWatcher.addObject(DW_justBorn, justBorn ? (byte) 1 : (byte) 0);

        if (eventName == null)
        {
            eventName = "";
        }

        dataWatcher.addObject(DW_eventName, eventName);
    }

    public void updateDataWatcher()
    {
        dataWatcher.updateObject(DW_ri, new Integer(ri));
        dataWatcher.updateObject(DW_gi, new Integer(gi));
        dataWatcher.updateObject(DW_bi, new Integer(bi));
        dataWatcher.updateObject(DW_ro, new Integer(ro));
        dataWatcher.updateObject(DW_go, new Integer(go));
        dataWatcher.updateObject(DW_bo, new Integer(bo));
        dataWatcher.updateObject(DW_rb, new Integer(rb));
        dataWatcher.updateObject(DW_gb, new Integer(gb));
        dataWatcher.updateObject(DW_bb, new Integer(bb));
        dataWatcher.updateObject(DW_rf, new Integer(rf));
        dataWatcher.updateObject(DW_gf, new Integer(gf));
        dataWatcher.updateObject(DW_bf, new Integer(bf));
        dataWatcher.updateObject(DW_starScale, new Float(starScale).byteValue());
        dataWatcher.updateObject(DW_starScaleY, new Float(starScaleY).byteValue());
        dataWatcher.updateObject(DW_renderBeam, renderBeam ? (byte) 1 : (byte) 0);
        dataWatcher.updateObject(DW_beamType,new Integer(beamType));
        dataWatcher.updateObject(DW_renderStar, renderStar ? (byte) 1 : (byte) 0);
        dataWatcher.updateObject(DW_justBorn, justBorn ? (byte) 1 : (byte) 0);
        
        if (eventName == null)
        {
            eventName = "";
        }

        dataWatcher.updateObject(DW_eventName, eventName);
    }

    public void updateEntityFromDataWatcher()
    {
        ri = dataWatcher.getWatchableObjectInt(DW_ri);
        gi = dataWatcher.getWatchableObjectInt(DW_gi);
        bi = dataWatcher.getWatchableObjectInt(DW_bi);
        ro = dataWatcher.getWatchableObjectInt(DW_ro);
        go = dataWatcher.getWatchableObjectInt(DW_go);
        bo = dataWatcher.getWatchableObjectInt(DW_bo);
        rb = dataWatcher.getWatchableObjectInt(DW_rb);
        gb = dataWatcher.getWatchableObjectInt(DW_gb);
        bb = dataWatcher.getWatchableObjectInt(DW_bb);
        rf = dataWatcher.getWatchableObjectInt(DW_rf);
        gf = dataWatcher.getWatchableObjectInt(DW_gf);
        bf = dataWatcher.getWatchableObjectInt(DW_bf);
        starScale = new Float(dataWatcher.getWatchableObjectByte(DW_starScale));
        starScaleY = new Float(dataWatcher.getWatchableObjectByte(DW_starScaleY));
        renderBeam = (dataWatcher.getWatchableObjectByte(DW_renderBeam) == (byte) 1);
        beamType = dataWatcher.getWatchableObjectInt(DW_beamType);
        renderStar = (dataWatcher.getWatchableObjectByte(DW_renderStar) == (byte) 1);
        justBorn = (dataWatcher.getWatchableObjectByte(DW_justBorn) == (byte) 1);
        eventName = dataWatcher.getWatchableObjectString(DW_eventName);

        if (eventName == null)
        {
            eventName = "";
        }
//    	System.out.println("Update from data " + renderStar);
    }

    public void setStarScale(float scale){
    	this.starScale = scale;
    }
    public void setStarScaleY(float scale){
    	this.starScaleY = scale;
    }
    public void setColorStar(int r, int g, int b)
    {
        setColorStarInner(r, g, b);
        setColorStarOuter(r, g, b);
    }

    public void setColorStarInner(int r, int g, int b)
    {
        ri = r;
        gi = g;
        bi = b;
        this.updateDataWatcher();
    }

    public void setColorStarOuter(int r, int g, int b)
    {
        ro = r;
        go = g;
        bo = b;
        this.updateDataWatcher();
    }

    public void setColorBeam(int r, int g, int b)
    {
        rb = r;
        gb = g;
        bb = b;
        this.updateDataWatcher();
    }

    public void setColorFire(int r, int g, int b)
    {
        rf = r;
        gf = g;
        bf = b;
        this.updateDataWatcher();
    }

    public void setRenderBeam(boolean b){
    	this.renderBeam = b;
    }
    public void setBeamType(int i){
    	this.beamType = i;
    }
    public void setRenderStar(boolean b){
    	this.renderStar = b;
    }
    public void setRenderFireOnRune(boolean b){
    	this.renderFlamesDust = b;
    	for(Integer[] i:dustPoints){
    		TileEntity te = worldObj.getBlockTileEntity(i[0], i[1], i[2]);
    		if(te instanceof TileEntityDust){
        		TileEntityDust ted = (TileEntityDust)te;
        		ted.setRenderFlame(b, rf, gf, bf);
    		}
    	}
    }
    public void setRenderFireOnRuts(boolean b){
    	this.renderFlamesRut = b;
    	for(Integer[] i:rutPoints){
    		TileEntity te = worldObj.getBlockTileEntity(i[0], i[1], i[2]);
    		if(te instanceof TileEntityRut){
        		TileEntityRut ter = (TileEntityRut)te;
        		ter.setRenderFlame(b, rf, gf, bf);
    		}
    	}
    }
    
    public void setIgnoreRune(boolean b){
    	this.ignoreRune = b;
    }

    public void setFollow(boolean b){
    	this.follow = b;
    }
    
    public void setEvent(DustEvent evt, String name)
    {
        event = evt;
        eventName = name;
        this.updateDataWatcher();
    }

    public float getStarScale(){return starScale;}
    public float getStarScaleY(){return starScaleY;}
    
    public int[] getColorStarInner(){return new int[]{ri,gi,bi};}
    public int[] getColorStarOuter(){return new int[]{ro,go,bo};}
    public int[] getColorBeam(){return new int[]{rb,gb,bb};}
    public int[] getColorFire(){return new int[]{rf,gf,bf};}
    
    public boolean getRenderBeam(){return renderBeam;}
    public int getBeamType(){return beamType;}
    public boolean getRenderStar(){return renderStar;}
    public boolean getRenderFireOnRune(){return renderFlamesDust;}
    public boolean getRenderFireOnRuts(){return renderFlamesRut;}
    public boolean getIgnoreRune(){return ignoreRune;}
    public boolean getFollow(){return follow;}
    public DustEvent getEvent(){return event;}
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound tag)
    {
        this.ignoreFrustumCheck = true;
        ri = gi = bi = 255;
        ro = 255;
        go = 236;
        bo = 128;
        this.width = 0.2F;
        this.height = 0.01F;
        this.noClip = true;

        if (tag.hasKey("entityDustID"))
        {
            entityDustID = tag.getLong("entityDustID");
            EntityDustManager.registerEntityDust(this, entityDustID);
        } else
        {
            entityDustID = -1L;
        }
        if (tag.hasKey("rot"))
        {
            rot = tag.getInteger("rot");
        }

        eventName = tag.getString("eventname");
        renderBeam = tag.getBoolean("rendBeam");
        if(tag.hasKey("beamType")) beamType = tag.getInteger("beamType");
        renderStar = tag.getBoolean("rendStar");

        if (tag.hasKey("renderFlamesDust"))
        {
            renderFlamesDust = tag.getBoolean("renderFlamesDust");
        }

        if (tag.hasKey("renderFlamesRut"))
        {
            renderFlamesRut = tag.getBoolean("renderFlamesRut");
        }

//        System.out.println("Sacrificewaiting = " + sacrificeWaiting);
        if (tag.hasKey("sacrificeWaitingIsInvalid") && tag.getBoolean("sacrificeWaitingIsInvalid"))
        {
            sacrificeWaiting = -1;
        } else if (!tag.hasKey("sacrificeWaitingIsInvalid"))
        {
            sacrificeWaiting = -1;
        } else if (tag.hasKey("sacrificeWaiting"))
        {
            sacrificeWaiting = tag.getInteger("sacrificeWatitng");
        }

//        System.out.println("Sacrificewaiting After = " + sacrificeWaiting);
        ignoreRune = tag.getBoolean("ignoreRune");
        ticksExisted = tag.getInteger("ticksexist");
        if (tag.hasKey("data"))
        {
            data[0] = tag.getInteger("data");
        } else
        {
            for (int i = 0; i < data.length; i++)
            {
                data[i] = tag.getInteger("data[" + i + "]");
            }
        }


        if (tag.hasKey("requiresFuel"))
        {
            requiresFuel = tag.getBoolean("requiresFuel");
        }

        if (tag.hasKey("remainingStrength"))
        {
            fuelAmount = tag.getInteger("remainingStrength");
        }

        event = DustManager.get(eventName);

        if (event == null)
        {
            reanimate = true;
            DustMod.log(Level.INFO, "This rune has been updated! Reactivating...");
//            System.out.println("[DustMod] This rune has been updated! Reactivating...");
        }

        dustPoints = new ArrayList<Integer[]>();
        int size = tag.getInteger("pointsize");


        for (int i = 0; i < size; i++)
        {
            Integer[] inPos = new Integer[3];
            inPos[0] = tag.getInteger(i + "x");
            inPos[1] = tag.getInteger(i + "y");
            inPos[2] = tag.getInteger(i + "z");
            dustPoints.add(inPos);
        }
//        System.out.println("READ " + dustPoints.size());

        if (tag.hasKey("rutsize"))
        {
            rutPoints = new ArrayList<Integer[]>();
            size = tag.getInteger("rutsize");

            for (int i = 0; i < size; i++)
            {
                Integer[] inPos = new Integer[3];
                inPos[0] = tag.getInteger("r" + i + "x");
                inPos[1] = tag.getInteger("r" + i + "y");
                inPos[2] = tag.getInteger("r" + i + "z");
                rutPoints.add(inPos);
            }
        }

        dustID = tag.getInteger("dustid");
        ri = tag.getInteger("ri");
        gi = tag.getInteger("gi");
        bi = tag.getInteger("bi");
        ro = tag.getInteger("ro");
        go = tag.getInteger("go");
        bo = tag.getInteger("bo");
        rb = tag.getInteger("rb");
        gb = tag.getInteger("gb");
        bb = tag.getInteger("bb");

        if (tag.hasKey("rf"))
        {
            rf = tag.getInteger("rf");
        }

        if (tag.hasKey("gf"))
        {
            gf = tag.getInteger("gf");
        }

        if (tag.hasKey("bf"))
        {
            bf = tag.getInteger("bf");
        }

        fade = tag.getBoolean("fade");
        follow = tag.getBoolean("follow");
        summonerUN = tag.getString("summonerUN");
        starScale = tag.getFloat("starScale");
        starScaleY = tag.getFloat("starScaleY");
        updateDataWatcher();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag)
    {
//        if (lifetime != -1)
//        {
//            return;
//        }

        tag.setString("eventname", eventName);
        tag.setInteger("ticksexist", ticksExisted);
        tag.setBoolean("rendBeam", renderBeam);
        tag.setInteger("beamType",beamType);
        tag.setBoolean("rendStar", renderStar);
        tag.setBoolean("renderFlamesDust", renderFlamesDust);
        tag.setBoolean("renderFlamesRut", renderFlamesRut);
        tag.setBoolean("requiresFuel", requiresFuel);
        tag.setBoolean("ignoreRune", ignoreRune);
        tag.setBoolean("fade", fade);
        tag.setBoolean("follow", follow);
        for (int i = 0; i < data.length; i++)
        {
            tag.setInteger("data[" + i + "]", data[i]);
        }
        tag.setInteger("remainingStrength", fuelAmount);
        tag.setInteger("sacrificeWaiting", sacrificeWaiting);
        tag.setBoolean("sacrificeWaitingIsInvalid", sacrificeWaiting == -1);
        tag.setInteger("dustid", dustID);
        tag.setInteger("rot", rot);
        tag.setFloat("starScale", starScale);
        tag.setFloat("starScaleY", starScaleY);
        tag.setString("summonerUN", summonerUN);
        tag.setInteger("ri", ri);
        tag.setInteger("gi", gi);
        tag.setInteger("bi", bi);
        tag.setInteger("ro", ro);
        tag.setInteger("go", go);
        tag.setInteger("bo", bo);
        tag.setInteger("rb", rb);
        tag.setInteger("gb", gb);
        tag.setInteger("bb", bb);
        tag.setInteger("rf", rf);
        tag.setInteger("gf", gf);
        tag.setInteger("bf", bf);
        tag.setLong("entityDustID", entityDustID);
        tag.setInteger("pointsize", dustPoints.size());

//        System.out.println("WRITE " + dustPoints.size());
        
        for (int i = 0; i < dustPoints.size(); i++)
        {
            Integer[] coord = dustPoints.get(i);
            tag.setInteger(i + "x", coord[0]);
            tag.setInteger(i + "y", coord[1]);
            tag.setInteger(i + "z", coord[2]);
        }

        if (rutPoints != null)
        {
            tag.setInteger("rutsize", rutPoints.size());

            for (int i = 0; i < rutPoints.size(); i++)
            {
                Integer[] coord = rutPoints.get(i);
                tag.setInteger("r" + i + "x", coord[0]);
                tag.setInteger("r" + i + "y", coord[1]);
                tag.setInteger("r" + i + "z", coord[2]);
            }
        }
    }

    @Override
    public void onEntityUpdate()
    {

//    	System.out.println("Alive");

//    	if(eventName == null || eventName.isEmpty()){
//    		updateEntityFromDataWatcher
//    	}
    	
        if (this.ticksExisted % 10 == 0)
        {
//        	System.out.println("Update entity " + eventName);
            updateEntityFromDataWatcher();
        }

        if(dustPoints == null && ticksExisted > 10) {
        	
        	int x = this.getX();
        	int y = this.getY();
        	int z = this.getZ();
        	
        	dustPoints = new ArrayList<Integer[]>();
        	rutPoints = new ArrayList<Integer[]>();
        	
        	this.checkConnections(x, y, z);
        }
        
        if (event == null)
        {
        	//updateDataWatcher();
            super.onEntityUpdate();
            return;
        }

        if (/*!worldObj.isRemote && */((this.ticksExisted % 10 == 0 && this.ticksExisted < 100) || this.ticksExisted % 60 == 0))
        {
            updateDataWatcher();
        }

        if (!worldObj.isRemote || true)
        {
//            if (!event.allowed)
//            {
//                reanimate = true;
//                EntityPlayer player = worldObj.getPlayerEntityByName(summonerUN);
//
//                if (player != null)
//                {
//                }
//            }


            if (!ignoreRune || (ticksExisted < birthLength && justBorn))
            {
                for (Integer[] i : dustPoints)
                {
                    int id = worldObj.getBlockId(i[0], i[1], i[2]);

                    if (!DustMod.isDust(id))
                    {
//                        System.out.println("Rune Broken!");
                        kill();
                        return;
                    }
                }
            }
            
            if(ticksExisted < birthLength && justBorn){
                super.onEntityUpdate();
                if(doFizzle){
                	ticksExisted += 2; //speed up!
                	this.setRenderBeam(false);
                	this.setRenderStar(false);
                	this.updateDataWatcher();
                }
            	return;
            }else if(justBorn) {
            	justBorn = false;
            	this.updateDataWatcher();
            	ticksExisted = 0;
            	for(Integer[] loc:dustPoints){
            		if(worldObj.getBlockId(loc[0],loc[1], loc[2]) == DustMod.dust.blockID)
            			worldObj.setBlockMetadataWithNotify(loc[0], loc[1], loc[2], BlockDust.ACTIVE_DUST,3);
            	}
            }
            if(doFizzle){
                int x = (int) Math.floor(posX);
                int z = (int) Math.floor(posZ);

                DustMod.spawnParticles(worldObj,"largesmoke", (double) x+0.5, (double) posY, (double) z+0.5, 0.0D, 0.0D, 0.0D, (int)(Math.random() * 3D + 4D) * runeWidth * runeLength, runeWidth,0.5,runeLength);

                kill();
                return;
            }

            givenFuelThisTick = false;

            if (!this.hasSetTileEntities)
            {
                for (Integer[] inPos : dustPoints)
                {
                    TileEntityDust ted = (TileEntityDust) worldObj.getBlockTileEntity(inPos[0], inPos[1], inPos[2]);

                    if (ted != null)
                    {
                        ted.setEntityDust(this);
                    }
                }

                this.hasSetTileEntities = true;
            }

            if (dustPoints == null)
            {
//            	System.out.println("Dust points null. killing");
                kill();
                return;
            }

            if (reanimate)
            {
//                System.out.println("Reanimating rune...");

                for (Integer[] i : dustPoints)
                {
                    int id = worldObj.getBlockId(i[0], i[1], i[2]);
                    int bid = 0;

                    if (DustMod.isDust(id))
                    {
                        worldObj.setBlockMetadataWithNotify(i[0], i[1], i[2], BlockDust.UNUSED_DUST,3);
                    }
                }

                super.kill();
                return;
            }

            if (event == null || eventName == null || eventName.isEmpty())
            {
//              System.out.println("poo " + event + " | " + eventName + " death");
                kill();
                return;
            }

            //        System.out.println("rawr " + ticksExisted + " " +fade);
            if (follow)
            {
                Entity tf = toFollow;

                if (tf == null)
                {
                    EntityPlayer player = worldObj.getPlayerEntityByName(summonerUN);

//                    if (true/*!worldObj.multiplayerWorld*/)
//                    {
//                        player = ModLoader.getMinecraftInstance().thePlayer;
//                    }

                    if (player == null)
                    {
                        return;
                    }

                    tf = player;
                }

                int period = 60;
                double dist = 0.27D;
                double ticks = ticksExisted % period;
                double sin = Math.sin((ticks / period) * Math.PI * 2);
                double cos = Math.cos((ticks / period) * Math.PI * 2);
                double dx = cos * dist;
                double dz = sin * dist;
                //        System.out.println("RAWR " + dx + " " + dz);
                //        dx = 0;
                //        dz = 0;
                setPosition(tf.posX + dx, tf.posY + 0.8D + yOffset + 1.2D, tf.posZ + dz);
            }

            if (fade)
            {
                ticksExisted += 2;

                if (ticksExisted % 200 > 190)
                {
                    this.kill();
                }

                super.onEntityUpdate();
                return;
            } else 
            {
                event.tick(this);
            }
        } 

//        kill();
//        if(ticksExisted > 1000) kill();
        super.onEntityUpdate();
    }

    private void checkConnections(int x, int y, int z){
    	for(int i = -1; i <= 1; i++){
			for(int k = -1; k <= 1; k++){
				if(hasDustPoint(x+i,y,z+k)) continue;
				
				int blockID = worldObj.getBlockId(x+i, y, z+k);
				int meta = worldObj.getBlockMetadata(x+i, y, z+k);
				
				if(blockID == DustMod.dust.blockID && (meta == BlockDust.ACTIVE_DUST || meta == BlockDust.ACTIVATING_DUST)){
					TileEntityDust ted = (TileEntityDust)worldObj.getBlockTileEntity(x+i, y, z+k);
					
					if(ted.dustEntID == this.entityId){
						dustPoints.add(new Integer[]{x+i,y,z+k});
						checkConnections(x+i,y,z+k);
					}
				} else if(blockID == DustMod.rutBlock.blockID){
					TileEntityRut ter = (TileEntityRut)worldObj.getBlockTileEntity(x+i, y, z+k);
					if(ter.dustEntID == this.entityId){
						rutPoints.add(new Integer[]{x+i,y,z+k});
						checkConnections(x+i,y,z+k);
					}
				}
			}
    		
    	}
    }
    
    /**
     * Returns true if any dust block under this entity's wing is powered by redstone
     * @return
     */
    public boolean isPowered(){
    	for(Integer[] i:dustPoints){
    		TileEntityDust ted = (TileEntityDust)worldObj.getBlockTileEntity(i[0], i[1], i[2]);
    		if(ted.isPowered()) return true;
    	}
    	return false;
    }
    
    private boolean hasDustPoint(int x,int y,int z){
    	for(Integer[] i:dustPoints){
    		if(i[0] == x && i[1] == y && i[2] == z)
    			return true;
    	}
    	return false;
    }
    
    public void shineRadius(float f, double d, double d1, double d2)
    {
        shineRadius(f, d, d1, d2, 2, "reddust");
    }

    public void shineRadius(float f, double d, double d1, double d2,
            int i, String s)
    {
        d++;
        Random random = new Random();
        float f1 = 0.7F;
        float f2 = 0;

        
        
        for (int j = 1; j <= 2; j++)
        {
            float f3 = f / (float) j;

            for (double d3 = 0.0D; d3 < (Math.PI / 2D); d3 += 0.1D)
            {
                float f4 = (float) ((double) f3 * Math.cos(d3));
                float f5 = (float) ((double) f3 * Math.sin(d3));

                for (int k = 0; k < i; k++)
                {
                    worldObj.spawnParticle(s, ((float) posX + f4 + random.nextFloat() * f1) - 0.5F, (float) posY + f2, ((float) posZ + f5 + random.nextFloat() * f1) - 0.5F, d, d1, d2);
                }

                for (int l = 0; l < i; l++)
                {
                    worldObj.spawnParticle(s, (((float) posX - f4) + random.nextFloat() * f1) - 0.5F, (float) posY + f2, ((float) posZ + f5 + random.nextFloat() * f1) - 0.5F, d, d1, d2);
                }

                for (int i1 = 0; i1 < i; i1++)
                {
                    worldObj.spawnParticle(s, ((float) posX + f4 + random.nextFloat() * f1) - 0.5F, (float) posY + f2, (((float) posZ - f5) + random.nextFloat() * f1) - 0.5F, d, d1, d2);
                }

                for (int j1 = 0; j1 < i; j1++)
                {
                    worldObj.spawnParticle(s, (((float) posX - f4) + random.nextFloat() * f1) - 0.5F, (float) posY + f2, (((float) posZ - f5) + random.nextFloat() * f1) - 0.5F, d, d1, d2);
                }
            }
        }
    }

    public void shineRadiusSphere(float f, double d, double d1, double d2)
    {
        d--;
        Random random = new Random();
        float f1 = 0.7F;
        float f2 = f;
        String s = "reddust";
        float f3 = 0;

        double max = Math.PI/2D;
        double iter = 0.05;
        
//        double[] locations = new double[12288];
        
        for (double d3 = 0.0D; d3 < (3.14 / 2D); d3 += 0.05)
        {
            float f4 = (float) Math.sin(d3);
            float f5 = (float) Math.cos(d3);
            float f6 = f2 * f4;

            for (double d4 = 0.0D; d4 < (3.14/ 2D); d4 += 0.05)
            {
                double[] locations = new double[12];
                int i = 0;
                float f7 = (float) Math.sin(d4);
                float f8 = (float) Math.cos(d4);
                float f9 = f2 * f8 * f5;
                float f10 = f2 * f7 * f5;
                locations[i] = ((double) posX + f9 + random.nextFloat() * f1) - 0.5D;
                locations[i+1] = (double) posY + f3 + f6 + random.nextFloat() * f1;
                locations[i+2] = ((double) posZ + f10 + random.nextFloat() * f1) - 0.5D;
                i+=3;

                locations[i] = (double)((((float) posX - f9) + random.nextFloat() * f1) - 0.5F);
                locations[i+1] = (double) ((float) posY + f3 + f6 + random.nextFloat() * f1);
                locations[i+2] = (double) (((float) posZ + f10 + random.nextFloat() * f1) - 0.5F);
                i+=3;

                locations[i] = (double)(((float) posX + f9 + random.nextFloat() * f1) - 0.5F);
                locations[i+1] = (double) ((float) posY + f3 + f6 + random.nextFloat() * f1);
                locations[i+2] = (double) ((((float) posZ - f10) + random.nextFloat() * f1) - 0.5F);
                i+=3;

                locations[i] = (double)((((float) posX - f9) + random.nextFloat() * f1) - 0.5F);
                locations[i+1] = (double) ((float) posY + f3 + f6 + random.nextFloat() * f1);
                locations[i+2] = (double) ((((float) posZ - f10) + random.nextFloat() * f1) - 0.5F);
                i+=3;
                DustMod.spawnParticles(worldObj, s, locations, d, d1, d2, 1, 0.01,0.01,0.01);
            }
        }
//        System.out.println("You bitch " + i + " " + locations.length);
        
    }
    
    
    /**
     * Checks if summoning player is allowed to modify blocks at that location
     * @param x
     * @param y
     * @param z
     * @return true if player can edit block, false otherwise
     */
    public boolean canAlterBlock(int x, int y, int z){

    	
    	EntityPlayer p = worldObj.getPlayerEntityByName(summonerUN);
    	if(p != null && !worldObj.canMineBlock(p, x, y, z)){
    		return false;
    	}
    	
    	return y >- 0 && y < worldObj.getHeight();
    }

    public void onRightClick(TileEntityDust ted, EntityPlayer p)
    {
        this.event.onRightClick(this, ted, p);
    }

    @Override
    public void kill()
    {
        if (event != null)
        {
            event.onUnload(this);
        }

        if (dustPoints == null)
        {
            super.kill();
            return;
        }

        for (Integer[] i : dustPoints)
        {
            int id = worldObj.getBlockId(i[0], i[1], i[2]);
            int bid = 0;

            if (DustMod.isDust(id))
            {
                worldObj.setBlockMetadataWithNotify(i[0], i[1], i[2], BlockDust.DEAD_DUST,3);
            }
        }

        super.kill();
    }

    /**
     * Used to slowly kill the rune. During fading, the animations still go but
     * no ticks are called to the event. Typically used when a rune is finished and ends
     */
    public void fade()
    {
        fade = true;
    }


    /**
     * Immediately kills the rune and spawns smoke particles. Typically used to 
     * indicate a failed sacrifice or something similar.
     */
    public void fizzle()
    {
    	doFizzle = true;
    }

    public int getX()
    {
        return (int) Math.floor(this.posX);
    }

    public int getY()
    {
        return (int) Math.floor(this.posY - EntityDust.yOffset);
    }

    public int getZ()
    {
        return (int) Math.floor(this.posZ);
    }
    
    public int getRotation(){
    	return rot;
    }

    public void setFuel(int fuel)
    {
        this.fuelAmount = fuel;
    }

    public int getFuel()
    {
        return this.fuelAmount;
    }
    public boolean fueledExternally = false;

    public boolean isFueledExternally()
    {
        return fueledExternally;
    }
//    @Override
//    public boolean isEntityInsideOpaqueBlock() {
//        return false;
//    }
//
    


    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }
}
