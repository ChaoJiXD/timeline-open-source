/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package NovClient.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class MouseButton {
    private static final String[] BUTTONS = new String[]{"LMB", "RMB"};
    private final Minecraft mc = Minecraft.getMinecraft();
    private final int button;
    private final int xOffset;
    private final int yOffset;
    private List clicks = new ArrayList();
    private boolean wasPressed = true;
    private long lastPress = 0L;
    private int color = 255;
    private double textBrightness = 1.0;

    public MouseButton(int button, int xOffset, int yOffset) {
        this.button = button;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    private int getCPS() {
        long time = System.currentTimeMillis();
        Iterator iterator = this.clicks.iterator();
        while (iterator.hasNext()) {
            if ((Long)iterator.next() + 1000L >= time) continue;
            iterator.remove();
        }
        return this.clicks.size();
    }

    public void renderMouseButton(int x, int y, int textColor) {
        boolean pressed = Mouse.isButtonDown((int)this.button);
        String name = BUTTONS[this.button];
        if (pressed != this.wasPressed) {
            this.wasPressed = pressed;
            this.lastPress = System.currentTimeMillis();
            if (pressed) {
                this.clicks.add(this.lastPress);
            }
        }
        if (pressed) {
            this.color = Math.min(255, (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            this.textBrightness = Math.max(0.0, 1.0 - (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        } else {
            this.color = Math.max(0, 255 - (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            this.textBrightness = Math.min(1.0, (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        }
        Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + 34, y + this.yOffset + 22, 2013265920 + (this.color << 16) + (this.color << 8) + this.color);
        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        this.mc.fontRendererObj.drawString(name, x + this.xOffset + 9, y + this.yOffset + 4, -16777216 + ((int)((double)red * this.textBrightness) << 16) + ((int)((double)green * this.textBrightness) << 8) + (int)((double)blue * this.textBrightness));
        String cpsText = String.valueOf(this.getCPS()) + " CPS";
        int cpsTextWidth = this.mc.fontRendererObj.getStringWidth(cpsText);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        this.mc.fontRendererObj.drawString(cpsText, (x + this.xOffset + 17) * 2 - cpsTextWidth / 2, (y + this.yOffset + 14) * 2, -16777216 + ((int)(255.0 * this.textBrightness) << 16) + ((int)(255.0 * this.textBrightness) << 8) + (int)(255.0 * this.textBrightness));
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }
}

