/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * 
 * @author billythegoat101
 */
public abstract class DustEvent {

	protected ArrayList<ArrayList<Sacrifice>> waitingSacrifices;
	private int sacID = 0;
	public String name;
	public boolean secret = false;
	public String permission = "ALL";
	
	// Set by the coder, unalterable by user configs.
	// Setting it to false disallows all use, but if true it will not affect
	// anything
	// This is for the case where something breaks, it gives the coder time to
	// fix it later
	public boolean permaAllowed = true;

	public DustEvent() {
		waitingSacrifices = new ArrayList<ArrayList<Sacrifice>>();
	}

	public DustEvent addSacr(Sacrifice s) {
		if (sacID >= waitingSacrifices.size()) {
			waitingSacrifices.add(new ArrayList<Sacrifice>());
		}

		waitingSacrifices.get(sacID).add(s);
		return this;
	}

	public DustEvent addSacrificeList(Sacrifice... s) {
		List<Sacrifice> sac = Arrays.asList(s);

		if (sacID >= waitingSacrifices.size()) {
			waitingSacrifices.add(new ArrayList<Sacrifice>());
		}

		ArrayList<Sacrifice> current = waitingSacrifices.get(sacID);
		current.addAll(sac);
		sacID++;
		return this;
	}

	public DustEvent shiftSacr() {
		sacID++;
		return this;
	}

	public final void init(EntityDust e) {
		onInit(e);
	}

	protected void initGraphics(EntityDust e) {

	}

	protected void onInit(EntityDust e) {
	}

	public final void tick(EntityDust e) {
		if (e.sacrificeWaiting > 0) {
			e.sacrificeWaiting--;

			for (ArrayList<Sacrifice> arr : waitingSacrifices) {
				Sacrifice[] sacr = sacrifice(e, arr);
				boolean cont = true;

				for (Sacrifice s : sacr) {
					if (!s.isComplete) {
						cont = false;
						break;
					}
				}

				if (cont) {
					handle(e, sacr);
					e.sacrificeWaiting = -1;
				}
			}
		} else if (e.sacrificeWaiting == 0) {
//			System.out.println("Waiting sacrifice = 0 death");
			e.fizzle();
			return;
		} else {
			onTick(e);
		}
	}

	protected void onTick(EntityDust e) {
	}

	public void onRightClick(EntityDust e, TileEntityDust ted, EntityPlayer p) {
	}

	public final void unload(EntityDust e) {
		onUnload(e);
	}

	protected void onUnload(EntityDust e) {
		if (e.rutPoints != null) {
			for (Integer[] i : e.rutPoints) {
				// world.setBlockWithNotify(i[0], i[1], i[2],
				// Block.melon.blockID);
				TileEntityRut ter = (TileEntityRut) e.worldObj
						.getBlockTileEntity(i[0], i[1], i[2]);

				if (ter != null) {
					ter.isBeingUsed = false;
				}
			}
		}
	}

