package dustmodtestpack.inscriptions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import dustmod.InscriptionEvent;

public class RespawnInscription extends InscriptionEvent {

	public RespawnInscription(int[][] design, String idName, String properName,
			int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101");
		this.setDescription("Description:\n" +
				"Set a temporary hope-point where you can teleport back to. " +
				"The coordinates are set to wherever you are standing when you put it on. " +
				"Shift+RightClick the ground at your feet with a bare hand to activate");
		this.setNotes("Sacrifice:\n" +
				"TBD");
	}
	
	public void onUpdate(EntityLiving wearer, ItemStack item, boolean[] buttons){
//		wearer.setPositionAndUpdate(wearer.posX,  wearer.posY+2, wearer.posZ);
		
		if(wearer.isSneaking() && buttons[1] && wearer.rotationPitch > 80){
			int x,y,z;
			NBTTagCompound tag = item.getTagCompound();
			x = tag.getInteger("RespawnX");
			y = tag.getInteger("RespawnY");
			z = tag.getInteger("RespawnZ");
			
			wearer.setPositionAndUpdate(x, y, z);
			wearer.fallDistance = 0;
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
	
//	@Override
//	public int getPreventedDamage(EntityLiving wearer, ItemStack item,
//			DamageSource source, int damage) {
//		int dmg = damage;
//		int rem = damage - wearer.getHealth() +1;
//		if(wearer.getHealth() - damage <= 1){
////			System.out.println("GO!");
//			wearer.heal(rem);
//			respawn(wearer,item);
//		}
////		System.out.println("damage " + damage + " " + rem + " " + wearer.getHealth() + " " + dmg);
//		return damage;
//	}
	
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
