/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.texturepacks.ITexturePack;
import net.minecraft.src.ModLoader;
import dustmod.DustItemManager;
import dustmod.DustMod;
import dustmod.DustShape;
import dustmod.InscriptionEvent;

/**
 *
 * @author billythegoat101
 */
public class PageHelper
{

	public static String folder = "\\dust_pages\\";
    public static String runeFolder = "\\dust_pages\\runes\\";
    public static String insFolder = "\\dust_pages\\inscriptions\\";
    public static BufferedImage background;
    public static BufferedImage backgroundIns;
    public static BufferedImage shade;
    public static BufferedImage colors;
    public static int bgw, bgh;
    public static PageHelper instance;
    private static BufferedImage missingExternalTextureImage;

    public PageHelper()
    {
    	String minecraftPath = DustMod.suggestedConfig.getParent();
    	File file = new File(minecraftPath);
    	minecraftPath = file.getParent();
    	
    	folder = minecraftPath + folder;
    	runeFolder = minecraftPath + runeFolder;
    	insFolder = minecraftPath + insFolder;
    	
        missingExternalTextureImage = new BufferedImage(64, 64, 2);
        Graphics var3 = missingExternalTextureImage.getGraphics();
        var3.setColor(Color.WHITE);
        var3.fillRect(0, 0, 64, 64);
        var3.setColor(Color.BLACK);
        var3.drawString("missingEXTtex", 1, 10);
        var3.dispose();
        images = new HashMap<String, BufferedImage>();
        try
        {
            background = getImage(DustMod.path + "/pages" + "/background.png");
            backgroundIns = getImage(DustMod.path + "/pages" + "/backgroundIns.png");
            shade = getImage(DustMod.path + "/pages" + "/shade.png");
            colors = getImage(DustMod.path + "/pages" + "/colors.png");

            bgw = background.getWidth();
            bgh = background.getHeight();

            boolean success = new File(folder).mkdir();
            if (success)
            {
            	DustMod.log(Level.INFO,"Lexicon Folder " + new File(folder).getAbsolutePath() + " created.");
//                System.out.println("[DustMod] Lexicon Folder " + new File(folder).getAbsolutePath() + " created.");
            }
            new File(runeFolder).mkdirs();
            new File(insFolder).mkdirs();

        } catch (IOException ex)
        {
            Logger.getLogger(PageHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void checkInscriptionImage(InscriptionEvent event){
    	BufferedImage dust = new BufferedImage(bgw, bgh, BufferedImage.TYPE_INT_ARGB);
        BufferedImage result = new BufferedImage(bgw, bgh, BufferedImage.TYPE_INT_ARGB);


        String name = "" + event.idName;
//        while (Character.isDigit(name.charAt(name.length() - 1)))
//        {
//            name = name.substring(0, name.length() - 1);
//        }
        
        File file = new File(insFolder + name + ".png");
        if(file.exists()) return;
        DustMod.log(Level.INFO, "Lexicon Inscription entry for " + name + " not found! Generating... [" + file.getAbsolutePath() + "]");
//        System.out.println("[DustMod] Lexicon Inscription entry for " + name + " not found! Generating...");
        
        int[][]values = event.referenceDesign;
        int width = values[0].length;
        int height = values.length;
//        System.out.println("Checking " + name + " " + width + " " + height);


        int pxwMax = bgw - 6;
        int pxhMax = bgh- 6;

        int pxWidth = 0;
        int pxHeight = 0;

        int dW = 1; //dotWidth
        int sW = 1; //spaceWidth

        pxWidth = 34;//width * dW + (width - 1) * sW;
        pxHeight = 34;//height * dW + (height - 1) * sW;

        //Dust
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                int value = values[y][x];
                if (value != 0)
                {
                    int[] loc = getPosition(x, y, dW, sW, bgw, bgh, width, height);
                    int colorCheck = value;
                    if (colorCheck == -1)
                    {
                        colorCheck = 0;
                    }
                    colorCheck *= 2;
                    colorCheck += 2;

                    if (x < width - 1 && values[y][x + 1] == value)
                    {
                        int[] nextLoc = getPosition(x + 1, y, dW, sW, bgw, bgh, width, height);
                        for (int i = Math.min(loc[0], nextLoc[0]); i < Math.max(loc[0], nextLoc[0]); i++)
                        {
                            for (int j = 0; j < dW; j++)
                            {
                                dust.setRGB(i, loc[1] + j,  getRandomDustColor(value, false));
                            }
                        }
                    }
                    if (y < height - 1 && values[y+1][x] == value)
                    {
                        int[] nextLoc = getPosition(x, y + 1, dW, sW, bgw, bgh, width, height);
                        for (int i = 0; i < dW; i++)
                        {
                            for (int j = Math.min(loc[1], nextLoc[1]); j < Math.max(loc[1], nextLoc[1]); j++)
                            {
                                dust.setRGB(loc[0] + i, j,  getRandomDustColor(value, false));
                            }
                        }
                    }
                    for (int i = 0; i < dW; i++)
                    {
                        for (int j = 0; j < dW; j++)
                        {
                            dust.setRGB(loc[0] + i, loc[1] + j,  getRandomDustColor(value, true));
                        }
                    }
                }
            }
        }


        //shade and finalization
        result.getGraphics().drawImage(backgroundIns, 0, 0, null);
        for (int x = 0; x < result.getWidth(); x++)
        {
            for (int y = 0; y < result.getHeight(); y++)
            {
                if (dust.getRGB(x, y) != 0)
                {
                    result.setRGB(x, y, dust.getRGB(x, y));
                }
                int color = result.getRGB(x, y);
                Color c = new Color(color);
                int r, g, b;
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();

                int shadeColor = shade.getRGB(x, y) & 0x0000FF;

                r = linearColorBurn(shadeColor, r);
                g = linearColorBurn(shadeColor, g);
                b = linearColorBurn(shadeColor, b);

                c = new Color(r, g, b);
                int resultColor = c.getRGB();
//                    if(resultColor < 0) resultColor = 0;
                result.setRGB(x, y, resultColor);
            }
        }
//            result.getGraphics().drawImage(shade, 0, 0, null);

        try
        {
            new File(insFolder).mkdirs();
            ImageIO.write(result, "PNG", new File(insFolder + name + ".png"));
        } catch (IOException ex)
        {
            Logger.getLogger(PageHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void checkRuneImage(DustShape shape)
    {

        BufferedImage dust = new BufferedImage(bgw, bgh, BufferedImage.TYPE_INT_ARGB);
        BufferedImage result = new BufferedImage(bgw, bgh, BufferedImage.TYPE_INT_ARGB);


        String name = "" + shape.name;
//        while (Character.isDigit(name.charAt(name.length() - 1)))
//        {
//            name = name.substring(0, name.length() - 1);
//        }
        
        File file = new File(runeFolder + name + ".png");
        if(file.exists()) return;
        DustMod.log(Level.FINEST, "Lexicon Rune entry for " + name + " not found! Generating...");
//        System.out.println("[DustMod] Lexicon Rune entry for " + name + " not found! Generating...");
        
        int[][][] values = shape.data;
        int width = shape.data[0][0].length;
        int height = shape.data[0].length;
//        System.out.println("Checking " + name + " " + width + " " + height);


        int pxwMax = bgw - 6;
        int pxhMax = bgh- 6;

        int pxWidth = 0;
        int pxHeight = 0;

        int dW = 3; //dotWidth
        int sW = 2; //spaceWidth

        pxWidth = width * dW + (width - 1) * sW + dW;
        pxHeight = height * dW + (height - 1) * sW + dW;
        if (pxWidth > pxwMax || pxHeight > pxhMax)
        {
            dW = 2;
            sW = 2;
            pxWidth = width * dW + (width - 1) * sW;
            pxHeight = height * dW + (height - 1) * sW;

            if (pxWidth > pxwMax || pxHeight > pxhMax)
            {
                dW = 2;
                sW = 1;
                pxWidth = width * dW + (width - 1) * sW;
                pxHeight = height * dW + (height - 1) * sW;

                if (pxWidth > pxwMax || pxHeight > pxhMax)
                {
                    dW = 1;
                    sW = 1;
                    pxWidth = width * dW + (width - 1) * sW;
                    pxHeight = height * dW + (height - 1) * sW;
                }
            }
        }

//        System.out.println("[" + pxWidth + "," + pxHeight + "] [" + bgw + "," + bgh + "]");

        //gold
        int tx = pxWidth / 2 + 2;
        int ty = pxHeight / 2 + 2;
        for (int x = -tx; x < tx; x++)
        {
            for (int y = -ty; y < ty; y++)
            {
                if (x == -tx || y == -ty || x == tx - 1 || y == ty - 1)
                {
//                        System.out.println("Go [" + (bgw/2 + x) + "," + (bgh/2 + y) + "]");
                    dust.setRGB(bgw / 2 + x, bgh / 2 + y, getColor(colors, 0));
                } else
                {
                    dust.setRGB(bgw / 2 + x, bgh / 2 + y, getColor(colors, 1));
                }
            }
        }
        //corners
        for (int x = 0; x <= 1; x++)
        {
            for (int y = 0; y <= 1; y++)
            {
                if (x != 0 || y != 0 || true)
                {
//                        System.out.println("corner [" + (bgw / 2 - tx) + "," + (bgw / 2 - ty) + "]");
                    dust.setRGB(bgw / 2 - tx + x - 1, bgh / 2 - ty + y - 1, getColor(colors, 0));
//                    System.out.println("corner [" + (bgw / 2 + tx + 1) + "," + (bgw / 2 - ty - 1) + "]");
                    dust.setRGB(bgw / 2 + tx + x - 1, bgh / 2 - ty + y - 1, getColor(colors, 0));
//                        System.out.println("corner [" + (bgw / 2 + tx) + "," + (bgw / 2 + ty) + "]");
                    dust.setRGB(bgw / 2 + tx + x - 1, bgh / 2 + ty + y - 1, getColor(colors, 0));
//                        System.out.println("corner [" + (bgw / 2 - tx) + "," + (bgw / 2 + ty) + "]");
                    dust.setRGB(bgw / 2 - tx + x - 1, bgh / 2 + ty + y - 1, getColor(colors, 0));
                }
            }
        }

        //Dust
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                int value = values[0][y][x];
                if (value != 0)
                {
                    int[] loc = getPosition(x, y, dW, sW, bgw, bgh, width, height);
                    int colorCheck = value;
                    if (colorCheck == -1)
                    {
                        colorCheck = 0;
                    }
                    colorCheck *= 2;
                    colorCheck += 2;

                    if (x < width - 1 && values[0][y][x + 1] == value)
                    {
                        int[] nextLoc = getPosition(x + 1, y, dW, sW, bgw, bgh, width, height);
                        for (int i = Math.min(loc[0], nextLoc[0]); i < Math.max(loc[0], nextLoc[0]); i++)
                        {
                            for (int j = 0; j < dW; j++)
                            {
                                dust.setRGB(i, loc[1] + j,  getRandomDustColor(value, false));
                            }
                        }
                    }
                    if (y < height - 1 && values[0][y + 1][x] == value)
                    {
                        int[] nextLoc = getPosition(x, y + 1, dW, sW, bgw, bgh, width, height);
                        for (int i = 0; i < dW; i++)
                        {
                            for (int j = Math.min(loc[1], nextLoc[1]); j < Math.max(loc[1], nextLoc[1]); j++)
                            {
                                dust.setRGB(loc[0] + i, j,  getRandomDustColor(value, false));
                            }
                        }
                    }
                    for (int i = 0; i < dW; i++)
                    {
                        for (int j = 0; j < dW; j++)
                        {
                            dust.setRGB(loc[0] + i, loc[1] + j,  getRandomDustColor(value, true));
                        }
                    }
                }
            }
        }


        //shade and finalization
        result.getGraphics().drawImage(background, 0, 0, null);
        for (int x = 0; x < result.getWidth(); x++)
        {
            for (int y = 0; y < result.getHeight(); y++)
            {
                if (dust.getRGB(x, y) != 0)
                {
                    result.setRGB(x, y, dust.getRGB(x, y));
                }
                int color = result.getRGB(x, y);
                Color c = new Color(color);
                int r, g, b;
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();

                int shadeColor = shade.getRGB(x, y) & 0x0000FF;

                r = linearColorBurn(shadeColor, r);
                g = linearColorBurn(shadeColor, g);
                b = linearColorBurn(shadeColor, b);

                c = new Color(r, g, b);
                int resultColor = c.getRGB();
//                    if(resultColor < 0) resultColor = 0;
                result.setRGB(x, y, resultColor);
            }
        }
//            result.getGraphics().drawImage(shade, 0, 0, null);

        try
        {
            new File(runeFolder).mkdirs();
            ImageIO.write(result, "PNG", new File(runeFolder + name + ".png"));
        } catch (IOException ex)
        {
            Logger.getLogger(PageHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static int linearColorBurn(int v1, int v2)
    {
        return ((v1 + v2) < 255) ? 0 : (v1 + v2 - 255);
    }

    public static int[] getPosition(int x, int y, int dW, int sW, int bgW, int bgH, int w, int h)
    {
        int[] rtn = new int[]
        {
            0, 0
        };

        x -= w / 2;
        y -= h / 2;

        if (x >= 0 && sW < 2)
        {
            rtn[0]++;
        }
        if (y >= 0 && sW < 2)
        {
            rtn[1]++;
        }
        if(dW == 2 && sW == 2) {
        	rtn[0] ++;
        	rtn[1] ++;
        }

        rtn[0] += x * dW;
        rtn[0] += (x - 1) * sW;

        rtn[1] += y * dW;
        rtn[1] += (y - 1) * sW;

        rtn[0] += bgW / 2;
        rtn[1] += bgH / 2;

        rtn[0] += dW;
        rtn[1] += dW;

        return rtn;
    }

    public static int getColor(BufferedImage colors, int color)
    {
        int rand = new Random().nextInt(16);
        return colors.getRGB(rand, color);
    }
    public static int getRandomDustColor(int dust, boolean primary)
    {
    	int color = DustItemManager.getPrimaryColor(dust);
    	if(!primary){
    		color = new Color(color).brighter().getRGB();
    	}
    	
    	int r = (color&0xFF0000) >> 16;
        int g = (color&0xFF00) >> 8;
        int b = (color&0xFF);
//        color = (color & 0xfefefe) >> 1;
        Color temp = new Color(DustItemManager.getFloorColorRGB(dust)[0],DustItemManager.getFloorColorRGB(dust)[0],DustItemManager.getFloorColorRGB(dust)[2]);
        Color c = primary? temp: temp;
        
        if(primary) {
//        	for(int i = 0; i < 1; i++) c = c.darker();
        	r+=10;
        	g+=10;
        	b+=10;
        }else{
//        	for(int i = 0; i < 1; i++) c = c.brighter();
        	r-=10;
        	g-=10;
        	b-=10;
        }
        
        int tol = 30;
        Random rand = new Random();
        int random = rand.nextInt(tol);
        
//        for(int i = 0; i < random; i++) c=c.darker(); 
        c = stain(c,(float)rand.nextGaussian()*0.05f + (primary ? 0.02F:0));
        if(primary) random *= -1;
        
        r = r + random;
        g = g + random;
        b = b + random;
//        r = linearColorBurn(0x010101,r);
//        g = linearColorBurn(0x010101,g);
//        b = linearColorBurn(0,b);
        
        if(r < 0) r = 0; 
        if(r > 255) r = 255;
        if(g < 0) g = 0; 
        if(g > 255) g = 255;
        if(b < 0) b = 0; 
        if(b > 255) b = 255;
        
        color = (r<<16) | (g<<8) | (b);
    	return color;
    }
    public static Color stain(Color color, float amount)
    {
      int r = (int) ((color.getRed() * (1 - amount) / 255) * 255);
      int g = (int) ((color.getGreen() * (1 - amount) / 255) * 255);
      int b = (int) ((color.getBlue() * (1 - amount) / 255) * 255);
      

      if(r < 0) r = 0; 
      if(r > 255) r = 255;
      if(g < 0) g = 0; 
      if(g > 255) g = 255;
      if(b < 0) b = 0; 
      if(b > 255) b = 255;
      
      return new Color(r, g, b);
    }
    
//    private static HashMap<String, Boolean> textures;
    private static HashMap<String, BufferedImage> images;

    public static BufferedImage getImage(String file) throws IOException
    {

        if (images.containsKey(file))
        {
            return images.get(file);
        }

        BufferedImage rtn = null;
        Minecraft mc = ModLoader.getMinecraftInstance();
        ITexturePack tp = mc.renderEngine.texturePack.getSelectedTexturePack();
        InputStream stream = tp.getResourceAsStream(file);
        if(stream == null){
        	throw new IllegalArgumentException("[DustMod] Image file not found! " + file + ". Perhaps you installed it wrong?");
        }
    	rtn = ImageIO.read(stream);
        images.put(file, rtn);
        return rtn;
    }
    
    public static IntBuffer singleIntBuffer;

    public static void bindExternalTexture(String file)
    {
    	if(singleIntBuffer == null){
    		 singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
    	}
    	file = file.replace('/', File.separatorChar);
        Minecraft mc = ModLoader.getMinecraftInstance();
        RenderEngine re = mc.renderEngine;

        singleIntBuffer.clear();
        GLAllocation.generateTextureNames(singleIntBuffer);
        int tex = singleIntBuffer.get(0);

        File f = new File(file);
        if (!f.exists())
        {
            re.setupTexture(missingExternalTextureImage, tex);
        } else
        {
            try
            {
                InputStream stream = new FileInputStream(f);
                BufferedImage image = ImageIO.read(stream);
                stream.close();
                re.setupTexture(image, tex);
            } catch (IOException ex)
            {
                Logger.getLogger(PageHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        re.bindTexture(tex);

    }
}
