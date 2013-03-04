/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.client;

import net.minecraft.client.renderer.entity.RenderFallingSand;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import dustmod.EntityBlock;

/**
 *
 * @author billythegoat101
 */
public class RenderEntityBlock extends RenderFallingSand
{
	
	public RenderEntityBlock() {
    	this.setRenderManager(RenderManager.instance);
	}
    @Override
    public void doRender(Entity ent, double par2, double par4, double par6, float par8, float par9)
    {
        if (!((EntityBlock)ent).lingering) //dont want it to be visible if lingering
        {
            if (((EntityBlock)ent).blockID != 0)
            {
            	GL11.glPushMatrix();
            	GL11.glTranslated(0.5, -0.5, 0.5);
                super.doRender(ent, par2, par4, par6, par8, par9);
            	GL11.glPopMatrix();
            }
        }
    }
}
