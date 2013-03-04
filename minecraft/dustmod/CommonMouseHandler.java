package dustmod;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.Player;

public class CommonMouseHandler {

	public HashMap<String,boolean[]> buttonsPressed;
	
	private static int lastTick = -1;
	public CommonMouseHandler(){
		buttonsPressed = new HashMap<String, boolean[]>();
	}
	
	public void checkPlayer(Player p){
		buttonsPressed.put(DustMod.getUsername(p), new boolean[3]);
	}
	
	public void tick(){
		for(String username: buttonsPressed.keySet()){
			Player p = (Player)MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(username);
			if(p == null) continue;
			boolean[] buttons = buttonsPressed.get(username);
			DustMod.inscriptionManager.tick(p, buttons, ((EntityPlayer)p).getCurrentArmor(2));
		}
	}
	
	public boolean[] getButtons(String player){
		boolean[] rtn = buttonsPressed.get(player);
		if(rtn == null) rtn = new boolean[3];
		return rtn;
	}
	public void setKey(Player p, int key, boolean pressed){
		String un = DustMod.getUsername(p);
		if(!buttonsPressed.containsKey(un)) checkPlayer(p);
		buttonsPressed.get(un)[key] = pressed;
	}
}
