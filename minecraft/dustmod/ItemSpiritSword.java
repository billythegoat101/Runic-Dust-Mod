/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

/**
 *
 * @author billythegoat101
 */
public class ItemSpiritSword extends ItemSword
{
    public ItemSpiritSword(int i)
    {
        super(i, EnumToolMaterial.EMERALD);
        setMaxDamage(131);
        //[non-forge]
//        this.setIconIndex(ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/spiritsword.png"));
        //[forge]
        this.setIconCoord(1,1);
        this.setTextureFile(DustMod.path + "/dustItems.png");
    }

    public EnumRarity func_40398_f(ItemStack itemstack)
    {
        return EnumRarity.epic;
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
//    	System.out.println("UPDATE SWORD");
        if (!itemstack.isItemEnchanted())
        {
            itemstack.addEnchantment(Enchantment.knockback, 10);
            itemstack.addEnchantment(Enchantment.smite, 5);
        }

//        ItemStack curr = ((EntityPlayer)entity).getCurrentEquippedItem();
//        if(curr != null && curr.itemID == itemstack.itemID){
//            System.out.println("durr?");
////            RenderEntityDust red = new RenderEntityDust();
//            EntityDust ent = new EntityDust(world);
//            ent.renderStar = true;
//            ent.ticksExisted = entity.ticksExisted;
//            float rot = entity.rotationYaw*(float)Math.PI/180F + 5F*(float)Math.PI/6F;
//            ent.setPosition(entity.posX + MathHelper.cos(rot), entity.posY + ent.yOffset, entity.posZ + MathHelper.sin(rot));
//            ent.setVelocity(entity.motionX, entity.motionY, entity.motionZ);
//            ent.lifetime = 1;
//            world.spawnEntityInWorld(ent);
//        }
        super.onUpdate(itemstack, world, entity, i, flag);
    }

    public int getDamageVsEntity(Entity entity)
    {
        return 12;
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player,
    		Entity entity) {

    	Random rand = new Random();
    	double r = rand.nextDouble();
    	
    	int level = player.experienceLevel+5;
    	double tol = (double)level/25D;
    	
    	if(r < tol){
    		int amt = 1;
    		if(rand.nextDouble() < 0.5D) amt = 2;
    		EntityItem ei = player.dropPlayerItem(new ItemStack(DustMod.idust,amt,200));
    		ei.setPosition(entity.posX, entity.posY, entity.posZ);
    		ei.delayBeforeCanPickup = 0;
    	}
    	
    	return false;
    }
}
