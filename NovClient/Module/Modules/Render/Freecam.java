/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Render;

import com.mojang.authlib.GameProfile;

import NovClient.API.EventHandler;
import NovClient.API.Events.Misc.EventCollideWithBlock;
import NovClient.API.Events.World.EventPacketReceive;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.awt.Color;

public class Freecam
extends Module {
    private EntityOtherPlayerMP copy;
    private double x;
    private double y;
    private double z;
    private static Numbers<Double> Speed = new Numbers<Double>("Speed", "Speed", 2.0, 1.0, 5.0, 1.0);
    public Freecam() {
        super("FreeCam", new String[]{"FreeCam"}, ModuleType.Render);
        super.addValues(Speed);
        this.setColor(new Color(221, 214, 51).getRGB());
    }

    @Override
    public void onEnable() {
        this.copy = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
        this.copy.clonePlayer(this.mc.thePlayer, true);
        this.copy.setLocationAndAngles(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch);
        this.copy.rotationYawHead = this.mc.thePlayer.rotationYawHead;
        this.copy.setEntityId(-1337);
        this.copy.setSneaking(this.mc.thePlayer.isSneaking());
        this.mc.theWorld.addEntityToWorld(this.copy.getEntityId(), this.copy);
        this.x = this.mc.thePlayer.posX;
        this.y = this.mc.thePlayer.posY;
        this.z = this.mc.thePlayer.posZ;
    }

    @EventHandler
    private void onPreMotion(EventPreUpdate e) {
    	this.mc.thePlayer.fallDistance = 0.0f;
        this.mc.thePlayer.capabilities.isFlying = true;
        this.mc.thePlayer.noClip = true;
        this.mc.thePlayer.capabilities.setFlySpeed(Speed.getValue().floatValue());
        e.setCancelled(true);
    }

    @EventHandler
    private void onPacketSend(EventPacketReceive e) {
        if (e.getPacket() instanceof C03PacketPlayer) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onBB(EventCollideWithBlock e) {
        e.setBoundingBox(null);
    }

    @Override
    public void onDisable() {
        this.mc.thePlayer.setSpeed(0.0);
        this.mc.thePlayer.setLocationAndAngles(this.copy.posX, this.copy.posY, this.copy.posZ, this.copy.rotationYaw, this.copy.rotationPitch);
        this.mc.thePlayer.rotationYawHead = this.copy.rotationYawHead;
        this.mc.theWorld.removeEntityFromWorld(this.copy.getEntityId());
        this.mc.thePlayer.setSneaking(this.copy.isSneaking());
        this.copy = null;
        this.mc.renderGlobal.loadRenderers();
        this.mc.thePlayer.setPosition(this.x, this.y, this.z);
        this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.01, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
        this.mc.thePlayer.capabilities.isFlying = false;
        this.mc.thePlayer.noClip = false;
        this.mc.theWorld.removeEntityFromWorld(-1);
    }
}

