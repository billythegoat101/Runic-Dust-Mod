package dustmod.client;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;
import dustmod.PacketHandler;

public class MouseManager {
	
	public boolean[] state;
	
	private MouseManager(){
		state = new boolean[3];
	}
	
	public static MouseManager instance = new MouseManager();
	
	public void onTick(){
		EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;
		GuiScreen screen = FMLClientHandler.instance().getClient().currentScreen;
		if(player == null || screen != null) return;
		for(int i = 0; i < state.length; i++){
			boolean s = Mouse.isButtonDown(i);
			
			if(state[i] != s){
				FMLClientHandler.instance().sendPacket(PacketHandler.getMousePacket(i, s));
			}
			
			state[i] = s;
		}
	}
	
}
