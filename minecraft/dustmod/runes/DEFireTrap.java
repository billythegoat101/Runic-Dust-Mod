/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEFireTrap extends DETrap
{
    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        ItemStack[] sac = new ItemStack[] {new ItemStack(Item.flint, 3)};
        sac = this.sacrifice(e, sac);

        if (sac[0].stackSize > 0)
        {
            e.fizzle();
            return;
        }
    }

    @Override
    public void trigger(EntityDust e, int dustLevel)
    {
        double firerand = 0;
        int rad = 0;
        int firedambase = 0;
        int firedamrand = 0;

        switch (dustLevel)
        {
            case 200:
                firerand = 0.05;
                rad = 3;
                firedambase = 5;
                firedamrand = 2;
                break;

            case 300:
                firerand = 0.12;
                rad = 4;
                firedambase = 7;
                firedamrand = 4;
                break;

            case 400:
                firerand = 0.4;
                rad = 6;
                firedambase = 10;
                firedamrand = 8;
                break;
        }

        List<Entity> kill = getEntities(e, rad);

        for (Entity k: kill)
        {
            k.setFire(firedambase + (int)(Math.random() * firedamrand));
        }

        int ex = e.getX();
        int ey = e.getY();
        int ez = e.getZ();

        for (int x = -rad; x <= rad; x++)
        {
            for (int y = -rad; y <= rad; y++)
            {
                for (int z = -rad; z <= rad; z++)
                {
                    if (e.worldObj.getBlockId(ex + x, ey + y - 1, ez + z) != 0 && e.worldObj.getBlockId(ex + x, ey + y, ez + z) == 0 && Math.random() < firerand)
                    {
                        e.worldObj.setBlockAndMetadataWithNotify(ex + x, ey + y, ez + z, Block.fire.blockID,0,3);
                    }
                }
            }
        }

        e.fade();
    }
}
