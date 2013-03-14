package dustmod;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IArmorTextureProvider;
import net.minecraftforge.common.ISpecialArmor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWornInscription extends ItemArmor implements
		IArmorTextureProvider, ISpecialArmor {

	public static final int max = 1001;
	
	public ItemWornInscription(int id) {
		super(id, EnumArmorMaterial.CLOTH, 0, 1);
//		 this.hasSubtypes = true;
		this.setMaxDamage(1001);
	}

	@Override
	public ArmorProperties getProperties(EntityLiving player, ItemStack armor,
			DamageSource source, double damage, int slot) {
		int prevented = (int) damage
				- DustMod.inscriptionManager.getPreventedDamage(player, armor,
						source, (int) damage);
		ArmorProperties rtn = new ArmorProperties(0, 1d, prevented);
		// rtn.AbsorbRatio = 0.5D;
		rtn.Slot = 1;
		return rtn;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {

		return DustMod.inscriptionManager.getArmor(player, armor);
	}

	@Override
	public void damageArmor(EntityLiving entity, ItemStack stack,
			DamageSource source, int damage, int slot) {
		DustMod.inscriptionManager.onDamage(entity, stack, source, damage);
	}

	// public

	@Override
	public String getArmorTextureFile(ItemStack itemstack) {
		return DustMod.path + "/wornInscription.png";
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if (itemstack.getItemDamage() == -1
				|| InscriptionManager.getEvent(itemstack) == null) {
			return "inscblank";
		}
		return "insc." + InscriptionManager.getEvent(itemstack).idName;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List list) {
		for (InscriptionEvent i : InscriptionManager.getEvents()) {
			ItemStack item = new ItemStack(itemID, 1, 0);
			if (!item.hasTagCompound())
				item.setTagCompound(new NBTTagCompound());
			item.getTagCompound().setInteger("eventID", i.id);
			list.add(item);
		}
		// super.getSubItems(par1, par2CreativeTabs, list);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack item,
			EntityPlayer par2EntityPlayer, List list, boolean par4) {
		super.addInformation(item, par2EntityPlayer, list, par4);
		if(item.hasTagCompound() && item.getTagCompound().hasKey("description")){
			String desc = item.getTagCompound().getString("description");
			for(String s:desc.split("\n")){
				list.add(s);
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack item, World world,
			EntityPlayer p) {
		return InscriptionManager.onItemRightClick(p, item);
	}

	protected MovingObjectPosition lastMOP = null;
	protected long lastCheck = 0;

	public MovingObjectPosition getMovingObjectPositionFromPlayer(World world,
			EntityPlayer par2EntityPlayer, boolean par3) {
		// System.out.println("MOP Check " + world.getWorldTime() + " " +
		// lastCheck);
		if (lastCheck > world.getWorldTime())
			lastCheck = world.getWorldTime();
		if (lastMOP != null && world.getWorldTime() - lastCheck < 0) {
			// System.out.println("MOP Cache");
			return lastMOP;
		}
		lastCheck = world.getWorldTime();
		float var4 = 1.0F;
		float var5 = par2EntityPlayer.prevRotationPitch
				+ (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch)
				* var4;
		float var6 = par2EntityPlayer.prevRotationYaw
				+ (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw)
				* var4;
		double var7 = par2EntityPlayer.prevPosX
				+ (par2EntityPlayer.posX - par2EntityPlayer.prevPosX)
				* (double) var4;
		double var9 = par2EntityPlayer.prevPosY
				+ (par2EntityPlayer.posY - par2EntityPlayer.prevPosY)
				* (double) var4 + 1.62D - (double) par2EntityPlayer.yOffset;
		double var11 = par2EntityPlayer.prevPosZ
				+ (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ)
				* (double) var4;
		Vec3 var13 = world.getWorldVec3Pool().getVecFromPool(var7, var9, var11);
		float var14 = MathHelper.cos(-var6 * 0.017453292F - (float) Math.PI);
		float var15 = MathHelper.sin(-var6 * 0.017453292F - (float) Math.PI);
		float var16 = -MathHelper.cos(-var5 * 0.017453292F);
		float var17 = MathHelper.sin(-var5 * 0.017453292F);
		float var18 = var15 * var16;
		float var20 = var14 * var16;
		double var21 = 65.0D;
		// if (par2EntityPlayer instanceof EntityPlayerMP)
		// {
		// var21 =
		// ((EntityPlayerMP)par2EntityPlayer).theItemInWorldManager.getBlockReachDistance();
		// }
		Vec3 var23 = var13.addVector((double) var18 * var21, (double) var17
				* var21, (double) var20 * var21);
		lastMOP = world.rayTraceBlocks_do_do(var13, var23, par3, !par3);
		return lastMOP;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamageForRenderPass(int par1, int par2) {
		return this.iconIndex;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister par1IconRegister) {
		this.iconIndex = par1IconRegister.func_94245_a(DustMod.spritePath + "wornInscription");
	}
}
