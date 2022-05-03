/*
 * Decompiled with CFR 0.136.
 */
package NovClient.Module.Modules.Render;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.NumberFormat;

import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Combat.KillAura;
import NovClient.UI.Font.CFontRenderer;
import NovClient.UI.Font.FontLoaders;
import NovClient.Util.LiuqidRender;
import NovClient.Util.R3DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TargetESP
extends Module {
   public TargetESP() {
        super("TargetESP", new String[]{"TargetESP"}, ModuleType.Render);
        this.setColor(new Color(244, 255, 149).getRGB());
    }

    @EventHandler
    public void onRender(EventRender3D event) {
    	if(KillAura.blockTarget != null)LiuqidRender.drawEntityBox(KillAura.blockTarget, KillAura.blockTarget.hurtTime > 0 ? Color.RED : Color.WHITE, false);
    }
       

        
    
        
    
    

    
    
    
    



}
