/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustexample.examplerunes;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import dustmod.DustEvent;
import dustmod.EntityDust;
import dustmod.TileEntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEMakeBlockFromDustLevel extends DustEvent
{
    
    public DEMakeBlockFromDustLevel()
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
    	
		//no graphics!
    }
    
    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        
        int blockID = 0;
        //Since it is a solid rune, e.dustID returns the dust it is made of
        //And depending on that dust, we select a block we want to use
        switch(e.dustID){
            case 1:
                blockID = Block.wood.blockID;
                break;
            case 2:
                blockID = Block.tnt.blockID;
                break;
            case 3:
                blockID = Block.blockLapis.blockID;
                break;
            case 4:
                blockID = Block.netherBrick.blockID;
                break;
        }
        
        World world = e.worldObj;
        //Loop through all the block coordinates beneath those taken up by the runes.
        for(Integer[] i:e.dustPoints){
            int x = i[0];
            int y = i[1] -1;
            int z = i[2];
            world.setBlockWithNotify(x, y, z, blockID);
        }
        
        e.fade();
    }
    
    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);
        //everything is done in init(), so there is no use for this method
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
