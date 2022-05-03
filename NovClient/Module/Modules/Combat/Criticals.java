/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


import org.apache.commons.lang3.RandomUtils;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventAttack;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Move.Fly;
import NovClient.Module.Modules.Move.LongJump;
import NovClient.Module.Modules.Move.Scaffold;
import NovClient.Module.Modules.Move.Speed;
import NovClient.Util.Helper;
import NovClient.Util.MoveUtils;
import NovClient.Util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

public class Criticals extends Module {
	public Mode mode = new Mode("Mode", "mode", (Enum[]) CritMode.values(), (Enum) CritMode.Packet);
	private TimerUtil timer = new TimerUtil();
	//private Numbers<Double> level = new Numbers("HurtTime", "HurtTime", Double.valueOf(0.0D), Double.valueOf(0.0D),
	//		Double.valueOf(20.0D), Double.valueOf(1.0D));
	private Numbers<Double> HurtTime = new Numbers("HurtTime", "HurtTime", Double.valueOf(17.0D), Double.valueOf(1.0D),
			Double.valueOf(20.0D), Double.valueOf(1.0D));
	public static Option<Boolean> StopSpeed = new Option("StopSpeed", "StopSpeed", Boolean.valueOf(true));
	public static Option<Boolean> Always = new Option("Always", "Always", Boolean.valueOf(true));
	private static Numbers<Double> Delay = new Numbers<Double>("Delay", "Delay", 333.0, 0.0, 1000.0, 1.0);
	public static Option<Boolean> C06 = new Option("C06", "C06", Boolean.valueOf(true));
	public static Option<Boolean> Random = new Option("Random", "Random", Boolean.valueOf(true));
	private static Random random = new Random();
	

	int stage, count;
	double y;
	private int groundTicks;

	public Criticals() {
		super("Criticals", new String[] { "Criticals", "crit" }, ModuleType.Combat);
		this.setColor(new Color(235, 194, 138).getRGB());
		this.addValues(this.mode, this.StopSpeed, HurtTime,this.Delay, this.Always,C06,Random);
	}

	private boolean canCrit(EntityLivingBase e) {
		EntityLivingBase target = e;
		return ( !Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled() && !Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() && target.hurtResistantTime <= HurtTime.getValue() && target.hurtResistantTime > 0 && mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && MoveUtils.isOnGround(0.001) && !Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled() && !Client.instance.getModuleManager().getModuleByClass(LongJump.class).isEnabled() && !Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled())  || this.Always.getValue();
	}
	@EventHandler
	private void onAttack(EventAttack e10) {
		if(e10.isPreAttack())
		{
			//System.out.println("111");
			autoCrit((EntityLivingBase)e10.getEntity());
		}
	}
	@EventHandler
	private void onPacket(EventPreUpdate e10) {
		super.setSuffix(mode.getValue());
		//mc.thePlayer.addPotionEffect(new PotionEffect(Potion.jump.getId(),100,level.getValue().intValue()-1));
		
	}
	@Override
	public void onDisable() {
		//mc.thePlayer.removePotionEffect(Potion.jump.getId());
	}
	
	boolean autoCrit(EntityLivingBase e) {
		if (this.isEnabled() == false)
			return false;
		mc.thePlayer.onCriticalHit(e);
		if(!(e instanceof EntityPlayer))return false;
		
		if (canCrit(e)) {
			if (this.timer.hasReached(Delay.getValue())) {
				this.timer.reset();
				switch(mode.getValue().toString())
				{
				case "Cracking":
					break;
				case "Edit":
					Crit(new Double[] { 0.0211521, 0.1239810, 0.0005123});
					break;
				case "Packet":
					Crit(new Double[] { 0.625, -RandomUtils.nextDouble(0.0, 0.625) });
					break;
				case "HVH":
					Crit2(new Double[] { 0.06250999867916107D, -9.999999747378752E-6D, 0.0010999999940395355D});
					break;
				case "Packet2":
					Crit2(new Double[] {0.419999986886978,0.3331999936342235 });
					break;
				case "Hypixel":
					Crit2(new Double[] {0.001, 0.02114514191981020070505, 0.0200705055201314});
					break;
				}

				return true;
			}
		}
		return false;

	}

	public static void Crit2(Double[] value) {
		NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
		Double curX = mc.thePlayer.posX;
		Double curY = mc.thePlayer.posY;
		Double curZ = mc.thePlayer.posZ;
		for (Double offset : value) {
			if (!C06.getValue()) {
				var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY+offset, curZ, false));
			} else {
				var1.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(curX, curY+offset, curZ,mc.thePlayer.rotationYaw,mc.thePlayer.rotationPitch, false));
			}
		}
	}
	public static void Crit(Double[] value) {
		NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
		Double curX = mc.thePlayer.posX;
		Double curY = mc.thePlayer.posY;
		Double curZ = mc.thePlayer.posZ;
		Double RandomY = 0.0;
		for (Double offset : value) {
			if(Random.getValue())RandomY = RandomUtils.nextDouble(0.0, 99.0)/1000000000000000.0;
			if(random.nextBoolean())RandomY = -RandomY;
			curY += offset;
			// Printer.print(String.valueOf(curY));
			if (!C06.getValue()) {
				var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, false));
			} else {
				var1.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(curX, curY, curZ,mc.thePlayer.rotationYaw,mc.thePlayer.rotationPitch, false));
			}
		}
	}
	public static void CritAppointRandom(Double[] value) {
		NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
		Double curX = mc.thePlayer.posX;
		Double curY = mc.thePlayer.posY;
		Double curZ = mc.thePlayer.posZ;
		Double RandomY = 0.0;
		if(Random.getValue())RandomY = RandomUtils.nextDouble(0.0, 9999999999.0)/1000000000000000.0;
		if(random.nextBoolean())RandomY = -RandomY;
		for (Double offset : value) {
			curY += offset;
			
			// Printer.print(String.valueOf(curY));
			if (!C06.getValue()) {
				var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY+RandomY, curZ, false));
			} else {
				var1.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(curX, curY+RandomY, curZ,mc.thePlayer.rotationYaw,mc.thePlayer.rotationPitch, false));
			}
		}
	}

	static enum CritMode {
		Packet, Packet2, Edit, Cracking, Hypixel, HVH,
	}

}
