package NovClient.Module.Modules.Move;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPacketReceive;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.TimerUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class AntiFreeze extends Module {
	boolean Laging = false;
	float Yaw = 0;
	float Pitch = 0;
	private static Numbers<Double> Time = new Numbers<Double>("Time", "Time", 1.0, 1.0, 5.0, 1.0);
	public AntiFreeze() {
		super("AntiFreeze", new String[] { "AntiFreeze" }, ModuleType.Move);
		super.addValues(Time);
	}
	TimerUtil timer = new TimerUtil();
	public void onEnable() {
		Laging = false;
		Yaw = 0;
		Pitch = 0;
	}
	@EventHandler
	public void onUpdate(EventPreUpdate e)
	{
		if(!timer.hasReached(Time.getValue() * 1000) && Laging)
		{
			mc.thePlayer.motionX = 0.0d;
			if(mc.thePlayer.motionX == 0.0d)mc.thePlayer.motionX = 0.0d;
			mc.thePlayer.motionX = 0.0d;
			mc.thePlayer.rotationYaw = Yaw;
			mc.thePlayer.rotationYaw = Pitch;
		}else
		{
			Laging = false;
		}
	}
	@EventHandler
	private void onPacket(EventPacketReceive ep) {
			if (ep.getPacket() instanceof S08PacketPlayerPosLook) {
				timer.reset();
				Laging = true;
				S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
				Yaw = pac.getYaw();
				Pitch = pac.getPitch();
			}
	}


}
