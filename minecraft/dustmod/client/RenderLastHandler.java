/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.client;

import java.util.HashMap;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
//forge

/**
 *
 * @author billythegoat101
 */
public class RenderLastHandler  /*implements IRenderWorldLastHandler */{ //[forge

    public static HashMap<Object[], IRenderLast> map = new HashMap<Object[], IRenderLast>();

    @ForgeSubscribe
    public void onRenderWorldLast(RenderWorldLastEvent evt)
    {
    	RenderGlobal renderer;
    	float partialTicks;
    	renderer = evt.context;
    	partialTicks = evt.partialTicks;
        for (Object[] o: map.keySet())
        {
//            System.out.println("Render");
            map.get(o).renderLast(o, partialTicks);
        }

//        System.out.println("Reset");
        map = new HashMap<Object[], IRenderLast>();
    }
    public static void registerLastRender(IRenderLast rend, Object[] o)
    {
//        System.out.println("Regist");
        map.put(o, rend);
    }
}
