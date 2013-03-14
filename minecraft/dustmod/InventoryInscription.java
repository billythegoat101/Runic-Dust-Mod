package dustmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryInscription implements IInventory {

	
	public int[] inv;
	public ItemStack[] items;
	
	public int width;
	public int height;
	
	public NBTTagCompound tag;
	
	public InventoryInscription(ItemStack inscription){
		this(inscription, 16,16);
	}
	
	public InventoryInscription(ItemStack inscription, int width, int height){
		this.width = width;
		this.height = height;
		inv = new int[width*height];
		items = new ItemStack[10];

		tag = inscription.getTagCompound();
		
        if (tag == null)
        {
        	tag = new NBTTagCompound();
        	inscription.setTagCompound(tag);
        }else{
        	for(int i = 0; i < 16; i++){
        		for(int j = 0; j < 16; j++){
        			inv[i*16 + j] = tag.getInteger(i + "," + j);
        		}
        	}
        }
		
	}
	
	@Override
	public int getSizeInventory() {
		return items.length + inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int loc) {
		if(loc < 10) return items[loc];
		return new ItemStack(DustMod.inscription, 1, inv[loc-10]);
	}

	@Override
	public ItemStack decrStackSize(int loc, int amt) {
		if(loc < 10) {
			items[loc].stackSize -= amt;
			if(items[loc].stackSize <= 0) items[loc] = null;
			return items[loc];
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int loc) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int loc, ItemStack value) {
		if(loc < 10) items[loc] = value;
		else {
			if(value != null) inv[loc-10] = value.getItemDamage();
			else inv[loc-10] = -1;
		}
	}

	public boolean canEdit(){
		if(tag.hasKey("dried")) return !tag.getBoolean("dried");
		tag.setBoolean("dried",false);
		return true;
	}
	
	
	@Override
	public String getInvName() {
		return "RunicInscription";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void onInventoryChanged() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {

	}

	@Override
	public void closeChest() {

	}

	@Override
	public boolean func_94042_c() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean func_94041_b(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}

}
