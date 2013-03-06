package dustmod.inscriptions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;

public class BounceInscription extends InscriptionEvent {

	public BounceInscription(int[][] design, String idName, String properName,
			int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101");
		this.setDescription("Description:\n"
				+ "Greatly reduce your fall damage by automatically bouncing back up upon landing.\n" +
				"Cancel the effect by crouching.");
		this.setNotes("Sacrifice:\n" +
				"-8xFeathers + 1xLeatherBoots");
	}

	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		ItemStack[] req = new ItemStack[]{new ItemStack(Item.slimeBall,8), new ItemStack(Item.bootsLeather, 1)};
		req = rune.sacrifice(e, req);
		if(!rune.checkSacrifice(req)) return false;
		item.setItemDamage(0);
		return true;
	}
	
	@Override
	public void onUpdate(EntityLiving wearer, ItemStack item, boolean[] buttons) {
		super.onUpdate(wearer, item, buttons);
		if(!wearer.onGround){
			if(getLastYVel(item) > wearer.motionY)
				setFalling(item,true, getFallDist(item) + wearer.fallDistance, (float)wearer.motionX, (float)wearer.motionY, (float)wearer.motionZ);
			wearer.fallDistance = 0;
		}else if(wearer.isSneaking()){
			wearer.fallDistance = getFallDist(item);
			setFalling(item,false,0,0,0,0);
		}else if(wasFalling(item) && getLastYVel(item) < -0.75f){
			wearer.fallDistance = getFallDist(item)/2f;
			wearer.motionX = -getLastXVel(item)*0.76D;
			wearer.motionY = -getLastYVel(item)*0.76D;
			wearer.motionZ = -getLastZVel(item)*0.76D;
			DustMod.sendEntMotionTraits(wearer);
			setFalling(item,false,0,0,0,0);
			
			this.damage((EntityPlayer)wearer, item, 8);
		}
	}
	
	private void setFalling(ItemStack item, boolean val, float dist, float xVel, float yVel, float zVel){
		item.getTagCompound().setBoolean("falling", val);
		item.getTagCompound().setFloat("fallDist", dist);
		item.getTagCompound().setFloat("xVel", xVel);
		item.getTagCompound().setFloat("yVel", yVel);
		item.getTagCompound().setFloat("zVel", zVel);
	}

	private boolean wasFalling(ItemStack item){
		if(item.getTagCompound().hasKey("falling"))
			return item.getTagCompound().getBoolean("falling");
		else {
			item.getTagCompound().setBoolean("falling", false);
			return false;
		}
	}

	private float getFallDist(ItemStack item){
		if(item.getTagCompound().hasKey("fallDist"))
			return item.getTagCompound().getFloat("fallDist");
		else {
			item.getTagCompound().setFloat("fallDist", 0f);
			return 0f;
		}
	}

	private float getLastXVel(ItemStack item){
		if(item.getTagCompound().hasKey("xVel"))
			return item.getTagCompound().getFloat("xVel");
		else {
			item.getTagCompound().setFloat("xVel", 0);
			return 0;
		}
	}

	private float getLastYVel(ItemStack item){
		if(item.getTagCompound().hasKey("yVel"))
			return item.getTagCompound().getFloat("yVel");
		else {
			item.getTagCompound().setFloat("yVel", 0);
			return 0;
		}
	}

	private float getLastZVel(ItemStack item){
		if(item.getTagCompound().hasKey("zVel"))
			return item.getTagCompound().getFloat("zVel");
		else {
			item.getTagCompound().setFloat("zVel", 0);
			return 0;
		}
	}

}