	/**
	 * Will the server send this rune's info to the player
	 * 
	 * @param player
	 *            the player
	 * @return true if can see, false otherwise
	 */
	public boolean canPlayerKnowRune(String player) {
		boolean isOP = false;
		if(player != null && !player.isEmpty())
			try {
				MinecraftServer server = MinecraftServer.getServer();
				ServerConfigurationManager manager = server.getConfigurationManager();
				Set ops = manager.getOps();
				isOP = (ops == null || ops.contains(player.toLowerCase()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		return permaAllowed
				&& (this.permission.equals("ALL") || (this.permission
						.equals("OPS") && isOP));
	}

	/**
	 * Take experience levels from the closest nearby player.
	 * 
	 * @param e
	 *            EntityDust instance
	 * @param levels
	 *            number of levels to take
	 * @return True if levels can be (and have been) taken, false if player
	 *         doesn't have enough
	 */
	public boolean takeXP(EntityDust e, int levels) {
		EntityPlayer player = e.worldObj.getClosestPlayerToEntity(e, 12D);

		if (player != null && player.capabilities.isCreativeMode) {
			return true;
		}

		if (player != null) {
			if (player.experienceLevel >= levels) {
				player.addExperienceLevel(-levels);
				return true;
			}
		}

		return false;
	}

	/**
	 * Take hunger bars from the closest nearby player
	 * 
	 * @param e
	 *            EntityDust instance
	 * @param halves
	 *            Number of bars to take (in halves)
	 * @return True if bars can be (and have been) taken, false if player
	 *         doesn't have enough
	 */
	public boolean takeHunger(EntityDust e, int halves) {
		EntityPlayer player = e.worldObj.getClosestPlayerToEntity(e, 12D);

		if (player != null && player.capabilities.isCreativeMode) {
			return true;
		}

		if (player != null) {
			if (player.getFoodStats().getFoodLevel() >= halves) {
				player.getFoodStats().addStats(-halves, 0);
				return true;
			}
		}

		return false;
	}

	/**
	 * Takes hearts away from the nearest player (in terms of half-hearts)
	 * 
	 * @param e
	 *            EntityDust instance
	 * @param halves
	 *            Number of halves of hearts to take.
	 * @param kill
	 *            Whether to kill the player if they don't have enough hearts
	 * @return True if the closest player has enough hearts and they have been
	 *         taken.
	 */
	public boolean takeLife(EntityDust e, int halves, boolean kill) {
		EntityPlayer player = e.worldObj.getClosestPlayerToEntity(e, 12D);

		if (player.capabilities.isCreativeMode) {
			return true;
		}

		if (player != null) {
			if (player.getHealth() >= halves) {
				player.attackEntityFrom(DamageSource.magic, halves);
				return true;
			} else if (kill) {
				player.attackEntityFrom(DamageSource.magic, halves);
			}
		}

		return false;
	}

	/**
	 * Loop through a list of ItemStacks and see if they are all empty. Useful
	 * to see if the sacrifice has been fulfilled.
	 * 
	 * @param req
	 *            The array of ItemStacks
	 * @return True if all items in the array have stackSizes <= 0, false if any
	 *         are >0
	 */
	public boolean checkSacrifice(ItemStack[] req) {
		for (ItemStack i : req) {
			if (i.stackSize > 0) {
				return false;
			}
		}

		return true;
	}

	public List getEntities(World world, double x, double y, double z) {
		return getEntities(world, x, y, z, 1D);
	}

	/**
	 * Get all entities within 1 block of the given Entity's position
	 * 
	 * @param e
	 *            The entity to look around
	 * @return A list of all entities within 1 block of the given entity's
	 *         position (including the entity itself)
	 */
	public List getEntities(Entity e) {
		return getEntities(e.worldObj, e.posX, e.posY - e.yOffset, e.posZ, 1D);
	}

	/**
	 * Get all entities within a radius of the given Entity's position
	 * 
	 * @param e
	 *            The entity to look around
	 * @param r
	 *            The radius to look around
	 * @return A list containing all entities within the radius of the given
	 *         Entity's position (including the entity itself)
	 */
	public List getEntities(Entity e, double r) {
		return getEntities(e.worldObj, e.posX, e.posY - e.yOffset, e.posZ, r);
	}

	/**
	 * Get all entities within a given radius of the coordinates
	 * 
	 * @param world
	 *            The current world to check in
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param z
	 *            Z coordinate
	 * @param radius
	 *            The radius around the location to check
	 * @return A list containing all entities within the radius of the
	 *         coordinates
	 */
	public List getEntities(World world, double x, double y, double z,
			double radius) {
		List l = world.getEntitiesWithinAABBExcludingEntity(
				null,
				AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D,
						z + 1.0D).expand(radius, radius, radius));
		return l;
	}

	public List getEntities(World world, Class entType, double x, double y,
			double z, double radius) {
		List l = world.getEntitiesWithinAABB(
				entType,
				AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D,
						z + 1.0D).expand(radius, radius, radius));
		// System.out.println("Retrieving entities " +
		// world.worldProvider.worldType + " [" + x + "," + y + "," + z + "] " +
		// l.size());
		return l;
	}

	public List getEntitiesExcluding(World world, Entity e, double x, double y,
			double z, double radius) {
		List l = world.getEntitiesWithinAABBExcludingEntity(
				e,
				AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D,
						z + 1.0D).expand(radius, radius, radius));
		// System.out.println("Retrieving entities " +
		// world.worldProvider.worldType + " [" + x + "," + y + "," + z + "] " +
		// l.size());
		return l;
	}

