package dustmod.client;

import java.util.Random;

import net.minecraft.client.renderer.RenderEngine;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLTextureFX;
import dustmod.DustMod;

public class TextureDustFX extends FMLTextureFX {
	private int ticks = 0;
	
	public TextureDustFX(int icon) {
		super(icon);
	}

	@Override
	public void onTick() {
		super.onTick();
		ticks++;
		if(ticks%2 == 0){
			byte[] swap = new byte[1024];
			Random rand = new Random();
			for(int y = 0; y < 16; y++){
				for(int x = 0; x < 16; x++){
					int c = 90 + rand.nextInt(40);
					int a = 255;
	                swap[((x+(y*16))*4)+0] = (byte)c; //Red
	                swap[((x+(y*16))*4)+1] = (byte)c; //Green
	                swap[((x+(y*16))*4)+2] = (byte)c; //Blue
	                swap[((x+(y*16))*4)+3] = (byte)a; //Alpha
				}
			}
			imageData = swap.clone();
		}
	}

	@Override
    public void bindImage(RenderEngine par1RenderEngine)
    {
        if (this.tileImage == 0)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture(DustMod.path + "/dustBlocks.png"));
        }
    }

}
