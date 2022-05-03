package NovClient.Module.Modules.Move;


import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPacketReceive;
import NovClient.API.Events.World.EventPacketSend;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;
import NovClient.Command.Commands.Help;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.*;
import NovClient.Util.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Disabler extends Module {
	private Mode<Enum> mode = new Mode("Mode", "mode", DisablerMode.values(), DisablerMode.BlocksMC);
	Queue<Packet> packet = new ConcurrentLinkedQueue<>();
	public Disabler() {
		
		super("Disabler", new String[] {"autoyool"},ModuleType.Move);
		addValues(this.mode );
    }
    TimerUtil timer = new TimerUtil();
	boolean teleported;
    @EventHandler
	public void onUpdate(EventPreUpdate e){
		if (timer.hasReached(490L)) {
			timer.reset();

			if (!packet.isEmpty()) {
				mc.getNetHandler().sendPacketNoEvent(packet.poll());
			}
		}
	}

	boolean verus2Stat;

	@EventHandler
	public void onPacket(EventPacketSend event){
		if (mc.thePlayer.ticksExisted < 60 ) {
			packet.clear();
			teleported = false;
			return;
		}
		Packet p = event.getPacket();
//		if (p instanceof C0BPacketEntityAction) {
//			final C0BPacketEntityAction c0B = (C0BPacketEntityAction) p;
//
//			if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
//				if (EntityPlayerSP.serverSprintState) {
//					mc.getNetHandler().sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
//					EntityPlayerSP.serverSprintState = false;
//				}
//				event.setCancelled(true);
//			}
//
//			if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
//				event.setCancelled(true);
//			}
//		}

//		if (p instanceof C00PacketKeepAlive || p instanceof C0FPacketConfirmTransaction) {
//			packet.add(p);
//			event.setCancelled(true);
//
//			if (packet.size() > 300) {
//				if(!verus2Stat) {
//					verus2Stat = true;
//				}
//				mc.getNetHandler().sendPacketNoEvent(packet.poll());
//			}
//		}

		if (p instanceof C03PacketPlayer) {
			final C03PacketPlayer c03 = (C03PacketPlayer) p;

			if (mc.thePlayer.ticksExisted % 20 == 0) {
				mc.getNetHandler().sendPacketNoEvent(new C18PacketSpectate(UUID.randomUUID()));
				mc.getNetHandler().sendPacketNoEvent(new C0CPacketInput(0.98F, 0.98F, false, false));
			}

			if (mc.thePlayer.ticksExisted % 120 == 0) {
				c03.onGround = (false);
				// 1 / 64
				// Math ground
				c03.y = (-0.015625);
				teleported = true;
			}
		}

		if (p instanceof S08PacketPlayerPosLook && !teleported) {
			final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;

			final ArrayList<Vec31> path = MainPathFinder.computePath(new Vec31(packet.x, packet.y, packet.z), new Vec31(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));

			mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, true));

			for (final Vec31 vec : path)
				mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(vec.getX(), vec.getY(), vec.getZ(), packet.yaw, packet.pitch, true));

			event.setCancelled(true);
			teleported = false;
		}
	}

	static enum DisablerMode {
		BlocksMC
	}

}