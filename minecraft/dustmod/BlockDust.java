/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author billythegoat101
 */
public class BlockDust extends BlockContainer {
	
	public static final int UNUSED_DUST = 0;
	public static final int ACTIVE_DUST = 1;
	public static final int DEAD_DUST = 2;
	public static final int ACTIVATING_DUST = 3;

	private Icon topTexture;
	private Icon sideTexture;
	public BlockDust(int i) {
		super(i, Material.circuits);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
		this.setHardness(0.2F);
		this.setStepSound(Block.soundGrassFootstep);
		this.disableStats();
	}

	@Override
	public Icon getBlockTextureFromSideAndMetadata(int i, int j) {
		
		return (i==1 ? topTexture:sideTexture);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i,
			int j, int k) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k,
			Entity entity) {
		int meta = world.getBlockMetadata(i,j,k);
		// if(world.isRemote) return;
		if (entity instanceof EntityItem && meta != DEAD_DUST) {
			EntityItem ei = (EntityItem) entity;
			ei.age = 0;
			EntityPlayer p = world.getClosestPlayerToEntity(ei, 0.6);

			if (p == null) {
				ei.delayBeforeCanPickup = 10;
				return;
			}

			double dist = p.getDistanceToEntity(ei);

//			if (dist < 0.2 && ei.delayBeforeCanPickup > 5) {
//				System.out.println("Drop " + dist);
//				ei.delayBeforeCanPickup = 5;
//			} else {
////				System.out.println("Grab " + dist);
//			}
		}

		if (entity instanceof EntityXPOrb && meta != DEAD_DUST) {
			EntityXPOrb orb = (EntityXPOrb) entity;
			orb.xpOrbAge = 0;
			EntityPlayer p = world.getClosestPlayerToEntity(orb, 3.0);

			if (p == null) {
				orb.setPosition(orb.prevPosX, orb.prevPosY, orb.prevPosZ);
				return;
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k,
			EntityLiving entityliving, ItemStack item) {
		super.onBlockPlacedBy(world, i, j, k, entityliving, item);
//		this.onBlockActivated(world, i, j, k, (EntityPlayer) entityliving, 0,
//				0, 0, 0);

		ItemStack equipped = ((EntityPlayer) entityliving).getCurrentEquippedItem();
		if (equipped != null) {
			if(equipped.itemID != DustMod.pouch.itemID)
				equipped.stackSize++;
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		Block block = Block.blocksList[world.getBlockId(i, j - 1, k)];

		if (block == null) {
			return false;
		} else {
			return world.isBlockSolidOnSide(i, j - 1, k, ForgeDirection.UP) || block == Block.glass || block == DustMod.rutBlock;
			// return block.renderAsNormalBlock() || block == Block.glass ||
			// world.isBlockSolidOnSide(i,j,k,0);
		}

		// return world.isBlockNormalCube(i, j - 1, k);
	}

	@Override
	public int getRenderType() {
		return DustMod.proxy.getBlockModel(this);
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		int meta = iblockaccess.getBlockMetadata(i, j, k);
		switch (meta) {
		case BlockDust.UNUSED_DUST:
			TileEntityDust ted = (TileEntityDust) iblockaccess
					.getBlockTileEntity(i, j, k);

			if (ted == null) {
				return 0xEFEFEF;
			}

			return ted.getRandomDustColor();

		case BlockDust.ACTIVE_DUST://case 3:
		case BlockDust.ACTIVATING_DUST:
			return 0xDD0000;

		case BlockDust.DEAD_DUST:
			return 0xEFEFEF;

		default:
//			System.out.println("derp? "
//					+ iblockaccess.getBlockMetadata(i, j, k));
			return 0;
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (world.isRemote) {
			return;
		}

		// if (world.multiplayerWorld) {
		// return;
		// }
		int i1 = world.getBlockMetadata(i, j, k);

		// boolean flag = world.getBlockId(i, j, k) ==
		// 0;//world.canBlockBePlacedAt(blockID, i, j, k, true, i1);
		if (world.getBlockId(i, j - 1, k) == 0
				|| !Block.blocksList[world.getBlockId(i, j - 1, k)].blockMaterial
						.isSolid()) {
			// System.out.println("aww " + i + " " + (j-1) + " " + k + " " + i1
			// + " " + world.getBlockId(i, j-1, k));
			// if (world.getBlockMetadata(i, j, k) == 0) {
			// onBlockRemoval(world, i, j, k);
			// }
			world.setBlockAndMetadataWithNotify(i, j, k, 0, 0, 3);
		} else if (world.isBlockIndirectlyGettingPowered(i, j, k) && i1 == 0) {
			updatePattern(world, i, j, k, null);
			world.notifyBlockChange(i, j, k, 0);
		} 
		
		TileEntityDust ted = (TileEntityDust)world.getBlockTileEntity(i, j, k);
		ted.onNeighborBlockChange();

		super.onNeighborBlockChange(world, i, j, k, l);
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an
	 * update, as appropriate
	 */
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int i, int j, int k) {
//		if (world.isRemote) {
//			return;
//		}

		if (world.getBlockMetadata(i, j, k) > 0) {
			world.setBlockAndMetadataWithNotify(i, j, k, 0,0,3);
			// for(int x = -1; x <= 1; x++)
			// for(int z = -1; z <= 1; z++){
			// if(world.getBlockId(i+x, j, k+z) == blockID &&
			// world.getBlockMetadata(i+x, j, k+z) == 1){
			// world.setBlockWithNotify(i+x,j,k+z,0);
			// }
			// }
		} else {
			TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(i,
					j, k);

//			if (world.isRemote) {
//				super.breakBlock(world, i, j, k, b, m);
//				return;
//			}

			if (ted == null || ted.isEmpty()) {
				// System.out.println("TED Was empty!!");
				return true;
			}

			// int amt = ted.getAmount()-1;
			// System.out.println("REMOVE " + amt+1);
			// int meta = world.getBlockMetadata(i, j, k);

			for (int x = 0; x < ted.size; x++) {
				for (int z = 0; z < ted.size; z++) {
					int dust = ted.getDust(x, z);

					if (dust > 0) {
						// if
						// (!ModLoader.getMinecraftInstance().thePlayer.capabilities.isCreativeMode)
						// {
						if(!player.capabilities.isCreativeMode)
							this.dropBlockAsItem_do(world, i, j, k, new ItemStack(
								DustMod.idust.itemID, 1, dust));
					}
					// }
				}
			}
		}

		return super.removeBlockByPlayer(world, player, i, j, k);
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer p, int face, float x, float y, float z) {

		if(!world.canMineBlock(p, i, j, k)) return false;
		
		ItemStack item = p.getCurrentEquippedItem();
		
		if(item != null && item.itemID == DustMod.chisel.itemID){
			int bid = world.getBlockId(i, j-1, k);
			if(bid == DustMod.rutBlock.blockID){
				return DustMod.rutBlock.onBlockActivated(world, i, j-1, k, p, face, x, y, z);
			}
		}

		if (world.getBlockMetadata(i, j, k) == ACTIVE_DUST) {
			TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(i,
					j, k);
			ted.onRightClick(p);
			return true;
		} else if (world.getBlockMetadata(i, j, k) > 1) {
			return false;
		}

		if (p.isSneaking()) {
			if(item == null || item.getItem() != DustMod.tome){
				onBlockClicked(world, i, j, k, p);
			}

			return false;
		}

		if (!world.isRemote
				&& item != null
				&& item.itemID == DustMod.tome.itemID) {
			updatePattern(world, i, j, k, p);
			world.notifyBlockChange(i, j, k, 0);
			return true;
		}

		if (item == null
				|| (item.itemID != DustMod.idust.itemID 
				&& item.itemID != DustMod.pouch.itemID)) {
			return false;
		}

		

		boolean isPouch = (item.itemID == DustMod.pouch.itemID);
		int dust = item.getItemDamage();// mod_DustMod.dustValue(p.getCurrentEquippedItem().itemID);
		if(isPouch) dust = ItemPouch.getValue(item);
		if(dust < 5) dust *= 100;
		
		if(isPouch && ItemPouch.getDustAmount(item) <= 0){
			return false;
		}
		
		int rx = (int)Math.floor(x*TileEntityDust.size);
		int rz = (int)Math.floor(z*TileEntityDust.size);
		rx = Math.min(TileEntityDust.size-1, rx);
		rz = Math.min(TileEntityDust.size-1, rz);
		
		// System.out.println("Result: " + rx + " " + rz);
		TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(
				i, j, k);

		if (ted.getDust(rx, rz) <= 0) {
			if (ted.getDust(rx, rz) == -2) {
				setVariableDust(ted, rx, rz, p, dust);
			} else {
				ted.setDust(p, rx, rz, dust);

				if (!p.capabilities.isCreativeMode) {
					ItemPouch.subtractDust(item, 1);

					if (!isPouch && item.stackSize == 0) {
						p.destroyCurrentEquippedItem();
					}
				}
			}

			world.notifyBlockChange(i, j, k, 0);
			world.playSoundEffect((float) i + 0.5F, (float) j + 0.5F,
					(float) k + 0.5F, stepSound.getStepSound(),
					(stepSound.getVolume() + 1.0F) / 6.0F,
					stepSound.getPitch() * 0.99F);
		}
		return true;
	}

	private void setVariableDust(TileEntityDust ted, int x, int z,
			EntityPlayer p, int dust) {
		if (ted.getDust(x, z) != -2) {
			return;
		}

		boolean found = false;

		if (!p.capabilities.isCreativeMode) {
			for (int sind = 0; sind < p.inventory.mainInventory.length; sind++) {
				ItemStack is = p.inventory.mainInventory[sind];

				if (is != null && ((is.itemID == DustMod.idust.itemID
						&& is.getItemDamage() == dust) ||
						(is.itemID == DustMod.pouch.itemID && ItemPouch.getValue(is) == dust && ItemPouch.getDustAmount(is) > 0))) {
					ItemPouch.subtractDust(is, 1);

					if (ItemPouch.getDustAmount(is) == 0 && is.itemID != DustMod.pouch.itemID) {
						p.inventory.mainInventory[sind] = null;
					}

					found = true;
					break;
				}
			}
		} else {
			found = true;
		}

		if (!found) {
			return;
		}

		ted.setDust(p, x, z, dust);

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 || j == 0) {
					int wx = ted.xCoord;
					int wz = ted.zCoord;
					int ix = x + i;
					int iz = z + j;

					if (ix < 0) {
						ix = ted.size - 1;
						wx--;
					} else if (ix >= ted.size) {
						ix = 0;
						wx++;
					}

					if (iz < 0) {
						iz = ted.size - 1;
						wz--;
					} else if (iz >= ted.size) {
						iz = 0;
						wz++;
					}

					TileEntity te = p.worldObj.getBlockTileEntity(wx,
							ted.yCoord, wz);

					if (!(te instanceof TileEntityDust)) {
						continue;
					}

					TileEntityDust nted = (TileEntityDust) te;
					setVariableDust(nted, ix, iz, p, dust);
				}
			}
		}
	}

	@Override
	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer p) {
				
		if(!world.canMineBlock(p, i, j, k)) return;

		Vec3 look = p.getLookVec();
		double mx = look.xCoord;// Math.cos((p.rotationYaw+90)*Math.PI/180);
		double my = look.yCoord;// Math.sin(-p.rotationPitch*Math.PI/180);
		double mz = look.zCoord;// Math.sin((p.rotationYaw+90)*Math.PI/180);

		for (double test = 0; test < 4; test += 0.01) {
			double tx = p.posX + mx * test;
			double ty = p.posY + p.getEyeHeight() + my * test;
			double tz = p.posZ + mz * test;

			if (ty - (double) j <= 0.02) {
				double dx = Math.abs(tx - (double) i) - 0.02;
				double dz = Math.abs(tz - (double) k) - 0.02;
				int rx = (int) Math.floor(dx * TileEntityDust.size);
				int rz = (int) Math.floor(dz * TileEntityDust.size);

				if (rx >= TileEntityDust.size) {
					rx = TileEntityDust.size - 1;
				}

				if (rz >= TileEntityDust.size) {
					rz = TileEntityDust.size - 1;
				}

				if (rx < 0) {
					rx = 0;
				}

				if (rz < 0) {
					rz = 0;
				}

				TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(
						i, j, k);

				if (ted.getDust(rx, rz) != 0
						&& world.getBlockMetadata(i, j, k) == 0) {
					if (ted.getDust(rx, rz) > 0
							&& !p.capabilities.isCreativeMode) {
						this.dropBlockAsItem_do(world, i, j, k,
								new ItemStack(DustMod.idust.itemID, 1,
										ted.getDust(rx, rz)));
					}

					world.playSoundEffect((float) i + 0.5F, (float) j + 0.5F,
							(float) k + 0.5F, stepSound.getStepSound(),
							(stepSound.getVolume() + 1.0F) / 6.0F,
							stepSound.getPitch() * 0.99F);
					world.notifyBlockChange(i, j, k, 0);
					ted.setDust(p, rx, rz, 0);

					// System.out.println("drop click");
					if (ted.isEmpty() && world.getBlockMetadata(i, j, k) != 10) {
						// System.out.println("Destroying");
						world.setBlockAndMetadataWithNotify(i, j, k, 0,0,3);
						this.onBlockDestroyedByPlayer(world, i, j, k, 0);
					}
				}
				break;
			}

			// world.setBlock((int)tx, (int)ty, (int)tz, Block.brick.blockID);
		}

		// super.onBlockClicked(world, i, j, k, p);
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return 0;// i == 0 ? mod_DustMod.ITEM_DustID+256:0;
	}

	public void updatePattern(World world, int i, int j, int k, EntityPlayer p) {
		List<Integer[]> n = new ArrayList<Integer[]>();
		addNeighbors(world, i, j, k, n);

		if (n.size() == 0) {
			return; // dudewat
		}

		for (Integer[] iter : n) {
			if (world.getBlockId(iter[0], j, iter[2]) == blockID) {
				world.setBlockMetadataWithNotify(iter[0], j, iter[2], ACTIVATING_DUST,2);
			}
		}

		int sx = n.get(0)[0];
		int sz = n.get(0)[2];
		int mx = n.get(0)[0];
		int mz = n.get(0)[2];

		for (Integer[] iter : n) {
			if (iter[0] < sx) {
				sx = iter[0];
			}

			if (iter[2] < sz) {
				sz = iter[2];
			}

			if (iter[0] > mx) {
				mx = iter[0];
			}

			if (iter[2] > mz) {
				mz = iter[2];
			}
		}

		int size = TileEntityDust.size;
		int dx = mx - sx;
		int dz = mz - sz;
		int[][] map = new int[(mx - sx + 1) * size][(mz - sz + 1) * size];

		for (int x = 0; x <= dx; x++) {
			for (int z = 0; z <= dz; z++) {
				if (world.getBlockId(x + sx, j, z + sz) == blockID) {
					TileEntityDust ted = (TileEntityDust) world
							.getBlockTileEntity(x + sx, j, z + sz);

					for (int ix = 0; ix < size; ix++) {
						for (int iz = 0; iz < size; iz++) {
							map[ix + x * size][iz + z * size] = ted.getDust(ix,
									iz);
						}
					}
				}
			}
		}

		// System.out.println("ASNASO " + Arrays.deepToString(map));
		DustManager.callShape(world, (double) sx + (double) dx / 2
				+ 0.5D, j + 1D, (double) sz + (double) dz / 2 + 0.5D, map, n,
				(p == null) ? null : p.username);
	}

	public void addNeighbors(World world, int i, int j, int k,
			List<Integer[]> list) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (world.getBlockId(i + x, j, k + z) == blockID
						&& world.getBlockMetadata(i + x, j, k + z) == 0) {
					boolean cont = true;
					stopcheck:

					for (Integer[] iter : list) {
						if (iter[0] == i + x && iter[2] == k + z) {
							cont = false;
							break stopcheck;
						}
					}

					if (cont) {
						list.add(new Integer[] { i + x, j, k + z });
						addNeighbors(world, i + x, j, k + z, list);
					}
				}
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityDust();
	}


    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World world, int i, int j, int k)
    {
        return world.getBlockId(i,j-1,k);
    }
    

    /**
     * Get the block's damage value (for use with pick block).
     */
    public int getDamageValue(World world, int i, int j, int k)
    {
        return world.getBlockMetadata(i,j-1,k);
    }
    


    @SideOnly(Side.CLIENT)

    /**
     * Returns the default ambient occlusion value based on block opacity
     */
    public float getAmbientOcclusionLightValue(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return 0;
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
        int meta = world.getBlockMetadata(x, y, z);
        if(meta == ACTIVE_DUST || meta == ACTIVATING_DUST){
        	return 8;
        }
        return lightValue[blockID];
    }

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
        this.topTexture = par1IconRegister.func_94245_a(DustMod.spritePath + "dust_top");
        this.sideTexture = par1IconRegister.func_94245_a(DustMod.spritePath + "dust_side");
    }
    
}
