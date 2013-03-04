/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import dustmod.EntityDust;
import dustmod.TileEntityDust;

/**
 *
 * @author billythegoat101
 */
public class DECage extends DETrap
{
    public DECage()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        e.setRenderStar(true);
    	
    }

    public void onInit(EntityDust e)
    {
        e.setRenderStar(true);
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.ingotIron, 6), new ItemStack(Item.redstone, 2)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req))
        {
            e.fizzle();
            return;
        }
    }

    public void onTick(EntityDust e)
    {
        e.setRenderStar(true);

        if (e.ticksExisted < 80)
        {
            e.setColorStarInner(140, 140, 140);
            e.setColorStarOuter(140, 140, 140);
            return;
        }

        e.setColorStarInner(0, 0, 255);
        e.setColorStarOuter(0, 0, 255);
        List<Entity> entities = getEntities(e, 2D);

        if (entities.size() > 0)
        {
            trigger(e, e.dustID);
//            e.fade();
        }
    }

    @Override
    public void trigger(EntityDust e, int dustLevel)
    {
        boolean found = false;
        List<Entity> trap = getEntities(e, 2D);

        for (Entity k: trap)
        {
            if (k instanceof EntityLiving)
            {
                
                if(k instanceof EntityPlayer){
                    EntityPlayer ep = (EntityPlayer)k;
                    if(ep.username.equals(e.summonerUN)){
                        continue;
                    }
                }
                found = true;
                EntityLiving el = (EntityLiving)k;
                int x = (int)Math.floor(el.posX);
                int y = (int)Math.floor(el.posY - el.yOffset);
                int z = (int)Math.floor(el.posZ);
                el.setPosition((double)x + 0.5D, (double)y + el.yOffset, (double)z + 0.5D);
                World world = e.worldObj;

                for (int ix = -1; ix <= 1; ix++)
                    for (int iy = 0; iy <= 1; iy++)
                        for (int iz = -1; iz <= 1; iz++)
                        {
                            if (ix == 0 && iz == 0)
                            {
                                continue;
                            }

                            world.setBlockWithNotify(x + ix, y + iy, z + iz, 0);
                            world.setBlockWithNotify(x + ix, y + iy, z + iz, Block.fenceIron.blockID);
                        }

                if (world.getBlockId(x, y - 1, z) == 0)
                {
                    for (Integer[] p: e.dustPoints)
                    {
                        TileEntityDust ted = (TileEntityDust)world.getBlockTileEntity(p[0], p[1], p[2]);

                        if (ted.getDusts()[3])
                        {
                            int id = world.getBlockId(p[0], p[1] - 1, p[2]);
                            world.setBlockWithNotify(p[0], p[1] - 1, p[2], 0);
                            world.setBlockWithNotify(x, y - 1, z, id);
                        }
                    }
                }

                break;
            }
        }

        if (found)
        {
            System.out.println("Found");
            e.fade();
        }
    }
}
