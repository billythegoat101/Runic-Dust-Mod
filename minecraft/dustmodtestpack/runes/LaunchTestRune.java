package dustmodtestpack.runes;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import dustmod.DustEvent;
import dustmod.EntityDust;

public class LaunchTestRune extends DustEvent {

	
	@Override
	protected void onInit(EntityDust e) {
		// TODO Auto-generated method stub
		super.onInit(e);
	}
	
	@Override
	protected void initGraphics(EntityDust e) {
		// TODO Auto-generated method stub
		super.initGraphics(e);
	}
	
	@Override
	protected void onTick(EntityDust e) {
		super.onTick(e);
		
		List<Entity> ents = this.getEntities(e);
		if(e.ticksExisted%3==0)
		for(Entity i:ents){
			e.fallDistance = 0;
//			System.out.println("GOT " + i.motionX + " " + i.motionZ);
			if(i.onGround){
				i.setPosition(e.posX, Math.floor(e.posY)+0.5, e.posZ);
				launchToward(i,e.posX + 16,i.posY,0);
				
			}
		}
	}
	
	//G = 1.56800003052
	
	public void launchToward(Entity e, double xf, double yf, double zf){
		double d; 
		double vi;
		double g;
		double theta;
		double h;
		
		double xi,yi,zi;
		xi = e.posX;
		yi = e.posY;
		zi = e.posZ;
		
		
		h = yi - yf;
		g = 0.03999999910593033D;
//		if(e instanceof EntityLiving || true){
//			g = 0.06;
//		}
		theta = 0.558505361;//3.14/3;
		d = Math.sqrt((xi-xf)*(xi-xf) + (yi-yf)*(yi-yf) + (zi-zf)*(zi-zf));
		
		double costheta = Math.cos(theta);
//		double num = d*d*g;
		double num = d*g;
//		double denum = 2 * costheta * costheta * (h + d * Math.tan(theta));
		double denum = Math.sin(2*theta);
		vi = Math.sqrt(num/denum);
		
		e.addVelocity(-e.motionX, -e.motionY, -e.motionZ);
		
		double horizVel = Math.cos(theta)*vi;
		double rotTheta = Math.atan(xf/zf);
		e.addVelocity(Math.sin(rotTheta)*horizVel, Math.sin(theta)*vi, Math.cos(rotTheta)*horizVel);

		if(e instanceof EntityPlayer){
			System.out.println("You wretched whore");
			EntityPlayer ent = (EntityPlayer)e;
			ent.addMovementStat(Math.sin(rotTheta)*horizVel, Math.sin(theta)*vi, Math.cos(rotTheta)*horizVel);;
		}
	}
}
