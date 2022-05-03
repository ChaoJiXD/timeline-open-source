package NovClient.Module.Modules.Render;

import java.awt.Color;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.Manager.FriendManager;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.UI.fontRenderer.UnicodeFontRenderer;
import NovClient.Util.Colors2;
import NovClient.Util.StringConversions;
import NovClient.Util.Timer;
import NovClient.Util.Render.RenderUtil;




public class MiniMap
extends Module {
    private boolean dragging;
    float hue;
    private Numbers<Double> scale = new Numbers<Double>("Scale", "Scale", 2.0,1.0,5.0,0.1);
    private Numbers<Double> x = new Numbers<Double>("X", "X", 0.0, 1.0, 1920.0, 5.0);
    private Numbers<Double> y = new Numbers<Double>("Y", "Y", 80.0,1.0,1080.0, 5.0);
    private Numbers<Double> size = new Numbers<Double>("Size", "Size", 50.0,50.0, 500.0, 5.0);
    public Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])RadarMode.values(), (Enum)RadarMode.Normal);

    public MiniMap() {
    	super("MiniMap", new String[]{"MiniMap"}, ModuleType.Render);
    	this.addValues(this.scale,this.x,this.y,this.size,this.mode);
    }

    @EventHandler
    public void onGui(EventRender2D e) {
    	if(this.mode.getValue()== RadarMode.Normal) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int size1 = ((Double)this.size.getValue()).intValue();
        float xOffset = ((Double)this.x.getValue()).floatValue();
        float yOffset = ((Double)this.y.getValue()).floatValue();
        float playerOffsetX = (float)mc.thePlayer.posX;
        float playerOffSetZ = (float)mc.thePlayer.posZ;
        int var141 = sr.getScaledWidth();
        int var151 = sr.getScaledHeight();
        int mouseX = Mouse.getX() * var141 / this.mc.displayWidth;
        int mouseY = var151 - Mouse.getY() * var151 / this.mc.displayHeight - 1;
        if ((float)mouseX >= xOffset && (float)mouseX <= xOffset + (float)size1 && (float)mouseY >= yOffset - 3.0f && (float)mouseY <= yOffset + 10.0f && Mouse.getEventButton() == 0) {
            this.dragging = !this.dragging;
            boolean bl = this.dragging;
        }
        if (this.dragging && this.mc.currentScreen instanceof GuiChat) {
            Object newValue = StringConversions.castNumber((String)Double.toString((double)(mouseX - size1 / 2)), (Object)5);
            this.x.setValue((Double)((Double)newValue));
            Object newValueY = StringConversions.castNumber((String)Double.toString((double)(mouseY - 2)), (Object)5);
            this.y.setValue((Double)((Double)newValueY));
        } else {
            this.dragging = false;
        }
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        float h = this.hue;
        float h2 = this.hue + 85.0f;
        float h3 = this.hue + 170.0f;
        if (h > 255.0f) {
            h = 0.0f;
        }
        if (h2 > 255.0f) {
            h2 -= 255.0f;
        }
        if (h3 > 255.0f) {
            h3 -= 255.0f;
        }
        Color color33 = Color.getHSBColor((float)(h / 255.0f), (float)0.9f, (float)1.0f);
        Color color332 = Color.getHSBColor((float)(h2 / 255.0f), (float)0.9f, (float)1.0f);
        Color color333 = Color.getHSBColor((float)(h3 / 255.0f), (float)0.9f, (float)1.0f);
    	int red = HUD.r.getValue().intValue();
        int green = HUD.g.getValue().intValue();
        int blue = HUD.b.getValue().intValue();
        int alph = HUD.a.getValue().intValue();
        int color1 = Colors2.getColor(red, green, blue, alph);
        int color2 = Colors2.getColor(red, green, blue, alph);
        int color3 = Colors2.getColor(red, green, blue, alph);
        this.hue = (float)((double)this.hue + 0.1);
        RenderUtil.rectangleBordered((double)(xOffset + 3.0f), (double)(yOffset + 3.0f), (double)(xOffset + (float)size1 - 3.0f), (double)(yOffset + (float)size1 - 3.0f), (double)0.5, new Color(0,0,0,150).getRGB(), new Color(0,0,0,255).getRGB());
        RenderUtil.drawGradientSideways((double)(xOffset + 4f), (double)(yOffset + 4.0f), (double)(xOffset + (float)(size1 / 2)), (double)((double)yOffset + 4.5), (int)color1, (int)color2);
        RenderUtil.drawGradientSideways((double)(xOffset +(float)(size1 / 2)), (double)(yOffset + 4.0f), (double)(xOffset + (float)size1 - 4f), (double)((double)yOffset + 4.5), (int)color2, (int)color3);
        RenderUtil.rectangle((double)((double)xOffset + ((double)(size1 / 2) - 0.5)), (double)((double)yOffset + 5), (double)((double)xOffset + ((double)(size1 / 2) + 0.5)), (double)((double)(yOffset + (float)size1) - 3.5), (int)Colors2.getColor((int)255, (int)80));
        RenderUtil.rectangle((double)((double)xOffset + 3.5), (double)((double)yOffset + ((double)(size1 / 2) - 0.5)), (double)((double)(xOffset + (float)size1) - 3.5), (double)((double)yOffset + ((double)(size1 / 2) + 0.5)), (int)Colors2.getColor((int)255, (int)80));
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            EntityPlayer ent;
            if (!(o instanceof EntityPlayer) || !(ent = (EntityPlayer)o).isEntityAlive() || ent == mc.thePlayer || ent.isInvisible() || ent.isInvisibleToPlayer((EntityPlayer)mc.thePlayer)) continue;
            float pTicks = this.mc.timer.renderPartialTicks;
            float posX = (float)((ent.posX + (ent.posX - ent.lastTickPosX) * (double)pTicks - (double)playerOffsetX) * (Double)this.scale.getValue());
            float posZ = (float)((ent.posZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - (double)playerOffSetZ) * (Double)this.scale.getValue());
            int color = mc.thePlayer.canEntityBeSeen((Entity)ent) ? Colors2.getColor(255, 0, 0) : Colors2.getColor(255, 255, 0);
            float cos = (float)Math.cos((double)((double)mc.thePlayer.rotationYaw * 0.017453292519943295));
            float sin = (float)Math.sin((double)((double)mc.thePlayer.rotationYaw * 0.017453292519943295));
            float rotY = (- posZ) * cos - posX * sin;
            float rotX = (- posX) * cos + posZ * sin;
            if (rotY > (float)(size1 / 2 - 5)) {
                rotY = (float)(size1 / 2) - 5.0f;
            } else if (rotY < (float)((- size1) / 2 - 5)) {
                rotY = (- size1) / 2 - 5;
            }
            if (rotX > (float)(size1 / 2) - 5.0f) {
                rotX = size1 / 2 - 5;
            } else if (rotX < (float)((- size1) / 2 - 5)) {
                rotX = - (float)(size1 / 2) - 5.0f;
            }
            RenderUtil.rectangleBordered((double)((double)(xOffset + (float)(size1 / 2) + rotX) - 1.5), (double)((double)(yOffset + (float)(size1 / 2) + rotY) - 1.5), (double)((double)(xOffset + (float)(size1 / 2) + rotX) + 1.5), (double)((double)(yOffset + (float)(size1 / 2) + rotY) + 1.5), (double)0.5, (int)color, (int)Colors2.getColor((int)46));
        }
    }if(this.mode.getValue()==RadarMode.Round) {
		Timer timer = new Timer();
        ScaledResolution sr = new ScaledResolution(this.mc);
        int size = ((Double)this.size.getValue()).intValue();
        float xOffset = ((Double)this.x.getValue()).floatValue();
        float yOffset = ((Double)this.y.getValue()).floatValue();
        float playerOffsetX = (float)mc.thePlayer.posX;
        float playerOffSetZ = (float)mc.thePlayer.posZ;
		Gui.drawFilledCircle(xOffset + (size / 2), yOffset + size / 2, size / 2 - 4, Colors2.getColor(50, 100), 0);
        GlStateManager.pushMatrix();
        GlStateManager.translate(xOffset + size / 2, yOffset + size / 2, 0);
        GlStateManager.rotate(-mc.thePlayer.rotationYaw, 0, 0, 1);
        RenderUtil.rectangle((-0.5), -size / 2 + 4, (0.5), size / 2 - 4, Colors2.getColor(255, 80));
        RenderUtil.rectangle(-size / 2 + 4, (-0.5), size / 2 - 4, (+0.5),
                Colors2.getColor(255, 80));
        GlStateManager.popMatrix();

        RenderUtil.drawCircle(xOffset + (size / 2), yOffset + size / 2, size / 2 - 4, 72, Colors2.getColor(0, 200));

        UnicodeFontRenderer normal = Client.instance.FontLoaders.Chinese18;
        float angle2 = -mc.thePlayer.rotationYaw + 90;
        float x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2))) + xOffset + size / 2; // angle is in radians
        float y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2))) + yOffset + size / 2;
        normal.drawStringWithShadow("N", x2 - normal.getStringWidth("N") / 2, y2 - 1, -1,255);
        x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2 + 90))) + xOffset + size / 2; // angle is in radians
        y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2 + 90))) + yOffset + size / 2;
        normal.drawStringWithShadow("E", x2 - normal.getStringWidth("E") / 2, y2 - 1, -1,255);
        x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2 + 180))) + xOffset + size / 2; // angle is in radians
        y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2 + 180))) + yOffset + size / 2;
        normal.drawStringWithShadow("S", x2 - normal.getStringWidth("S") / 2, y2 - 1, -1,255);
        x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2 - 90))) + xOffset + size / 2; // angle is in radians
        y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2 - 90))) + yOffset + size / 2;
        normal.drawStringWithShadow("W", x2 - normal.getStringWidth("W") / 2, y2 - 1, -1,255);

        int var141 = sr.getScaledWidth();
        int var151 = sr.getScaledHeight();
        final int mouseX = Mouse.getX() * var141 / mc.displayWidth;
        final int mouseY = var151 - Mouse.getY() * var151 / mc.displayHeight - 1;
        if (mouseX >= xOffset && mouseX <= xOffset + size && mouseY >= yOffset - 3 && mouseY <= yOffset + 10 && Mouse.getEventButton() == 0 ) {
        	timer.reset();
            dragging = !dragging;
        }
        if (dragging && mc.currentScreen instanceof GuiChat) {
            Object newValue = (StringConversions.castNumber(Double.toString(mouseX - size / 2), 5));
            x.setValue((Double)newValue);
            Object newValueY = (StringConversions.castNumber(Double.toString(mouseY - 2), 5));
           y.setValue((Double) newValueY);
        } else {
            dragging = false;
        }

        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityPlayer) {
                EntityPlayer ent = (EntityPlayer) o;
                if (ent.isEntityAlive() && ent != mc.thePlayer && !(ent.isInvisible() || ent.isInvisibleToPlayer(mc.thePlayer))) {

                    float pTicks = mc.timer.renderPartialTicks;
                    float posX = (float) (((ent.posX + (ent.posX - ent.lastTickPosX) * pTicks) -
                            playerOffsetX) * ((Number) scale.getValue()).doubleValue());

                    float posZ = (float) (((ent.posZ + (ent.posZ - ent.lastTickPosZ) * pTicks) -
                            playerOffSetZ) * ((Number) scale.getValue()).doubleValue());
                    int color;
                    if (FriendManager.isFriend(ent.getName())) {
                        color = mc.thePlayer.canEntityBeSeen(ent) ? Colors2.getColor(0, 195, 255)
                                : Colors2.getColor(0, 195, 255);
                    } else {
                        color = mc.thePlayer.canEntityBeSeen(ent) ? Colors2.getColor(255, 0, 0)
                                : Colors2.getColor(255, 255, 0);
                    }

                    float cos = (float) Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                    float sin = (float) Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                    float rotY = -(posZ * cos - posX * sin);
                    float rotX = -(posX * cos + posZ * sin);
                    float var7 = 0 - rotX;
                    float var9 = 0 - rotY;
                    if (MathHelper.sqrt_double(var7 * var7 + var9 * var9) > size / 2 - 4) {
                        float angle = findAngle(0, rotX, 0, rotY);
                        float x = (float) ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2; // angle is in radians
                        float y = (float) ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(x, y, 0);
                        GlStateManager.rotate(angle, 0, 0, 1);
                        GlStateManager.scale(1.5f, 0.5, 0.5);
                        RenderUtil.drawCircle(0, 0, 1.5f, 3, Colors2.getColor(46));
                        RenderUtil.drawCircle(0, 0, 1, 3, color);
                        GlStateManager.popMatrix();
                    } else {
                    	RenderUtil.rectangleBordered(xOffset + (size / 2) + rotX - 1.5,
                                yOffset + (size / 2) + rotY - 1.5, xOffset + (size / 2) + rotX + 1.5,
                                yOffset + (size / 2) + rotY + 1.5, 0.5, color, Colors2.getColor(46));
                    }

                    /*
                     * Clamps to the edge of the radar, have it less than
                     * the radar if you don't want squares to come out.
                     */


                }
            }
        }


    }
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEnable() {
        super.isEnabled();
    }
    private float findAngle(float x, float x2, float y, float y2) {
        return (float) (Math.atan2(y2 - y, x2 - x) * 180 / Math.PI);
    }
    public static enum RadarMode {
        Normal,
        Round;
    }
}
