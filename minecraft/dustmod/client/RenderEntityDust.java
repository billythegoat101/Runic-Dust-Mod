/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.client;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import dustmod.DustMod;
import dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class RenderEntityDust extends Render implements IRenderLast
{
    public RenderEntityDust()
    {
    	this.setRenderManager(RenderManager.instance);
    }

    public void renderStar(EntityDust g, float f, float ri, float gi, float bi, float ro, float go, float bo, float scale)
    {
        Tessellator tessellator = Tessellator.instance;
        RenderHelper.disableStandardItemLighting();
        int ticks = g.ticksExisted % 200;

        if (ticks >= 100)
        {
            ticks = 200 - ticks - 1;
        }

        float f1 = ((float)ticks) / 200F;
        float f2 = 0.0F;

        if (f1 > 0.7F)
        {
            f2 = (f1 - 0.7F) / 0.2F;
        }
        
        float yOffset = 0;
        
        if(g.justBorn && ticks < EntityDust.birthLength){
        	double offset = g.posY - Math.floor(g.posY); 
        	double offsetPerc = offset/(1-0.1875);
        	double perc = ((double)ticks / (double) EntityDust.birthLength);
        	scale *= Math.min(perc+0.2,1);
//        	System.out.println(offset + " " + perc);
        	yOffset = (float)perc*(float)offsetPerc-(float)offset;
        }        

        Random random = new Random(432L);
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT | GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -1F + yOffset, -2F);
        GL11.glScalef(0.02F, 0.02F, 0.02F);
        GL11.glScalef(scale,scale,scale);
        GL11.glScalef(1F, g.starScaleY, 1F);
        for (int i = 0; (float)i < ((f1 + f1 * f1) / 2F) * 90F + 30F; i++)
        {
            GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F + f1 * 90F, 0.0F, 0.0F, 1.0F);
            tessellator.startDrawing(6);
            float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
            //tessellator.setColorRGBA_I(0xffffFF, (int)(255F * (1.0F - f2)));
            tessellator.setColorRGBA_F(ri, gi, bi, (int)(255F * (1.0F - f2)));
            tessellator.addVertex(0.0D, 0.0D, 0.0D);
//            tessellator.setColorRGBA_I(0x0000FF, 0);
            tessellator.setColorRGBA_F(ro, go, bo, 0);
            tessellator.addVertex(-0.86599999999999999D * (double)f4, f3, -0.5F * f4);
            tessellator.addVertex(0.86599999999999999D * (double)f4, f3, -0.5F * f4);
            tessellator.addVertex(0.0D, f3, 1.0F * f4);
            tessellator.addVertex(-0.86599999999999999D * (double)f4, f3, -0.5F * f4);
            tessellator.draw();
        }

        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopAttrib();
        RenderHelper.enableStandardItemLighting();
    }

    public void renderBeam1(EntityDust e, double x, double y, double z, double i, double j, double k, float f)
    {
//        float f2 = (float)entitydragon.field_41013_bH.field_41032_a + f1;
//        float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
//        f3 = (f3 * f3 + f3) * 0.2F;
//            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapEnabled);
//            GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT | GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        int ticks = e.ticksExisted;
        float f4 = (float)(i - x);
        float f5 = (float)((/*(double)f3 + */j) - 1.0D - y);
        float f6 = (float)(k - z);
        float f7 = MathHelper.sqrt_float(f4 * f4 + f6 * f6);
        float f8 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6);
        GL11.glPushMatrix();

        if(e.justBorn && ticks < EntityDust.birthLength){
        	double perc = ((double)ticks / (double) EntityDust.birthLength);
        	y += 64 - perc*64D;
        }  
        
        GL11.glTranslatef((float)x, (float)y + 2.0F, (float)z);
        GL11.glRotatef(((float)(-Math.atan2(f6, f4)) * 180F) / (float)Math.PI - 90F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(((float)(-Math.atan2(f7, f5)) * 180F) / (float)Math.PI - 90F, 1.0F, 0.0F, 0.0F);
        Tessellator tessellator = Tessellator.instance;
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
        loadTexture(DustMod.path + "/beam.png");
        GL11.glShadeModel(GL11.GL_SMOOTH);
        float f9 = 0.0F - ((float)(ticks)) * 0.01F;
        float f10 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6) / 32F - ((float)(ticks)) * 0.01F;
        tessellator.startDrawing(5);
        //tessellator.setBrightness(15);
        int it = 8;
        GL11.glColor4f(e.rb, e.gb, e.bb, 1F);

        for (int jt = 0; jt <= it; jt++)
        {
            float f11 = MathHelper.sin(((float)(jt % it) * (float)Math.PI * 2.0F) / (float)it) * 0.75F;
            float f12 = MathHelper.cos(((float)(jt % it) * (float)Math.PI * 2.0F) / (float)it) * 0.75F;
            float f13 = ((float)(jt % it) * 1.0F) / (float)it;
            //tessellator.setColorOpaque_I(0xFFFFFF);
            tessellator.setColorRGBA(e.rb, e.gb, e.bb, 128);
            tessellator.addVertexWithUV(f11 * 0.2F, f12 * 0.2F, 0.0D, f13, f10);
            //tessellator.setColorOpaque_I(0x000000);
            tessellator.addVertexWithUV(f11, f12, f8, f13, f9);
        }

        tessellator.draw();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_LIGHTING);
        RenderHelper.enableStandardItemLighting();
