/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Events.World.EventTick;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Option;
import NovClient.Libraries.javax.vecmath.Vector3f;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.BlockUtils;
import NovClient.Util.Helper;
import NovClient.Util.LiuqidRender;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

public class ItemTeleport
extends Module {
    private float old;
	private Option<Boolean> resetAfterTp = new Option("resetAfterTp", "resetAfterTp", Boolean.valueOf(false));
    public Mode Cmode = new Mode("Mode", "Mode", (Enum[]) ClickMode.values(), (Enum) ClickMode.Middle);
    public Mode Tmode = new Mode("Mode", "Mode", (Enum[]) TMode.values(), (Enum) TMode.New);
    private int delay;
    private BlockPos endPos;
    private MovingObjectPosition objectPosition;
    
    public ItemTeleport() {
        super("ItemTeleport", new String[]{"ItemTeleport", "ItemTeleport", "ItemTeleport"}, ModuleType.Misc);
        this.setColor(new Color(244, 255, 149).getRGB());
        super.addValues(resetAfterTp,Cmode,Tmode);
    }
    @EventHandler
    public void onUpdate(final EventPreUpdate event) {
        if(mc.currentScreen == null && Mouse.isButtonDown(Arrays.asList(ClickMode.values()).indexOf(Cmode.getValue())) && delay <= 0) {
            endPos = objectPosition.getBlockPos();

            if(BlockUtils.getBlock(endPos).getMaterial() == Material.air) {
                endPos = null;
                return;
            }

            Helper.sendMessage("¡ì7[¡ì8¡ìlItemTeleport¡ì7] ¡ì3Position was set to ¡ì8" + endPos.getX() + "¡ì3, ¡ì8" + endPos.getY() + "¡ì3, ¡ì8" + endPos.getZ());
            delay = 6;
        }

        if(delay > 0)
            --delay;

        if(endPos != null && mc.thePlayer.isSneaking()) {
            if(!mc.thePlayer.onGround) {
                final double endX = (double) endPos.getX() + 0.5D;
                final double endY = (double) endPos.getY() + 1D;
                final double endZ = (double) endPos.getZ() + 0.5D;

                switch(Tmode.getValue().toString().toLowerCase()) {
                    case "old":
                        for(final Vector3f vector3f : vanillaTeleportPositions(endX, endY, endZ, 4D))
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vector3f.getX(), vector3f.getY(), vector3f.getZ(), false));
                        break;
                    case "new":
                        for(final Vector3f vector3f : vanillaTeleportPositions(endX, endY, endZ, 5D)) {
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vector3f.x, vector3f.y, vector3f.z, true));
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4.0, mc.thePlayer.posZ, true));
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vector3f.x, vector3f.y, vector3f.z, true));
                            forward(0.04);
                        }
                        break;
                }

                if(resetAfterTp.getValue())
                    endPos = null;

                Helper.sendMessage("¡ì7[¡ì8¡ìlItemTeleport¡ì7] ¡ì3Tried to collect items");
            }else
                mc.thePlayer.jump();
        }
    }
    public static void forward(final double length) {
        final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        mc.thePlayer.setPosition(mc.thePlayer.posX + (-Math.sin(yaw) * length), mc.thePlayer.posY, mc.thePlayer.posZ + (Math.cos(yaw) * length));
    }
    @EventHandler
    public void onRender3D(final EventRender3D event) {
        objectPosition = mc.thePlayer.rayTrace(1000, event.getPartialTicks());

        if(objectPosition.getBlockPos() == null)
            return;

        final int x = objectPosition.getBlockPos().getX();
        final int y = objectPosition.getBlockPos().getY();
        final int z = objectPosition.getBlockPos().getZ();

        if(BlockUtils.getBlock(objectPosition.getBlockPos()).getMaterial() != Material.air) {
            final RenderManager renderManager = mc.getRenderManager();

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(2F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            LiuqidRender.glColor(BlockUtils.getBlock(objectPosition.getBlockPos().up()).getMaterial() != Material.air ? new Color(255, 0, 0, 90) : new Color(0, 255, 0, 90));
            LiuqidRender.drawFilledBox(new AxisAlignedBB(x - renderManager.renderPosX, (y + 1) - renderManager.renderPosY, z - renderManager.renderPosZ, x - renderManager.renderPosX + 1D, y + 1.2D - renderManager.renderPosY, z - renderManager.renderPosZ + 1D));
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_BLEND);

            LiuqidRender.renderNameTag(Math.round(mc.thePlayer.getDistance(x, y, z)) + "m", x + 0.5, y + 1.7, z + 0.5);
            GlStateManager.resetColor();
        }
    }
    @Override
    public void onEnable() {
        this.old = this.mc.gameSettings.gammaSetting;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        delay = 0;
        endPos = null;
        super.onDisable();
    }
    private List<Vector3f> vanillaTeleportPositions(final double tpX, final double tpY, final double tpZ, final double speed) {
        final List<Vector3f> positions = new ArrayList<>();
        double posX = tpX - mc.thePlayer.posX;
        double posZ = tpZ - mc.thePlayer.posZ;
        float yaw = (float) ((Math.atan2(posZ, posX) * 180 / Math.PI) - 90F);
        double tmpX;
        double tmpY = mc.thePlayer.posY;
        double tmpZ;
        double steps = 1;

        for(double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY, tpZ); d += speed)
            steps++;

        for(double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY, tpZ); d += speed) {
            tmpX = mc.thePlayer.posX - (Math.sin(Math.toRadians(yaw)) * d);
            tmpZ = mc.thePlayer.posZ + Math.cos(Math.toRadians(yaw)) * d;
            tmpY -= (mc.thePlayer.posY - tpY) / steps;
            positions.add(new Vector3f((float) tmpX, (float) tmpY, (float) tmpZ));
        }

        positions.add(new Vector3f((float) tpX, (float) tpY, (float) tpZ));

        return positions;
    }
    private double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d1 = y1 - y2;
        double d2 = z1 - z2;
        return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }
	public enum ClickMode {
		Left, Right, Middle;
	}
	public enum TMode {
		New, Old;
	}
}

