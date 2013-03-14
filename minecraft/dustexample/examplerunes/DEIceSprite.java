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
import dustmod.EntityDust;
import dustmod.PoweredEvent;
import dustmod.TileEntityDust;

/**
 *
 * Rune of the Ice Sprite
 * 
 * Good example of:
 * -Powered runes
 * -Pausing powered runes
 * -Sprites & following the summoner
 * -XP and Item sacrifices
 * 
 * @author billythegoat101
 */
public class DEIceSprite extends PoweredEvent
{
    
    public DEIceSprite()
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
        
        /**GRAPHICS**/
        
        //Star color and such
        e.setRenderStar(true);
        e.setColorStarOuter(0,255,255);
        
        //Set this rune sprite to hover around the player and follow him
        e.setFollow(true);
		
    }
    
    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        
        /**SACRIFICES**/
        
        //If these items have not been sacrificed
        if(!this.takeItems(e, new ItemStack(Block.snow, 1, -1), new ItemStack(Item.ghastTear, 1))){
            //Then kill the rune.
            e.fizzle();
            return;
        }
        
        //If the XP sacrifice has not been fulfilled
        if(!this.takeXP(e, 20)){
            //Then kill the rune.
            e.fizzle();
            return;
        }
    }
    
    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);
        //The outside color is constantly set because the PoweredEvent will 
        //otherwise constantly be setting both the inside and outside colors
        //to white or red depending on the fuel level.
        e.setColorStarOuter(0,255,255);
        
        //Find the player who created this rune
        EntityPlayer player = e.worldObj.getPlayerEntityByName(e.summonerUN);
        
        if(player == null){ 
            //******IMPORTANT TO NOTE: Since the summoner player is found based on the username, 
            //If anyone else tries to play this world in ssp with a different username
            //They will not be followed
            //MCP CHANGES THE USERNAME EACH TIME YOU PLAY
            //SO IF THE SPRITE STOPS FOLLOWING YOU, THAT'S PROBABLY WHY
            
            //If the player is not found, we don't want to kill the rune because
            //on a server, the player might just be logged out or on a different
            //dimension
            
            //Also saves the fact that the player can't be found so that we can 
            //say that the rune is paused and won't take fuel
            e.data[0] = 1; 
            return;
        }else {
            //The player has been found, so unpause
            e.data[0] = 0;
        }
        
        if(e.ticksExisted%10 == 0){
            int rad = 5;
            
            World world = e.worldObj;
            int x,y,z;
            x = e.getX();
            y = e.getY();
            z = e.getZ();
            
            //Loop through all blocks around and below the player
            for(int i = -rad; i <= rad; i++){
                for(int j = 0; j > -rad; j--){
                    for(int k = -rad; k <= rad; k++){
                        
                        //Find the block at the checked location and the block one above it
                        int blockID = world.getBlockId(x+i, y+j, z+k);
                        int blockAbove = world.getBlockId(x+i, y+j+1, z+k);
                        
                        //Check if the checked block is water and the block above it is not
                        //(That way you are not freezing anything but the surface)
                        if(isWater(blockID) && !isWater(blockAbove) && blockAbove != Block.ice.blockID){
                            world.setBlockAndMetadataWithNotify(x+i,y+j,z+k,Block.ice.blockID,0,3);
                        }
                        //Same for lava, but change to obsidian
                        if(isLava(blockID) && !isLava(blockAbove) && blockAbove != Block.obsidian.blockID){
                            world.setBlockAndMetadataWithNotify(x+i,y+j,z+k,Block.obsidian.blockID,0,3);
                        }
                    }
                }
            }
        }
    }
    
    //Checks if the BlockID checked is made of water
    public boolean isWater(int id){
        return id == Block.waterMoving.blockID || id == Block.waterStill.blockID;
    }
    public boolean isLava(int id){
        return id == Block.lavaMoving.blockID || id == Block.lavaStill.blockID;
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


    @Override
    public int getStartFuel()
    {
        return dayLength * 3;
    }

    @Override
    public int getMaxFuel()
    {
        return dayLength * 7;
    }

    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return dayLength + dayLength / 2;
    }

    @Override
    public boolean isPaused(EntityDust e)
    {
        //data[0] is set to 1 or 0 based on whether the player was found or not
        return e.data[0] == 1;
    }
}
