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
import dustmod.DustMod;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DESpiritTool extends DustEvent
{
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderStar(true);
		e.setRenderBeam(true);
        e.setColorStarOuter(0,0,255);
        e.setColorBeam(0,0,255);
		
    }
	
    public void onInit(EntityDust e)
    {
//        int compare = mod_DustMod.compareDust(mod_DustMod.lapisDID, e.dustID);
//        if(compare < 0){
//            e.fizzle();
//            return;
//        }
//
		e.setRenderStar(true);
		e.setRenderBeam(true);
        e.setColorStarOuter(0,0,255);
        e.setColorBeam(0,0,255);
//
//        //get sacrifice
//        ArrayList<EntityItem> itemstacks = new ArrayList<EntityItem>();
//        List l = getEntities(e);
//        for(Object o:l){
//            if(o instanceof EntityItem){
//                EntityItem ei = (EntityItem)o;
//                itemstacks.add(ei);
//            }
//        }
////        System.out.println("CHECK size:" + itemstacks.size());
//        if(itemstacks.size() == 0){
//            e.kill();
//            return;
//        }
//        int meatamt = 8;
//
//        int[] meats = new int[]{Item.porkCooked.shiftedIndex, Item.porkRaw.shiftedIndex, Item.beefCooked.shiftedIndex, Item.beefRaw.shiftedIndex, Item.chickenCooked.shiftedIndex, Item.chickenRaw.shiftedIndex, Item.rottenFlesh.shiftedIndex, Item.fishCooked.shiftedIndex, Item.fishRaw.shiftedIndex};
//        for(int i = 0; i < itemstacks.size(); i++){
//            EntityItem ei = itemstacks.get(i);
//            ItemStack is = ei.item;
//            boolean found = false;
//            for(int m:meats){
//                if(m==is.itemID) found = true;
//            }
//            if(found && meatamt > 0){
//                while(meatamt > 0 && is.stackSize > 0){
//                    meatamt--;
//                    is.stackSize--;
//                }
//                if(is.stackSize <= 0) {
//                    mod_DustMod.killEntity(ei);
//                }
//                itemstacks.remove(ei);
//                i--;
//            }
//        }
//
//        ItemStack get = null;
//        for(EntityItem ei:itemstacks){
//            get = getItemstack(ei.item);
//            if(get != null){
//                mod_DustMod.killEntity(ei);
//                break;
//            }
//        }
//
//
//        if(get == null || meatamt > 0){
//            e.fizzle();
//            return;
//        }
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.pickaxeGold, 1)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req))
        {
//            System.out.println("check1");
            req = new ItemStack[] {new ItemStack(Item.swordGold, 1), new ItemStack(Block.glowStone, 1)};
            req = this.sacrifice(e, req);

            if (!checkSacrifice(req))
            {
//                System.out.println("check2");
                e.fizzle();
                return;
            }
            else
            {
//                System.out.println("check3");
                e.data[0] = 2;
            }
        }
        else
        {
            req = new ItemStack[] {new ItemStack(Block.tnt, 4)};
            req = this.sacrifice(e, req);

            if (!checkSacrifice(req))
            {
//                System.out.println("check4");
                e.fizzle();
                return;
            }

//            System.out.println("check5");
            e.data[0] = 1;
        }

        if (!this.takeXP(e, 18))
        {
            e.fizzle();
            return;
        }

//        System.out.println("blah " + (int)(e.data[0]));
    }

    public void onTick(EntityDust e)
    {
        e.setStarScale(e.getStarScale() + 0.001F);

        if (e.ticksExisted > 20)
        {
            Entity en = null;
            int itemID = 0;

            if (e.data[0] == 1)
            {
                itemID = DustMod.spiritPickaxe.shiftedIndex;
            }
            else if (e.data[0] == 2)
            {
                itemID = DustMod.spiritSword.shiftedIndex;
            }

            ItemStack create =  new ItemStack(itemID, 1, 0);

            if (e.data[0] == 2)
            {
                create.addEnchantment(Enchantment.knockback, 10);
                create.addEnchantment(Enchantment.smite, 5);
            }

//            System.out.println("derp " + create.itemID);
            en = new EntityItem(e.worldObj, e.posX, e.posY - EntityDust.yOffset, e.posZ, create);

            if (en != null)
            {
                en.setPosition(e.posX, e.posY - EntityDust.yOffset, e.posZ);
                e.worldObj.spawnEntityInWorld(en);
            }

            e.fade();
        }
    }

//    public static ItemStack getItemstack(ItemStack is){
////        System.out.println("CHECK " + is.itemID + " " + is.stackSize + " " + is.getItemDamage());
//        for(ItemStack i:entdrops.keySet()){
////            System.out.println("grr " + i.itemID + " " + i.stackSize + " " + i.getItemDamage());
//            if(i.itemID == is.itemID && (i.getItemDamage() == is.getItemDamage() || i.getItemDamage() == -1)){
////                System.out.println("ent found");
//                return entdrops.get(i);
//            }
//        }
////        System.out.println("ent not found");
//        return null;
//    }
//
//    public static HashMap<ItemStack,ItemStack> entdrops = new HashMap<ItemStack,ItemStack>();

//    static{
//        entdrops.put(new ItemStack(Item.pickaxeStone, 0, -1), new ItemStack(mod_DustMod.spiritPickaxe.shiftedIndex, 1, 0));
//        entdrops.put(new ItemStack(Item.swordStone, 0, -1), new ItemStack(mod_DustMod.spiritSword.shiftedIndex, 1, 0));
//    }
}