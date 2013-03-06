package dustmod.inscriptions;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import dustmod.DustEvent;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;

public class RespawnInscription extends InscriptionEvent {

	public RespawnInscription(int[][] design, String idName, String properName,
			int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101");
		this.setDescription("Description:\n" +
				"Set a temporary home-point where you can teleport back to. " +
				"The coordinates are set to wherever you are standing when you put it on. " +
				"Shift+RightClick the ground at your feet with a bare hand to activate");
		this.setNotes("Sacrifice:\n" +
				"-1xLapisBlock + 2xEnderPearl");
	}
	
	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		ItemStack[] req = new ItemStack[]{new ItemStack(Block.blockLapis,1), new ItemStack(Item.enderPearl, 2)};
		req = rune.sacrifice(e, req);
		if(!rune.checkSacrifice(req)) return false;
		item.setItemDamage(0);
		return true;
	}
	
	public void onUpdate(EntityLiving wearer, ItemStack item, boolean[] buttons){
//		wearer.setPositionAndUpdate(wearer.posX,  wearer.posY+2, wearer.posZ);
		
		if(wearer.isSneaking() && buttons[1] && wearer.rotationPitch > 80){
			int x,y,z;
			NBTTagCompound tag = item.getTagCompound();
			x = tag.getInteger("RespawnX");
			y = tag.getInteger("RespawnY");
			z = tag.getInteger("RespawnZ");
			
			wearer.setPositionAndUpdate(x+0.5, y, z+0.5);
			wearer.fallDistance = 0;
			this.damage((EntityPlayer)wearer, item, 20);
		}
	}

	public void onCreate(EntityLiving creator, ItemStack item){
	}
	
	@Override
	public void onEquip(EntityLiving wearer, ItemStack item) {
		super.onEquip(wearer, item);

		int x = MathHelper.floor_double(wearer.posX);
		int y = MathHelper.floor_double(wearer.posY);
		int z = MathHelper.floor_double(wearer.posZ);
		
		item.getTagCompound().setInteger("RespawnX" , x);
		item.getTagCompound().setInteger("RespawnY" , y);
		item.getTagCompound().setInteger("RespawnZ" , z);
	}
	
	/**
	 * Actually respawns someone instead of bringing to pseudo respawn point 
	 * @param wearer
	 * @param item
	 */
	public void respawn(EntityLiving wearer, ItemStack item){
		NBTTagCompound tag = item.getTagCompound();
		double x = 0,y = 128,z = 0;
		if(wearer instanceof EntityPlayer){
			ChunkCoordinates chunk = wearer.worldObj.getSpawnPoint();
			if(chunk != null){
				x = (double)chunk.posX + 0.5;
				y = (double)chunk.posY + 1;
				z = (double)chunk.posZ + 0.5;
			}
		}else if(!tag.hasKey("RespawnX")){
			x = 0;
			y = 128;
			z = 0;
		} else{
			x = tag.getInteger("RespawnX");
			y = tag.getInteger("RespawnY");
			z = tag.getInteger("RespawnZ");
		}
//		wearer.setLocationAndAngles(x, y, z, 0, 0);//(x, y, z);
		wearer.setPositionAndUpdate(x, y, z);
	}
}
