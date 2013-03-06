package dustmod.inscriptions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;
import dustmod.InscriptionManager;

public class RocketLaunch extends InscriptionEvent {
public double power;
public int level;
	
	public RocketLaunch(int[][] design, String idName, String properName,
			int id, int level) {
		super(design, idName, properName, id);
		
		this.level = level;
		
		this.setAuthor("billythegoat101");
		
		switch(level){
		case 1:
			this.power = 1.15;
			this.setDescription("Description:\n" +
					"Shift+Left Click with a bare hand to launch yourself in that direction!\n" +
					"It will protect from fall damage but only that which is caused by the launch");
			this.setNotes("Sacrifice:\n" +
					"-4xFeathers + 1xFirework + 5XP");
			break;
		case 2:
			this.power = 1.85;
			this.setDescription("Description:\n" +
					"Shift+Left Click with a bare hand to launch yourself in that direction!\n" +
					"It will protect from fall damage but only that which is caused by the launch\n" +
					"Level II provides extra height and duration as well as adding an extra half block to your normal jumping height.");
			this.setNotes("Sacrifice:\n" +
					"-1xLeapI (Fully charged) + 1xSlimeEgg + 7XP");
			break;
		}
	}
	
	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		ItemStack[] req = new ItemStack[0];
		int xp = 0;
		if(level == 1){
			req = new ItemStack[]{new ItemStack(Item.feather,4), new ItemStack(Item.firework.itemID, 1, -1)};
			xp = 5;
		}else if(level == 2){
			ItemStack leapI = new ItemStack(DustMod.wornInscription.itemID, 1, 0);
			InscriptionManager.setEvent(leapI, "leapI");
			req = new ItemStack[]{leapI, new ItemStack(Item.monsterPlacer.itemID, 1, 55)};
			xp = 7;
		}
		req = rune.sacrifice(e, req);
		if(!rune.checkSacrifice(req)) return false;
		if(!rune.takeXP(e, xp));
		item.setItemDamage(0);
		return true;
	}
	
	@Override
	public void onUpdate(EntityLiving wearer, ItemStack item, boolean[] buttons) {
		super.onUpdate(wearer, item, buttons);

		if(wasFalling(item) && wearer.ticksExisted - item.getTagCompound().getInteger("lastTele") > 20){
			if(wearer.onGround){
				setFalling(item,false);
			}
		}
		if(!wearer.onGround && wasFalling(item)){
			wearer.fallDistance = 0;
//			wearer.jumpMovementFactor = 0.5F;
			wearer.motionX *= 1.042;
			wearer.motionZ *= 1.042;
		}else {
			wearer.jumpMovementFactor = 0.02F;
		}
		
		if (((EntityPlayer)wearer).getCurrentEquippedItem() == null && wearer.isSneaking() && wearer.onGround) {
			double[] testLoc = new double[3];
			
			Vec3 look = wearer.getLookVec();
			testLoc[0] = look.xCoord; 
			testLoc[1] = look.yCoord; 
			testLoc[2] = look.zCoord;
			double strength = power;
			if(buttons[0]){
				onLaunch(item,wearer);
				wearer.addVelocity(
						-wearer.motionX+testLoc[0]*strength*1.000000000000000000000000006, 
						-wearer.motionY+testLoc[1]*strength, 
						-wearer.motionZ+testLoc[2]*strength*1.000000000000000000000000006);
				
				wearer.fallDistance = 0.0F;
				setFalling(item,true);
				DustMod.sendEntMotionTraits(wearer);
				
				this.damage((EntityPlayer)wearer, item, level == 1 ? 5:2);
			}
			recordMouseClick(item,buttons[0]);
		}
	}
	
	private void recordMouseClick(ItemStack item, boolean mouse){
		item.getTagCompound().setBoolean("mouse", mouse);
	}
	private boolean getLastMouse(ItemStack item){
		if(item.getTagCompound().hasKey("mouse"))
			return item.getTagCompound().getBoolean("mouse");
		else {
			item.getTagCompound().setBoolean("mouse", false);
			return false;
		}
	}
	
	private void onLaunch(ItemStack item, EntityLiving wearer){
		item.getTagCompound().setInteger("lastTele", wearer.ticksExisted);
	}
	
	private void setFalling(ItemStack item, boolean val){
		item.getTagCompound().setBoolean("falling", val);
	}

	private boolean wasFalling(ItemStack item){
		if(item.getTagCompound().hasKey("falling"))
			return item.getTagCompound().getBoolean("falling");
		else {
			item.getTagCompound().setBoolean("falling", false);
			return false;
		}
	}

}
