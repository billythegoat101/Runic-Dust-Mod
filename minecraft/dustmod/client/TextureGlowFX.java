package dustmod.client;

import net.minecraft.client.renderer.RenderEngine;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLTextureFX;
import dustmod.DustMod;

public class TextureGlowFX extends FMLTextureFX {

	private byte[] swap = new byte[1024];
	private static int ticks = 0;
	private static int frame = 0;
	public TextureGlowFX(int icon) {
		super(icon);
	}

	
	@Override
	public void onTick() {
		super.onTick();
		ticks++;
		
		if(ticks%2 == 0)
			frame++;
		
		for(int y = 0; y < 16; y++){
			int pix = 0;
			
			byte c,a;
			double perc = (double)y/16D;
			a = (byte)(perc*221);
			
			c = (byte)255;
			if(((y+frame) %4==0 || (y+frame) %4==1) && y <= 11) a = 0;//(byte)Math.max(0, a-30);
			
			for(int x = 0; x < 16; x++){
//                swap[((x+(y*16))*4)+0] = (byte)((pix>>16)&0xFF); //Red
//                swap[((x+(y*16))*4)+1] = (byte)((pix>>8)&0xFF); //Green
//                swap[((x+(y*16))*4)+2] = (byte)((pix)&0xFF); //Blue
//                swap[((x+(y*16))*4)+3] = (byte)((pix>>24)&0xFF); //Alpha
                swap[((x+(y*16))*4)+0] = c; //Red
                swap[((x+(y*16))*4)+1] = c; //Green
                swap[((x+(y*16))*4)+2] = c; //Blue
                swap[((x+(y*16))*4)+3] = a; //Alpha
			}
		}
		
		this.imageData = swap.clone();
		
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
