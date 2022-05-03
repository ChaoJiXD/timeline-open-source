package NovClient.Module.Modules.Misc;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPacketSend;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.Manager.FriendManager;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.MovingObjectPosition;

import java.awt.Color;

import org.lwjgl.input.Mouse;

public class ChatBypass extends Module {
	private boolean down;

	public ChatBypass() {
		super("ChatBypass", new String[] { "ChatBypass", "ChatBypass" }, ModuleType.Misc);
		this.setColor(new Color(241, 175, 67).getRGB());
	}

	@EventHandler
	private void onPacket(EventPacketSend e) {
        if(e.getPacket() instanceof C01PacketChatMessage) {
            final C01PacketChatMessage chatMessage = (C01PacketChatMessage) e.getPacket();

            final String message = chatMessage.getMessage();
            final StringBuilder stringBuilder = new StringBuilder();

            for(char c : message.toCharArray())
                if(c >= 33 && c <= 128)
                    stringBuilder.append(Character.toChars(c + 65248));
                else
                    stringBuilder.append(c);

            chatMessage.message = stringBuilder.toString();
        }
	}
}
