/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *
 * @author billythegoat101
 */
public class BlockRut extends BlockContainer
{
    public BlockRut(int i)
    {
        super(i, 7, Material.wood);
        this.setLightOpacity(0);
//        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
//        standTex = ModLoader.addOverride("/terrain.png", mod_DustMod.path + "/standTop.png");
    }

    @Override
    public int getRenderType()
    {
        return DustMod.proxy.getBlockModel(this);
    }
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        boolean notified = false;
        //DustModBouncer.notifyBlockChange(world, i, j, k, 0);
        for (int ix = -1; ix <= 1; ix++)
        {
            for (int iy = -1; iy <= 1; iy++)
            {
                for (int iz = -1; iz <= 1; iz++)
                {
                    if (ix == iy || ix == iz || iy == iz)
                    {
                        TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(i, j, k);
                        int check = world.getBlockId(i + ix, j + iy, k + iz);

                        if (ter.fluid == 0)
                        {
                            if (check == Block.lavaStill.blockID || check == Block.lavaMoving.blockID)
                            {
                                ter.setFluid(Block.lavaStill.blockID);
                                notified = true;
//                                mod_DustMod.notifyBlockChange(world, i, j, k, 0);
                            }
                            else if (check == Block.waterStill.blockID || check == Block.waterMoving.blockID)
                            {
                                ter.setFluid(Block.waterStill.blockID);
                                notified = true;
//                                mod_DustMod.notifyBlockChange(world, i, j, k, 0);
                            }
                        }

                        if (ter.fluid == Block.waterStill.blockID)
                        {
                            if (check == Block.lavaStill.blockID || check == Block.lavaMoving.blockID)
                            {
                                ter.setFluid(Block.cobblestone.blockID);
                                notified = true;
//                                mod_DustMod.notifyBlockChange(world, i, j, k, 0);
                            }
                        }

                        if (ter.fluid == Block.lavaStill.blockID)
                        {
                            if (check == Block.waterStill.blockID || check == Block.waterMoving.blockID)
                            {
                                ter.setFluid(Block.obsidian.blockID);
                                notified = true;
//                                mod_DustMod.notifyBlockChange(world, i, j, k, 0);
                            }
                        }
                    }
                }
            }
        }

//        if(((TileEntityRut)world.getBlockTileEntity(i, j, k)).updateNeighbors() && !notified){
//        	DustModBouncer.notifyBlockChange(world, i, j, k, 0);
//        }
        world.markBlockForRenderUpdate(i, j, k);
    }
    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer p, int face, float x, float y, float z)
    {

		if(!world.canMineBlock(p, i, j, k)) return false;
		
        ItemStack pItem = p.getCurrentEquippedItem();
        boolean isNull = (pItem == null);

        TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(i, j, k);

        if (ter.isBeingUsed)
        {
            return false;
        }

//        ter.fluid = Block.obsidian.blockID;
        if (/*ter.fluid == 0 && */!isNull && pItem.itemID == Item.bucketWater.itemID)
        {
            if (!p.capabilities.isCreativeMode)
            {
                pItem.itemID = Item.bucketEmpty.itemID;
            }

            ter.setFluid(Block.waterStill.blockID);
            return true;
        }

        if (/*ter.fluid == 0 && */!isNull && pItem.itemID == Item.bucketLava.itemID)
        {
            if (!p.capabilities.isCreativeMode)
            {
                pItem.itemID = Item.bucketEmpty.itemID;
            }

            ter.setFluid(Block.lavaStill.blockID);
            return true;
        }

        if (!isNull && (ter.fluid == 0 || ter.fluidIsFluid()))
        {
            int bid = pItem.itemID;
            
            if (bid < Block.blocksList.length && Block.blocksList[bid] != null && pItem.getItem() instanceof ItemBlock)
            {
                Block b = Block.blocksList[bid];

                if (b.renderAsNormalBlock() && b.isOpaqueCube() && (b.getBlockHardness(world, i,j,k) <= TileEntityRut.hardnessStandard || DustMod.Enable_Decorative_Ruts))
                {
                    if (!p.capabilities.isCreativeMode)
                    {
                        pItem.stackSize--;
                    }

                    ter.setFluid(bid);
                    return true;
                }
            }
        }

        if (!isNull && ter.fluid != 0 && !ter.fluidIsFluid() && (Block.blocksList[ter.fluid].getBlockHardness(world, i,j,k) <= TileEntityRut.hardnessStandard || DustMod.Enable_Decorative_Ruts))
        {
            if (Item.itemsList[pItem.itemID] instanceof ItemSpade)
            {
                this.dropBlockAsItem_do(world, i, j + 1, k, new ItemStack(ter.fluid, 1, 0));
                ter.setFluid(0);
                return true;
            }
        }

        if (isNull || pItem.itemID != DustMod.chisel.itemID)
        {
            return false;
        }

        Block block = Block.blocksList[ter.maskBlock];
        world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 6.0F, block.stepSound.getPitch() * 0.99F);

        
        int bx,by,bz;
        bx = (int)Math.floor(x*3);
        by = (int)Math.floor(y*3);
        bz = (int)Math.floor(z*3);
        
        bx = (int)Math.min(2, bx);
        by = (int)Math.min(2, by);
        bz = (int)Math.min(2, bz);
        
        toggleRut(ter, p, bx,by,bz);
        
        return true;
    }

    public void toggleRut(TileEntityRut rut, EntityPlayer p, int x, int y, int z)
    {
        rut.setRut(p, x, y, z, rut.getRut(x, y, z) == 0 ? 1 : 0);

        if (rut.isEmpty())
        {
            rut.resetBlock();
        }
    }

    private static int determineOrientation(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if (MathHelper.abs((float)entityplayer.posX - (float)i) < 2.0F && MathHelper.abs((float)entityplayer.posZ - (float)k) < 2.0F)
        {
            double d = (entityplayer.posY + 1.8200000000000001D) - (double)entityplayer.yOffset;

            if (d - (double)j > 2D)
            {
                return 1;
            }

            if ((double)j - d > 0.0D)
            {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double)((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;

        if (l == 0)
        {
            return 2;
        }

        if (l == 1)
        {
            return 5;
        }

        if (l == 2)
        {
            return 3;
        }

        return l != 3 ? 0 : 4;
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int b, int m) {
//		if (world.isRemote) {
//        	super.breakBlock(world, i, j, k, b, m);
//			return;
//		}
        TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(i, j, k);

        if (ter.isDead)
        {
        	super.breakBlock(world, i, j, k, b, m);
            return;
        }

        super.onBlockDestroyedByPlayer(world, i, j, k, m);
        int bid = ter.maskBlock;
        int meta = ter.maskMeta;
        int drop = Block.blocksList[bid].idDropped(meta, new Random(), 0);
        int mdrop = Block.blocksList[bid].damageDropped(meta);
        int qdrop = Block.blocksList[bid].quantityDropped(new Random());
        this.dropBlockAsItem_do(world, i, j, k, new ItemStack(drop, qdrop, mdrop));

        if (ter.fluid != 0 && !ter.fluidIsFluid() && ter.canEdit())
        {
            this.dropBlockAsItem_do(world, i, j, k, new ItemStack(ter.fluid, 1, 0));
        }
    	super.breakBlock(world, i, j, k, b, m);
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityRut();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockTexture(IBlockAccess world, int i,
    		int j, int k, int meta) {
    	TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(i, j, k);
    	Block b = Block.blocksList[ter.maskBlock];
    	return b.getBlockTexture(world, i, j, k, ter.maskMeta);
    }



    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World world, int i, int j, int k)
    {
    	TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(i, j, k);
    	return ter.maskBlock;
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    public int getDamageValue(World world, int i, int j, int k)
    {
    	TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(i, j, k);
    	return ter.maskMeta;
    }
    


    /**
     * Get a light value for the block at the specified coordinates, normal ranges are between 0 and 15
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return The light value
     */
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(x, y, z);
        
        Block block = blocksList[ter.fluid];
        if (block != null && block != this)
        {
        	DustMod.log("WTF " + ter.fluid + " " + block);
            return lightValue[ter.fluid];
        }
        return lightValue[blockID];
    }
}
