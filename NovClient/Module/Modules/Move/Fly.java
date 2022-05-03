/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Move;

import java.awt.Color;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import org.apache.commons.lang3.RandomUtils;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventMove;
import NovClient.API.Events.World.EventPacketReceive;
import NovClient.API.Events.World.EventPacketSend;
import NovClient.API.Events.World.EventPostUpdate;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Events.World.EventTick;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.Helper;
import NovClient.Util.MoveUtils;
import NovClient.Util.PlayerUtil;
import NovClient.Util.TickTimer;
import NovClient.Util.TimerUtil;
import NovClient.Util.Math.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class Fly extends Module {
	public Mode mode = new Mode("Mode", "Mode", (Enum[]) FlyMode.values(), (Enum) FlyMode.Vanilla);
	public Mode DMode = new Mode("DamageMode", "DamageMode", (Enum[]) DamageMode.values(), (Enum) DamageMode.Semi);
	private Option<Boolean> lagcheck = new Option<Boolean>("LagCheck", "LagCheck", true);
	private Option<Boolean> dragonvalue = new Option<Boolean>("Dragon", "Dragon", false);
	private Option<Boolean> Watting = new Option<Boolean>("Waitting", "Waitting", false);
	private static Numbers<Double> WattingTime = new Numbers<Double>("WaittingTime", "WaittingTime", 2.1, 1.0, 5.0,
			0.1);
	private static Numbers<Double> zoomboost = new Numbers<Double>("BoostTime", "BoostTime", 5.0, 0.0, 20.0, 0.1);
	private static Numbers<Double> timerboost = new Numbers<Double>("timerboost", "timerboost", 0.0, 0.0, 5.0, 0.1);
	private static Numbers<Double> groundboost = new Numbers<Double>("Boost", "Boost", 2.1, 1.0, 5.0, 0.1);

	private Option<Boolean> par = new Option<Boolean>("Particle", "Particle", false);
	private Option<Boolean> UHC = new Option<Boolean>("UHC", "UHC", false);
	public static Option<Boolean> groundpacket = new Option<Boolean>("GroundPacket", "GroundPacket", false);
	private Option<Boolean> jump = new Option<Boolean>("Jump", "Jump", false);
	private Option<Boolean> bob = new Option<Boolean>("Bobbing", "Bobbing", false);
	private Option<Boolean> DoubleSpeed = new Option<Boolean>("DoubleSpeed", "DoubleSpeed", false);
	private TimerUtil time = new TimerUtil();
	private TimerUtil BeginTimer = new TimerUtil();
	private TimerUtil kickTimer = new TimerUtil();
	private double movementSpeed;
	private int hypixelCounter;
	Double RandomValue = 0.0;
	private List<Packet> packetList = new CopyOnWriteArrayList<Packet>();
	private TimerUtil Packettimer = new TimerUtil();
	private EntityDragon dragon;
	private int zcounter, zboost;
	private int hypixelCounter2;
	int counter, level;
	private boolean fast;
	private TimerUtil SlowTimer = new TimerUtil();
	TimerUtil BoostTimer = new TimerUtil();
	RandomUtils random = new RandomUtils();
	private double flyHeight;
	int i, slotId, ticks;
	private boolean dragoncrea = false;
	private int zoom;
	private int packetCounter;
	private TimerUtil deactivationDelay = new TimerUtil();
	double moveSpeed, lastDist;
	boolean b2;
	public static boolean damaged = true;
	boolean fly;
	private boolean failedStart;
	private double lastDistance;
	private int boostHypixelState;
	private TickTimer latestHypixelTimer = new TickTimer();
	private int stage, groundtick;

	public Fly() {
		super("Fly", new String[] { "fly", "angel" }, ModuleType.Move);
		this.setColor(new Color(158, 114, 243).getRGB());
		this.addValues(this.mode, DMode, this.timerboost, this.lagcheck, this.dragonvalue, this.par, Watting,
				WattingTime, this.zoomboost, this.UHC, this.groundboost, bob, this.jump, groundpacket, DoubleSpeed);
	}

	public void damagePlayerNew2() {
		if (this.mc.thePlayer.onGround) {
			for (int var1 = 0; var1 <= (this.UHC.getValue().booleanValue() ? 9 : 8); ++var1) {

				this.mc.getNetHandler().getNetworkManager()
						.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
								this.mc.thePlayer.posY + 0.41999998688698D, this.mc.thePlayer.posZ, false));
				// this.mc.getNetHandler().getNetworkManager().sendPacket(new
				// C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.posy +
				// 0.10643676261265D, this.mc.thePlayer.posZ, false));
				this.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
						this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));

			}

			this.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
					this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
			// this.mc.getNetHandler().getNetworkManager().sendPacket(new
			// C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.posy,
			// this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw,
			// this.mc.thePlayer.rotationPitch, true));
		}
	}

	public void damagePlayerNew4() {
		if (this.mc.thePlayer.onGround) {
			for (int var1 = 0; var1 <= (this.UHC.getValue().booleanValue() ? 9 : 8); ++var1) {
				this.mc.getNetHandler().getNetworkManager()
						.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX,
								this.mc.thePlayer.posY + 0.41999998688698D, this.mc.thePlayer.posZ,
								mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
				this.mc.getNetHandler().getNetworkManager()
						.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX,
								this.mc.thePlayer.posY, this.mc.thePlayer.posZ, mc.thePlayer.rotationYaw,
								mc.thePlayer.rotationPitch, false));

			}

			this.mc.getNetHandler().getNetworkManager()
					.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX,
							this.mc.thePlayer.posY, this.mc.thePlayer.posZ, mc.thePlayer.rotationYaw,
							mc.thePlayer.rotationPitch, true));
		}
	}

	public void damagePlayerNew3() {
		if (this.mc.thePlayer.onGround) {
			for (int i = 0; i <= (this.UHC.getValue().booleanValue() ? 9 : 8); i++) {
				this.mc.getNetHandler().getNetworkManager()
						.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
								this.mc.thePlayer.posY + 0.41999998688698D, this.mc.thePlayer.posZ, false));
				if (i > 2 && i < 6)
					this.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
							this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
						this.mc.thePlayer.posY + 0.00000000325, this.mc.thePlayer.posZ, false));
			}
			this.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
					this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
		}
	}

	public void goToGround() {
		if (this.flyHeight > 300.0D) {
			return;
		}
		double minY = mc.thePlayer.posY - this.flyHeight;
		if (minY <= 0.0D) {
			return;
		}
		for (double y = mc.thePlayer.posY; y > minY;) {
			y -= 8.0D;
			if (y < minY) {
				y = minY;
			}
			C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(
					mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
		for (double y = minY; y < mc.thePlayer.posY;) {
			y += 8.0D;
			if (y > mc.thePlayer.posY) {
				y = mc.thePlayer.posY;
			}
			C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(
					mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}

	@EventHandler
	private void onPacket(EventPacketReceive ep) {
		if (this.lagcheck.getValue().booleanValue()) {
			if (ep.getPacket() instanceof S08PacketPlayerPosLook && this.deactivationDelay.delay(2000F)) {
				++this.packetCounter;
				S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
				pac.yaw = mc.thePlayer.rotationYaw;
				pac.pitch = mc.thePlayer.rotationPitch;
				this.level = -5;
				if (Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled()) {
					Client.instance.getModuleManager().getModuleByClass(Fly.class).setEnabled(false);
				}
			}
		}
	}

	@EventHandler
	private void onPacket(EventPacketSend event) {
		final Packet packet = event.getPacket();

		if (packet instanceof C03PacketPlayer) {
			final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;
			if (this.mode.getValue() == FlyMode.HypixelDamage) {
				if (!damaged)
					return;
				// packetPlayer.onGround = true;
				// TODO Hypixel Onground
				// packetPlayer.onGround = MoveUtils.isOnGround(0.001);
			}
		}
		if (packet instanceof C03PacketPlayer) {
			final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;
			if (this.mode.getValue() == FlyMode.Float) {
				packetPlayer.onGround = new Random().nextBoolean();
			}
		}
		if (packet instanceof S08PacketPlayerPosLook) {

			if (this.mode.getValue() == FlyMode.HypixelDamage) {
				failedStart = true;
			}
		}

	}

	public void damage() {
		double fallDistance = 0;
		while (fallDistance < 3.5 + (UHC.getValue().booleanValue() ? 1 : 0)) {
			float offset = 0.4f;
			sendPacket(offset + (new Random()).nextDouble() * 0.02, false);
			sendPacket(0, (fallDistance + offset) >= (3.5 + (UHC.getValue().booleanValue() ? 1 : 0)));
			fallDistance += offset;
		}
	}

	void sendPacket(double addY, boolean ground) {
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
				mc.thePlayer.posY + addY, mc.thePlayer.posZ, ground));
	}

	@Override
	public void onEnable() {
		zboost = 1;
		BoostTimer.reset();
		SlowTimer.reset();
		if (DoubleSpeed.getValue())
			fast = false;
		else
			fast = true;
		this.zoom = (int) (this.zoomboost.getValue() * 10);
		float forward = MovementInput.moveForward;
		float strafe = MovementInput.moveStrafe;


		damaged = false;
		if (this.mode.getValue() != FlyMode.HypixelDamage)
			damaged = true;
		if (this.mode.getValue() == FlyMode.HypixelDamage) {
//			if (this.UHC.getValue()) {
//				this.damagePlayer(2);
//			} else {
//				damagePlayer();
//			}
			if (DMode.getValue() == DamageMode.Semi) {
				damagePlayerNew2();
			} else if (DMode.getValue() == DamageMode.Semi2) {
				damagePlayerNew4();
			} else if (DMode.getValue() == DamageMode.posLook) {

				damagePlayerNew();
			} else if (DMode.getValue() == DamageMode.Basic) {
				if (this.UHC.getValue()) {
					this.damagePlayer(2);
				} else {
					this.damagePlayer(1);
				}
			} else if (DMode.getValue() == DamageMode.FakeGround) {
				damagePlayerNew3();
			} else if (DMode.getValue() == DamageMode.RandomFloat) {
				damage();
			}
			if (Watting.getValue()) {
				(new Thread(() -> {
					try {
						Thread.sleep(WattingTime.getValue().longValue() * 100l);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					damaged = true;
					if (jump.getValue())
						mc.thePlayer.jump();
				})).start();
			} else {
				damaged = true;
				if (jump.getValue())
					mc.thePlayer.jump();
			}

			boostHypixelState = 1;
			moveSpeed = 0.1D;
			lastDistance = 0D;
			failedStart = false;
		}

		level = 1;
		dragoncrea = false;
		moveSpeed = 0.1D;
		b2 = true;
		lastDist = 0.0D;
	}

	public void damagePlayer(double d) {
		if (d < 1)
			d = 1;
		if (d > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
			d = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

		double offset = 0.0625;
		if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
			for (int i = 0; i <= ((3 + d) / offset); i++) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + d) / offset))));
			}
		}
	}

	public void HdamagePlayer(int var1) {
		if (var1 < 1) {
			var1 = 1;
		}

		if (var1 > MathHelper.floor_double((double) this.mc.thePlayer.getMaxHealth())) {
			var1 = MathHelper.floor_double((double) this.mc.thePlayer.getMaxHealth());
		}

		double var2 = 0.0625D;
		if (this.mc.thePlayer != null && this.mc.getNetHandler() != null && this.mc.thePlayer.onGround) {
			for (int var4 = 0; (double) var4 <= (double) (3 + var1) / var2; ++var4) {
				this.mc.getNetHandler()
						.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
								this.mc.thePlayer.posY + var2 + (double) (new Random()).nextFloat() * 1.0E-6D,
								this.mc.thePlayer.posZ, false));
				this.mc.getNetHandler()
						.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
								this.mc.thePlayer.posY, this.mc.thePlayer.posZ,
								(boolean) ((double) var4 == (double) (3 + var1) / var2 ? true : false)));
			}
		}

	}

	public void damagePlayerNew() {
		if (this.mc.thePlayer.onGround) {
			for (int var1 = 0; var1 <= ((Boolean) this.UHC.getValue() ? 60 : 49); ++var1) {
				double[] var2 = new double[] { 0.05099991337D, 0.06199991337D, 0.0D };
				double[] var3 = var2;
				int var4 = var2.length;

				for (int var5 = 0; var5 < var4; ++var5) {
					double var6 = var3[var5];
					this.mc.getNetHandler().getNetworkManager()
							.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX,
									this.mc.thePlayer.posY + var6, this.mc.thePlayer.posZ,
									this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, false));
				}
			}

			this.mc.getNetHandler().getNetworkManager()
					.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX,
							this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw,
							this.mc.thePlayer.rotationPitch, true));
		}
	}

	@Override
	public void onDisable() {
		this.hypixelCounter = 0;
		if (this.dragonvalue.getValue().booleanValue()) {
			mc.theWorld.removeEntity(dragon);
		}
		final double posX = this.mc.thePlayer.posX;
		final double posY = this.mc.thePlayer.posY;
		final double posZ = this.mc.thePlayer.posZ;
		this.hypixelCounter2 = 100;
		this.mc.timer.timerSpeed = 1.0f;
		level = 1;
		moveSpeed = 0.1D;
		b2 = false;
		lastDist = 0.0D;
		zboost = 1;
		mc.thePlayer.motionX = 0.0;
		mc.thePlayer.motionZ = 0.0;
	}

	public void updateFlyHeight() {
		double h = 1.0D;
		AxisAlignedBB box = mc.thePlayer.getEntityBoundingBox().expand(0.0625D, 0.0625D, 0.0625D);
		for (this.flyHeight = 0.0D; this.flyHeight < mc.thePlayer.posY; this.flyHeight += h) {
			AxisAlignedBB nextBox = box.offset(0.0D, -this.flyHeight, 0.0D);
			if (mc.theWorld.checkBlockCollision(nextBox)) {
				if (h < 0.0625D) {
					break;
				}
				this.flyHeight -= h;
				h /= 2.0D;
			}
		}
	}

	public static boolean isOnGround(double height) {
		if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
				mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	double getRandomDouble(int index) {
		switch (index) {

		case 0:
			return 1000000000000.0;
		case 1:
			return 100000000.0;
		case 2:
			return 1000000000.0;
		case 3:
			return 10000000000.0;
		case 4:
			return 100000000000.0;
		case 5:
			return 1000000000000.0;
		}
		return 100000000.0;
	}

	@EventHandler
	public void onUpdate(EventPreUpdate e) {
		this.setSuffix(this.mode.getValue());
		if (this.bob.getValue()) {
			mc.thePlayer.cameraYaw = 0.090909086f;
		}

		double speed = Math.max(3.0f, getBaseMoveSpeed());
		if (this.mode.getValue() == FlyMode.Motion) {
			mc.thePlayer.onGround = false;
			e.setOnground(isOnGround(0.001));
			if (this.mc.thePlayer.isMoving()) {
				if (this.mc.thePlayer.hurtTime > 15) {
					e.setCancelled(false);
				} else {
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							e.setCancelled(true);
							this.cancel();
						}
					}, 10L);
				}
			} else {
				e.setCancelled(false);
				this.mc.thePlayer.motionX = 0;
				this.mc.thePlayer.motionY = 0;
				this.mc.thePlayer.motionZ = 0;
			}
			if (mc.thePlayer.movementInput.jump) {
				mc.thePlayer.motionY = speed * 0.6;
			} else if (mc.thePlayer.movementInput.sneak) {
				mc.thePlayer.motionY = -speed * 0.6;
			} else {
				mc.thePlayer.motionY = 0;
			}
			updateFlyHeight();
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
		} else if (this.mode.getValue() == FlyMode.Vanilla) {
			this.mc.thePlayer.motionY = this.mc.thePlayer.movementInput.jump ? 1.0
					: (this.mc.thePlayer.movementInput.sneak ? -1.0 : 0.0);
			if (this.mc.thePlayer.isMoving()) {
				this.mc.thePlayer.setSpeed(3.75);
			} else {
				this.mc.thePlayer.setSpeed(0.0);
			}
			mc.thePlayer.capabilities.allowFlying = true;
		} else if (this.mode.getValue() == FlyMode.HypixelDamage) {

			if (!damaged)
				return;
			if (BoostTimer.hasReached(zoomboost.getValue() * 1000) || !fast) {
				mc.timer.timerSpeed = 1.0f;
			} else {
				mc.timer.timerSpeed = 1.0f + timerboost.getValue().floatValue();
			}
			if (SlowTimer.hasReached(333) && !fast && DoubleSpeed.getValue()) {
				boostHypixelState = 1;
				moveSpeed = 0.1D;
				lastDistance = 0D;
				failedStart = false;
				fast = true;
				BoostTimer.reset();
			}
//			latestHypixelTimer.update();
//
//			if (latestHypixelTimer.hasTimePassed(2)) {
//				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-5, mc.thePlayer.posZ);
//				latestHypixelTimer.reset();
//			}

			if (!failedStart)
				mc.thePlayer.motionY = 0D;
		} else if (this.mode.getValue() == FlyMode.Float) {
			if (!MoveUtils.isOnGround(0.001)) {
				mc.thePlayer.motionY = 0.0049D;
				++this.stage;
				switch (this.stage) {
				case 1:
					RandomValue = 1.0 * new Random().nextDouble() / 1000000000.0;
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomValue, mc.thePlayer.posZ);
					break;
				case 2:
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - RandomValue, mc.thePlayer.posZ);
					break;
				case 3:
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomValue, mc.thePlayer.posZ);
					this.stage = 0;
				}
			}

			// PlayerUtil.setSpeed(PlayerUtil.getSpeed());
		}
	}

	@EventHandler
	public void onEvent(EventPostUpdate e) {
		if (this.mode.getValue() == FlyMode.HypixelDamage) {
			if (!damaged)
				return;

//			RandomValue = random.nextDouble(0.00000000000001235423512348964562,
//					0.00000000000001235423512348964562 * 10);
			// mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomValue,
			// mc.thePlayer.posZ);
//			latestHypixelTimer.update();
//
//			if (latestHypixelTimer.hasTimePassed(2)) {
//				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomValue, mc.thePlayer.posZ);
//				latestHypixelTimer.reset();
//			}

			++this.stage;
			switch (this.stage) {
			case 1:
				RandomValue = getRandomDouble(random.nextInt(0, 5)) / 100000000.0
						/ getRandomDouble(random.nextInt(0, 5));
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomValue, mc.thePlayer.posZ);
				break;
			case 2:
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomValue, mc.thePlayer.posZ);
				break;
			case 3:
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - RandomValue, mc.thePlayer.posZ);
				break;
			case 4:
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - RandomValue, mc.thePlayer.posZ);
				this.stage = 0;
				break;
			}

			// TODO posY
			// System.out.println(mc.thePlayer.posY);

			double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
		}
		if (this.dragonvalue.getValue().booleanValue() && this.dragoncrea != true) {
			this.dragoncrea = true;
			dragon = new EntityDragon(mc.theWorld);
			mc.theWorld.addEntityToWorld(666, dragon);
			mc.thePlayer.ridingEntity = dragon;
		} else if (this.dragonvalue.getValue().booleanValue()) {
			final double posX4 = mc.thePlayer.posX;
			final double posX = posX4 - MathHelper.cos(mc.thePlayer.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
			final double posY = mc.thePlayer.posY;
			final double posZ2 = mc.thePlayer.posZ;
			final double posZ = posZ2 - MathHelper.sin(mc.thePlayer.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
			final float f = 0.4f;
			final float n2 = -MathHelper.sin(mc.thePlayer.rotationYaw / 180.0f * 3.1415927f);
			final double motionX = n2 * MathHelper.cos(mc.thePlayer.rotationPitch / 180.0f * 3.1415927f) * f;
			final float cos = MathHelper.cos(mc.thePlayer.rotationYaw / 180.0f * 3.1415927f);
			final double motionZ = cos * MathHelper.cos(mc.thePlayer.rotationPitch / 180.0f * 3.1415927f) * f;
			final double motionY = -MathHelper.sin((mc.thePlayer.rotationPitch + 2.0f) / 180.0f * 3.1415927f) * f;
			final double xCoord = posX + motionX;
			final double yCoord = posY + motionY;
			final double zCoord = posZ + motionZ;
			dragon.rotationPitch = mc.thePlayer.rotationPitch;
			dragon.rotationYaw = mc.thePlayer.rotationYawHead - 180;
			dragon.setRotationYawHead(mc.thePlayer.rotationYawHead);
			dragon.setPosition(xCoord, mc.thePlayer.posY - 2, zCoord);
		}
		if (this.par.getValue().booleanValue()) {
			final double posX4 = mc.thePlayer.posX;
			final double posX = posX4 - MathHelper.cos(mc.thePlayer.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
			final double posY = mc.thePlayer.posY;
			final double posZ2 = mc.thePlayer.posZ;
			final double posZ = posZ2 - MathHelper.sin(mc.thePlayer.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
			final float f = 0.4f;
			final float n2 = -MathHelper.sin(mc.thePlayer.rotationYaw / 180.0f * 3.1415927f);
			final double motionX = n2 * MathHelper.cos(mc.thePlayer.rotationPitch / 180.0f * 3.1415927f) * f;
			final float cos = MathHelper.cos(mc.thePlayer.rotationYaw / 180.0f * 3.1415927f);
			final double motionZ = cos * MathHelper.cos(mc.thePlayer.rotationPitch / 180.0f * 3.1415927f) * f;
			final double motionY = -MathHelper.sin((mc.thePlayer.rotationPitch + 2.0f) / 180.0f * 3.1415927f) * f;
			for (int i = 0; i < 90; ++i) {
				final WorldClient theWorld = this.mc.theWorld;
				final EnumParticleTypes particleType = (i % 4 == 0) ? (EnumParticleTypes.CRIT_MAGIC)
						: (new Random().nextBoolean() ? EnumParticleTypes.HEART : EnumParticleTypes.ENCHANTMENT_TABLE);
				final double xCoord = posX + motionX;
				final double yCoord = posY + motionY;
				final double zCoord = posZ + motionZ;
				final double motionX2 = mc.thePlayer.motionX;
				final double motionY2 = mc.thePlayer.motionY;
				theWorld.spawnParticle(particleType, xCoord, yCoord, zCoord, motionX2, motionY2, mc.thePlayer.motionZ,
						new int[0]);
			}
		}
	}

	@EventHandler
	private void onMove(EventMove e) {

		if (this.mode.getValue() == FlyMode.HypixelDamage) {
			if (!damaged)
				return;
			if (!MoveUtils.isMoving()) {
				e.setX(0D);
				e.setZ(0D);
				return;
			}

			if (failedStart)
				return;

			final double amplifier = 1 + (mc.thePlayer.isPotionActive(Potion.moveSpeed)
					? 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1)
					: 0);
			final double baseSpeed = 0.29D * amplifier;

			switch (boostHypixelState) {
			case 1:
				moveSpeed = (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56 : 2.034) * baseSpeed;
				boostHypixelState = 2;
				break;
			case 2:
				moveSpeed *= DoubleSpeed.getValue() ? (fast ? groundboost.getValue() : 1.5) : groundboost.getValue();
				boostHypixelState = 3;
				break;
			case 3:
				moveSpeed = lastDistance
						- (mc.thePlayer.ticksExisted % 2 == 0 ? 0.0103D : 0.0123D) * (lastDistance - baseSpeed);

				boostHypixelState = 4;
				break;
			default:
				moveSpeed = lastDistance - lastDistance / 159.8D;
				break;
			}

			moveSpeed = Math.max(moveSpeed, 0.3D);

			final double yaw = MoveUtils.getDirection();
			e.setX(-Math.sin(yaw) * moveSpeed);
			e.setZ(Math.cos(yaw) * moveSpeed);
			mc.thePlayer.motionX = e.getX();
			mc.thePlayer.motionZ = e.getZ();
		} else if (this.mode.getValue() == FlyMode.Motion) {
			float forward = MovementInput.moveForward;
			float strafe = MovementInput.moveStrafe;
			float yaw = mc.thePlayer.rotationYaw;
			if (forward == 0.0F && strafe == 0.0F) {
				e.x = 0.0D;
				e.z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (float) (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (float) (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}

				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			mc.thePlayer.onGround = false;
			if (this.mc.thePlayer.isMoving()) {
				this.mc.thePlayer.motionY = (-3) / 6.0;
				moveSpeed = 3.75D;
			} else {
				moveSpeed = 0.0D;
			}
			moveSpeed = Math.max(moveSpeed, MathUtil.getBaseMovementSpeed());
			this.mc.thePlayer.setMoveSpeed(e, moveSpeed);
		}
	}

	double getBaseMoveSpeed() {
		double baseSpeed = 0.275;
		if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
		}
		return baseSpeed;
	}

	public static enum FlyMode {
		Vanilla, Motion, HypixelDamage, Float;
	}

	public static enum DamageMode {
		Semi, Semi2,  RandomFloat, Basic, posLook, FakeGround;
	}
}
