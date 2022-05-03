/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Misc;

import java.awt.Color;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import NovClient.API.Events.World.EventPacketReceive;
import NovClient.API.Events.World.EventPacketSend;
import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventTick;
import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class WorldTime
extends Module {
	private Numbers<Double> Time = new Numbers<Double>("Time", "Time", 18000.0, 0.0, 24000.0, 1.0);
    public WorldTime() {
        super("WorldTime", new String[]{"WorldTime", "WorldTime"}, ModuleType.Misc);
        this.setColor(new Color(198, 253, 191).getRGB());
        super.addValues(Time);
        
    }

    @Override
    public void onEnable() {

        super.onEnable();
    }
	@EventHandler
	public void EventPacketSend(EventPacketReceive e) {
		if (e.getPacket() instanceof S03PacketTimeUpdate) {
			e.setCancelled(true);
		}
	}
    @EventHandler
    public void onTick(EventTick event) {
    	mc.theWorld.setWorldTime(Time.getValue().longValue());
    }



}