	/**
	 * Get a list of all dropped items within a 1 block radius of the EntityDust
	 * 
	 * @param e
	 *            The EntityDust instance
	 * @return a List<EntityItem> of all nearby dropped items
	 */
	public final List<EntityItem> getItems(EntityDust e) {
		return getItems(e, 1D);
	}

	/**
	 * Get a list of all dropped items within a given radius of the EntityDust
	 * 
	 * @param e
	 *            The EntityDust instance
	 * @param radius
	 *            The radius in which to check for dropped items
	 * @return A List<EntityItem> of all nearby dropped items.
	 */
	public final List<EntityItem> getItems(EntityDust e, double radius) {
		ArrayList<EntityItem> itemstacks = new ArrayList<EntityItem>();
		List l = getEntities(e.worldObj, e.posX, e.posY - e.yOffset, e.posZ,
				radius);

		for (Object o : l) {
			if (o instanceof EntityItem) {
				EntityItem ei = (EntityItem) o;
				itemstacks.add(ei);
			}
		}

		return itemstacks;
	}

	/**
	 * Checks around the rune for the listed items and destroys them. Returns
	 * true if all listed items are found
	 * 
	 * @param e
	 *            EntityDust instance
	 * @param items
	 *            Itemst to look for
	 * @return True if all items found and consumed
	 */
	public final boolean takeItems(EntityDust e, ItemStack... items) {
		List<EntityItem> sacrifice = getItems(e);

		for (EntityItem i : sacrifice) {
			for (ItemStack item : items) {
				ItemStack is = i.getEntityItem();
				if (is.itemID == DustMod.negateSacrifice.itemID) {
					return true;
				}

				if (is.itemID == item.itemID
						&& (item.getItemDamage() == -1 || i.getEntityItem()
								.getItemDamage() == item.getItemDamage())) {
					if (is.stackSize <= item.stackSize
							&& item.stackSize > 0) {
						item.stackSize -= is.stackSize;
						i.setDead();
					} else {
						is.stackSize -= item.stackSize;
						i.func_92058_a(is);
						break;
					}
				}
			}
		}

		for (ItemStack item : items)
			if (item.stackSize > 0) {
				return false;
			}

		return true;
	}

	public void registerFollower(EntityDust e, Object o) {
	}

	public ItemStack[] sacrifice(EntityDust e, ItemStack[] req) {
		List<EntityItem> sacrifice = getItems(e);
		boolean negate = false;

		for (EntityItem i : sacrifice) {
			ItemStack is = i.getEntityItem();

			if (is.itemID == DustMod.negateSacrifice.itemID) {
				negate = true;
				break;
			}

			for (ItemStack c : req) {
				if (c.itemID == is.itemID
						&& (c.getItemDamage() == -1 || c.getItemDamage() == is
								.getItemDamage())) {
					if(c.hasTagCompound() == is.hasTagCompound() || c.getItemDamage() == -1){
						boolean match = false;
						if(c.hasTagCompound() && c.getItemDamage() != -1){
							match = c.getTagCompound().equals(is.getTagCompound());
						}else{
							match = true;
						}
						while (match && c.stackSize > 0 && is.stackSize > 0) {
							is.stackSize--;
							c.stackSize--;
						}
					}
				}
			}

			if (is.stackSize <= 0) {
				i.setDead();
			} else {
				double rx, rz;
				double r = 0.1;
				double min = 0.065;
				rx = Math.random() * r * 2 - r;
				rx += (rx < 0) ? -min : min;
				rz = Math.random() * r * 2 - r;
				rz += (rz < 0) ? -min : min;
				i.addVelocity(rx, Math.random() * 0.5, rz);
			}
	        i.func_92058_a(is);
		}

		if (negate) {
			for (ItemStack c : req) {
				c.stackSize = 0;
			}
		}


		return req;
	}

