package NovClient.Module.Modules.Move;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventMove;
import NovClient.API.Events.World.EventPacketReceive;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Combat.Criticals;
import NovClient.Module.Modules.Combat.KillAura;
import NovClient.Util.*;

import NovClient.Util.Math.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import net.minecraft.world.World;

public class Speed extends Module {

	private double delay1 = 0.0;
	private double delay2 = 0.0;
	int delay;
	private boolean boost;
	private int setback;
	private int timeState;
	public int state;
	private int cooldownHops;
	private float i;
	private boolean wasOnWater;
	private boolean doTime;
	private double delay3 = 1.7;
	private double hAllowedDistance = 0.2873000087011036;
	private int JUMPTicks;
	private boolean b2;
	private boolean b3;

	private boolean glide = false;
	private boolean slowFall = false;
	private Minecraft var10000;
	private Minecraft var10001;
	private double randomY = this.random(1334.0D, -1332.0D) / 250000.0D;
	private double zDist;
	private double xDist;
	private Mode<Enum> mode = new Mode("Mode", "Mode", (Enum[]) BiAnHuaSpeedMode.values(), (Enum) BiAnHuaSpeedMode.Bhop);
	private Option<Boolean> lagcheck = new Option<Boolean>("LagCheck", "LagCheck", true);
	private double[] values = { 0.08D, 0.09316090325960147D, 1.688D, 2.149D, 0.66D };
	public static int stage;
	private int stage2 = 1;
	private int stage3 = 2;
	private int stag = 0;
	private double movementSpeed;
	private boolean firstjump;
	private int stupidAutisticTickCounting;
	private World world;
	double moveSpeed;
	private double distance;
	private int packetCounter;
	private TimerUtil deactivationDelay = new TimerUtil();
	private double posY;
	private int speedTick;
	private boolean legitHop = false;
	private double lastDist;
	private int tick;
	private List<AxisAlignedBB> collidingList;
	public boolean shouldslow = false;
	private TimerUtil timer = new TimerUtil();
	private double difference;
	private double speed;
	int steps;
	private int level;
	public static int aacCount;
	boolean collided = false, lessSlow;
	TimerUtil aac = new TimerUtil();
	TimerUtil lastCheck = new TimerUtil();
	TimerUtil lastFall = new TimerUtil();
	SigmaBhop sigmabhop = new SigmaBhop();
	double less, stair;
	private int counter = 0;
	private int ticks;
	private double field_1;

	public Speed() {
		super("Speed", new String[] { "zoom" }, ModuleType.Move);
		this.setColor(new Color(99, 248, 91).getRGB());
		this.addValues(this.mode, this.lagcheck);
	}

	private double random(double var1, double var3) {
		return Math.random() * (var1 - var3) + var3;
	}

