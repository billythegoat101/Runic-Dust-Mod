/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEBomb extends DustEvent
{
    public DEBomb()
    {
        super();
    }
    
    @Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);
    	

        e.setRenderStar(true);
    	
    }

    @Override
    public void onInit(EntityDust e)
    {
        ItemStack[] sac = new ItemStack[] {new ItemStack(Item.gunpowder, 2)};
        sac = this.sacrifice(e, sac);

        if (sac[0].stackSize > 0)
        {
            e.fizzle();
            return;
        }

        int[] center = new int[4];
        int[] fuse = new int[4];
        int[][] dusts = e.dusts;
        center[0] = dusts[3][1];
        center[1] = dusts[4][1];
        center[2] = dusts[3][2];
        center[3] = dusts[4][2];
        fuse[0] = dusts[0][4];
        fuse[1] = dusts[1][4];
        fuse[2] = dusts[1][3];
        fuse[3] = dusts[2][3];

        String rawr = "";
        for(int i = 0; i < dusts.length; i++){
        	for(int j = 0; j < dusts[0].length; j++){
        		rawr += dusts[i][j] + ",";
        	}
        	rawr += "\n";
        }
        for (int i = 0; i < 4; i++)
        {
            if (center[0] != center[i])
            {
                e.fizzle();
                return;
            }

            if (fuse[0] != fuse[i])
            {
                e.fizzle();
                return;
            }
        }

        int c = center[0];
        int f = fuse[0];
        e.data[0] = c;
        e.data[1] = f;
        e.setRenderStar(true);
    }
    public void onTick(EntityDust e)
    {
        int f = getTime(e.data[1]);
        int c = e.data[0];
        e.setRenderStar(true);

        
        if (e.ticksExisted < f * 30)
        {
            e.setColorStarInner(140, 140, 140);
            e.setColorStarOuter(140, 140, 140);
            return;
        }

        e.setColorStarInner(0, 0, 255);
        e.setColorStarOuter(0, 0, 255);
        List<Entity> entities = getEntities(e);

        if (entities.size() > 0 || f > 1)
        {
            trigger(e, c);
            e.fade();
        }
    }
    
    public int getTime(int f){
    	switch(f){
    	case 100:
    		return 1;
    	case 200:
    		return 2;
    	case 300:
    		return 3;
    	case 400:
    		return 4;
    	}
    	return 1;
    }

    public void trigger(EntityDust e, int level)
    {
        e.worldObj.createExplosion(e, e.posX, e.posY - EntityDust.yOffset, e.posZ, (float)(level * level)/10000 + 2F, true);
    }
}