	public Sacrifice[] sacrifice(EntityDust e, ArrayList<Sacrifice> reqA) {
		List<Entity> sacrifice = this.getEntities(e, 3D);
		Sacrifice[] req = new Sacrifice[reqA.size()];

		for (int i = 0; i < reqA.size(); i++) {
			Sacrifice is = reqA.get(i);
			req[i] = is.clone();
		}

		boolean negate = false;

		for (Entity i : sacrifice) {
			EntityItem ei = null;

			if (i instanceof EntityItem) {
				ei = (EntityItem) i;
			}

			if (ei != null
					&& ei.getEntityItem().itemID == DustMod.negateSacrifice.itemID) {
				negate = true;
				break;
			}

			for (Sacrifice s : req) {
				if (s.isComplete) {
					continue;
				}

				if (!(i instanceof EntityPlayer || i instanceof EntityDust)
						&& s.matchObject(i)) {
					if (ei != null) {
						s.itemType.stackSize -= ei.getEntityItem().stackSize;

						if (s.itemType.stackSize == 0) {
							s.isComplete = true;
						}
					} else {
						s.isComplete = true;
					}

//					System.out.println("Sacrifice matched to " + i + " "
//							+ s.isComplete);
					s.entity = i;
					break;
				}
			}
		}

		if (negate)
			for (Sacrifice s : req) {
				s.isComplete = true;
			}

		return req;
	}

	public void handle(EntityDust e, Sacrifice[] sac) {
		for (Sacrifice s : sac) {
			s.handleObject(e, s.entity);
		}
	}

	protected final void findRuts(EntityDust e) {
		World w = e.worldObj;
		// int ix = e.getX();
		// int iy = e.getY();
		// int iz = e.getZ();
		//
		// iy = e.dustPoints.get(0)[1]-1;
		ArrayList<Integer[]> ruts = new ArrayList<Integer[]>();

		for (Integer[] i : e.dustPoints) {
			ruts.add(new Integer[] { i[0], i[1] - 1, i[2] });
			checkNeighbors(w, ruts, i[0], i[1] - 1, i[2]);
		}

		e.rutPoints = ruts;
	}

	protected final void findRutsWithDistance(EntityDust e, int distance) {
		World w = e.worldObj;
		// int ix = e.getX();
		// int iy = e.getY();
		// int iz = e.getZ();
		//
		// iy = e.dustPoints.get(0)[1]-1;
		ArrayList<Integer[]> ruts = new ArrayList<Integer[]>();

		for (Integer[] i : e.dustPoints) {
			ruts.add(new Integer[] { i[0], i[1] - 1, i[2] });
			checkNeighborsWithDistance(w, ruts, i[0], i[1] - 1, i[2],
					distance - 1);
		}

		e.rutPoints = ruts;
	}

	protected final void findRuts(EntityDust e, int fluidID) {
		World w = e.worldObj;
		// int ix = e.getX();
		// int iy = e.getY();
		// int iz = e.getZ();
		//
		// iy = e.dustPoints.get(0)[1]-1;
		ArrayList<Integer[]> ruts = new ArrayList<Integer[]>();

		for (Integer[] i : e.dustPoints) {
			ruts.add(new Integer[] { i[0], i[1] - 1, i[2] });
			checkNeighbors(w, ruts, i[0], i[1] - 1, i[2], fluidID);
		}

		e.rutPoints = ruts;
	}

