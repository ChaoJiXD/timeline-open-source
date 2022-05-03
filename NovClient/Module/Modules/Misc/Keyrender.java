/*
 * Decompiled with CFR 0.148.
 */
package NovClient.Module.Modules.Misc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.Key;
import NovClient.Util.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class Keyrender
extends Module {
    private static final int[] COLORS = new int[]{16777215, 16711680, 65280, 255, 16776960, 11141290};
    private final Key[] movementKeys = new Key[4];
    private final MouseButton[] mouseButtons = new MouseButton[2];
    private Numbers<Double> X = new Numbers<Double>("X", "X", 5.0, 0.0, Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 10.0);
    private Numbers<Double> Y = new Numbers<Double>("Y", "Y", 150.0, 0.0, Toolkit.getDefaultToolkit().getScreenSize().getHeight(), 10.0);

    public Keyrender() {
        super("KeyStrokes", new String[]{"KeyStrokes"}, ModuleType.Misc);
        this.addValues(this.X, this.Y);
        this.movementKeys[0] = new Key(Keyrender.mc.gameSettings.keyBindForward, 26, 2);
        this.movementKeys[1] = new Key(Keyrender.mc.gameSettings.keyBindLeft, 2, 26);
        this.movementKeys[2] = new Key(Keyrender.mc.gameSettings.keyBindBack, 26, 26);
        this.movementKeys[3] = new Key(Keyrender.mc.gameSettings.keyBindRight, 50, 26);
        this.mouseButtons[0] = new MouseButton(0, 2, 50);
        this.mouseButtons[1] = new MouseButton(1, 38, 50);
    }

    @EventHandler
    public void renderKeystrokes(EventRender2D e) {
        int x = ((Double)this.X.getValue()).intValue();
        int y = ((Double)this.Y.getValue()).intValue();
        int textColor = this.getColor(6);
        boolean showingMouseButtons = true;
        ScaledResolution res = new ScaledResolution(mc);
        int width = 74;
        int height = showingMouseButtons ? 74 : 50;
        this.drawMovementKeys(x, y, textColor);
        if (showingMouseButtons) {
            this.drawMouseButtons(x, y, textColor);
        }
    }

    private int getColor(int index) {
        return index == 6 ? Color.HSBtoRGB((float)(System.currentTimeMillis() % 1000L) / 1000.0f, 0.8f, 0.8f) : COLORS[index];
    }

    private void drawMovementKeys(int x, int y, int textColor) {
        for (Key key : this.movementKeys) {
            key.renderKey(x, y, textColor);
        }
    }

    private void drawMouseButtons(int x, int y, int textColor) {
        for (MouseButton button : this.mouseButtons) {
            button.renderMouseButton(x, y, textColor);
        }
    }
}