//            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapDisabled);
//                GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public void renderBeam2(EntityDust e, double x, double y, double z, double i, double j, double k, float f)
    {
        float var9 = 1f;

        x-=0.5;
        y+=2;
        z-=0.5;
        if (var9 > 0.0F)
        {
        	GL11.glPushMatrix();
//            GL11.glTranslatef(0, -1.0F, 0);
            Tessellator var10 = Tessellator.instance;
            RenderHelper.disableStandardItemLighting();
            var10.setBrightness(Integer.MAX_VALUE);
            RenderHelper.disableStandardItemLighting();
            loadTexture("/misc/beam.png");
            RenderHelper.disableStandardItemLighting();
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            RenderHelper.disableStandardItemLighting();
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_CULL_FACE);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_BLEND);
            RenderHelper.disableStandardItemLighting();
            GL11.glDepthMask(true);
            RenderHelper.disableStandardItemLighting();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            RenderHelper.disableStandardItemLighting();
            float var11 = (float)e.worldObj.getTotalWorldTime() + f;
            float var12 = -var11 * 0.2F - (float)MathHelper.floor_float(-var11 * 0.1F);
            byte var13 = 1;
            double var14 = (double)var11 * 0.025D * (1.0D - (double)(var13 & 1) * 2.5D);
            var10.startDrawingQuads();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(false);
            RenderHelper.disableStandardItemLighting();
            var10.setColorRGBA(191 + e.rb/4, 191 + e.gb/4, 191 + e.bb/4, 128);
//          var10.setColorRGBA(255, 255, 255, 32);
            double var16 = (double)var13 * 0.2D;
            double var18 = 0.5D + Math.cos(var14 + 2.356194490192345D) * var16;
            double var20 = 0.5D + Math.sin(var14 + 2.356194490192345D) * var16;
            double var22 = 0.5D + Math.cos(var14 + (Math.PI / 4D)) * var16;
            double var24 = 0.5D + Math.sin(var14 + (Math.PI / 4D)) * var16;
            double var26 = 0.5D + Math.cos(var14 + 3.9269908169872414D) * var16;
            double var28 = 0.5D + Math.sin(var14 + 3.9269908169872414D) * var16;
            double var30 = 0.5D + Math.cos(var14 + 5.497787143782138D) * var16;
            double var32 = 0.5D + Math.sin(var14 + 5.497787143782138D) * var16;
            double var34 = (double)(256.0F * var9);
            double var36 = 0.0D;
            double var38 = 1.0D;
            double var40 = (double)(-1.0F + var12);
            double var42 = (double)(256.0F * var9) * (0.5D / var16) + var40;
            var10.addVertexWithUV(x + var18, y + var34, z + var20, var38, var42);
            var10.addVertexWithUV(x + var18, y, z + var20, var38, var40);
            var10.addVertexWithUV(x + var22, y, z + var24, var36, var40);
            var10.addVertexWithUV(x + var22, y + var34, z + var24, var36, var42);
            var10.addVertexWithUV(x + var30, y + var34, z + var32, var38, var42);
            var10.addVertexWithUV(x + var30, y, z + var32, var38, var40);
            var10.addVertexWithUV(x + var26, y, z + var28, var36, var40);
            var10.addVertexWithUV(x + var26, y + var34, z + var28, var36, var42);
            var10.addVertexWithUV(x + var22, y + var34, z + var24, var38, var42);
            var10.addVertexWithUV(x + var22, y, z + var24, var38, var40);
            var10.addVertexWithUV(x + var30, y, z + var32, var36, var40);
            var10.addVertexWithUV(x + var30, y + var34, z + var32, var36, var42);
            var10.addVertexWithUV(x + var26, y + var34, z + var28, var38, var42);
            var10.addVertexWithUV(x + var26, y, z + var28, var38, var40);
            var10.addVertexWithUV(x + var18, y, z + var20, var36, var40);
            var10.addVertexWithUV(x + var18, y + var34, z + var20, var36, var42);
            var10.draw();
            var10.startDrawingQuads();
            var10.setColorRGBA(e.rb, e.gb, e.bb, 48);
