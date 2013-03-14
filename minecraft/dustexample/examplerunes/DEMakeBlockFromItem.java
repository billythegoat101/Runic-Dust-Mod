/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustexample.examplerunes;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import dustmod.DustEvent;
import dustmod.EntityDust;
import dustmod.TileEntityDust;

/**
 *
 * 
 * Rune of Cheating (the first of many)
 * 
 * Good example of:
 * -Variable item sacrifices.
 * -Slow world changes
 * -A rune that lasts even though destroyed
 * 
 * @author billythegoat101
 */
public class DEMakeBlockFromItem extends DustEvent
{

    public DEMakeBlockFromItem()
    {
        super();
    }
	
	/**
	 * Called to set the graphical components of the rune
     * @param e EntityDust instance
	 */
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        /**Graphics**/
        e.setRenderBeam(true);
		
    }

    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);

        /**Sacrifice**/
        
        //Goes through and checks for each different type of item: iron, gold or diamond
        //Then sets the entity data at [0] to 1,2,or 3 respectively for later reference
        if (this.takeItems(e, new ItemStack(Item.ingotIron, 1)))
        {
            e.data[0] = 1;
        } else if (this.takeItems(e, new ItemStack(Item.ingotGold, 1)))
        {
            e.data[0] = 2;
        } else if (this.takeItems(e, new ItemStack(Item.diamond, 1)))
        {
            e.data[0] = 3;
        } else
        {
            //If none of those items were found, then kill the rune
            e.fizzle();
            return;
        }

        /**Graphics**/
        //Since this rune requires the sacrifice to be tallied, some of the graphics are 
        //set during the onInit instead of initGraphics.
        
        //Depending on which item was found (iron,gold,diamond) set the color of the beam.
        switch (e.data[0])
        {
            case 1:
                e.setColorBeam(255, 255, 255);
                break;
            case 2:
                e.setColorBeam(255, 255, 0);
                break;
            case 3:
                e.setColorBeam(0, 255, 255);
                break;
        }
        
        //This means that even if the rune is destroyed, it will still run until finished.
        //This is usually used for runes that sometimes end up destroying themselves
        //in the midst of doing their task.
        //However, some runes should leave this as false because it acts as a nice off-switch
        e.setRenderBeam(true);
    }

    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);
        
        //Every second (20 ticks) for 8 runs
        if(e.data[1] < 8 && e.ticksExisted % 20 == 0){
            World world = e.worldObj;
            int x = e.getX();
            int y = e.getY() + e.data[1];
            int z = e.getZ();
            
            //Find which block to place down depending on e.data[0]
            //e.data[0] was set in the init() method to 1,2,or3 depending on what was sacrificed
            int blockID = 0;
            switch (e.data[0]){
                case 1:
                    blockID = Block.blockSteel.blockID;
                    break;
                case 2:
                    blockID = Block.blockGold.blockID;
                    break;
                case 3:
                    blockID = Block.blockDiamond.blockID;
                    break;
            }
            
            world.setBlockAndMetadataWithNotify(x,y,z,0,0,3); //clears the block first. Sometimes a good idea since it could be a TileEntity
            world.setBlockAndMetadataWithNotify(x,y,z,blockID,0,3);
            e.data[1] ++;
        }
        else if(e.data[1] >= 8){
            e.fade(); //kills the rune (nicely)
        }
    }

    @Override
    public void onRightClick(EntityDust e, TileEntityDust ted, EntityPlayer p)
    {
        super.onRightClick(e, ted, p);
    }

    @Override
    public void onUnload(EntityDust e)
    {
        super.onUnload(e);
    }
}
