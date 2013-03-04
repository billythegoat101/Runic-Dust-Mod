/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.client;

import java.util.Random;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import dustmod.DustManager;
import dustmod.DustMod;
import dustmod.DustShape;
import dustmod.InscriptionEvent;
import dustmod.InscriptionManager;

/**
 *
 * @author billythegoat101
 */
public class GuiTome extends GuiScreen
{
	
    public static int runePage = 0;
    public static int insPage = 0;
    
    public static int type = 0;

    /** The X size of the inventory window in pixels. */
    protected int xSize;

    /** The Y size of the inventory window in pixels. */
    protected int ySize;
    
    /** The starting coords of where the gui is drawn */
    protected int xStart,yStart;

    /**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiLeft;

    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiTop;


    public GuiButton button;
//    public GuiTextField nameField;

    public int offX;

    public static boolean showSacrifices = true;
    public ItemStack itemstack;
    public GuiTome(ItemStack itemstack)
    {
        super();
        this.itemstack = itemstack;
        xSize = 206;//176;
        ySize = 166;
        offX = xSize / 4;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        drawDefaultBackground();
        drawGuiContainerBackgroundLayer(par3, par1, par2);
        drawGuiContainerForegroundLayer();

        for (int i = 0; i < controlList.size(); i++)
        {
            GuiButton guibutton = (GuiButton)controlList.get(i);
            guibutton.drawButton(mc, par1, par2);
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
        this.controlList.add(button = new GuiButton(1, (width + xSize) / 2 + 2 - offX, (height - ySize) / 2 + 2 + ySize - 20, (width - xSize) / 2 + offX - 2, 20, "Description >"));
//        nameField = new GuiTextField(this.fontRenderer, (width-xSize)/2 - offX,(height-ySize)/2-fontRenderer.FONT_HEIGHT-2, xSize,12);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
    }


    String[] derp = new String[]{"Hi", "I should get rid of this page...", 
    		"Hope you enjoy!", "Make some runes!","Space for rent.", 
    		"Modders: Make custom runes!", "Insert joke here.","Direwolf20 is cool!",
    		"Notch is cool!","Jeb_ is cool!", "Stop annoying LexManos!","Go play Thaumcraft.", 
    		"The QubeTubers are cool!", "Try Ars Magica!", "Play outside!"};
    int randAuthor = (int)(Math.random()*derp.length);
    /**
     * Draw the foreground layer for the GuiContainer (everythin in front of the items)
     */
    protected void drawGuiContainerForegroundLayer()
    {
        String name = "";
        String author = "";
        String notes = "";
        boolean recolor = false;

        if ((isRunes() && getRunePage() == 0) || (!isRunes() && getInsPage() == 0))
        {
            name = "Legend: " + (isRunes() ? DustManager.namesRemote.size():InscriptionManager.eventsRemote.size()) + " installed";
            notes = "\n\n\n"
                    + "Meat: Pork, Beef, or Chicken raw or cooked.\n---\n"
                    + "Drops: Any item corresponding to a particular mob.\n---\n"
                    + "Variable: The dust is interchangable and allows you to set traits of the rune.\n---\n"
                    + "Powered: If the name is red, then it requires fueling via smeltables.";
            
            author = derp[randAuthor];
        }
        else
        {
        	if(isRunes()){
	            DustShape shape = DustManager.getShape(getRunePage() - 1);
	            name = shape.getRuneName();
	            notes = showSacrifices ? shape.getNotes() : shape.getDescription();
	            author = "by " + shape.getAuthor();
	            if (shape.isPower)
	            {
	                recolor = true;
	            }
	            Random rand = new Random();
	            randAuthor = (int)(rand.nextInt(derp.length));
        	}else{
        		InscriptionEvent event = InscriptionManager.getEventInOrder(getInsPage() - 1);
	            name = event.getInscriptionName();
	            notes = showSacrifices ? event.getNotes() : event.getDescription();
	            author = "by " + event.getAuthor();
	            Random rand = new Random();
	            randAuthor = (int)(rand.nextInt(derp.length));
        	}
        }

        GL11.glColor3f(255, 0, 0);
        fontRenderer.drawString(name, (width - xSize) / 2 - offX, (height - ySize) / 2 - fontRenderer.FONT_HEIGHT - 2, recolor ? 0xFF0000 : 0xEEEEEE);
        fontRenderer.drawSplitString(notes, (width + xSize) / 2 + 2 - offX, (height - ySize) / 2 + 2, (width - xSize) / 2 + offX, 0xffa0a0a0);
        GL11.glPushMatrix();
        float scale = 0.6666F;
        GL11.glTranslated((width - xSize) / 2 - offX, (height - ySize) / 2 + ySize, 0);
        GL11.glScalef(scale,scale,scale);
        fontRenderer.drawString(author, 0,0, 0xffa0a0a0);
        GL11.glPopMatrix();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
    }
    
