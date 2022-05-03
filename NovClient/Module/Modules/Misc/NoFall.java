package NovClient.Module.Modules.Misc;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Move.AntiFall;
import NovClient.Util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;

import java.awt.Color;

public class NoFall extends Module {
	public Mode blockmode = new Mode("blockmode", "blockmode", (Enum[]) NoFallMode.values(), (Enum) NoFallMode.NCP);

	public NoFall() {
		super("NoFall", new String[] { "Nofalldamage" }, ModuleType.Misc);
		this.setColor(new Color(242, 137, 73).getRGB());
		super.addValues(blockmode);
	}

	float lastFall;
	int times;
	boolean showed;
	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		super.setSuffix(blockmode.getValue());
		NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
		Double curX = mc.thePlayer.posX;
		Double curY = mc.thePlayer.posY;
		Double curZ = mc.thePlayer.posZ;
		if (blockmode.getValue() == NoFallMode.NCP) {
			
			if (times < 5) {
				System.out.println(times);
				if (mc.thePlayer.fallDistance - lastFall >= 3.0f && isBlockUnder()) {
					lastFall = mc.thePlayer.fallDistance;
					var1.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(curX, curY, curZ,
							mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
					// e.setOnground(true);
					Helper.sendMessage("nofall packet");
					++times;
				} else if (mc.thePlayer.isCollidedVertically) {
					lastFall = 0.0f;
					times = 0;
					showed = false;
				}
			}
			else if(!showed) {
				Helper.sendMessage(EnumChatFormatting.DARK_RED +"警告："+EnumChatFormatting.YELLOW +"NoFall已停止工作，操你妈再继续下去你号没了我告诉你，三秒之内杀了你，骨灰都给你扬咯。");
				showed = true;
			} 
		} else if (blockmode.getValue() == NoFallMode.Hypixel) {
			if(mc.thePlayer.fallDistance >3)
				e.setOnground(true);
		}

	}

	public static boolean isBlockUnder() {
		if (mc.thePlayer.posY < 0.0D)
			return false;
		for (int off = 0; off < (int) mc.thePlayer.posY + 2; off += 2) {
			AxisAlignedBB bb = mc.thePlayer.boundingBox.offset(0.0D, -off, 0.0D);
			if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty())
				return true;
		}
		return false;
	}

	public enum NoFallMode {
		Hypixel, NCP

	}
}
