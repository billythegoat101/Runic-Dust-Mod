package dustmod.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import dustmod.DustItemManager;
import dustmod.DustMod;
import dustmod.InscriptionGuiContainer;
import dustmod.InventoryInscription;
import dustmod.ItemInk;
import dustmod.PacketHandler;

public class GuiInscription extends GuiContainer {

	public InventoryInscription insc;
	public InventoryPlayer playerInv;
	public EntityPlayer player;
	
	public boolean newInscription = true;
	public boolean changed = false;
	
	public int buttonUpDelay = 0;
	
	
	public GuiInscription(EntityPlayer player, InventoryInscription insc) {
		super(new InscriptionGuiContainer(player.inventory, insc));
		 this.insc = insc;
		 this.playerInv = player.inventory;
		 this.player = player;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
        int texture = mc.renderEngine.getTexture(DustMod.path + "/inscription.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        ItemStack inscription = player.getCurrentEquippedItem();
        if(inscription == null || inscription.itemID != DustMod.inscription.shiftedIndex){
            this.mc.thePlayer.closeScreen();
        }
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int a, int b) {
		super.drawGuiContainerForegroundLayer(a, b);
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glDisable(GL11.GL_LIGHTING);
        int texture = mc.renderEngine.getTexture(DustMod.path + "/inscription.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);

		Slot info = this.inventorySlots.getSlot(0);
		if(info.getHasStack() && insc.canEdit()){
			ItemStack stack = info.getStack();
			int slot = stack.getItemDamage();// slot = 259;
			int id = stack.getItemDamage();
//			System.out.println("AKSNFASNF " + slot);
			Slot highlightSlot = this.inventorySlots.getSlot(slot+1);
			this.drawTexturedModalRect(highlightSlot.xDisplayPosition-2, highlightSlot.yDisplayPosition-2, 0, ySize+6, 20, 20);
		}
		
		for(int x = 0; x < 16; x++){
			for(int y = 0; y < 16; y++){
				if(getDust(x,y) != 0){
					drawCell(x,y,DustItemManager.getPrimaryColor(getDust(x,y)));
				}
			}
		}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopAttrib();
	}
	
	@Override
	public void onGuiClosed() {
		sendData();
		super.onGuiClosed();
	}
	
	public void sendData(){
		if(!insc.canEdit()) return;
		boolean cont = changed;
		for(int i = 0; i < 16 &&!cont; i++){
			for(int j = 0; j < 16 &&!cont; j++){
				if(getDust(i,j) != 0) {
					cont = true;
				}
			}
		}
		if(cont){
			FMLClientHandler.instance().sendPacket(PacketHandler.getSetInscriptionPacket(insc.inv));
		}
	}
	
	protected void drawCell(int x, int y, int color){
		int red = (color & 0xFF0000) >> 16;
		int green = (color & 0xFF00) >> 8;
		int blue = (color & 0xFF);
		GL11.glColor3ub((byte)red, (byte)green, (byte)blue);

		
		int dx = x*6;
		int dy = y*6;
		
		dx += 42;
		dy += 19;
		
		int dust = getDust(x,y);
		
		if(dust == getDust(x-1,y)){
			this.drawTexturedModalRect(dx,dy+1,6,ySize+1, 5,4);
		}
		if(dust == getDust(x+1,y)){
			this.drawTexturedModalRect(dx +1,dy +1,7,ySize+1, 5,4);
		}
		if(dust == getDust(x,y-1)){
			this.drawTexturedModalRect(dx+1,dy,7,ySize, 4,5);
		}
		if(dust == getDust(x,y+1)){
			this.drawTexturedModalRect(dx +1,dy +1,7,ySize+1, 4,5);
		}
		this.drawTexturedModalRect(dx +1,dy +1,7,ySize+1, 4,4);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int b) {

        Slot slot = this.getSlotAtPosition(x, y);
        if(slot != null && slot.getHasStack() && slot.getStack().itemID == DustMod.ink.shiftedIndex){
        	int id = ItemInk.getDustID(slot.getStack());
        	insc.setInventorySlotContents(0, new ItemStack(DustMod.ink.shiftedIndex, id, slot.slotNumber-1));
        }
        if(isOnMap(x,y)){
//        	System.out.println("rawr");
			isDown = true;
			button = b;
			buttonUpDelay = 3;
        }
//		super.mouseClicked(x, y, b);
	}
	
	public int button = -1;
	public boolean isDown = false;
	@Override //Stopped being constant. Would only call when mouse released
	protected void mouseMovedOrUp(int x, int y, int b) {
//		System.out.println("waat " + isDown + " " + b + " " + buttonUpDelay);
		if(buttonUpDelay > 0) buttonUpDelay --;
		
		if(b != -1 && buttonUpDelay <= 0){
			button = b;
			isDown = false;
		}
		if(!insc.canEdit()) return;
		if(isDown){
	        int sx = this.guiLeft;
	        int sy = this.guiTop;
			x -= 42 + sx;
			y -= 19 + sy;
			x/=6;
			y/=6;
			
			if(x < 0 || y < 0 || x >= 16 || y >= 16){
				return;
			}
			
			if(button == 1) {
				setDust(x,y,0);
			}
			if(button == 0 && this.inventorySlots.getSlot(0).getStack() != null) {
				int slot = this.inventorySlots.getSlot(0).getStack().getItemDamage();
				int id = this.inventorySlots.getSlot(0).getStack().stackSize;
				ItemStack stack = this.playerInv.getStackInSlot(slot);
				if(getDust(x,y) != id && ItemInk.reduce(this.player,stack, 1)){
					setDust(x,y,id);
					FMLClientHandler.instance().sendPacket(PacketHandler.getUseInkPacket(slot, 1));
//					this.inventorySlots.putStackInSlot(slot, stack);
					this.playerInv.setInventorySlotContents(slot, stack);
					this.inventorySlots.putStackInSlot(slot+1, stack);
					if(stack.itemID != DustMod.ink.shiftedIndex){
//						this.inventorySlots.putStackInSlot(0, new ItemStack(DustMod.ink.shiftedIndex, 0, -1));
						 //Loop through player's hotbar for inks
//						 for(int i = 1; i < 10; i++){
//							 ItemStack item = this.playerInv.getStackInSlot(i-1);
//							 if(item != null && item.itemID == DustMod.ink.shiftedIndex){
//						        	int dustId = ItemInk.getDustID(item);
//						        	this.inventorySlots.putStackInSlot(0, new ItemStack(DustMod.ink.shiftedIndex, dustId, i-1));
//								 break;
//							 }
//						 }
					}
				}
			}
			
		}
//		super.mouseMovedOrUp(x, y, b);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
      int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
      int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
      int b = Mouse.getEventButton();
//		System.out.println(Mouse.getEventButton());
//		if(Mouse.getEventButton() == -1) isDown = false;
		mouseMovedOrUp(x,y,b);
	}
	
//	@Override
//	public void updateScreen() {
//		super.updateScreen();
//
//        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
//        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
//        int b = Mouse.getEventButton();
//
//		
//		if(!insc.canEdit()) return;
//		System.out.println("isdown " + isDown);
//		if(isDown){
//	        int sx = this.guiLeft;
//	        int sy = this.guiTop;
//			x -= 42 + sx;
//			y -= 19 + sy;
//			x/=6;
//			y/=6;
//			
//			if(x < 0 || y < 0 || x >= 16 || y >= 16){
//				return;
//			}
//			
//			if(button == 1) {
//				setDust(x,y,0);
//			}
//			if(button == 0 && this.inventorySlots.getSlot(0).getStack() != null) {
//				int slot = this.inventorySlots.getSlot(0).getStack().getItemDamage();
//				int id = this.inventorySlots.getSlot(0).getStack().stackSize;
//				ItemStack stack = this.playerInv.getStackInSlot(slot);
//				if(getDust(x,y) != id && ItemInk.reduce(stack, 1)){
//					setDust(x,y,id);
//					FMLClientHandler.instance().sendPacket(PacketHandler.getUseInkPacket(slot, 1));
////					this.inventorySlots.putStackInSlot(slot, stack);
//					this.playerInv.setInventorySlotContents(slot, stack);
//					this.inventorySlots.putStackInSlot(slot+1, stack);
//					if(stack.itemID != DustMod.ink.shiftedIndex){
////						this.inventorySlots.putStackInSlot(0, new ItemStack(DustMod.ink.shiftedIndex, 0, -1));
//						 //Loop through player's hotbar for inks
////						 for(int i = 1; i < 10; i++){
////							 ItemStack item = this.playerInv.getStackInSlot(i-1);
////							 if(item != null && item.itemID == DustMod.ink.shiftedIndex){
////						        	int dustId = ItemInk.getDustID(item);
////						        	this.inventorySlots.putStackInSlot(0, new ItemStack(DustMod.ink.shiftedIndex, dustId, i-1));
////								 break;
////							 }
////						 }
//					}
//				}
//			}
//			
//		}
//		
//		if(b != -1){
//			button = b;
//			isDown = false;
//		}
//	}
	
	public boolean isOnMap(int x, int y){
		int tol = 0;
        x -= this.guiLeft;
        y -= this.guiTop;
		return x > 42-tol && x < 42+16*6+tol && y > 19-tol && y < 19+16*6+tol;
	}
	
	public int getDust(int x, int y){
		if(x<0||x>=16||y<0||y>=16) return -1;
		ItemStack stack = this.insc.getStackInSlot(x * 16 + y + 10);
		if(stack == null || stack.stackSize == 0){
			return 0;
		}
		else return stack.getItemDamage();
	}
	
	public void setDust(int x, int y, int dust){
		changed = true;
		
		this.insc.setInventorySlotContents(x*16 + y + 10, new ItemStack(DustMod.inscription.shiftedIndex, 1, dust));
		

		if(newInscription){
			this.sendData();
			newInscription = false;
		}
	}
	
	public int getCurrentDust(){
		ItemStack item = insc.getStackInSlot(0);
		return item.getItemDamage();
	}
	
	public Slot getSlotAtPosition(int x, int y)
    {
        for (int var3 = 1; var3 < 10; ++var3)
        {
            Slot var4 = (Slot)this.inventorySlots.getSlot(var3);

            if (this.isMouseOverSlot(var4, x, y))
            {
                return var4;
            }
        }

        return null;
    }
	
	public boolean isMouseOverSlot(Slot par1Slot, int x, int y)
    {
        return this.getSomething(par1Slot.xDisplayPosition, par1Slot.yDisplayPosition, 16, 16, x, y);
    }
	

	//it has an undefined name, and I don't know what it does, so to avoid getting broken in later versions, I do this
    protected boolean getSomething(int par1, int par2, int par3, int par4, int x, int y)
    {
        int var7 = this.guiLeft;
        int var8 = this.guiTop;
        x -= var7;
        y -= var8;
        return x >= par1 - 1 && x < par1 + par3 + 1 && y >= par2 - 1 && y < par2 + par4 + 1;
    }

}
