package NovClient.Module.Modules.Move;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.API.Events.World.EventMove;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Combat.KillAura;
import NovClient.Util.Math.RotationUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class TargetStrafe extends Module {
	public static Numbers<Double> MaxDistance = new Numbers<Double>("Distance", "Distance", 3.0d, 1.0d, 5.0d, 0.1d);
	public static Option<Boolean> keep = new Option<Boolean>("KeepDist", "KeepDist", true);
	public static Option<Boolean> Esp = new Option<Boolean>("ESP", "ESP", true);
	public static Option<Boolean> OnlySpeed = new Option<Boolean>("OnlySpeed", "OnlySpeed", true);
	public static Option<Boolean> Auto = new Option<Boolean>("Auto", "Auto", false);

	public TargetStrafe() {
		super("TargetStrafe", new String[] { "TargetStrafe" }, ModuleType.Combat);
		this.addValues(MaxDistance, keep, Esp, OnlySpeed);
	}

	@Override
	public void onDisable() {
		// KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(),
		// false);
		super.onDisable();
	}

	public static double getSpeedByXZ(double motionX, double motionZ) {
		final double vel = Math.sqrt(motionX * motionX + motionZ * motionZ);
		return vel;
	}

	@EventHandler
	public void onMotion(EventPreUpdate e) {
			mc.gameSettings.keyBindJump.Doing = false;
			mc.gameSettings.keyBindForward.Doing = false;
	}

	@EventHandler
	public void onMotion(EventMove eventMove) {
		if (KillAura.blockTarget != null
				&& Client.instance.getModuleManager().getModuleByClass(KillAura.class).isEnabled()
				&& !mc.thePlayer.isOnLadder()) {
			if (!OnlySpeed.getValue())
				onStrafe(eventMove);
		}

	}

	public static void onStrafe(EventMove eventMove) {
		if (KillAura.blockTarget != null) {
			if (!RotationUtil.canEntityBeSeen(KillAura.blockTarget)) {
				return;
			}
			double speed = getSpeedByXZ(eventMove.getX(), eventMove.getZ());
			setMoveSpeed(speed * 0.9, Client.Yaw,
					Math.abs(mc.thePlayer.getDistanceToEntity(KillAura.blockTarget)
							- MaxDistance.getValue().floatValue()) <= 0.4,
					eventMove, MaxDistance.getValue().floatValue());
		}
	}

	public static void onStrafe(EventMove eventMove, double speed) {
		if (KillAura.blockTarget != null) {
			if (!RotationUtil.canEntityBeSeen(KillAura.blockTarget)) {
				return;
			}
			setMoveSpeed(speed * 0.9, Client.Yaw,
					Math.abs(mc.thePlayer.getDistanceToEntity(KillAura.blockTarget)
							- MaxDistance.getValue().floatValue()) <= 0.4,
					eventMove, MaxDistance.getValue().floatValue());
		}
	}

	public static void setMoveSpeed(final double speed, float yaw, boolean forwardTo, EventMove eventMove, float dist) {
		double forward = mc.thePlayer.movementInput.moveForward;
		double strafe = mc.thePlayer.movementInput.moveStrafe;
		if (keep.getValue()) {
			if (forwardTo) {
				if (forward > 0) {
					forward = 0;
				}
			} else {
				if (mc.thePlayer.getDistanceToEntity(KillAura.blockTarget) < dist)
					forward = -speed;
			}
		} else {
			forward = forward > 0 ? 1 : (forward < 0 ? -1 : 0);
			forward *= speed;
		}
		strafe = strafe > 0 ? 1 : (strafe < 0 ? -1 : 1);
		eventMove.x = (forward * speed * Math.cos(Math.toRadians((double) (yaw + 90.0f)))
				+ strafe * speed * Math.sin(Math.toRadians((double) (yaw + 90.0f))));
		eventMove.z = (forward * speed * Math.sin(Math.toRadians((double) (yaw + 90.0f)))
				- strafe * speed * Math.cos(Math.toRadians((double) (yaw + 90.0f))));
	}

	@EventHandler
	public void onRender(EventRender3D render) {
		if (KillAura.blockTarget != null)
			drawESP(render);
	}

	private void drawESP(EventRender3D render) {
		if (!Esp.getValue())
			return;
		double x = KillAura.blockTarget.lastTickPosX
				+ (KillAura.blockTarget.posX - KillAura.blockTarget.lastTickPosX) * render.getPartialTicks()
				- mc.getRenderManager().viewerPosX;
		double y = KillAura.blockTarget.lastTickPosY
				+ (KillAura.blockTarget.posY - KillAura.blockTarget.lastTickPosY) * render.getPartialTicks()
				- mc.getRenderManager().viewerPosY;
		double z = KillAura.blockTarget.lastTickPosZ
				+ (KillAura.blockTarget.posZ - KillAura.blockTarget.lastTickPosZ) * render.getPartialTicks()
				- mc.getRenderManager().viewerPosZ;
		esp(KillAura.blockTarget, x, y, z);
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
		if (!(KillAura.blockTarget.hurtTime > 0)) {
			GlStateManager.color(0.25f, 2.0f, 0.0f, 1.0f);
		} else {
			GlStateManager.color(1.35f, 0.0f, 0.0f, 1.0f);
		}
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
		c.draw(MaxDistance.getValue().floatValue(), MaxDistance.getValue().floatValue(), 0f, 120, 2);

		GL11.glDepthMask(true);
		GL11.glDisable(2848);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glPopMatrix();
	}
}