//            var10.setColorRGBA(255, 255, 255, 32);
            double var44 = 0.2D;
            double var15 = 0.2D;
            double var17 = 0.8D;
            double var19 = 0.2D;
            double var21 = 0.2D;
            double var23 = 0.8D;
            double var25 = 0.8D;
            double var27 = 0.8D;
            double var29 = (double)(256.0F * var9);
            double var31 = 0.0D;
            double var33 = 1.0D;
            double var35 = (double)(-1.0F + var12);
            double var37 = (double)(256.0F * var9) + var35;
            var10.addVertexWithUV(x + var44, y + var29, z + var15, var33, var37);
            var10.addVertexWithUV(x + var44, y, z + var15, var33, var35);
            var10.addVertexWithUV(x + var17, y, z + var19, var31, var35);
            var10.addVertexWithUV(x + var17, y + var29, z + var19, var31, var37);
            var10.addVertexWithUV(x + var25, y + var29, z + var27, var33, var37);
            var10.addVertexWithUV(x + var25, y, z + var27, var33, var35);
            var10.addVertexWithUV(x + var21, y, z + var23, var31, var35);
            var10.addVertexWithUV(x + var21, y + var29, z + var23, var31, var37);
            var10.addVertexWithUV(x + var17, y + var29, z + var19, var33, var37);
            var10.addVertexWithUV(x + var17, y, z + var19, var33, var35);
            var10.addVertexWithUV(x + var25, y, z + var27, var31, var35);
            var10.addVertexWithUV(x + var25, y + var29, z + var27, var31, var37);
            var10.addVertexWithUV(x + var21, y + var29, z + var23, var33, var37);
            var10.addVertexWithUV(x + var21, y, z + var23, var33, var35);
            var10.addVertexWithUV(x + var44, y, z + var15, var31, var35);
            var10.addVertexWithUV(x + var44, y + var29, z + var15, var31, var37);
            var10.draw();
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(true);
            
            GL11.glPopMatrix();
        }
    }
    
    public void renderFlames(EntityDust e, float par2)
    {
    	System.out.println("BASIFM wat" + DustMod.Enable_Render_Flames_On_Dust + " " + e.renderFlamesDust + " " + (e.dustPoints == null));
    	
//        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT | GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        boolean renderFlamesDust = (DustMod.Enable_Render_Flames_On_Dust && e.renderFlamesDust && e.dustPoints != null);
        boolean renderFlamesRut  = (DustMod.Enable_Render_Flames_On_Ruts && e.renderFlamesRut && e.rutPoints != null);

        if (renderFlamesDust && e.flameRenderHelperDust == null)
        {
            e.flameRenderHelperDust = new HashMap<Integer, Integer[]>();
            int size = e.dustPoints.size();

            for (int i = 0; i < size; i++)
            {
                Integer[] p = e.dustPoints.get(i);
                Integer[] sides = new Integer[] {1, 1, 1, 1};
                int x = p[0];
                int z = p[2];

                for (int j = 0; j < size; j++)
                {
                    if (j != i)
                    {
                        Integer[] c = e.dustPoints.get(j);
                        int cx = c[0];
                        int cz = c[2];

                        if (cx == x && cz == z - 1)
                        {
                            sides[0] = 0;
                        }

                        if (cx == x && cz == z + 1)
                        {
                            sides[1] = 0;
                        }

                        if (cx == x - 1 && cz == z)
                        {
                            sides[2] = 0;
                        }

                        if (cx == x + 1 && cz == z)
                        {
                            sides[3] = 0;
                        }
                    }
                }

                e.flameRenderHelperDust.put(i, sides);
            }
        }

        if (renderFlamesRut && e.flameRenderHelperRut == null && e.rutPoints != null)
        {
            e.flameRenderHelperRut = new HashMap<Integer, Integer[]>();
            int size = e.rutPoints.size();

            for (int i = 0; i < size; i++)
            {
                Integer[] p = e.rutPoints.get(i);
                Integer[] sides = new Integer[] {1, 1, 1, 1};
                int x = p[0];
                int y = p[1];
                int z = p[2];

                if (e.worldObj.getBlockId(x, y + 1, z) != 0)
                {
                    sides = new Integer[] {0, 0, 0, 0};
                    e.flameRenderHelperRut.put(i, sides);
                    continue;
                }

                if (e.worldObj.getBlockId(x, y + 1, z - 1) != 0)
                {
                    sides[0] = 0;
                }

                if (e.worldObj.getBlockId(x, y + 1, z + 1) != 0)
                {
                    sides[1] = 0;
                }

                if (e.worldObj.getBlockId(x - 1, y + 1, z) != 0)
                {
                    sides[2] = 0;
                }

                if (e.worldObj.getBlockId(x + 1, y + 1, z) != 0)
                {
                    sides[3] = 0;
                }

                for (int j = 0; j < size; j++)
                {
                    if (j != i)
                    {
                        Integer[] c = e.rutPoints.get(j);
                        int cx = c[0];
                        int cy = c[1];
                        int cz = c[2];

                        if (cx == x && cz == z - 1 && cy == y)
                        {
                            sides[0] = 0;
                        }

                        if (cx == x && cz == z + 1 && cy == y)
                        {
                            sides[1] = 0;
                        }

                        if (cx == x - 1 && cz == z && cy == y)
                        {
                            sides[2] = 0;
                        }

                        if (cx == x + 1 && cz == z && cy == y)
                        {
                            sides[3] = 0;
                        }
                    }
                }

                e.flameRenderHelperRut.put(i, sides);
            }
        }

        renderBlocks.blockAccess = e.worldObj;
        GL11.glPushMatrix();
        float scaley = 0.5F;
//        GL11.glScalef(1F, scaley, 1F);
//        GL11.glTranslatef(0, -scaley, 0);
        loadTexture("/terrain.png");
        int i = MathHelper.floor_double(e.posX);
        int j = MathHelper.floor_double(e.posY);
        int k = MathHelper.floor_double(e.posZ);

        if (renderFlamesDust)
            for (int iter = 0; iter < e.dustPoints.size(); iter++)
            {
                Integer[] p = e.dustPoints.get(iter);
                Integer[] sides = e.flameRenderHelperDust.get(iter);
                GL11.glPushMatrix();
                GL11.glTranslatef((float)p[0] - i, (float)p[1] - j, (float)p[2] - k);
                renderBlockFire(e.worldObj, i, j, k, e.rf, e.gf, e.bf, sides);
                GL11.glPopMatrix();
            }

        if (renderFlamesRut)
            for (int iter = 0; iter < e.rutPoints.size(); iter++)
            {
                Integer[] p = e.rutPoints.get(iter);
                Integer[] sides = e.flameRenderHelperRut.get(iter);
                GL11.glPushMatrix();
                GL11.glTranslatef((float)p[0] - i, (float)p[1] - j + 1, (float)p[2] - k);
                renderBlockFire(e.worldObj, i, j + 1, k, e.rf, e.gf, e.bf, sides);
                GL11.glPopMatrix();
            }

        GL11.glPopMatrix();
//        GL11.glPopAttrib();
    }

    public void renderBlockFire(World world, int i, int j, int k, int r, int g, int b, Integer[] sides)
    {
    	System.out.println("BLOCK FIRE");
    	GL11.glPushMatrix();
//      GL11.glTranslatef((float)d, (float)d1+1, (float)d2);
      loadTexture("/terrain.png");
      Block block = Block.fire;//Block.blocksList[Block.fire.blockID];
      GL11.glDisable(GL11.GL_LIGHTING);
      float f = 0.5F;
      float f1 = 1.0F;
      float f2 = 0.8F;
      float f3 = 0.6F;
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.setBrightness(block.getMixedBrightnessForBlock(world, i, j, k));
      float f4 = 1.0F;
      float f5 = 1.0F;

      if (f5 < f4)
      {
          f5 = f4;
      }

      tessellator.setColorOpaque_F((float)(r / 255), (float)(g / 255), (float)(b / 255));

      if (sides[0] == 1)
      {
          renderBlocks.renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(2));
          renderBlocks.renderWestFace(block, -0.5D, -0.5D, -1.5D, block.getBlockTextureFromSide(3));
      }

      if (sides[1] == 1)
      {
          renderBlocks.renderWestFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(3));
          renderBlocks.renderEastFace(block, -0.5D, -0.5D, 0.5D, block.getBlockTextureFromSide(2));
      }

      if (sides[2] == 1)
      {
          renderBlocks.renderNorthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(4));
          renderBlocks.renderSouthFace(block, -1.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(5));
      }

      if (sides[3] == 1)
      {
          renderBlocks.renderSouthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(5));
          renderBlocks.renderNorthFace(block, 0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(4));
      }

      tessellator.draw();
      GL11.glEnable(GL11.GL_LIGHTING);
      GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity e, double d, double d1, double d2, float f, float f1)
    {
//    	System.out.println("Render entity dust");
        EntityDust dust = (EntityDust)e;
        float ri = (float)dust.ri / 255F;
        float gi = (float)dust.gi / 255F;
        float bi = (float)dust.bi / 255F;
        float ro = (float)dust.ro / 255F;
        float go = (float)dust.go / 255F;
        float bo = (float)dust.bo / 255F;

//        if (dust.renderFlamesDust || dust.renderFlamesRut)
//        {
//            GL11.glPushMatrix();
//            GL11.glTranslatef((float)d, (float)d1 + 0.3F, (float)d2);
//            this.renderFlames(dust, f);
//            GL11.glPopMatrix();
//        }

        if (dust.renderBeam)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)0F, (float) - 2.167F, (float)0F);
            switch(dust.beamType){
            case 0:
            	renderBeam1(dust, d, d1/*+EntityDust.yOffset*/, d2, d, 256, d2, f);
            	break;
            case 1:
            	renderBeam2(dust, d, d1/*+EntityDust.yOffset*/, d2, d, 256, d2, f);
            	break;
        	default:
            	renderBeam1(dust, d, d1/*+EntityDust.yOffset*/, d2, d, 256, d2, f);
            }
            GL11.glPopMatrix();
        }

        if (dust.renderStar)
        {
            RenderLastHandler.registerLastRender(this, new Object[]{dust,d,d1,d2,f,f1}); //forge
//            GL11.glPushMatrix();
//            GL11.glTranslatef((float)d, (float)d1 + 1.0F, (float)d2 + 2F); //noforge
//            renderStar(dust, f, (float)ri / 255F, (float)gi / 255F, (float)bi / 255F,
//                    (float)ro / 255F, (float)go / 255F, (float)bo / 255F, dust.starScale);
//            dust.ticksExisted += 20;
//            renderStar(dust, f, (float)ri / 255F, (float)gi / 255F, (float)bi / 255F,
//                    (float)ro / 255F, (float)go / 255F, (float)bo / 255F, dust.starScale);
//            dust.ticksExisted -= 20;
//            GL11.glPopMatrix();
        }
    }

    public void renderLast(Object[] params, float frame)
    {
        EntityDust dust = (EntityDust)params[0];
        double d = (Double)params[1];
        double d1 = (Double)params[2];
        double d2 = (Double)params[3];
        float f = (Float)params[4];
        float f1 = (Float)params[5];
        float ri = (float)dust.ri / 255F;
        float gi = (float)dust.gi / 255F;
        float bi = (float)dust.bi / 255F;
        float ro = (float)dust.ro / 255F;
        float go = (float)dust.go / 255F;
        float bo = (float)dust.bo / 255F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1 + 1.0F, (float)d2 + 2F);
        renderStar(dust, f, ri, gi, bi, ro, go, bo, dust.starScale);
//        dust.ticksExisted += 20;
//        renderStar(dust, f, ri, gi, bi, ro, go, bo, dust.starScale);
//        dust.ticksExisted -= 20;
        GL11.glPopMatrix();
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
//        GL11.glPopAttrib();
    }
}
