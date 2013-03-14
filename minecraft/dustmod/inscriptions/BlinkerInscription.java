package dustmod.inscriptions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;
import dustmod.InscriptionManager;

public class BlinkerInscription extends InscriptionEvent {

	
	public BlinkerInscription(int[][] design, String idName, String properName,
			int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101");
		this.setDescription("Description:\n" +
				"Blink like an enderman! Shift+RightClick with a bare hand to teleport. It will cost 1 heart per blink but you will not take fall damage." +
				"Level II enables precision teleporting and extra duration.");
		this.setNotes("Sacrifice:\n" +
				"-1xBlinkI (Fully charged) + 8xEnderPearl, 4xObsidian, 8xEndStone + 10XP");
	}
	
	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		ItemStack[] req = new ItemStack[0];
		int xp = 10;
		
		ItemStack blinkI = new ItemStack(DustMod.wornInscription.itemID, 1, 0);
		InscriptionManager.setEvent(blinkI, "leapI");
		req = new ItemStack[]{blinkI, new ItemStack(Item.enderPearl, 8)};
		
		req = rune.sacrifice(e, req);
		if(!rune.checkSacrifice(req)) return false;
		if(!rune.takeXP(e, xp));
		item.setItemDamage(0);
		return true;
	}
	
	@Override
	public void onUpdate(EntityLiving wearer, ItemStack item, boolean[] buttons) {
		super.onUpdate(wearer, item, buttons);
		
//		System.out.println("huh " + wasFalling(item) +  " " + wearer.posY + " " + wearer.isCollidedVertically + " " + wearer.onGround);

		if(wasFalling(item) && wearer.ticksExisted - item.getTagCompound().getInteger("lastTele") > 20){
			if(wearer.onGround){
				setFalling(item,false);
			}
		}
		if(!wearer.onGround && wasFalling(item)){
			wearer.fallDistance = 5F;
		}
		
		if (((EntityPlayer)wearer).getCurrentEquippedItem() == null && wearer.isSneaking()) {
			double[] testLoc = new double[3];
			
			Vec3 look = wearer.getLookVec();
			double dist = 11D;
			System.out.println();
			testLoc[0] = wearer.posX + dist*look.xCoord; 
			testLoc[1] = wearer.posY + dist*look.yCoord + wearer.getEyeHeight(); 
			testLoc[2] = wearer.posZ + dist*look.zCoord;
			
			int[] target = getClickedBlock(wearer,item);
			double x = wearer.posX;
			double y = wearer.posY;
			double z = wearer.posZ;
			
			if(target != null){
				double distT = (target[0] - x)*(target[0]-x) + (target[1]-y)*(target[1] - y) + (target[2]-z)*(target[2]-z);
//				double distL = (testLoc[0] - x)*(testLoc[0]-x) + (testLoc[1]-y)*(testLoc[1] - y) + (testLoc[2]-z)*(testLoc[2]-z);
				if(distT < dist*dist){
					dist = Math.sqrt(distT)-1;
					testLoc[0] = wearer.posX + dist*look.xCoord; 
					testLoc[1] = wearer.posY + dist*look.yCoord + wearer.getEyeHeight(); 
					testLoc[2] = wearer.posZ + dist*look.zCoord;
				}
			}

			double newY = testLoc[1];
			if(wearer.worldObj.getBlockId((int)testLoc[0], (int)testLoc[1], (int)testLoc[2]) != 0){
				for(int i = 0; i < 3; i++){
					if(wearer.worldObj.getBlockId((int)testLoc[0], (int)Math.floor(testLoc[1])+i, (int)testLoc[2]) == 0){
						newY = Math.floor(testLoc[1])+i;
						break;
					}
				}
			}else{
				for(int i = 0; i < 64; i++){
					if(wearer.worldObj.getBlockId((int)testLoc[0], (int)Math.floor(testLoc[1])-i, (int)testLoc[2]) != 0){
						newY = Math.floor(testLoc[1])-i+1;
						break;
					}
				}
			}
			
			double rad =0.5;
			boolean canTele = canTele(item,wearer);
			canTele &= dist > 2.5;
			
			DustMod.spawnParticles(wearer.worldObj, "reddust", testLoc, canTele ? -1:1, canTele ? 0.6:0, canTele ? 1:0, 6 / (dist < 3 ? 6:1), 0.1, 0.1,0.1);
			DustMod.spawnParticles(wearer.worldObj, "reddust", Math.floor(testLoc[0]) +0.5, newY, Math.floor(testLoc[2]) +0.5, canTele ? -1:0, canTele ? 0.8:0, canTele ? 0.8:0, 2, 0.5, 0.1,0.5);
			
			if(buttons[0] && !getLastMouse(item) && canTele){
				onTele(item,wearer);
				//Play at both before and after
	            wearer.worldObj.playSoundEffect(wearer.posX, wearer.posY, wearer.posZ, "mob.endermen.portal", 1.0F, 1.0F);
	            wearer.playSound("mob.endermen.portal", 1.0F, 1.0F);
	            
				wearer.setPositionAndUpdate(testLoc[0], (testLoc[1] > newY ? testLoc[1]:newY), testLoc[2]);
				wearer.fallDistance = 5.0F;
				
	            wearer.worldObj.playSoundEffect(wearer.posX, wearer.posY, wearer.posZ, "mob.endermen.portal", 1.0F, 1.0F);
	            wearer.playSound("mob.endermen.portal", 1.0F, 1.0F);
	            this.damage((EntityPlayer)wearer, item, 10);
				setFalling(item,true);
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
	
	private void onTele(ItemStack item, EntityLiving wearer){
		item.getTagCompound().setInteger("lastTele", wearer.ticksExisted);
	}
	private boolean canTele(ItemStack item, EntityLiving wearer){
		int last = 0;
		if(item.getTagCompound().hasKey("mouse"))
			last = item.getTagCompound().getInteger("lastTele");
		else {
			item.getTagCompound().setInteger("lastTele", wearer.ticksExisted);
			last = wearer.ticksExisted;
		}
		
		if(wearer.ticksExisted - last > 10){
			return true;
		}else {
//			System.out.println("penis " + wearer.ticksExisted + " " + last);
			if(wearer.ticksExisted < last){
				onTele(item,wearer);
				return true;
			}
			return false;
		}

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
	
	public int[] getClickedBlock(Entity wearer, ItemStack item){
		MovingObjectPosition click = DustMod.getWornInscription().getMovingObjectPositionFromPlayer(wearer.worldObj, (EntityPlayer)wearer, false);
		if(click != null && click.typeOfHit == EnumMovingObjectType.TILE){
			int tx = click.blockX;
			int ty = click.blockY;
			int tz = click.blockZ;
			return new int[]{tx,ty,tz};
		}
		return null;
	}

}
