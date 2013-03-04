package dustmod;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler {

	private boolean mouseDown = false;

	private static long lastTick = -1;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		// if(MinecraftServer.getServer() != null &&
		// MinecraftServer.getServer().worldServers.length > 0){
		// World world = MinecraftServer.getServer().worldServers[0];
		// if(world != null) {
		// if(lastTick == world.getWorldTime()){
		// return;
		// }
		// lastTick = world.getWorldTime();
		// }
		// }
		DustMod.proxy.tickMouseManager();
		DustMod.keyHandler.tick();
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "DustMod";
	}

}
