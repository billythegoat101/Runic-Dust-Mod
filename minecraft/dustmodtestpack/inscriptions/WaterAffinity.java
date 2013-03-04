package dustmodtestpack.inscriptions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;

public class WaterAffinity extends InscriptionEvent {
	public WaterAffinity(int[][] design, String idName, String properName,
			int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101 -TestPack");
		this.setDescription("Description:\n"
				+ "Currently: Hold shift while on waters surface to hop across it.");
		this.setNotes("Sacrifice:\n" + "TBD");
	}

	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		// TODO Auto-generated method stub
		return super.callSacrifice(rune, e, item);
	}

	@Override
	public void onUpdate(EntityLiving wearer, ItemStack item, boolean[] buttons) {
//		super.onUpdate(wearer, item, buttons);
		EntityPlayer player = (EntityPlayer)wearer;
//		player.jumpMovementFactor = 0.02f;



		Side side = FMLCommonHandler.instance().getSide();
		boolean isServer = side == Side.SERVER;
		int headBID = player.worldObj.getBlockId((int)player.posX, (int)player.posY + 2, (int) player.posZ);
		boolean inWater = player.isInWater();
		
		boolean wasInWater = (item.getTagCompound().hasKey("inWater") ? item.getTagCompound().getBoolean("inWater"):false) || headBID != 0;
		item.getTagCompound().setBoolean("inWater", player.isInWater());
		
		if((wearer.onGround || player.capabilities.isFlying) && isTriggered(item)){
			player.jumpMovementFactor = 0.02f;
			setTriggered(item,false);
			DustMod.sendEntMotionTraits(wearer);
		}else if(isTriggered(item) && isServer && !inWater){
			player.jumpMovementFactor = 0.71F;
			player.motionX *= 6.5D;
			player.motionZ *= 6.5D;
			if(player.motionY < 0) player.motionY *= 0.85;
			DustMod.sendEntMotionTraits(wearer);
		}
		if(inWater && !wasInWater && player.motionY < 0 && !player.isSneaking() && !player.capabilities.isFlying){
			player.motionY = isServer ? 0.68:0.6;
			player.fallDistance = 0;
			
			player.jumpMovementFactor = 0.61F;
			setTriggered(item, true);
			DustMod.sendEntMotionTraits(wearer);
		}
	}
	
	@Override
	public ItemStack onItemPickup(EntityLiving wearer, ItemStack insc,
			ItemStack pickedup) {
//		VoidStorageManager.addItemToVoidInventory(
//				((EntityPlayer) wearer).username, pickedup);
//		ItemStack rtn = pickedup.copy();
//		rtn.stackSize = 0;
		return super.onItemPickup(wearer, insc, pickedup);
	}
	
	public void setTriggered(ItemStack item, boolean triggered){
		NBTTagCompound tag = item.getTagCompound();
		if(tag == null){
			tag = new NBTTagCompound();
			item.setTagCompound(tag);
		}
		
		tag.setBoolean("skipperTriggered", triggered);
	}
	
	public boolean isTriggered(ItemStack item){
		NBTTagCompound tag = item.getTagCompound();
		if(tag == null){
			tag = new NBTTagCompound();
			item.setTagCompound(tag);
		}

		return tag.hasKey("skipperTriggered") && tag.getBoolean("skipperTriggered");
	}
}