	protected final boolean findRutArea(EntityDust e) {
		World w = e.worldObj;
		List<Integer[]> horiz = new ArrayList<Integer[]>();
		List<Integer[]> length = new ArrayList<Integer[]>();
		List<Integer[]> vert = new ArrayList<Integer[]>();
		List<Integer[]> farea = new ArrayList<Integer[]>();
		List<Integer[]> ruts = e.rutPoints;

		if (ruts == null) {
			findRuts(e);
			ruts = e.rutPoints;

			if (ruts == null) {
				return false;
			}
		}

		int rutSize = ruts.size();

		for (int i = 0; i < rutSize; i++) {
			Integer[] p = ruts.get(i);
			int ix = p[0];
			int iy = p[1];
			int iz = p[2];
			next:

			for (int j = 0; j < rutSize; j++) {
				if (i != j) {
					Integer[] t = ruts.get(j);
					int jx = t[0];
					int jy = t[1];
					int jz = t[2];

					if (iy == jy && ix == jx && jz > iz) {
						int dist = jz - iz;

						for (int l = 1; l < dist; l++) {
							Integer[] a = new Integer[] { jx, jy, iz + l };
							length.add(a);
						}

						// break next;
					}

					if (iy == jy && iz == jz && jx > ix) {
						int dist = jx - ix;

						for (int h = 1; h < dist; h++) {
							Integer[] a = new Integer[] { ix + h, jy, jz };
							horiz.add(a);
						}

						// break next;
					}

					if (ix == jx && iz == jz && jy > iy) {
						int dist = jy - iy;

						for (int v = 1; v < dist; v++) {
							Integer[] a = new Integer[] { jx, iy + v, jz };
							vert.add(a);
						}

						// break next;
					}
				}
			}
		}

		// check for confimration
		for (Integer[] h : horiz) {
			int hx = h[0];
			int hy = h[1];
			int hz = h[2];
			boolean get = false;
			next:

			for (Integer[] v : vert) {
				int vx = v[0];
				int vy = v[1];
				int vz = v[2];

				if (hx == vx && hy == vy && hz == vz) {
					farea.add(new Integer[] { vx, vy, vz });
					get = true;
					// break next;
				}
			}

			if (!get)
				next: for (Integer[] l : length) {
					int lx = l[0];
					int ly = l[1];
					int lz = l[2];

					if (hx == lx && hy == ly && hz == lz) {
						farea.add(new Integer[] { lx, ly, lz });
						// farea.add(new Integer[]{lx,ly+3,lz});
						// farea.add(new Integer[]{hx,hy+1,hz});
						get = true;
						break next;
					}
				}
		}

		for (Integer[] v : vert) {
			int vx = v[0];
			int vy = v[1];
			int vz = v[2];
			boolean get = false;
			next:

			for (Integer[] h : horiz) {
				int hx = h[0];
				int hy = h[1];
				int hz = h[2];

				if (vx == hx && vy == hy && vz == hz) {
					farea.add(new Integer[] { hx, hy, hz });
					get = true;
					// break next;
				}
			}

			if (!get)
				next: for (Integer[] l : length) {
					int lx = l[0];
					int ly = l[1];
					int lz = l[2];

					if (vx == lx && vy == ly && vz == lz) {
						farea.add(new Integer[] { lx, ly, lz });
						get = true;
						// break next;
					}
				}
		}

		for (Integer[] l : length) {
			int lx = l[0];
			int ly = l[1];
			int lz = l[2];
			boolean get = false;
			next:

			for (Integer[] v : vert) {
				int vx = v[0];
				int vy = v[1];
				int vz = v[2];

				if (vx == lx && vy == ly && vz == lz) {
					farea.add(new Integer[] { vx, vy, vz });
					get = true;
					// break next;
				}
			}

			if (!get)
				next: for (Integer[] h : horiz) {
					int hx = h[0];
					int hy = h[1];
					int hz = h[2];

					if (lx == hx && ly == hy && lz == hz) {
						farea.add(new Integer[] { hx, hy, hz });
						get = true;
						// break next;
					}
				}
		}

		// farea = ruts;
		// cleanup because I'm not smart enough to do it right the first time
		List<Integer> remove = new ArrayList<Integer>();
		next:

		for (int i = 0; i < farea.size(); i++) {
			Integer[] ii = farea.get(i);
			int ix = ii[0];
			int iy = ii[1];
			int iz = ii[2];

			for (Integer r : remove) {
				if (r == i) {
					continue next;
				}
			}

			boolean rem = false;
			nextIn:

			for (int j = 0; j < farea.size(); j++) {
				if (i != j) {
					Integer[] ji = farea.get(j);
					int jx = ji[0];
					int jy = ji[1];
					int jz = ji[2];

					if (ix == jx && iy == jy && iz == jz) {
						// for(Integer r:remove){
						// if(r == j){
						// continue nextIn;
						// }
						// }
						remove.add(j);
						rem = true;
					}
				}
			}

			// if(!rem)
			remRut:

			for (Integer[] rut : ruts) {
				int rx = rut[0];
				int ry = rut[1];
				int rz = rut[2];

				if (rx == ix && ry == iy && rz == iz) {
					remove.add(i);
					break remRut;
				}
			}
		}

		List<Integer[]> temp = new ArrayList<Integer[]>();
		next:

		for (int i = 0; i < farea.size(); i++) {
			for (Integer r : remove) {
				if (r.equals(i)) {
					continue next;
				}
			}

			temp.add(farea.get(i));
		}

//		System.out.println("Area : " + farea.size() + " " + temp.size() + " "
//				+ remove.size());
		farea = temp;
		// if(farea.size() > 10000) return false;
		e.rutAreaPoints = farea;
		return true;
	}

