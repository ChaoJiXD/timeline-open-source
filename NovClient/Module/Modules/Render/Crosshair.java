package NovClient.Module.Modules.Render;


import net.minecraft.client.gui.*;

import java.awt.*;

import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.Colors2;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.client.*;
import net.minecraft.util.*;

public class Crosshair extends Module
{
    private boolean dragging;
    float hue;
    private Option<Boolean> DYNAMIC;
    public static Numbers<Double> GAP;
    private Numbers<Double> WIDTH;
    public static Numbers<Double> SIZE;
    
    static {
        Crosshair.GAP = new Numbers<Double>("gap", "gap", 5.0, 0.25, 15.0, 0.25);
        Crosshair.SIZE = new Numbers<Double>("size", "size", 7.0, 0.25, 15.0, 0.25);
    }
    
    public Crosshair() {
        super("Crosshair", new String[] { "Crosshair" }, ModuleType.Misc);
        this.DYNAMIC = new Option<Boolean>("DYNAMIC", "DYNAMIC", true);
        this.WIDTH = new Numbers<Double>("width", "width", 2.0, 0.25, 10.0, 0.25);
        this.addValues(this.DYNAMIC, Crosshair.GAP, this.WIDTH, Crosshair.SIZE);
    }
    
    @EventHandler
    public void onGui(final EventRender2D e) {
        final int red = HUD.r.getValue().intValue();
        final int green = HUD.g.getValue().intValue();
        final int blue = HUD.b.getValue().intValue();
        final int alph = 255;
        final double gap = Crosshair.GAP.getValue();
        final double width = this.WIDTH.getValue();
        final double size = Crosshair.SIZE.getValue();
        final ScaledResolution scaledRes = new ScaledResolution(Crosshair.mc);
        RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - width, scaledRes.getScaledHeight() / 2 - gap - size - (this.isMoving() ? 2 : 0), scaledRes.getScaledWidth() / 2 + 1.0f + width, scaledRes.getScaledHeight() / 2 - gap - (this.isMoving() ? 2 : 0), 0.5, Colors2.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - width, scaledRes.getScaledHeight() / 2 + gap + 1.0 + (this.isMoving() ? 2 : 0) - 0.15, scaledRes.getScaledWidth() / 2 + 1.0f + width, scaledRes.getScaledHeight() / 2 + 1 + gap + size + (this.isMoving() ? 2 : 0) - 0.15, 0.5, Colors2.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - gap - size - (this.isMoving() ? 2 : 0) + 0.15, scaledRes.getScaledHeight() / 2 - width, scaledRes.getScaledWidth() / 2 - gap - (this.isMoving() ? 2 : 0) + 0.15, scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, Colors2.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 + 1 + gap + (this.isMoving() ? 2 : 0), scaledRes.getScaledHeight() / 2 - width, scaledRes.getScaledWidth() / 2 + size + gap + 1.0 + (this.isMoving() ? 2 : 0), scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, Colors2.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
    }
    
    public boolean isMoving() {
        if (this.DYNAMIC.getValue()) {
            final Minecraft mc = Crosshair.mc;
            if (!mc.thePlayer.isCollidedHorizontally) {
                final Minecraft mc2 = Crosshair.mc;
                if (!mc.thePlayer.isSneaking()) {
                    final MovementInput movementInput = mc.thePlayer.movementInput;
                    if (MovementInput.moveForward == 0.0f) {
                        final MovementInput movementInput2 = mc.thePlayer.movementInput;
                        if (MovementInput.moveStrafe == 0.0f) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
