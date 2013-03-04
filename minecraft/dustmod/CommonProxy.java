package dustmod;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy implements IGuiHandler{

	public int getBlockModel(Block b){
		return -1;
	}
	
	public void registerTileEntityRenderers() {
	}

	public void registerRenderInformation() {
	}

	public void resetPlayerTomePage() {
	}

	public World getClientWorld() {
		return null;
	}


	/**
	 * Check local directory for the tome page for the given shape.
	 * @param shape	Shape to check.
	 */
	public void checkRunePage(DustShape shape) {}

	public void checkInscriptionPage(InscriptionEvent shape) {}

	public boolean placeDustWithTome(ItemStack itemstack, EntityPlayer p,
			World world, int i, int j, int k, int l) {
		return false;
	}

	public void openTomeGUI(ItemStack itemstack, EntityPlayer p) {
	}

	public void registerEventHandlers() {
//		System.out.println("Register");
		TickHandler tick = new TickHandler();
		TickRegistry.registerTickHandler(tick, Side.CLIENT);
		TickRegistry.registerTickHandler(tick, Side.SERVER);
		GenericHandler handler = new GenericHandler();
		
		GameRegistry.registerCraftingHandler(handler);
		MinecraftForge.EVENT_BUS.register(handler);
	}


	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
//		Object rtn = new GuiTome(player.getCurrentEquippedItem());
		return  null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
//		Object rtn = new GuiTome(player.getCurrentEquippedItem());
		return  null;
	}

	public boolean isClient() {
		return false;
	}
	
	public void tickMouseManager(){
	}
}
