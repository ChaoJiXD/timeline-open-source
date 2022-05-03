/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Misc;

import java.awt.Color;

import NovClient.API.EventHandler;
import NovClient.API.Events.Misc.EventChat;
import NovClient.API.Events.World.EventTick;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoReconnect
extends Module {
    private float old;

    public AutoReconnect() {
        super("AutoReconnect", new String[]{"AutoReconnect", "AutoReconnect", "AutoReconnect"}, ModuleType.Misc);
        this.setColor(new Color(244, 255, 149).getRGB());
    }

    @EventHandler
    private void onChat(EventChat e) {
        if(e.getMessage().contains("Flying or related."))mc.thePlayer.sendChatMessage("/back");
    }

}

