package dustmodtestpack.inscriptions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import dustmod.DustEvent;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;
import dustmod.VoidStorageManager;

public class VoidInscription extends InscriptionEvent {

	public VoidInscription(int[][] design, String idName, String properName,
			int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101 -TestPack");
		this.setDescription("Description:\n"
				+ "Automatically store all picked-up items into the void. Use the Void Storage rune to summon them back.");
		this.setNotes("Sacrifice:\n" + "TBD");
	}

	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		// TODO Auto-generated method stub
		return super.callSacrifice(rune, e, item);
	}

	@Override
	public ItemStack onItemPickup(EntityLiving wearer, ItemStack insc,
			ItemStack pickedup) {
		VoidStorageManager.addItemToVoidInventory(
				((EntityPlayer) wearer).username, pickedup);
		ItemStack rtn = pickedup.copy();
		rtn.stackSize = 0;
		return rtn;
	}

}
