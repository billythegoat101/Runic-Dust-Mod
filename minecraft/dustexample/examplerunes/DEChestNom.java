/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustexample.examplerunes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import dustmod.DustEvent;
import dustmod.EntityDust;
import dustmod.TileEntityDust;

/**
 *
 * Rune of OmNomChest
 * 
 * Good example of:
 * -Item sacrifices
 * -Rune positions
 * -Star graphics
 * -What the unload function is for
 * 
 * @author billythegoat101
 */
public class DEChestNom extends DustEvent
{

	/**
	 * Called to set the graphical components of the rune
     * @param e EntityDust instance
	 */
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        /***GRAPHICS**/
        
        //To make it look cool. This enables the rune to have that glowing spinning star effect.
        e.setRenderStar(true);
        
        //Make the star slightly bigger than the chest so that it shines through and looks cool.
        e.setStarScale(2.2F);
        
        //Sets the color of the star (both inside and out) to look yellow.
        e.setColorStar(255, 255, 0); 
        //Use EntityDust.setColorInner and setColorOuter to change inner and outer separately.
		
    }
	
    /**
     * Called when a rune of this type is created in the world
     * @param e EntityDust instance
     */
    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
     
        /**SACRIFICES**/
        
        
            /**Item sacrifice**/
        
        //An array of all item/block sacrifices required.
        ItemStack[] sacr = new ItemStack[]{new ItemStack(Block.chest.blockID,1,0), new ItemStack(Item.ingotGold.itemID,1,0)};
        //This array is optional, You can just list all the itemstacks directly into takeItems
        //Like this : boolean success = this.takeItems(e,new ItemStack(Block.chest.blockID,1,0), new ItemStack(Item.ingotGold.itemID,1,0));
        
        //Automatically searches near the rune for the requested item sacrifices
        //For each item found, the stacksize in sacr is brought down.
        //returns true if the full sacrifice was fulfilled
        if(!this.takeItems(e, sacr)){
            //Sacrifice not fulfilled, therefore kill the rune.
            e.fizzle();
            return;
        }
        
            /**XP Sacrifice**/
        int levels = 1;
        //Takes required levels from the nearby players
        if(!this.takeXP(e, levels)){
            //it returned false, therefore the level requirement was not met
            e.fizzle();
            return;
        }
        
            /**Hunger Sacrifice**/
        int numHalfBars = 1;
        if(!this.takeHunger(e, numHalfBars)){
            e.fizzle();
            return;
        }
        
        //If we've made it here then the sacrifice must have been fulfilled.
        //so now we place that chest
        World world = e.worldObj;
        world.setBlockWithNotify(e.getX(), e.getY(), e.getZ(), Block.chest.blockID);
        
    }
    
    /**
     * Called by the EnitityDust instance that has been assigned this DustEvent type
     * @param e EntityDust instance
     */
    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);
        //Used to help free up the framerate a little. You don't need to check
        //for items every single tick, it will just cause lag. This checks ever 1/4 second
        
        
        if(e.ticksExisted%5 == 0) 
        {
            World world = e.worldObj;
            //Getting the block position of the EntityDust
            int x = e.getX();
            int y = e.getY();
            int z = e.getZ();
            
            TileEntityChest tec = null;
            if(world.getBlockId(x, y, z) == Block.chest.blockID)
            {
                tec = (TileEntityChest)world.getBlockTileEntity(x, y, z);
            }else
            {
                //Kills the rune.
                e.fizzle();
                return;
            }
            
            //Get all items within 2 blocks of this EntityDust
            List<EntityItem> items = this.getItems(e,2D);
            
            //If there are items detected nearby, trigger the openChest animation
            if(items.size() > 0){
                tec.openChest();
                e.data[0] = e.ticksExisted+20; //Used to delay-trigger the closing chest animation
            }
            
            //Loop through all detected dropped items nearby
            for(EntityItem ei:items)
            {
                //Getting the actual ItemStack of the dropped item.
                ItemStack item = ei.getEntityItem();
                
                //Loop through all the slots in the chest
                for(int i = 0; i < tec.getSizeInventory() && item.stackSize > 0; i++)
                {
                    ItemStack inv = tec.getStackInSlot(i);
                    
                    //If the item in that chest slot matches the one dropped, 
                    if(inv != null && inv.itemID == item.itemID && inv.getItemDamage() == item.getItemDamage())
                    {
                        //Then add the dropped item to that itemstack in the chest.
                        inv.stackSize += item.stackSize;
                        item.stackSize = 0;
                        if(inv.stackSize > inv.getMaxStackSize()){
                            item.stackSize = inv.stackSize- inv.getMaxStackSize();
                            inv.stackSize = inv.getMaxStackSize();
                        }
                        if(item.stackSize <= 0){
                            ei.setDead();
                            break;
                        }
                    }
                }
                //Loop through the slots again if there is still an amount of items in the dropped itemstack
                for(int i = 0; i < tec.getSizeInventory() && item.stackSize > 0; i++)
                {
                    ItemStack inv = tec.getStackInSlot(i);
                    //If the slot is empty, put the dropped itemstack there.
                    if(inv == null || inv.itemID == 0)
                    {
                        tec.setInventorySlotContents(i, item);
                        ei.setDead();
                        break;
                    }
                }
                ei.func_92058_a(item);
            }
        }
        
        //Triggering of the closeChest animation after the delay
        if(e.ticksExisted >= e.data[0]){
            World world = e.worldObj;
            int x = e.getX();
            int y = e.getY();
            int z = e.getZ();
            
            TileEntityChest tec = null;
            if(world.getBlockId(x, y, z) == Block.chest.blockID)
            {
                tec = (TileEntityChest)world.getBlockTileEntity(x, y, z);
                tec.closeChest();
                //Helps makes sure the chest can open again
                if(tec.numUsingPlayers < 0) tec.numUsingPlayers = 0;
            }
        }
    }
    
    
    /**
     * Called if an activated rune is ever right-clicked
     * @param e EntityDust instance
     * @param ted   The TileEntityDust that was right-clicked
     * @param p The player who clicked
     */
    @Override
    public void onRightClick(EntityDust e, TileEntityDust ted, EntityPlayer p)
    {
		//No purpose with this rune.
        super.onRightClick(e, ted, p);
    }
    
    /**
     * Called when a rune is destroyed or caused by any means to stop.
     * @param e The EntityDust instance
     */
    @Override
    public void onUnload(EntityDust e)
    {
        super.onUnload(e);
        
        //This just finds the TileEntityChest (if it still exists) and closes
        //it because otherwise there's a chance it will be left glitched open
        //forever.
        World world = e.worldObj;
        int x = e.getX();
        int y = e.getY();
        int z = e.getZ();

        TileEntityChest tec = null;
        if(world.getBlockId(x, y, z) == Block.chest.blockID)
        {
            tec = (TileEntityChest)world.getBlockTileEntity(x, y, z);
            tec.closeChest();
        }
    }
}
