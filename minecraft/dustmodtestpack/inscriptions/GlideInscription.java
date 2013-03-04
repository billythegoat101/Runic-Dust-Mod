package dustmodtestpack.inscriptions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;

public class GlideInscription extends InscriptionEvent {

	private int power = 1;
	private double motionYMul;
	private float jumpFactor;
	
	public GlideInscription(int power, int[][] design, String idName, String properName,
			int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101");
		this.setDescription("Description:\n" +
				"Hold shift while falling to glide and prevent fall damage.");
		
		this.power = power;
		switch(power){
		
		default:
		case 1:
			motionYMul = 0.25;
			jumpFactor = 0.61F;
			this.setNotes("Sacrifice:\n" +
					"2xFeather + 5XP");
			break;
		case 2:
			motionYMul = 0.1;
			jumpFactor = 0.89F;
			this.setNotes("Sacrifice:\n" +
					"2xFeather + 1xGoldIngot + 7XP");
			break;
		}
	}
	
	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		// TODO Auto-generated method stub
		
		if(power == 1){
			ItemStack[] req = new ItemStack[]{new ItemStack(Item.feather,2)};
			rune.sacrifice(e, req);
			
			if(!rune.checkSacrifice(req) || !rune.takeXP(e, 5)){
				return false;
			}
			item.setItemDamage(0);
			return true;
		}else if(power == 2){
			ItemStack[] req = new ItemStack[]{new ItemStack(Item.feather,2), new ItemStack(Item.ingotGold,1)};
			rune.sacrifice(e, req);
			
			if(!rune.checkSacrifice(req) || !rune.takeXP(e, 7)){
				return false;
			}
			item.setItemDamage(0);
			return true;
		}
		return false;
	}
	
	@Override
	public void onUpdate(EntityLiving wearer, ItemStack item, boolean[] buttons) {
		super.onUpdate(wearer, item, buttons);
		
		EntityPlayer player = (EntityPlayer)wearer;
		if(player.motionY < 0 && !player.onGround && player.isSneaking() && !player.capabilities.isFlying){
			DustMod.log("FALL " + player.motionY);
//			if(player.motionY < -motionYMul){
//				player.motionY = -motionYMul;
//			}
//			player.motionY *= motionYMul;
			if(player.motionY < -0.2){
				player.motionY *= 0.88;
			}
			player.fallDistance = 0;
			player.jumpMovementFactor = jumpFactor;
			DustMod.sendEntMotionTraits(wearer);
			
			this.damage((EntityPlayer)wearer, item, 2/power);
		} 
		else if(player.jumpMovementFactor == jumpFactor){
			player.jumpMovementFactor = 0.02f; //Default playerjump movement factor
			DustMod.sendEntMotionTraits(wearer);
		}
	}
}
