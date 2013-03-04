package dustmodtestpack.inscriptions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;

public class RocketLaunch extends InscriptionEvent {
public int power;
	
	public RocketLaunch(int[][] design, String idName, String properName,
			int id, int power) {
		super(design, idName, properName, id);
		this.power = power;
		this.setAuthor("billythegoat101");
		this.setDescription("Description:\n" +
				"Fire away!");
		this.setNotes("Sacrifice:\n" +
				"TBD");
	}
	
	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		// TODO Auto-generated method stub
		return super.callSacrifice(rune, e, item);
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
			double dist = 9D;
			testLoc[0] = look.xCoord; 
			testLoc[1] = look.yCoord; 
			testLoc[2] = look.zCoord;
			double strength = 3.12D;
			if(buttons[0]){
				System.out.println("hrm " + wearer.motionY + " " + wearer.worldObj.isRemote);
				onTele(item,wearer);
				wearer.addVelocity(
						-wearer.motionX+testLoc[0]*strength*1.000000000000000000000000006, 
						-wearer.motionY+testLoc[1]*strength, 
						-wearer.motionZ+testLoc[2]*strength*1.000000000000000000000000006);
//				wearer.addVelocity(-wearer.motionX, -wearer.motionY, -wearer.motionZ);
//				wearer.motionX = testLoc[0]*strength*1.6;
//				wearer.motionY = testLoc[1]*strength;
//				wearer.motionZ = testLoc[2]*strength*1.6;

				System.out.println("grr " + wearer.motionY);
				wearer.fallDistance = 0.0F;
				setFalling(item,true);
//				if(!wearer.worldObj.isRemote){
//					((WorldServer)wearer.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(wearer, new Packet28EntityVelocity(wearer));
//				}
				DustMod.sendEntMotionTraits(wearer);
			}
			recordMouseClick(item,buttons[0]);
		}
	}
	
	@Override
		public void onRemoval(EntityLiving wearer, ItemStack item) {
			super.onRemoval(wearer, item);
			System.out.println("REMOVED");
		}
	
	@Override
		public void onEquip(EntityLiving wearer, ItemStack item) {
			super.onEquip(wearer, item);
			System.out.println("EQUIP");
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
