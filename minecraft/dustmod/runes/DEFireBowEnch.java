/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import dustmod.DustEvent;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEFireBowEnch extends DustEvent
{
    public DEFireBowEnch()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderStar(true);
		e.setRenderBeam(true);
        e.setColorStarOuter(0, 0, 255);
        e.setColorBeam(0, 0, 255);
    	
    	
    }

    public void onInit(EntityDust e)
    {
//        List<EntityItem> sacrifice = getSacrifice(e);
//        int item = Item.bow.itemID;
//        for(EntityItem i:sacrifice){
//            ItemStack is = i.item;
//
//            if(is.itemID == Item.pickaxeDiamond.itemID || is.itemID == Item.shovelDiamond.itemID) {
//                item = is.itemID;
//                break;
//            }
//        }
//        int gold = ((item == Item.pickaxeDiamond.itemID) ? Item.pickaxeGold.itemID:Item.shovelGold.itemID);
        ItemStack[] req = this.sacrifice(e, new ItemStack[] {new ItemStack(Item.bow, 1, 0),
                      new ItemStack(Block.blockGold.blockID, 1, 0), new ItemStack(Item.fireballCharge, 9)
        });

        if (!checkSacrifice(req) || !takeXP(e, 30))
        {
            e.fizzle();
            return;
        }

		e.setRenderStar(true);
		e.setRenderBeam(true);
        e.setColorStarOuter(0, 0, 255);
        e.setColorBeam(0, 0, 255);
    }

    public void onTick(EntityDust e)
    {
        e.setStarScale(e.getStarScale() + 0.001F);

        if (e.ticksExisted > 20)
        {
            Entity en = null;
            ItemStack create =  new ItemStack((int)Item.bow.itemID, 1, 0);
//            if(e.data == mod_DustMod.spiritSword.itemID){
            create.addEnchantment(Enchantment.flame, 1);
//            }
//            System.out.println("derp " + create.itemID);
            en = new EntityItem(e.worldObj, e.posX, e.posY - EntityDust.yOffset, e.posZ, create);

            if (en != null)
            {
                en.setPosition(e.posX, e.posY, e.posZ);
                e.worldObj.spawnEntityInWorld(en);
            }

            e.fade();
        }
    }
}
