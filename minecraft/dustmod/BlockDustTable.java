package dustmod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDustTable extends BlockContainer 
{

	private Icon topTex;
	private Icon sideTex;
	private Icon botTex;
    public BlockDustTable(int i)
    {
        super(i, Material.wood);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        setLightOpacity(0);
        this.setHardness(3F);
        this.setHardness(2.5F);
        this.setUnlocalizedName("dustTable");
        this.setStepSound(Block.soundWoodFootstep);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

//    public void randomDisplayTick(World world, int i, int j, int k, Random random)
//    {
//        super.randomDisplayTick(world, i, j, k, random);
//        for (int l = i - 2; l <= i + 2; l++)
//        {
//            for (int i1 = k - 2; i1 <= k + 2; i1++)
//            {
//                if (l > i - 2 && l < i + 2 && i1 == k - 1)
//                {
//                    i1 = k + 2;
//                }
//                if (random.nextInt(16) != 0)
//                {
//                    continue;
//                }
//                for (int j1 = j; j1 <= j + 1; j1++)
//                {
//                    if (world.getBlockId(l, j1, i1) != Block.bookShelf.blockID)
//                    {
//                        continue;
//                    }
//                    if (!world.isAirBlock((l - i) / 2 + i, j1, (i1 - k) / 2 + k))
//                    {
//                        break;
//                    }
//                    world.spawnParticle("enchantmenttable", (double)i + 0.5D, (double)j + 2D, (double)k + 0.5D, (double)((float)(l - i) + random.nextFloat()) - 0.5D, (float)(j1 - j) - random.nextFloat() - 1.0F, (double)((float)(i1 - k) + random.nextFloat()) - 0.5D);
//                }
//            }
//        }
//    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        world.setBlockMetadataWithNotify(i, j, k, l - 1,3);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public Icon getBlockTextureFromSideAndMetadata(int i, int meta)
    {
        if (i == 1)
        {
            return topTex;
        }

        if (i == 0)
        {
            return botTex;
        }

        return sideTex;
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player,int dir, float x, float y, float z)
    {
        if (/*world.multiplayerWorld*/false)
        {
            return true;
        }
        else if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().itemID == DustMod.runicPaper.itemID)
        {
            int page = (((TileEntityDustTable)world.getBlockTileEntity(i, j, k)).page - 1);

            if (page == -1)
            {
                return true;
            }

            page = DustManager.getShape(page).id;
            ItemStack to = new ItemStack(DustMod.dustScroll, 1, page);
            ItemStack cur = player.getCurrentEquippedItem() ;

            if (cur.stackSize == 1)
            {
                cur.itemID = DustMod.dustScroll.itemID;
                cur.setItemDamage(to.getItemDamage());
            }
            else
            {
                player.inventory.addItemStackToInventory(to);
                cur.stackSize--;
            }

//            cur.itemID =
            return true;
        }
        else
        {
            if (player.isSneaking())
            {
                onBlockClicked(world, i, j, k, player);
                return true;
            }

            TileEntityDustTable tedt = (TileEntityDustTable)world.getBlockTileEntity(i, j, k);
            tedt.page --;

            if (tedt.page < 0)
            {
                tedt.page = DustManager.getNames().size() - DustMod.numSec;
            }

            return true;
        }
    }

    @Override
    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if (/*world.multiplayerWorld*/false)
        {
            return;
        }
        else
        {
            TileEntityDustTable tedt = (TileEntityDustTable)world.getBlockTileEntity(i, j, k);
            tedt.page++;

            if (tedt.page >= DustManager.getNames().size() - DustMod.numSec + 1)
            {
                tedt.page = 0;
            }
        }
    }

	@Override
	public TileEntity createNewTileEntity(World var1) {
        return new TileEntityDustTable();
	}

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
        this.topTex = par1IconRegister.func_94245_a(DustMod.resPath + "table_top");
        this.sideTex = par1IconRegister.func_94245_a(DustMod.resPath + "table_side");
        this.botTex = par1IconRegister.func_94245_a(DustMod.resPath + "table_bottom");
    }
}
