/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Misc;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPacketSend;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.ChatComponentText;


public class PingSpoof
extends Module {
    private List<Packet> packetList = new CopyOnWriteArrayList<Packet>();
    private TimerUtil Packettimer = new TimerUtil();

    public PingSpoof() {
        super("PingSpoof", new String[]{"PingSpoof", "ping"}, ModuleType.Misc);
        this.setColor(new Color(117, 52, 203).getRGB());
        super.addValues(Delay);
    }


	private static Numbers<Double> Delay = new Numbers<Double>("Delay", "Delay", 1000.0, 0.0, 5000.0, 1.0);

    


    private final HashMap<Packet<?>, Long> packetsMap = new HashMap<>();


    public void onDisable() {
        packetsMap.clear();
        super.onDisable();
    }

	@EventHandler
    public void onPacket(final EventPacketSend event) {
        final Packet packet = event.getPacket();

        if ((packet instanceof C00PacketKeepAlive || packet instanceof C16PacketClientStatus) && !(mc.thePlayer.isDead || mc.thePlayer.getHealth() <= 0) && !packetsMap.containsKey(packet)) {
            event.setCancelled(true);

            synchronized(packetsMap) {
                packetsMap.put(packet, System.currentTimeMillis() + Delay.getValue().longValue());
            }
        }
    }

	@EventHandler
    public void onUpdate(final EventPreUpdate event) {
        try {
            synchronized(packetsMap) {
                for(final Iterator<Map.Entry<Packet<?>, Long>> iterator = packetsMap.entrySet().iterator(); iterator.hasNext(); ) {
                    final Map.Entry<Packet<?>, Long> entry = iterator.next();

                    if(entry.getValue() < System.currentTimeMillis()) {
                        mc.getNetHandler().addToSendQueue(entry.getKey());
                        iterator.remove();
                    }
                }
            }
        }catch(final Throwable t) {
            t.printStackTrace();
        }
    }
}