	protected final boolean findRutArea(EntityDust e, int fluidID) {
		World w = e.worldObj;
		List<Integer[]> horiz = new ArrayList<Integer[]>();
		List<Integer[]> length = new ArrayList<Integer[]>();
		List<Integer[]> vert = new ArrayList<Integer[]>();
		List<Integer[]> farea = new ArrayList<Integer[]>();
		List<Integer[]> ruts = e.rutPoints;

		if (ruts == null) {
			findRuts(e, fluidID);
			ruts = e.rutPoints;

			if (ruts == null) {
				return false;
			}
		}

		int rutSize = ruts.size();

		for (int i = 0; i < rutSize; i++) {
			Integer[] p = ruts.get(i);
			int ix = p[0];
			int iy = p[1];
			int iz = p[2];
			next:

			for (int j = 0; j < rutSize; j++) {
				if (i != j) {
					Integer[] t = ruts.get(j);
					int jx = t[0];
					int jy = t[1];
					int jz = t[2];

					if (iy == jy && ix == jx && jz > iz) {
						int dist = jz - iz;

						for (int l = 1; l < dist; l++) {
							Integer[] a = new Integer[] { jx, jy, iz + l };
							length.add(a);
						}

						// break next;
					} else if (iy == jy && iz == jz && jx > ix) {
						int dist = jx - ix;

						for (int h = 1; h < dist; h++) {
							Integer[] a = new Integer[] { ix + h, jy, jz };
							horiz.add(a);
						}

						// break next;
					} else if (ix == jx && iz == jz && jy > iy) {
						int dist = jy - iy;

						for (int v = 1; v < dist; v++) {
							Integer[] a = new Integer[] { jx, iy + v, jz };
							vert.add(a);
						}

						// break next;
					}
				}
			}
		}

		// check for confimration
		for (Integer[] h : horiz) {
			int hx = h[0];
			int hy = h[1];
			int hz = h[2];
			boolean get = false;
			next:

			for (Integer[] v : vert) {
				int vx = v[0];
				int vy = v[1];
				int vz = v[2];

				if (hx == vx && hy == vy && hz == vz) {
					farea.add(v);
					get = true;
					// break next;
				}
			}

			if (!get)
				next: for (Integer[] l : length) {
					int lx = l[0];
					int ly = l[1];
					int lz = l[2];

					if (hx == lx && hy == ly && hz == lz) {
						farea.add(l);
						// farea.add(new Integer[]{lx,ly+3,lz});
						// farea.add(new Integer[]{hx,hy+1,hz});
						get = true;
						// break next;
					}
				}
		}

		for (Integer[] v : vert) {
			int vx = v[0];
			int vy = v[1];
			int vz = v[2];
			boolean get = false;
			next:

			for (Integer[] h : horiz) {
				int hx = h[0];
				int hy = h[1];
				int hz = h[2];

				if (hx == hx && hy == hy && hz == hz) {
					farea.add(h);
					get = true;
					// break next;
				}
			}

			if (!get)
				next: for (Integer[] l : length) {
					int lx = l[0];
					int ly = l[1];
					int lz = l[2];

					if (vx == lx && vy == ly && vz == lz) {
						farea.add(l);
						get = true;
						// break next;
					}
				}
		}

		for (Integer[] l : length) {
			int lx = l[0];
			int ly = l[1];
			int lz = l[2];
			boolean get = false;
			next:

			for (Integer[] v : vert) {
				int vx = v[0];
				int vy = v[1];
				int vz = v[2];

				if (vx == lx && vy == ly && vz == lz) {
					farea.add(v);
					get = true;
					// break next;
				}
			}

			if (!get)
				next: for (Integer[] h : horiz) {
					int hx = h[0];
					int hy = h[1];
					int hz = h[2];

					if (lx == hx && ly == hy && lz == hz) {
						farea.add(h);
						get = true;
						// break next;
					}
				}
		}

		// farea = ruts;
		// cleanup because I'm not smart enough to do it right the first time
		List<Integer> remove = new ArrayList<Integer>();
		next:

		for (int i = 0; i < farea.size(); i++) {
			Integer[] ii = farea.get(i);
			int ix = ii[0];
			int iy = ii[1];
			int iz = ii[2];

			for (Integer r : remove) {
				if (r == i) {
					continue next;
				}
			}

			boolean rem = false;
			nextIn:

			for (int j = 0; j < farea.size(); j++) {
				if (i != j) {
					Integer[] ji = farea.get(j);
					int jx = ji[0];
					int jy = ji[1];
					int jz = ji[2];

					if (ix == jx && iy == jy && iz == jz) {
						// for(Integer r:remove){
						// if(r == j){
						// continue nextIn;
						// }
						// }
						remove.add(j);
						rem = true;
					}
				}
			}

			// if(!rem)
			remRut:

			for (Integer[] rut : ruts) {
				int rx = rut[0];
				int ry = rut[1];
				int rz = rut[2];

				if (rx == ix && ry == iy && rz == iz) {
					remove.add(i);
					break remRut;
				}
			}
		}

		List<Integer[]> temp = new ArrayList<Integer[]>();
		next:

		for (int i = 0; i < farea.size(); i++) {
			for (Integer r : remove) {
				if (r.equals(i)) {
					continue next;
				}
			}

			temp.add(farea.get(i));
		}

//		System.out.println("Area : " + farea.size() + " " + temp.size() + " "
//				+ remove.size());
		farea = temp;
		// if(farea.size() > 10000) return false;
		e.rutAreaPoints = farea;
		return true;
	}

