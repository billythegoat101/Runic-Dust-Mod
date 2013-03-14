/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;

/**
 * 
 * @author billythegoat101
 */
public class DEFarm extends DustEvent {
	public DEFarm() {
		super();
	}

	public void onInit(EntityDust e) {
		ItemStack[] req = new ItemStack[] { new ItemStack(Item.ingotIron, 8, -1) };
		sacrifice(e, req);

		if (req[0].stackSize > 0 || !this.takeXP(e, 4)) {
			e.fizzle();
			return;
		}
		e.setRenderStar(true);

		// e.ignoreRune = true;
		int dustID = e.dusts[e.dusts.length / 2][e.dusts[0].length / 2];
		// System.out.println("DATA: \n" + Arrays.deepToString(e.dusts));
		int r = 1;
		int cBase = 0;
		int cRand = 1;

		switch (dustID) {
		case 100:
			r = 1;
			cBase = 0;
			cRand = 2;
			break;

		case 200:
			r = 2;
			cBase = 1;
			cRand = 3;
			break;

		case 300:
			r = 3;
			cBase = 3;
			cRand = 3;
			break;

		case 400:
			r = 4;
			cBase = 4;
			cRand = 5;
			break;
		}
		e.data[0] = r;
		e.data[1] = cBase;
		e.data[2] = cRand;
	}

	public void onTick(EntityDust e) {

		if (e.ticksExisted == 0) {

			int r, cBase, cRand;
			r = e.data[0];
			cBase = e.data[1];
			cRand = e.data[2];

			int i = e.getX();
			int j = e.getY();
			int k = e.getZ();
			// System.out.println("R = " + r + " " + dustID);
			World world = e.worldObj;
			world.setBlockAndMetadataWithNotify(i, j - 1, k, Block.waterStill.blockID,0,3);
			Random rand = new Random();

			ArrayList<Double> locs = new ArrayList<Double>();
			
			for (int di = -r; di <= r; di++) {
				for (int dk = -r; dk <= r; dk++) {
					layer:

					for (int dj = r; dj >= -r; dj--) {
						int bidt = world.getBlockId(di + i, dj + j, dk + k);
						int bidb = world.getBlockId(di + i, dj + j - 1, dk + k);

						if ((bidb == Block.dirt.blockID
								|| bidb == Block.grass.blockID
								|| bidb == Block.tilledField.blockID || bidb == Block.sand.blockID)
								&& (bidt == 0 || DustMod.isDust(bidt) || bidt == Block.tallGrass.blockID)) {
							world.setBlockAndMetadataWithNotify(i + di, j + dj - 1,
									k + dk, Block.tilledField.blockID,0,3);
							int meta = cBase + rand.nextInt(cRand);

							if (meta > 7) {
								meta = 7;
							}

							world.setBlockAndMetadataWithNotify(i + di, j + dj,
									k + dk, 0, 0,3);
							world.setBlockAndMetadataWithNotify(i + di, j + dj,
									k + dk, Block.crops.blockID, meta,3);
							locs.add(i+di +0.5);
							locs.add((double)j+dj);
							locs.add(k+dk +0.5);
							// System.out.println("setting");
							break layer;
						}// else

						// System.out.println("wat " + bidb + " " + bidt + " " +
						// dj);
						// System.out.println("DURR " + bidu + "," + bid + " " +
						// Block.grass.blockID + " " + Block.dirt.blockID );
					}
				}
			}

			locs.add((double)i);
			locs.add(j-1D);
			locs.add((double)k);
			world.setBlockAndMetadataWithNotify(i, j - 1, k, Block.waterStill.blockID,0,3);
			
			double[] locations = new double[locs.size()];
			for(int d = 0; d < locs.size(); d++){
				locations[d] = locs.get(d);
			}
			DustMod.spawnParticles(e.worldObj, "smoke", locations, 0,0,0, 8, 0.5,0.2,0.5);
		}
		e.fade();
	}
}
