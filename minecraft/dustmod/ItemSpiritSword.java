/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IconRegister;
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
    }

    public EnumRarity func_40398_f(ItemStack itemstack)
    {
        return EnumRarity.epic;
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        if (!itemstack.isItemEnchanted())
        {
            itemstack.addEnchantment(Enchantment.knockback, 10);
            itemstack.addEnchantment(Enchantment.smite, 5);
        }
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister par1IconRegister) {
		this.iconIndex = par1IconRegister.func_94245_a(DustMod.resPath + this.getUnlocalizedName().replace("item.", ""));
	}
}
