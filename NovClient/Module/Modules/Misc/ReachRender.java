package NovClient.Module.Modules.Misc;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Combat.KillAura;
import NovClient.Module.Modules.Legit.Reach;
import NovClient.Module.Modules.Render.ESP;
import NovClient.Module.Modules.Render.HUD;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ReachRender extends Module {


	public ReachRender() {
		super("ReachRender", new String[] { "ReachRender" }, ModuleType.Legit);
	}

	@EventHandler
	public void onUpdate(EventPreUpdate e) {
//        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && this.mc.objectMouseOver.entityHit.getEntityId() == e.getEntity().getEntityId()) {
//            Vec3 vec3 = this.mc.getRenderViewEntity().getPositionEyes(1.0f);
//            double range = this.mc.objectMouseOver.hitVec.distanceTo(vec3)*(3.0/ (Client.instance.getModuleManager().getModuleByClass(Reach.class).isEnabled() ? Reach.CombatReach.getValue() : 3.0));
//            if(range>3.0)range = 3.0;
//            this.rangeText = new DecimalFormat(".##").format(range)  + (HUD.ÖÐÎÄ.getValue() ? "¸ñ" : " blocks");
//            
//            
//        } else {
//            this.rangeText = "Not on target?";
//        }
//        this.lastAttack = System.currentTimeMillis();
		//mc.thePlayer.attack
	    for (Entity e2 : mc.theWorld.getLoadedEntityList()) {
	    	if (!(e2 instanceof EntityPlayer) || ((EntityPlayer)e2) == mc.thePlayer) continue;
	    	//System.out.println(((EntityLivingBase)e2).attackedAtYaw);
        }

//		if(mc.thePlayer.getAITarget() != null)
//			System.out.println(mc.thePlayer.getAITarget().getName());
	}
	@EventHandler
	public void onRender(EventRender3D render) {
		    for (Entity e2 : mc.theWorld.getLoadedEntityList()) {
		    	if (!(e2 instanceof EntityPlayer) || ((EntityPlayer)e2) == mc.thePlayer) continue;
	        	drawESP(render,e2);
	        }
			
	}

	private void drawESP(EventRender3D render,Entity e) {

		double x = e.lastTickPosX
				+ (e.posX - e.lastTickPosX) * render.getPartialTicks()
				- mc.getRenderManager().viewerPosX;
		double y = e.lastTickPosY
				+ (e.posY - e.lastTickPosY) * render.getPartialTicks()
				- mc.getRenderManager().viewerPosY;
		double z = e.lastTickPosZ
				+ (e.posZ - e.lastTickPosZ) * render.getPartialTicks()
				- mc.getRenderManager().viewerPosZ;
		esp(e, x, y, z);
	}

	public void esp(final Entity player, final double x, final double y, final double z) {
		GL11.glPushMatrix();
		GL11.glDisable(2896);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(2929);
		GL11.glEnable(2848);
		GL11.glDepthMask(true);
		GlStateManager.translate(x, y, z);
//		if (!(player.hurtTime > 0)) {
//			GlStateManager.color(0.25f, 2.0f, 0.0f, 1.0f);
//		} else {
//			GlStateManager.color(1.35f, 0.0f, 0.0f, 1.0f);
//		}
		
		
		
		if(mc.thePlayer.getDistanceToEntity(player) <= 3.0+0.5f)
			GlStateManager.color(0.25f, 2.0f, 0.0f, 1.0f);
		else
		GlStateManager.color(1.35f, 0.0f, 0.0f, 1.0f);
		// RenderUtil.color(Colors.WHITE.c);
		GlStateManager.rotate(180, 90.0f, 0, 2.0f);
		GlStateManager.rotate(180, 0.0f, 90, 90.0f);
		Cylinder c = new Cylinder();
		// if (TargetStrafe.espmode.isCurrentMode("Point")) {
		// c.setDrawStyle(100010);
		/*
		 * } else { c.setDrawStyle(100011); }
		 */
		c.setDrawStyle(100011);
		c.draw(Client.instance.getModuleManager().getModuleByClass(Reach.class).isEnabled() ? Reach.CombatReach.getValue().floatValue()+0.5f : 3.0f+0.5f, Client.instance.getModuleManager().getModuleByClass(Reach.class).isEnabled() ? Reach.CombatReach.getValue().floatValue()+0.5f : 3.0f+0.5f, 0f, 120, 2);

		GL11.glDepthMask(true);
		GL11.glDisable(2848);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glPopMatrix();
	}
}
