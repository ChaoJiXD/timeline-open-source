package NovClient.Module.Modules.Combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.vecmath.Vector2f;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Misc.EventRotation;
import NovClient.API.Events.World.EventPostUpdate;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Manager.FriendManager;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Misc.Teams;
import NovClient.Module.Modules.Move.Scaffold;
import NovClient.Util.TimerUtil;
import NovClient.Util.Math.RotationUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class KillAura extends Module {
public Mode mode = new Mode("Mode", "Mode", (Enum[]) RotationMode.values(), (Enum) RotationMode.Basic);
public Mode attackmode = new Mode("attackmode", "attackmode", (Enum[]) typeMode.values(), (Enum) typeMode.Pre);
public Mode blockmode = new Mode("blockmode", "blockmode", (Enum[]) typeMode.values(), (Enum) typeMode.Post);
	private Numbers<Double> MaxCPS = new Numbers<Double>("MaxCPS", "MaxCPS", Double.valueOf(17.0D),
			Double.valueOf(1.0D), Double.valueOf(20.0D), Double.valueOf(1D));
	private Numbers<Double> MinCPS = new Numbers<Double>("MinCPS", "MinCPS", Double.valueOf(6.0D), Double.valueOf(1.0D),
			Double.valueOf(20.0D), Double.valueOf(1D));
	private Numbers<Double> reach = new Numbers<Double>("Reach", "Reach", Double.valueOf(4.5D), Double.valueOf(1.0D),
			Double.valueOf(8.0D), Double.valueOf(0.1D));
	private Option<Boolean> blocking = new Option("Autoblock", "Autoblock", Boolean.valueOf(true));
	private Option<Boolean> players = new Option("Players", "Players", Boolean.valueOf(true));
	private Option<Boolean> animals = new Option("Animals", "Animals", Boolean.valueOf(false));
	private Option<Boolean> mobs = new Option("Mobs", "Mobs", Boolean.valueOf(false));
	private Option<Boolean> other = new Option("Other", "Other", Boolean.valueOf(false));

	private Option<Boolean> invis = new Option("Invisibles", "Invisibles", Boolean.valueOf(false));
	private Option<Boolean> Sort = new Option("Sort", "Sort", Boolean.valueOf(true));
	
	private Numbers<Double> BlockReach = new Numbers("BlockReach", "BlockReach", Double.valueOf(4.9D),
			Double.valueOf(0.0D), Double.valueOf(8.0D), Double.valueOf(0.1D));
	public static Numbers<Double> Existed = new Numbers<Double>("Existed", "Existed", 30.0, 0.0, 100.0, 1.0);
	private List<EntityLivingBase> targets = new ArrayList(0);
	List<EntityLivingBase> BlockTargets = new ArrayList(0);
	private boolean isBlocking;
	private boolean isAttacking;
	public static Numbers<Double> ThroughWallReach = new Numbers<Double>("ThroughWallReach", "ThroughWallReach", 3.0,
			0.0, 8.0, 0.1);
	public static EntityLivingBase curTarget;
	private TimerUtil timer = new TimerUtil();

	private Vector2f lastAngles = new Vector2f(0.0F, 0.0F);
	private boolean inBlockReach;
	public static List<EntityLivingBase> Attacked = new ArrayList();
	public static EntityLivingBase blockTarget;

	public KillAura() {
		super("KillAura", new String[] { "KillAura" }, ModuleType.Combat);
		super.addValues(MinCPS, MaxCPS, reach, BlockReach, ThroughWallReach, blocking, players, animals, mobs,
				other, invis, Existed, Sort, mode,attackmode,blockmode);
	}

	private List<EntityLivingBase> getTargets(double dis) {
		return (List) mc.theWorld.loadedEntityList.stream()
				.filter(e2 -> (mc.thePlayer.getDistanceToEntity(e2) <= dis && Check(e2))).collect(Collectors.toList());
	}

	@Override
	public void onDisable() {
		Client.Yaw = 0.0f;
		Client.Pitch = 0.0f;
		isAttacking = false;
		curTarget = null;
		blockTarget = null;
		if (mc.thePlayer.isBlocking() && blocking.getValue()) {
			stopBlocking();
		}
	}

	@Override
	public void onEnable() {
		Client.Yaw = 0.0f;
		Client.Pitch = 0.0f;
		isAttacking = false;
		curTarget = null;
		blockTarget = null;
	}

	@EventHandler
	public void onPreUpdate(EventPreUpdate e) {
		blockTarget = null;
		curTarget = null;
		if (Scaffold.auracheck.getValue()
				&& Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled()) {
			if (isBlocking) {
				inBlockReach = false;
				stopBlocking();
			}
			return;
		}
		// 搭路检测
		if(blockmode.getValue() == typeMode.Pre) {
			BlockTargets = getTargets(BlockReach.getValue());
			if (Sort.getValue())
				BlockTargets.sort(
						(o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
			if (!BlockTargets.isEmpty())
				blockTarget = BlockTargets.get(0);
			if (blocking.getValue())
				if (!BlockTargets.isEmpty() && !isAttacking && canBlock()) {
					//System.out.println("111");
					inBlockReach = true;
					startBlocking();
				}
			if (BlockTargets.isEmpty() && !isAttacking && isBlocking) {
				inBlockReach = false;
				stopBlocking();
			}
		}
		// 格挡
		targets = getTargets(reach.getValue());

		if (!targets.isEmpty()) {
			if (Sort.getValue())
				targets.sort((o1,
						o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
			curTarget = targets.get(0);
			blockTarget = curTarget;
		}
		if (BlockTargets.isEmpty())
			return;
		// 获取target
	    float[] rots = null;
		switch (mode.getValue().toString()) {
		case "Basic":
			if(blockTarget == null)return;
			rots = RotationUtil.rotate(blockTarget);
			Client.Yaw = rots[0];
			Client.Pitch = rots[1];
			e.setYaw(rots[0]);
			e.setPitch(rots[1]);
			break;
		case "Random":
			if(blockTarget == null)return;
			rots = RotationUtil.rotate(blockTarget);
			int r1 =this.randomNumber(-13, 13);
			int r2 =this.randomNumber(-3, 3);
			Client.Yaw = rots[0]+r1;
			Client.Pitch = rots[1]+r2;
			//System.out.println(111);
			e.setYaw(rots[0]+r1);
			e.setPitch(rots[1]+r2);
			break;
		}
		Client.RenderRotate(Client.Yaw);
		//System.out.println(blockTarget.hurtResistantTime);
		// 转头
		if(attackmode.getValue() == typeMode.Pre)
		if (curTarget != null && shouldAttack()) {
			Criticals Crit = (Criticals) Client.instance.getModuleManager().getModuleByClass(Criticals.class);
			Crit.autoCrit(curTarget);
			// Criticals
			attack();
			// attack
		}
	}

	@EventHandler
	public void onPostUpdate(EventPreUpdate e) {
		if (Scaffold.auracheck.getValue()
				&& Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled()) {
			if (isBlocking) {
				inBlockReach = false;
				stopBlocking();
			}
			return;
		}
		// 搭路检测
		if(attackmode.getValue() == typeMode.Post)
		if (curTarget != null && shouldAttack()) {
			Criticals Crit = (Criticals) Client.instance.getModuleManager().getModuleByClass(Criticals.class);
			Crit.autoCrit(curTarget);
			// Criticals
			attack();
			// attack
		}
		if(blockmode.getValue() == typeMode.Post) {
			BlockTargets = getTargets(BlockReach.getValue());
			if (Sort.getValue())
				BlockTargets.sort(
						(o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
			if (!BlockTargets.isEmpty())
				blockTarget = BlockTargets.get(0);
			if (blocking.getValue())
				if (!BlockTargets.isEmpty() && !isAttacking && canBlock()) {
					//System.out.println("111");
					inBlockReach = true;
					//startBlocking();
					mc.thePlayer.sendQueue.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255, mc.thePlayer.getItemInUse(), 0 ,0 ,0));

				}
			if (BlockTargets.isEmpty() && !isAttacking && isBlocking) {
				inBlockReach = false;
				//stopBlocking();
			}
		}
		// 格挡

	}

	private final int randomNumber(int var1, int var2) {
		return (int) (Math.random() * (double) (var1 - var2)) + var2;
	}

	private boolean shouldAttack() {
		int APS = 20 / this.randomNumber(MaxCPS.getValue().intValue(), MinCPS.getValue().intValue());
		if (this.timer.hasReached((int) (50 * APS))) {
			this.timer.reset();
			return true;
		}
		return false;
	}

	private boolean Check(Entity e2) {
		if (e2.ticksExisted <= Existed.getValue().intValue())
			return false;
		if (!RotationUtil.canEntityBeSeen(e2)
				&& mc.thePlayer.getDistanceToEntity(e2) > this.ThroughWallReach.getValue()+0.5f)
			return false;
		if (e2.isInvisible() && !this.invis.getValue())
			return false;
		if (!e2.isEntityAlive())
			return false;
		if (e2 == mc.thePlayer)
			return false;
		if ((e2 instanceof EntityMob || e2 instanceof EntityGhast || e2 instanceof EntityGolem
				|| e2 instanceof EntityDragon || e2 instanceof EntitySlime) && this.mobs.getValue())
			return true;
		if ((e2 instanceof EntitySquid || e2 instanceof EntityBat || e2 instanceof EntityVillager)
				&& this.other.getValue())
			return true;
		if (e2 instanceof EntityAnimal && this.animals.getValue())
			return true;
		AntiBot ab2 = (AntiBot) Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
		if (ab2.isServerBot(e2))
			return false;
		if (FriendManager.isFriend(e2.getName()))
			return false;
		if (e2 instanceof EntityPlayer && this.players.getValue() && !Teams.isOnSameTeam(e2))
			return true;

		return false;
	}

	private void attack() {

		if (mc.thePlayer.getDistanceToEntity(blockTarget) > this.reach.getValue()+0.5f) {
			return;
		}
		if(!Attacked.contains(blockTarget))Attacked.add(blockTarget);
		isAttacking = true;
		if (mc.thePlayer.isBlocking() && canBlock() && inBlockReach) {
			stopBlocking();
		}

		mc.playerController.syncCurrentPlayItem();
		mc.thePlayer.swingItem();

		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(blockTarget, C02PacketUseEntity.Action.ATTACK));

		mc.thePlayer.attackTargetEntityWithCurrentItem(blockTarget);

		if (!mc.thePlayer.isBlocking() && blocking.getValue() && canBlock() && inBlockReach) {
			startBlocking();

		}
		isAttacking = false;

	}

	private boolean canBlock() {
		if (mc.thePlayer.getCurrentEquippedItem() != null
				&& mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword)
			return true;
		return false;
	}

	private void startBlocking() {
		isBlocking = true;
		this.mc.gameSettings.keyBindUseItem.Doing = true;
		if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem()))
			this.mc.getItemRenderer().resetEquippedProgress2();
	}

	private void stopBlocking() {
		isBlocking = false;
		this.mc.gameSettings.keyBindUseItem.Doing = false;
		mc.playerController.onStoppedUsingItem(mc.thePlayer);

	}

	public static enum RotationMode {
		Basic, Random;
	}
	public static enum typeMode {
		Pre, Post;
	}
}
