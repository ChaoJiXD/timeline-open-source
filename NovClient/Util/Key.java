/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package NovClient.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class Key {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final KeyBinding key;
    private final int xOffset;
    private final int yOffset;
    private boolean wasPressed = true;
    private long lastPress = 0L;
    private int color = 255;
    private double textBrightness = 1.0;

    public Key(KeyBinding key, int xOffset, int yOffset) {
        this.key = key;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void renderKey(int x, int y, int textColor) {
        boolean pressed = this.key.isKeyDown();
        String name = Keyboard.getKeyName((int)this.key.getKeyCode());
        if (pressed != this.wasPressed) {
            this.wasPressed = pressed;
            this.lastPress = System.currentTimeMillis();
        }
        if (pressed) {
            this.color = Math.min(255, (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            this.textBrightness = Math.max(0.0, 1.0 - (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        } else {
            this.color = Math.max(0, 255 - (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            this.textBrightness = Math.min(1.0, (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        }
        Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + 22, y + this.yOffset + 22, 2013265920 + (this.color << 16) + (this.color << 8) + this.color);
        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        this.mc.fontRendererObj.drawString(name, x + this.xOffset + 8, y + this.yOffset + 8, -16777216 + ((int)((double)red * this.textBrightness) << 16) + ((int)((double)green * this.textBrightness) << 8) + (int)((double)blue * this.textBrightness));
    }
}