	protected final boolean findRutAreaFlat(EntityDust e, int fluidID) {
		World w = e.worldObj;
		List<Integer[]> horiz = new ArrayList<Integer[]>();
		List<Integer[]> length = new ArrayList<Integer[]>();
		List<Integer[]> vert = new ArrayList<Integer[]>();
		List<Integer[]> farea = new ArrayList<Integer[]>();
		List<Integer[]> ruts = e.rutPoints;

		if (ruts == null) {
			findRuts(e, fluidID);
			ruts = e.rutPoints;

			if (ruts == null) {
				return false;
			}
		}

		int rutSize = ruts.size();

		for (int i = 0; i < rutSize; i++) {
			Integer[] p = ruts.get(i);
			int ix = p[0];
			int iz = p[2];
			next:

			for (int j = 0; j < rutSize; j++) {
				if (i != j) {
					Integer[] t = ruts.get(j);
					int jx = t[0];
					int jz = t[2];

					if (ix == jx && jz > iz) {
						int dist = jz - iz;

						for (int l = 1; l < dist; l++) {
							Integer[] a = new Integer[] { jx, iz + l };
							length.add(a);
						}

						// break next;
					} else if (iz == jz && jx > ix) {
						int dist = jx - ix;

						for (int h = 1; h < dist; h++) {
							Integer[] a = new Integer[] { ix + h, jz };
							horiz.add(a);
						}

						// break next;
					}
				}
			}
		}

		// check for confimration
		for (Integer[] h : horiz) {
			int hx = h[0];
			int hz = h[1];
			boolean get = false;
			next:

			for (Integer[] l : length) {
				int lx = l[0];
				int lz = l[1];

				if (hx == lx && hz == lz) {
					farea.add(l);
					// farea.add(new Integer[]{lx,ly+3,lz});
					// farea.add(new Integer[]{hx,hy+1,hz});
					get = true;
					// break next;
				}
			}
		}

		// cleanup because I'm not smart enough to do it right the first time
		List<Integer> remove = new ArrayList<Integer>();
		next:

		for (int i = 0; i < farea.size(); i++) {
			Integer[] ii = farea.get(i);
			int ix = ii[0];
			int iz = ii[1];

			for (Integer r : remove) {
				if (r == i) {
					continue next;
				}
			}

			boolean rem = false;
			nextIn:

			for (int j = 0; j < farea.size(); j++) {
				if (i != j) {
					Integer[] ji = farea.get(j);
					int jx = ji[0];
					int jz = ji[1];

					if (ix == jx && iz == jz) {
						remove.add(j);
						rem = true;
					}
				}
			}

			// if(!rem)
			remRut:

			for (Integer[] rut : ruts) {
				int rx = rut[0];
				int rz = rut[2];

				if (rx == ix && rz == iz) {
					remove.add(i);
					break remRut;
				}
			}
		}

		List<Integer[]> temp = new ArrayList<Integer[]>();
		next:

		for (int i = 0; i < farea.size(); i++) {
			for (Integer r : remove) {
				if (r.equals(i)) {
					continue next;
				}
			}

			temp.add(farea.get(i));
		}

//		System.out.println("Area : " + farea.size() + " " + temp.size() + " "
//				+ remove.size());
		farea = temp;
		// if(farea.size() > 10000) return false;
		e.rutAreaPoints = farea;
		return true;
	}

