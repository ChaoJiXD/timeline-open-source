/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Misc;

import java.awt.Color;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventTick;
import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Timer
extends Module {
    private float old;
	private Numbers<Double> Speed = new Numbers<Double>("Speed", "Speed", Double.valueOf(1.0D), Double.valueOf(1.0D),
			Double.valueOf(20.0D), Double.valueOf(1D));
    public Timer() {
        super("Timer", new String[]{"Timer", "Timer", "Timer"}, ModuleType.Misc);
        this.setColor(new Color(244, 255, 149).getRGB());
        super.addValues(Speed);
    }
    public void onEnable() {
    	mc.timer.timerSpeed = Speed.getValue().floatValue();
        super.onEnable();
    }

    public void onDisable() {
    	mc.timer.timerSpeed = 1f;
        super.onDisable();
    }
}

