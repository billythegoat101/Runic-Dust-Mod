/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEWall extends DustEvent
{
    public static final int ticksperblock = 7;
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);
		
    }

    public void onInit(EntityDust e)
    {
        e.setIgnoreRune(true);
        ItemStack[] req = this.sacrifice(e, new ItemStack[] {new ItemStack(Block.oreIron, 1)});

        if (req[0].stackSize != 0 || !this.takeXP(e, 3))
        {
            e.fizzle();
            return;
        }
        
        e.data[0] = (e.rot+1)%2;
    }

    public void onTick(EntityDust e)
    {
        if (e.ticksExisted % ticksperblock == 0)
        {
            World world = e.worldObj;
            int currentHeight = (int)(e.ticksExisted / ticksperblock);
            int x = (int) e.getX();
            int y = (int) e.getY();
            int z = (int) e.getZ();
            boolean dir = e.data[0] == 0;
            int width = 2;
            int height = 8;

            int entC = 0;

            for (int t = -height; t <= height + 1; t++)
            {
                for (int w = -width; w <= width; w++)
                {
                    if (y - t + currentHeight <= 0)
                    {
                        e.fade();
                        return;
                    }

                    int b = world.getBlockId(x + (dir ? w : 0), y - t + currentHeight, z + (dir ? 0 : w));
                    int m = world.getBlockId(x + (dir ? w : 0), y - t + currentHeight, z + (dir ? 0 : w));
                    int nb = world.getBlockId(x + (dir ? w : 0), y - t + currentHeight + 1, z + (dir ? 0 : w));
                    Block B = Block.blocksList[b];
                    Block nB = Block.blocksList[nb];

                    if (B == DustMod.dust)
                    {
                        b = 0;
                        B = null;
                    }
                    else if (nB == DustMod.dust)
                    {
                        nb = 0;
                        nB = null;
                    }

                    if ((B != null && B instanceof BlockContainer) || (nB != null && nB instanceof BlockContainer))
                    {
                        e.fade();
                        return;
                    }

                    world.setBlockAndMetadataWithNotify(x + (dir ? w : 0), y - t + currentHeight + 1, z + (dir ? 0 : w), b, m);
                    world.setBlockWithNotify(x + (dir ? w : 0), y - t + currentHeight, z + (dir ? 0 : w), /*(t == height+1) ? Block.brick.blockID:*/0);
                }
            }

            if (currentHeight > 4)
            {
                e.fade();
            }
        }
    }
}