	private boolean isInLiquid() {
		if (mc.thePlayer == null) {
			return false;
		}
		for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper
					.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
				BlockPos pos = new BlockPos(x, (int) mc.thePlayer.boundingBox.minY, z);
				Block block = mc.theWorld.getBlockState(pos).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					return block instanceof BlockLiquid;
				}
			}
		}
		return false;
	}

	@Override
	public void onDisable() {
		mc.gameSettings.keyBindForward.Doing = false;
		mc.gameSettings.keyBindBack.Doing = false;
		mc.gameSettings.keyBindLeft.Doing = false;
		mc.gameSettings.keyBindRight.Doing = false;
		mc.gameSettings.keyBindSneak.Doing = false;
		mc.gameSettings.keyBindJump.Doing = false;
		this.mc.timer.timerSpeed = 1.0f;
		this.tick = 0;
		aacCount = 0;

		super.onDisable();
		this.counter = 0;
		this.delay3 = 0.0;
		this.delay2 = 0.0;
		this.delay1 = 0.0;
		mc.timer.timerSpeed = 1.0f;
		this.slowFall = false;
		this.moveSpeed = this.getBaseMoveSpeed();
		this.b2 = true;
		this.glide = false;
		this.level = 0;
		this.posY = 0.0;
		this.mc.timer.timerSpeed = 1.0f;
		this.tick = 0;
		this.legitHop = true;
		this.firstjump = false;
		this.mc.thePlayer.speedInAir = 0.02f;
		this.speedTick = 0;
		Blocks.packed_ice.slipperiness = 0.98f;
		Blocks.ice.slipperiness = 0.98f;
		this.mc.gameSettings.viewBobbing = this.b2;

		if (this.mode.getValue() == BiAnHuaSpeedMode.HypixelCN) {
			sigmabhop.onEnable();
		}
		
	}

	public void onEnable() {
		boolean player = mc.thePlayer == null;
		collided = player ? false : mc.thePlayer.isCollidedHorizontally;
		lessSlow = false;


		less = 0;
		lastDist = 0.0;
		stage = 2;
		this.b2 = this.mc.gameSettings.viewBobbing;
		this.level = 1;
		this.mc.timer.timerSpeed = 1.0f;
		this.stag = 0;
		this.delay3 = 0.0;
		this.moveSpeed = this.mc.thePlayer == null ? 0.2873 : this.getBaseMoveSpeed();
		this.b3 = true;

        if(this.mc.thePlayer.isMoving() && this.mc.thePlayer.onGround) {
            this.mc.thePlayer.motionY = this.mc.thePlayer.getJumpHeight(0.399999986886975D);
            this.speed *= 2.0D;
         }

         double v11 = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
         double v3 = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
         this.field_1 = Math.sqrt(v11 * v11 + v3 * v3);
		
		super.onEnable();
	}

	private boolean canZoom() {
		if (this.mc.thePlayer.isMoving() && this.mc.thePlayer.onGround) {
			return true;
		}
		return false;
	}

	public double getDistanceToFall() {
		double distance = 0;
		for (double i = mc.thePlayer.posY; i > 0; i -= 0.1) {
			if (i < 0)
				break;
			Block block = BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ));
			if (block.getMaterial() != Material.air && (block.isCollidable())
					&& (block.isFullBlock() || block instanceof BlockSlab || block instanceof BlockBarrier
							|| block instanceof BlockStairs || block instanceof BlockGlass
							|| block instanceof BlockStainedGlass)) {
				if (block instanceof BlockSlab)
					i -= 0.5;
				distance = i;
				break;
			}
		}
		return (mc.thePlayer.posY - distance);
	}

	public void freezePlayer() {
		this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + 1.0, this.mc.thePlayer.posY,
				this.mc.thePlayer.posZ + 1.0);
		this.mc.thePlayer.setPosition(this.mc.thePlayer.prevPosX, this.mc.thePlayer.posY, this.mc.thePlayer.prevPosZ);
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		this.setSuffix(this.mode.getValue());

		Scaffold i3 = (Scaffold) Client.instance.getModuleManager().getModuleByClass(Scaffold.class);
		if (i3.isEnabled() && i3.Lag.getValue()) {
			return;
		}
		Criticals i4 = (Criticals) Client.instance.getModuleManager().getModuleByClass(Criticals.class);
		if (i4.isEnabled() && i4.StopSpeed.getValue()) {
			if (KillAura.blockTarget != null) {
				return;
			}
		}


		if (this.mode.getValue() == BiAnHuaSpeedMode.Hypixel) {
			double var7 = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			this.distance = Math.sqrt(var7 * var7 + zDist * zDist);
		}


	}

	private boolean isBlockUnder(Material air) {
		return false;
	}

	private int getRandom(int i) {
		return 0;
	}

	public void setSpeed(final double speed) {
		mc.thePlayer.motionX = -(Math.sin(getDirection()) * speed);
		mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
	}
	public float getDirection() {
		float yaw = mc.thePlayer.rotationYaw;
		if (mc.thePlayer.moveForward < 0.0f) {
			yaw += 180.0f;
		}
		float forward = 1.0f;
		if (mc.thePlayer.moveForward < 0.0f) {
			forward = -0.5f;
		} else if (mc.thePlayer.moveForward > 0.0f) {
			forward = 0.5f;
		}
		if (mc.thePlayer.moveStrafing > 0.0f) {
			yaw -= 90.0f * forward;
		}
		if (mc.thePlayer.moveStrafing < 0.0f) {
			yaw += 90.0f * forward;
		}
		yaw *= 0.017453292f;
		return yaw;
	}

	@EventHandler
	private void onPacket(EventPacketReceive ep) {
		if (this.lagcheck.getValue().booleanValue()) {
			if (ep.getPacket() instanceof S08PacketPlayerPosLook && this.deactivationDelay.delay(2000.0F)) {
				++this.packetCounter;
				aacCount = 0;
				S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
				pac.yaw = mc.thePlayer.rotationYaw;
				pac.pitch = mc.thePlayer.rotationPitch;
				Speed.stage = -5;
				if (Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
					Client.instance.getModuleManager().getModuleByClass(Speed.class).setEnabled(false);
				}
			}
		}
	}

	private double getAACSpeed(int stage, int jumps) {
		double value = 0.29;
		double firstvalue = 0.3019;
		double thirdvalue = 0.0286 - (double) stage / 1000;
		if (stage == 0) {
			// JUMP
			value = 0.497;
			if (jumps >= 2) {
				value += 0.1069;
			}
			if (jumps >= 3) {
				value += 0.046;
			}
			Block block = MoveUtils.getBlockUnderPlayer(mc.thePlayer, 0.01);
			if (block instanceof BlockIce || block instanceof BlockPackedIce) {
				value = 0.59;
			}
		} else if (stage == 1) {
			value = 0.3031;
			if (jumps >= 2) {
				value += 0.0642;
			}
			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 2) {
			value = 0.302;
			if (jumps >= 2) {
				value += 0.0629;
			}
			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 3) {
			value = firstvalue;
			if (jumps >= 2) {
				value += 0.0607;
			}
			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 4) {
			value = firstvalue;
			if (jumps >= 2) {
				value += 0.0584;
			}
			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 5) {
			value = firstvalue;
			if (jumps >= 2) {
				value += 0.0561;
			}
			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 6) {
			value = firstvalue;
			if (jumps >= 2) {
				value += 0.0539;
			}
			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 7) {
			value = firstvalue;
			if (jumps >= 2) {
				value += 0.0517;
			}
			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 8) {
			value = firstvalue;
			if (MoveUtils.isOnGround(0.05))
				value -= 0.002;

			if (jumps >= 2) {
				value += 0.0496;
			}
			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 9) {
			value = firstvalue;
			if (jumps >= 2) {
				value += 0.0475;
			}
			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 10) {

			value = firstvalue;
			if (jumps >= 2) {
				value += 0.0455;
			}
			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 11) {

			value = 0.3;
			if (jumps >= 2) {
				value += 0.045;
			}
			if (jumps >= 3) {
				value += 0.018;
			}

		} else if (stage == 12) {
			value = 0.301;
			if (jumps <= 2)
				aacCount = 0;
			if (jumps >= 2) {
				value += 0.042;
			}
			if (jumps >= 3) {
				value += thirdvalue + 0.001;
			}
		} else if (stage == 13) {
			value = 0.298;
			if (jumps >= 2) {
				value += 0.042;
			}
			if (jumps >= 3) {
				value += thirdvalue + 0.001;
			}
		} else if (stage == 14) {

			value = 0.297;
			if (jumps >= 2) {
				value += 0.042;
			}
			if (jumps >= 3) {
				value += thirdvalue + 0.001;
			}
		}
		if (mc.thePlayer.moveForward <= 0) {
			value -= 0.06;
		}

		if (mc.thePlayer.isCollidedHorizontally) {
			value -= 0.1;
			aacCount = 0;
		}
		return value;
	}

	@EventHandler
	private void onMove(EventMove e) {

		Scaffold i3 = (Scaffold) Client.instance.getModuleManager().getModuleByClass(Scaffold.class);
		if (i3.isEnabled() && i3.Lag.getValue()) {
			return;
		}
		Criticals i4 = (Criticals) Client.instance.getModuleManager().getModuleByClass(Criticals.class);
		if (i4.isEnabled() && i4.StopSpeed.getValue()) {
			if (KillAura.blockTarget != null) {
				return;
			}
		}

		double a;
		if (this.mode.getValue() == BiAnHuaSpeedMode.Hypixel) {

			if (mc.thePlayer.isCollidedHorizontally) {
				this.collided = true;
			}

			if (this.collided) {
				mc.timer.timerSpeed = 1.0F;
				stage = -1;
			}

			if (this.stair > 0.0D) {
				this.stair -= 0.25D;
			}

			this.less -= this.less > 1.0D ? 0.12D : 0.11D;
			if (this.less < 0.0D) {
				this.less = 0.0D;
			}

			if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01D) && isMoving2()) {
				this.collided = mc.thePlayer.isCollidedHorizontally;
				if (stage >= 0 || this.collided) {
					stage = 0;
					a = 0.4086666D + (double) MoveUtils.getJumpEffect() * 0.1D;
					if (this.stair == 0.0D) {
						mc.thePlayer.jump();
						mc.thePlayer.motionY = a;
						EventMove.setY(mc.thePlayer.motionY);
					}

					++this.less;
					this.lessSlow = this.less > 1.0D && !this.lessSlow;
					if (this.less > 1.12D) {
						this.less = 1.12D;
					}
				}
			}

			this.speed = this.getHypixelSpeed(stage) + 0.0331D;
			this.speed *= 0.91D;
			if (this.stair > 0.0D) {
				this.speed *= 0.66D - (double) MoveUtils.getSpeedEffect() * 0.1D;
			}

			if (stage < 0) {
				this.speed = getBaseMoveSpeed();
			}

			if (this.lessSlow) {
				this.speed *= 0.93D;
			}

			if (BlockUtils.isInLiquid()) {
				this.speed = 0.12D;
			}
			Label1:
			if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) {
				if(Client.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled() && KillAura.blockTarget != null) {
				TargetStrafe.onStrafe(e, speed);
				++stage;
				break Label1;
				}
				this.setMotion(e, this.speed);
				++stage;
			}
		}else if (this.mode.getValue() == BiAnHuaSpeedMode.HypixelCN) {
			sigmabhop.onMove(e);
		}else if (mode.getValue() == BiAnHuaSpeedMode.Bhop) {
			mc.timer.timerSpeed = 1.7F;
			if (mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f) {
				speed = getBaseMoveSpeed();
			}
			if (stage == 1 && mc.thePlayer.isCollidedVertically
					&& (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
				speed = 1.35 + getBaseMoveSpeed() - 0.01;
			}
			if (!isInLiquid() && stage == 2 && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01)
					&& (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
				if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump))
					e.setY(mc.thePlayer.motionY = 0.41999998688698
							+ (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1)
									* 0.1);
				else
					e.setY(mc.thePlayer.motionY = 0.41999998688698);
				mc.thePlayer.jump();
				speed *= 1.533D;
			} else if (stage == 3) {
				final double difference = 0.66 * (lastDist - getBaseMoveSpeed());
				speed = lastDist - difference;
			} else {
				final List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0));
				if ((collidingList.size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
					stage = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
				}
				speed = lastDist - lastDist / 159.0;
			}
			speed = Math.max(speed, getBaseMoveSpeed());

			// Stage checks if you're greater than 0 as step sets you -6 stage to make sure
			// the player wont flag.
			Label1:
			if (stage > 0) {
				// Set strafe motion.
				if (BlockUtils.isInLiquid())
					speed = 0.1;
				if(Client.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled() && KillAura.blockTarget != null) {
				TargetStrafe.onStrafe(e, speed);
				break Label1;
				}
				setMotion(e, speed);
			}
			// If the player is moving, step the stage up.
			if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
				++stage;
			}

		} else if (mode.getValue() == BiAnHuaSpeedMode.AAC) {
			if (mc.thePlayer.fallDistance > 1.2) {
				lastFall.reset();
			}
			if (!BlockUtils.isInLiquid() && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01)
					&& (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
				stage = 0;
				mc.thePlayer.jump();
				e.setY(mc.thePlayer.motionY = 0.41999998688698 + MoveUtils.getJumpEffect());
				if (aacCount < 4)
					aacCount++;
			}
			speed = getAACSpeed(stage, aacCount);
			Label1:
			if ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
				if (BlockUtils.isInLiquid()) {
					speed = 0.075;
				}
				
					
				if(Client.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled() && KillAura.blockTarget != null) {
				TargetStrafe.onStrafe(e, speed);
				break Label1;
				}
				setMotion(e, speed);
				
			}

			if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
				++stage;
			}
		}
	}

	/*     */ public static boolean isMoving2() {
		/* 310 */ Minecraft.getMinecraft();
		if (mc.thePlayer.moveForward == 0.0F) {
			Minecraft.getMinecraft();
			if (mc.thePlayer.moveStrafing == 0.0F)
				/* 311 */ return false;
		}
		/*     */
		/* 313 */ return true;
		/*     */ }

	private boolean MovementInput() {
		if (!(this.mc.gameSettings.keyBindForward.pressed || this.mc.gameSettings.keyBindLeft.pressed
				|| this.mc.gameSettings.keyBindRight.pressed || this.mc.gameSettings.keyBindBack.pressed)) {
			return false;
		}
		return true;
	}

	@EventHandler
	public void will(EventPreUpdate e){
		if(mode.getValue() == BiAnHuaSpeedMode.Hypixel){
			Try t = new Try();
			e.setYaw(t.getMoveYaw());
			Client.Yaw = t.getMoveYaw();
		}
	}

	public void setMoveSpeed(EventMove event, double speed) {
		MovementInput movementInput = mc.thePlayer.movementInput;
		double forward = movementInput.moveForward;
		double strafe = movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;

		if ((forward == 0.0D) && (strafe == 0.0D)) {
			event.x = 0.0D;
			event.x = 0.0D;
		} else {
			if (forward != 0.0D) {
				this.mc.thePlayer.setSpeed(0.279);
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}

				strafe = 0.0D;

				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}
			}

			event.x = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
			event.z = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
		}
	}


	
	private double defaultSpeed() {
		double baseSpeed = 0.2873;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
		}
		return baseSpeed;
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	private void setMotion(EventMove em, double speed) {
		double forward = this.mc.thePlayer.movementInput.moveForward;
		double strafe = this.mc.thePlayer.movementInput.moveStrafe;
		float yaw = this.mc.thePlayer.rotationYaw;
		if (forward == 0.0 && strafe == 0.0) {
			em.setX(0.0);
			em.setZ(0.0);
		} else {
			if (forward != 0.0) {
				if (strafe > 0.0) {
					yaw += (float) (forward > 0.0 ? -45 : 45);
				} else if (strafe < 0.0) {
					yaw += (float) (forward > 0.0 ? 45 : -45);
				}
				strafe = 0.0;
				if (forward > 0.0) {
					forward = 1.0;
				} else if (forward < 0.0) {
					forward = -1.0;
				}
			}
			em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
			em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
		}
	}



	private void setMoveSpeed2(final EventMove event, final double speed) {
		double forward = mc.thePlayer.movementInput.moveForward;
		double strafe = mc.thePlayer.movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if (forward == 0.0 && strafe == 0.0) {
			event.setX(0.0);
			event.setZ(0.0);
		} else {
			if (forward != 0.0) {
				if (strafe > 0.0) {
					yaw += ((forward > 0.0) ? -45 : 45);
				} else if (strafe < 0.0) {
					yaw += ((forward > 0.0) ? 45 : -45);
				}
				strafe = 0.0;
				if (forward > 0.0) {
					forward = 1.0;
				} else if (forward < 0.0) {
					forward = -1.0;
				}
			}
			event.setX(
					forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
			event.setZ(
					forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
		}
	}

	private double getHypixelSpeed(int stage) {
		double value = getBaseMoveSpeed() + (0.028 * MoveUtils.getSpeedEffect())
				+ (double) MoveUtils.getSpeedEffect() / 15;
		double firstvalue = 0.4145 + (double) MoveUtils.getSpeedEffect() / 12.5;
		double decr = (((double) stage / 500) * 2);

		if (stage == 0) {
			// JUMP
			if (timer.delay(300)) {
				timer.reset();

			}
			if (!lastCheck.delay(500)) {
				if (!shouldslow)
					shouldslow = true;
			} else {
				if (shouldslow)
					shouldslow = false;
			}
			value = 0.64 + (MoveUtils.getSpeedEffect() + (0.028 * MoveUtils.getSpeedEffect())) * 0.134;

		} else if (stage == 1) {
			value = firstvalue;
		} else if (stage >= 2) {
			value = firstvalue - decr;
		}
		if (shouldslow || !lastCheck.delay(500) || collided) {
			value = 0.2;
			if (stage == 0)
				value = 0;
		}

		return Math.max(value, shouldslow ? value : getBaseMoveSpeed() + (0.028 * MoveUtils.getSpeedEffect()));
	}

	public static double getSpeed() {
		return Math.sqrt(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX
				+ Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ);
	}

	private boolean canSpeed() {
		boolean ignore;
		// boolean bl = ignore = this.mode.value == speedMode.HOP || this.mode.value ==
		// speedMode.YPORT;
		// if (!(Helper.player().isOnLadder() || Helper.player().isInWater() ||
		// Helper.player().isSneaking() || !ignore && !this.mc.thePlayer.onGround ||
		// !ignore && this.mc.gameSettings.keyBindJump.getIsKeyPressed() ||
		// Helper.player().motionX == 0.0 || Helper.player().motionZ == 0.0)) {
		// Helper.blockUtils();
		if (!this.mc.thePlayer.isInWater()) {
			return true;
			// }
		}
		return false;
	}

	public void toFwd(final double speed) {
		final float yaw = mc.thePlayer.rotationYaw * 0.017453292f;
		final EntityPlayerSP thePlayer = mc.thePlayer;
		thePlayer.motionX -= MathHelper.sin(yaw) * speed;
		final EntityPlayerSP thePlayer2 = mc.thePlayer;
		thePlayer2.motionZ += MathHelper.cos(yaw) * speed;
	}

	public double getJump() {
		double baseJump = 0.416565;
		if (mc.thePlayer.isPotionActive(Potion.jump)) {
			final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
			baseJump = 0.42 + amplifier;
		}
		return baseJump;
	}



	private static int GetRandomNumber(final int n, final int n2) {
		return (int) (Math.random() * (n - n2)) + n2;
	}

	public void clear() {
		this.delay3 = 0.0;
		this.delay2 = 0.0;
		this.delay1 = 0.0;
		mc.timer.timerSpeed = 1.0f;
		this.moveSpeed = this.getBaseMoveSpeed();
		this.b2 = true;
		this.glide = false;
		this.level = 0;
		Blocks.packed_ice.slipperiness = 0.98f;
		Blocks.ice.slipperiness = 0.98f;
	}
	
	   private boolean isDownIce() {
		      return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1.0D, mc.thePlayer.posZ)).getBlock() instanceof BlockPackedIce || mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1.0D, mc.thePlayer.posZ)).getBlock() instanceof BlockIce;
		   }
	
	   private double getBaseMoveSpeed() {
		      double v2 = isDownIce()?0.548300006389618D:(PlayerUtil.isInLiquid()?0.1283000063896179D:0.2598000063896179D);
		      return mc.thePlayer.getBaseSpeed(v2, 0.2D);
		   }
	static enum BiAnHuaSpeedMode {
		Hypixel,HypixelCN, Bhop, AAC;
	}
}

//  LAC Client DevCode by LEFgangaDEV
