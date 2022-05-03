package NovClient.Module.Modules.Move;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventMove;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Value;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.Math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;

import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LongJump extends Module {
	private Mode<JumpMode> mode = new Mode("Mode", "Mode", (Enum[]) JumpMode.values(), (Enum) JumpMode.NCP);
	private int stage;
	private double moveSpeed;
	private double lastDist;

	public LongJump() {
		super("LongJump", new String[] { "lj", "jumpman", "jump" }, ModuleType.Move);
		this.addValues(this.mode);
		this.setColor(new Color(76, 67, 216).getRGB());
	}

	@Override
	public void onDisable() {
		this.mc.timer.timerSpeed = 1.0f;
		if (this.mc.thePlayer != null) {
			this.moveSpeed = this.getBaseMoveSpeed();
		}
		this.lastDist = 0.0;
		this.stage = 0;
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		this.setSuffix((Object) this.mode.getValue());
		if (e.getType() == 0) {
			double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
			double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
			this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		}
	}

	@EventHandler
	private void onMove(EventMove e) {
		if (this.mode.getValue() == JumpMode.NCP) {
			if (this.mc.thePlayer.moveStrafing <= 0.0f && this.mc.thePlayer.moveForward <= 0.0f) {
				this.stage = 1;
			}
			if (this.stage == 1 && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)) {
				this.stage = 2;
				this.moveSpeed = 3.0 * this.getBaseMoveSpeed() - 0.01;
			} else if (this.stage == 2) {
				this.stage = 3;
				this.mc.thePlayer.motionY = 0.424;
				EventMove.y = 0.424;
				this.moveSpeed *= 2.149802;
			} else if (this.stage == 3) {
				this.stage = 4;
				double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
				this.moveSpeed = this.lastDist - difference;
			} else {
				if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer,
						this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0
						|| this.mc.thePlayer.isCollidedVertically) {
					this.stage = 1;
				}
				this.moveSpeed = this.lastDist - this.lastDist / 159.0;
			}
			if (this.stage > 3) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						Client.instance.getModuleManager().getModuleByClass(LongJump.class).setEnabled(false);
						this.cancel();
					}
				}, 240L);
			}
			this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
			MovementInput movementInput = this.mc.thePlayer.movementInput;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = this.mc.thePlayer.rotationYaw;
			if (forward == 0.0f && strafe == 0.0f) {
				EventMove.x = 0.0;
				EventMove.z = 0.0;
			} else if (forward != 0.0f) {
				if (strafe >= 1.0f) {
					yaw += (float) (forward > 0.0f ? -45 : 45);
					strafe = 0.0f;
				} else if (strafe <= -1.0f) {
					yaw += (float) (forward > 0.0f ? 45 : -45);
					strafe = 0.0f;
				}
				if (forward > 0.0f) {
					forward = 1.0f;
				} else if (forward < 0.0f) {
					forward = -1.0f;
				}
			}
			double mx = Math.cos(Math.toRadians(yaw + 90.0f));
			double mz = Math.sin(Math.toRadians(yaw + 90.0f));
			EventMove.x = (double) forward * this.moveSpeed * mx + (double) strafe * this.moveSpeed * mz;
			EventMove.z = (double) forward * this.moveSpeed * mz - (double) strafe * this.moveSpeed * mx;
			if (forward == 0.0f && strafe == 0.0f) {
				EventMove.x = 0.0;
				EventMove.z = 0.0;
			} else if (forward != 0.0f) {
				if (strafe >= 1.0f) {
					yaw += (float) (forward > 0.0f ? -45 : 45);
					strafe = 0.0f;
				} else if (strafe <= -1.0f) {
					yaw += (float) (forward > 0.0f ? 45 : -45);
					strafe = 0.0f;
				}
				if (forward > 0.0f) {
					forward = 1.0f;
				} else if (forward < 0.0f) {
					forward = -1.0f;
				}
			}
		}
	}

	double getBaseMoveSpeed() {
		double baseSpeed = 0.2873;
		if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
		}
		return baseSpeed;
	}

	static enum JumpMode {
		NCP;
	}
}
