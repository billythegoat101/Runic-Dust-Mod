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
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;

public class RespawnInscription extends InscriptionEvent {

	public RespawnInscription(int[][] design, String idName, String properName,
			int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101");
		this.setDescription("Description:\n" +
				"Set a temporary home-point where you can teleport back to. " +
				"Right-Click with the item in hand to set the coordinates. " +
				"Shift+RightClick with a bare hand while looking at the ground to activate");
		this.setNotes("Sacrifice:\n" +
				"-1xQuartzBlock + 2xEnderPearl");
	}
	
	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		ItemStack[] req = new ItemStack[]{new ItemStack(Block.field_94339_ct,1), new ItemStack(Item.enderPearl, 2)};
		req = rune.sacrifice(e, req);
		if(!rune.checkSacrifice(req)) return false;
		item.setItemDamage(0);
		return true;
	}
	
	public void onUpdate(EntityLiving wearer, ItemStack item, boolean[] buttons){
//		wearer.setPositionAndUpdate(wearer.posX,  wearer.posY+2, wearer.posZ);

		NBTTagCompound tag = item.getTagCompound();
		if(wearer.isSneaking() && buttons[1] && wearer.rotationPitch > 80 && tag.hasKey("RespawnX")){
			int x,y,z;
			x = tag.getInteger("RespawnX");
			y = tag.getInteger("RespawnY");
			z = tag.getInteger("RespawnZ");
			
			if(wearer.getDistanceSq(x, y, z) <= 9D) return;

			//Play at both before and after
            wearer.worldObj.playSoundEffect(wearer.posX, wearer.posY, wearer.posZ, "mob.endermen.portal", 1.0F, 1.0F);
            wearer.playSound("mob.endermen.portal", 1.0F, 1.0F);
            
			wearer.setPositionAndUpdate(x+0.5, y, z+0.5);
			wearer.fallDistance = 0;
			this.damage((EntityPlayer)wearer, item, 20);
			
            wearer.worldObj.playSoundEffect(wearer.posX, wearer.posY, wearer.posZ, "mob.endermen.portal", 1.0F, 1.0F);
            wearer.playSound("mob.endermen.portal", 1.0F, 1.0F);
		}
	}

	public void onCreate(EntityLiving creator, ItemStack item){
	}
	
	private static long bullshit = 0;
	@Override
	public ItemStack onItemRightClick(EntityPlayer wearer, ItemStack item) {

		int x = MathHelper.floor_double(wearer.posX);
		int y = MathHelper.floor_double(wearer.posY);
		int z = MathHelper.floor_double(wearer.posZ);

		double lastX = item.getTagCompound().getInteger("RespawnX");
		double lastY = item.getTagCompound().getInteger("RespawnY");
		double lastZ = item.getTagCompound().getInteger("RespawnZ");
//		lastY = lastY - lastY%3 + y%3;
		
		double dist = (lastX - (double)x)*(lastX - (double)x) + (lastY - (double)y)*(lastY - (double)y) + (lastZ - (double)z)*(lastZ - (double)z);
		
		item.getTagCompound().setInteger("RespawnX" , x);
		item.getTagCompound().setInteger("RespawnY" , y);
		item.getTagCompound().setInteger("RespawnZ" , z);
		
		item.getTagCompound().setString("description", "Set to [" + x + "x, " + y + "y, " + z + "z]");
		if(dist > 1)
			wearer.sendChatToPlayer("Set return point to [" + x + "x, " + y + "y, " + z + "z]");
		bullshit = wearer.worldObj.getTotalWorldTime();
		return item;
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
			x = tag.getFloat("RespawnX");
			y = tag.getFloat("RespawnY");
			z = tag.getFloat("RespawnZ");
		}
//		wearer.setLocationAndAngles(x, y, z, 0, 0);//(x, y, z);
		wearer.setPositionAndUpdate(x, y, z);
	}
}
