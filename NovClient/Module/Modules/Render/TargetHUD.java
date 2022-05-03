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

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Combat.KillAura;
import NovClient.UI.Font.CFontRenderer;
import NovClient.UI.Font.FontLoaders;
import NovClient.UI.fontRenderer.UnicodeFontRenderer;
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

public class TargetHUD
extends Module {
	private UnicodeFontRenderer font1 = Client.instance.FontLoaders.Chinese18;
   public TargetHUD() {
        super("TargetHUD", new String[]{"TargetHUD"}, ModuleType.Render);
        this.setColor(new Color(244, 255, 149).getRGB());
    }

    @EventHandler
    public void onRender(EventRender2D event) {
    	Edebug();
    }
       

        
    
        
    
    private void Edebug() {
        ScaledResolution res = new ScaledResolution(this.mc);
        ScaledResolution sr2 = new ScaledResolution(this.mc);
        int y2 = 2;
        boolean count = false;
        Color color = new Color(1.0f, 0.75f, 0.0f, 0.45f);
        int x1 = 600;
        int y1 = 355;
        int x2 = 750;
        int y22 = sr2.getScaledHeight() - 50;
        int nametag = y1 + 12;
        double thickness = 0.014999999664723873;
        double xLeft = -20.0;
        double xRight = 20.0;
        double yUp = 27.0;
        double yDown = 130.0;
        double size = 10.0;
        int right = new ScaledResolution(this.mc).getScaledWidth() - new ScaledResolution(this.mc).getScaledWidth() / 2;
        int right2 = new ScaledResolution(this.mc).getScaledWidth() - new ScaledResolution(this.mc).getScaledWidth() / 2 + 30;
        int height = new ScaledResolution(this.mc).getScaledHeight() - 70;
        if ((KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity ) != null || mc.pointedEntity != null) {
            Gui.drawRect((double)right, (double)(height - 50), (double)(right + 130), (double)(height - 90), (int)new Color(0, 0, 0, 130).getRGB());
            font1.drawString((KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity ).getName(), right + 30, height - 87, 16777215,255);
            font1.drawString("HP:" + (int)((EntityLivingBase)(KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity )).getHealth() + "/" + (int)((EntityLivingBase)(KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity )).getMaxHealth() + " " + "Hurt:" + ((KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity ).hurtResistantTime > 0), right + 30, height - 70, new Color(255, 255, 255).getRGB(),255);
            font1.drawString("Coords: " + (int)(KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity ).posX + " " + (int)(KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity ).posY + " " + (int)(KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity ).posZ, right + 30, height - 60, new Color(255, 255, 255).getRGB(),255);
            R3DUtil.drawEntityOnScreen((int)(right + 14), (int)(height - 54), (int)15, (float)2.0f, (float)15.0f, (EntityLivingBase)((EntityLivingBase)(KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity )));
            Gui.drawRainbowRectVertical((int)right2, (int)(height - 77), (int)((int)((float)right2 + 95.0f * (float)Math.min((int)right2, ((EntityLivingBase)((KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity ))).getHealth() / ((EntityLivingBase)((KillAura.blockTarget != null ? KillAura.blockTarget :mc.pointedEntity ))).getMaxHealth()))), (int)(height - 73), (int)3, (float)1.0f);
            //Gui.drawRainbowRectVertical((int)right2, (int)(height - 77), (int)(right2 + 95.0f * Math.min(right2, (int)(((EntityLivingBase)Aura.blockTarget).getHealth() / ((int)((EntityLivingBase)Aura.blockTarget).getMaxHealth())))), (int)(height - 73),3, 1);
        }
    }

    
    
    
    



}
