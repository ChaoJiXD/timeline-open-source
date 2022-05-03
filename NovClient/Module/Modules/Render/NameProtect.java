/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Render;

import java.awt.Color;

import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FoodStats;

public class NameProtect
extends Module {

    public NameProtect() {
        super("NameProtect", new String[]{"NameProtect"}, ModuleType.Render);
        this.setColor(new Color(208, 30, 142).getRGB());
    }


    }


