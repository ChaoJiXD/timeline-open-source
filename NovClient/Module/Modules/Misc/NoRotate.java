/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Misc;

import java.awt.Color;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPacketReceive;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate
extends Module {
	private Option<Boolean> Confirm = new Option("Confirm", "Confirm", Boolean.valueOf(false));
	private Option<Boolean> ConfirmIllegalRotation = new Option("ConfirmIllegalRotation", "ConfirmIllegalRotation", Boolean.valueOf(false));
	private Option<Boolean> NoZero = new Option("NoZero", "NoZero", Boolean.valueOf(false));
	public NoRotate() {
        super("NoRotate", new String[]{"NoRotateSet"}, ModuleType.Misc);
        this.setColor(new Color(17, 250, 154).getRGB());
        super.addValues(Confirm,ConfirmIllegalRotation,NoZero);
    }

    @EventHandler
    private void onPacket(EventPacketReceive event) {
        Packet packet = event.getPacket();

        if(mc.thePlayer == null)return;


        if (packet instanceof S08PacketPlayerPosLook) {
        	S08PacketPlayerPosLook thePacket = (S08PacketPlayerPosLook)packet;
            if (NoZero.getValue() && thePacket.getYaw() == 0F && thePacket.getPitch() == 0F)
                return;

            if (ConfirmIllegalRotation.getValue() || thePacket.getPitch() <= 90 && thePacket.getPitch() >= -90 && thePacket.getYaw() != mc.thePlayer.rotationYaw &&
                    		thePacket.getPitch() != mc.thePlayer.rotationPitch) {

                if (Confirm.getValue())
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(thePacket.getYaw(), thePacket.getPitch(), mc.thePlayer.onGround));
            }

            thePacket.yaw = mc.thePlayer.rotationYaw;
            thePacket.pitch = mc.thePlayer.rotationPitch;
        }
    }
}