	private final void checkNeighbors(World w, ArrayList<Integer[]> ruts,
			int x, int y, int z) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					if ((i == 0 || i == 2) && i == j && (k == 0 || k == 2)
							&& j != 0) {
						continue;
					}

					if ((i == 0 || i == 2) && (j == 0 || j == 2) && i != j
							&& (k == 0 || k == 2) && j != 0) {
						continue;
					}

					int bid = w.getBlockId(x + i, y + j, z + k);

					if (bid == DustMod.rutBlock.blockID) {
						TileEntityRut ter = (TileEntityRut) w
								.getBlockTileEntity(x + i, y + j, z + k);

						if (!ter.isBeingUsed) {
							ter.isBeingUsed = true;
							ruts.add(new Integer[] { x + i, y + j, z + k });
							checkNeighbors(w, ruts, x + i, y + j, z + k);
						}
					}
				}
			}
		}
	}

	private final void checkNeighborsWithDistance(World w,
			ArrayList<Integer[]> ruts, int x, int y, int z, int distance) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					if ((i == 0 || i == 2) && i == j && (k == 0 || k == 2)
							&& j != 0) {
						continue;
					}

					if ((i == 0 || i == 2) && (j == 0 || j == 2) && i != j
							&& (k == 0 || k == 2) && j != 0) {
						continue;
					}

					int bid = w.getBlockId(x + i, y + j, z + k);

					if (bid == DustMod.rutBlock.blockID) {
						TileEntityRut ter = (TileEntityRut) w
								.getBlockTileEntity(x + i, y + j, z + k);

						if (!ter.isBeingUsed && distance > 0) {
							ter.isBeingUsed = true;
							ruts.add(new Integer[] { x + i, y + j, z + k });
							checkNeighborsWithDistance(w, ruts, x + i, y + j, z
									+ k, distance - 1);
						}
					}
				}
			}
		}
	}

	private final void checkNeighbors(World w, ArrayList<Integer[]> ruts,
			int x, int y, int z, int fluidID) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					if ((i == 0 || i == 2) && i == j && (k == 0 || k == 2)) {
						continue;
					}

					if ((i == 0 || i == 2) && (j == 0 || j == 2) && i != j
							&& (k == 0 || k == 2)) {
						continue;
					}

					int bid = w.getBlockId(x + i, y + j, z + k);

					if (bid == DustMod.rutBlock.blockID) {
						TileEntityRut ter = (TileEntityRut) w
								.getBlockTileEntity(x + i, y + j, z + k);

						if (!ter.isBeingUsed && ter.fluid == fluidID) {
							ter.isBeingUsed = true;
							ruts.add(new Integer[] { x + i, y + j, z + k });
							checkNeighbors(w, ruts, x + i, y + j, z + k);
						}
					}
				}
			}
		}
	}

	public DustEvent setSecret(boolean secret) {
		this.secret = secret;
		return this;
	}

	public DustEvent setPermission(String allowed) {
		this.permission = allowed;
		return this;
	}

	public DustEvent setPermaAllowed(boolean allowed) {
		this.permaAllowed = allowed;
		return this;
	}

	@Override
	public String toString() {
		return "DustEvent:" + this.name;
	}
}
