package NovClient.Module.Modules.Misc;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.Manager.FriendManager;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

import java.awt.Color;

import org.lwjgl.input.Mouse;

public class MCF extends Module {
	private boolean down;

	public MCF() {
		super("MCF", new String[] { "middleclickfriends", "middleclick" }, ModuleType.Misc);
		this.setColor(new Color(241, 175, 67).getRGB());
	}

	@EventHandler
	private void onClick(EventPreUpdate e) {
		if (Mouse.isButtonDown((int) 2) && !this.down) {
			if (this.mc.objectMouseOver.entityHit != null) {
				EntityPlayer player = (EntityPlayer) this.mc.objectMouseOver.entityHit;
				String playername = player.getName();
				if (!FriendManager.isFriend(playername)) {
					this.mc.thePlayer.sendChatMessage(".f add " + playername);
				} else {
					this.mc.thePlayer.sendChatMessage(".f del " + playername);
				}
			}
			this.down = true;
		}
		if (!Mouse.isButtonDown((int) 2)) {
			this.down = false;
		}
	}
}
