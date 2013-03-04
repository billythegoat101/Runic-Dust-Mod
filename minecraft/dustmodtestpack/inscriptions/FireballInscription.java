package dustmodtestpack.inscriptions;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import dustmod.DustEvent;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;

public class FireballInscription extends InscriptionEvent {

	public FireballInscription(int[][] design, String idName, String properName,
			int id) {
			super(design, idName, properName, id);
			this.setAuthor("billythegoat101");
			this.setDescription("Description:\n" +
					"Set your foes ablaze with fireballs!. LeftClick with a bare hand while lit on fire to activate.");
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
			EntityPlayer player = (EntityPlayer)wearer;

			
			
			if(player.isBurning() || wearer.worldObj.isMaterialInBB(wearer.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.fire)){
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 10, 3));
				if (((EntityPlayer)wearer).getCurrentEquippedItem() == null && buttons[0] && !wasClickDown(item)) {
			        if (!wearer.worldObj.isRemote)
			        {
			        	double ox =  - Math.cos(wearer.rotationYaw * 3.14159/180D)/3;
			        	double oz =  - Math.sin(wearer.rotationYaw * 3.14159/180D)/3;
			        	
			        	Vec3 look = wearer.getLookVec();
			        	EntitySmallFireball fire = new EntitySmallFireball(wearer.worldObj, wearer, look.xCoord, look.yCoord, look.zCoord);
			        	double par3 = look.xCoord;
			        	double par5 = look.yCoord;
			        	double par7 = look.zCoord;
			        	
			            fire.shootingEntity = wearer;
			            fire.setLocationAndAngles(wearer.posX, wearer.posY, wearer.posZ, wearer.rotationYaw, wearer.rotationPitch);
			            fire.setPosition(fire.posX+ox, fire.posY + wearer.getEyeHeight()-0.2, fire.posZ+oz);
			            fire.yOffset = 0.0F;
			            fire.motionX = fire.motionY = fire.motionZ = 0.0D;
			            double var9 = (double)MathHelper.sqrt_double(par3 * par3 + par5 * par5 + par7 * par7);
			            fire.accelerationX = par3 / var9 * 0.1D;
			            fire.accelerationY = par5 / var9 * 0.1D;
			            fire.accelerationZ = par7 / var9 * 0.1D;
			            wearer.worldObj.spawnEntityInWorld(fire);
			        }
				}
			}
			
			setClickDown(item,buttons[0]);
		}
		
		public boolean wasClickDown(ItemStack item){
			return item.getTagCompound().getBoolean("wasClicked");
		}
		
		public void setClickDown(ItemStack item, boolean val){
			item.getTagCompound().setBoolean("wasClicked", val);
		}

}
