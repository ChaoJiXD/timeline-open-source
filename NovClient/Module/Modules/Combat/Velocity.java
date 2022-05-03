/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Combat;

import java.awt.Color;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPacketReceive;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Move.Fly;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity
extends Module {
    private Numbers<Double> Vertical = new Numbers<Double>("Vertical", "Vertical", 0.0, 0.0, 100.0, 5.0);
    private Numbers<Double> Horizontal = new Numbers<Double>("Horizontal", "Horizontal", 0.0, 0.0, 100.0, 5.0);
    private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])ThisMode.values(), (Enum)ThisMode.Normal);
    
    
    public Velocity() {
        super("Velocity", new String[]{"AntiVelocity", "antiknockback", "antikb"}, ModuleType.Combat);
        this.addValues(this.Vertical,Horizontal,this.mode);
        this.setColor(new Color(191, 191, 191).getRGB());
    }

    @EventHandler
    private void onPacket(EventPacketReceive e) {
    	if(!Fly.groundpacket.getValue() && !Fly.damaged)return;
        if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
            if(this.mode.getValue() == ThisMode.AAC && !mc.thePlayer.onGround) {
            	if (this.Vertical.getValue().equals(0.0) && this.Horizontal.getValue().equals(0.0)) {
                    e.setCancelled(true);
                } else {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                    packet.motionX = (int)(this.Horizontal.getValue() / 100.0);
                    packet.motionY = (int)(this.Vertical.getValue() / 100.0);
                    packet.motionZ = (int)(this.Horizontal.getValue() / 100.0);
                }
            }
            if(this.mode.getValue() == ThisMode.HuaYuTing && mc.thePlayer.onGround) {
            	if (this.Vertical.getValue().equals(0.0) && this.Horizontal.getValue().equals(0.0)) {
                    e.setCancelled(true);
                } else {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                    packet.motionX = (int)(this.Horizontal.getValue() / 100.0);
                    packet.motionY = (int)(this.Vertical.getValue() / 100.0);
                    packet.motionZ = (int)(this.Horizontal.getValue() / 100.0);
                }
            }
            if(this.mode.getValue() == ThisMode.Normal) {
            	if (this.Vertical.getValue().equals(0.0) && this.Horizontal.getValue().equals(0.0)) {
                    e.setCancelled(true);
                } else {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
//                    packet.motionX = (int)(this.Horizontal.getValue() / 100.0);
//                    packet.motionY = (int)(this.Vertical.getValue() / 100.0);
//                    packet.motionZ = (int)(this.Horizontal.getValue() / 100.0);
                    mc.thePlayer.motionX *= (this.Horizontal.getValue() / 100.0);
                    mc.thePlayer.motionY *= (this.Vertical.getValue() / 100.0);
                    mc.thePlayer.motionZ *= (this.Horizontal.getValue() / 100.0);
                }
            }
        }
    }
}

enum ThisMode {
    Normal,
    AAC,
    HuaYuTing,

}
