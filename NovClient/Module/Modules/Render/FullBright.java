/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Render;

import java.awt.Color;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventTick;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright
extends Module {
    private float old;

    public FullBright() {
        super("FullBright", new String[]{"FullBright", "brightness", "bright"}, ModuleType.Render);
        this.setColor(new Color(244, 255, 149).getRGB());
    }
    public void onEnable() {
        this.old = this.mc.gameSettings.gammaSetting;
        super.onEnable();
    }

    @EventHandler
    private void onTick(EventTick e) {
        mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(),10000,1));
    }

    public void onDisable() {
    	mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
        super.onDisable();
    }
}

