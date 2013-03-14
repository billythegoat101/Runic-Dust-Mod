package dustmod.inscriptions;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;

public class ForesightInscription extends InscriptionEvent {

	public ForesightInscription(int[][] design, String idName,
			String properName, int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101");
		this.setDescription("Description:\n" +
				"Enables you to anticipate the coming of mobs in the dark. Ground on which they are able to spawn will shine.");
		this.setNotes("Sacrifice:\n" +
				"-1xLapisBlock + 20XP");
	}
	
	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		ItemStack[] req = new ItemStack[]{new ItemStack(Block.blockLapis,1)};
		req = rune.sacrifice(e, req);
		if(!rune.checkSacrifice(req)) return false;
		if(!rune.takeXP(e, 20));
		item.setItemDamage(0);
		return true;
	}
	
	@Override
	public void onUpdate(EntityLiving wearer, ItemStack item, boolean[] buttons) {
		super.onUpdate(wearer, item, buttons);
		
		EntityPlayer player = (EntityPlayer)wearer;
		World world = player.worldObj;
		if(world.getTotalWorldTime()%10 == 0){//!world.isDaytime()){
			int x = (int)player.posX;
			int y = (int)player.posY;
			int z = (int)player.posZ;
			int r = 5;
			EntityZombie dummy = new EntityZombie(world);
			for(int i = -r; i <= r; i++){
				for(int j = -1; j <= 3; j++){
					for(int k = -r; k <= r; k++){
						int bid = world.getBlockId(x+i,y+j,z+k);
						if(bid == 0){
							int bidUnder = world.getBlockId(x+i,y+j-1,z+k);
							Block b = Block.blocksList[bidUnder];
							if(b != null && b.isOpaqueCube() && Math.random() < 0.2){
								dummy.setPosition(x+i, y+j, z+k);
								if(dummy.getCanSpawnHere())
									DustMod.spawnParticles(world, "witchMagic", x+i+0.5, y+j, z+k+0.5, 0, -0.8, 0, 5, 0.5d);
							}
						}
					}
				}
			}
			
		} 
		
		if(world.getTotalWorldTime()%40 == 0){
			this.damage((EntityPlayer)wearer, item, 1);
		}
	}
}
