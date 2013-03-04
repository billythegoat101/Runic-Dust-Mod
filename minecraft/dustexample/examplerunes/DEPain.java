/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustexample.examplerunes;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import dustmod.EntityDust;
import dustmod.PoweredEvent;
import dustmod.TileEntityDust;

/**
 *
 * Rune of Pain
 * 
 * Good example of:
 * -Powered runes
 * -Pausing powered runes
 * -Basic AOE entity effects
 * -Checking for a rune's creator
 * -Right-clicking 
 * 
 * @author billythegoat101
 */
public class DEPain extends PoweredEvent //This is a Powered rune
{
    
    public DEPain()
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
        
        //These 4 points are the 4 variable dusts in the center of the rune.
        int a,b,c,d;
        a = e.dusts[3][3];
        b = e.dusts[3][4];
        c = e.dusts[4][4];
        d = e.dusts[4][3];
        
        //First we check to make sure they are all equal
        if(a == b && b == c && c == d){
            //Then we store the dust for future reference
            e.data[0] = a;
        }else{
        //If not we end here
            e.fizzle();
            return;
        }
    }
    
    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);
        
        //Doing entity checks every single tick just seems like it would cause 
        //an unnecessary amount of lag, so I don't do it
        if(e.ticksExisted % 10 == 0)
        {
            
            //Set stats based off of the dust level set earlier
            int damage = 0;
            double rad = 0;
            switch(e.data[0]){
                case 100:
                    damage = 1;
                    rad = 8D;
                    break;
                case 200:
                    damage = 2;
                    rad = 10D;
                    break;
                case 300:
                    damage = 4;
                    rad = 12D;
                    break;
                case 350:
                	damage = 5;
                	rad = 14D;
                	break;
                case 400:
                    damage = 6;
                    rad = 16D;
                    break;
            }
            
            //Get all entities within radius
            List<Entity> ents = this.getEntities(e, rad);
            for(Entity i: ents){
                if(i instanceof EntityPlayer){
                    //Check if it is the summoning player
                    EntityPlayer player = (EntityPlayer) i;
                    if(player.username.equals(e.summonerUN)){
                        continue; //Skip this entity, don't hurt them
                    }
                }
                
                //hurt the entity
                i.attackEntityFrom(DamageSource.magic, damage);
            }
        }
    }
    
    @Override
    public void onRightClick(EntityDust e, TileEntityDust ted, EntityPlayer p)
    {
        super.onRightClick(e, ted, p);
        //Used to toggle paused.
        if(e.data[1] == 0)
            e.data[1] = 1;
        else
            e.data[1] = 0;
    }
    
    @Override
    public void onUnload(EntityDust e)
    {
        super.onUnload(e);
    }

    /**
     * Get how much fuel this rune should begin with upon creation.
     * dayLength is the amount of fuel that will last 1 minecraft day
     * @return The amount of fuel to begin with
     */
    @Override
    public int getStartFuel()
    {
        return dayLength;
    }

    /**
     * Get the maximum amount of fuel this rune can ever have. Any more sacrificed
     * will be wasted and not be stored.
     * @return The maximum amount of fuel
     */
    @Override
    public int getMaxFuel()
    {
        return dayLength * 3;
    }

    /**
     * Get the amoung of fuel this rune should try to have if possible.
     * If over this amount, power relay runes will put this rune on low priority
     * under others that might still need power
     * @param e EntityDust instance
     * @return An adequate amount of fuel for this rune.
     */
    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return dayLength / 2;
    }

    /**
     * Return true if the rune should be paused and therefore not conusming fuel.
     * @param e EntityDust instance
     * @return True if paused, false otherwise
     */
    @Override
    public boolean isPaused(EntityDust e)
    {
        return e.data[1] == 1;
    }
}