    public void setRunePage(int p){
    	runePage = p;
//    	itemstack.setItemDamage(p);
    }

    public int getRunePage(){
    	return runePage;
//    	return itemstack.getItemDamage();
    }
    public void setInsPage(int p){
    	insPage = p;
//    	itemstack.setItemDamage(p);
    }
    
    public int getInsPage(){
    	return insPage;
//    	return itemstack.getItemDamage();
    }
    
    @Override
    protected void keyTyped(char par1, int key)
    {
        super.keyTyped(par1, key);

        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.keyCode)
        {
            this.mc.thePlayer.closeScreen();
//        	this.mc.displayGuiScreen((GuiScreen)null);
        }

        if (key == mc.gameSettings.keyBindLeft.keyCode)
        {
            retreatPage();
        }
        else if (key == mc.gameSettings.keyBindRight.keyCode)
        {
            advancePage();
        }

//        if (DustMod.debug && key == mc.gameSettings.keyBindChat.keyCode)
//        {
//            EntityPlayer player = ModLoader.getMinecraftInstance().thePlayer;
//            int scroll = 0;
//
//            if (getRunePage() != 0)
//            {
//                scroll = DustManager.getShape(getRunePage() - 1).id;
//                ItemStack to = new ItemStack(DustMod.dustScroll, 1, scroll);
//                player.inventory.addItemStackToInventory(to);
//            }
//            else
//            {
//                ItemStack to = new ItemStack(DustMod.negateSacrifice, 64);
//                player.inventory.addItemStackToInventory(to);
//            }
//        }
    }
    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int x, int y, int m)
    {
        super.mouseClicked(x, y, m);

        if(x < xStart+6 && y-yStart < 100 && y-yStart > 16){
        	y-=yStart;
        	if(y > 56) setType(1);
        	else if(y > 16) setType(0);
        }else if (y >= (height / 2 - ySize / 2) && y <= (height / 2 + ySize / 2))
        {
            if (x >= width / 2 - xSize / 2 - offX && x <= width / 2 + xSize / 2 - offX)
            {
                if (m == 0)
                {
                    advancePage();
                }
                else if (m == 1)
                {
                    retreatPage();
                }
            }
        }

//        System.out.println("Click " + par1 + " " + par2 + " " + par3 + " " + width + " " + height);
    }
    private void advancePage()
    {
    	if(isRunes()){
	//        itemstack.setItemDamage(itemstack.getItemDamage() + 1);
	    	setRunePage(getRunePage() + 1);
	
	        if (getRunePage() >= DustManager.getShapes().size() - DustMod.numSec + 1)
	        {
	        	setRunePage(0);
	        }
    	} else {

	    	setInsPage(getInsPage() + 1);
	
	        if (getInsPage() >= InscriptionManager.getEvents().size() + 1)
	        {
	        	setInsPage(0);
	        }
    	}
    }

    private void retreatPage()
    {
    	if(isRunes()){
	        setRunePage(getRunePage()-1);
	
	        if (getRunePage() < 0)
	        {
	        	setRunePage(DustManager.getShapes().size() - DustMod.numSec);
	//        	itemstack.setItemDamage(DustManager.getShapes().size() - DustMod.numSec);
	//            page = DustManager.getShapes().size() - DustMod.numSec;
	        }
    	} else {
	        setInsPage(getInsPage()-1);
	
	        if (getInsPage() < 0)
	        {
	        	setInsPage(InscriptionManager.getEvents().size());
	//        	itemstack.setItemDamage(DustManager.getShapes().size() - DustMod.numSec);
	//            page = DustManager.getShapes().size() - DustMod.numSec;
	        }
    	}
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
//    	System.out.println("RAWR " + DustManager.isEmpty() + " " + InscriptionManager.isEmpty());
        int i = mc.renderEngine.getTexture(DustMod.path + "/tomeGui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
//        mc.renderEngine.bindTexture(mc.renderEngine.getTexture(RenderDustTable.getPagePath(page)));
        int j = (width - xSize) / 2 - offX;
        int k = (height - ySize) / 2;
        xStart = j;
        yStart = k;
        int pageWidth = 70;
        int pageHeight = 56;
        int ox = 4;
        int oy = 4;
        float scalex = (float)(xSize - ox * 2) / 256F;
        float scaley = (float)(ySize - oy * 2) / 256F;
        float res = xSize / ySize;
        drawTexturedModalRect(j, k, 24, 0, xSize, ySize);
        GL11.glPushMatrix();
        GL11.glScalef(1 / res, res, 1);
        GL11.glTranslatef(j + ox, k + oy, 0);
        GL11.glScalef(scalex, scaley, 1f);
//        System.out.println("Scale " + scalex + " " + scaley);
        if(isRunes()){
	        if(getRunePage() == 0){
//	        	if(DustManager.isEmpty()){
//	        		mc.renderEngine.bindTexture(mc.renderEngine.getTexture(DustMod.path + "/pages" + "/no_runes.png"));
//	        	}else {
	        		mc.renderEngine.bindTexture(mc.renderEngine.getTexture(DustMod.path + "/pages/info.png"));
//	        	}
	        }
	        else PageHelper.bindExternalTexture(PageHelper.runeFolder + RenderDustTable.getRunePageName(getRunePage()) + ".png");
        }else {
	        if(getInsPage() == 0){
//	        	if(InscriptionManager.isEmpty()){
//	        		mc.renderEngine.bindTexture(mc.renderEngine.getTexture(DustMod.path + "/pages" + "/no_inscriptions.png"));
//	        	}else {
	        		mc.renderEngine.bindTexture(mc.renderEngine.getTexture(DustMod.path + "/pages" + "/info.png"));
//	        	}
	        }
	        else PageHelper.bindExternalTexture(PageHelper.insFolder + InscriptionManager.getEventInOrder(getInsPage() -1).getIDName() + ".png");
        }
        drawTexturedModalRect(0, 0, 0, 0, 256, 256);
        
        GL11.glPopMatrix();
        
        if(isRunes()){
        	mc.renderEngine.bindTexture(i);
        	drawTexturedModalRect(j-6,k,12,0,12,ySize);
        }else {
        	mc.renderEngine.bindTexture(i);
        	drawTexturedModalRect(j-6,k,0,0,12,ySize);
        }
    }
    
    public boolean isRunes(){
    	return type == 0;
    }
    public void setType(int type){
    	this.type = type;
    }

    @Override
    protected void actionPerformed(GuiButton but)
    {
        if (but == button)
        {
            showSacrifices = !showSacrifices;

            if (showSacrifices)
            {
                but.displayString = "Description >";
            }
            else
            {
                but.displayString = "< Information";
            }
        }
        else
        {
            super.actionPerformed(but);
        }
    }
}
