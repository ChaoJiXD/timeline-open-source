/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Misc;

import java.awt.Color;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.BlockUtils;
import NovClient.Util.Math.RotationUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;

public class AntiObsidian
extends Module {
    public AntiObsidian() {
        super("AntiObsidian", new String[]{"AntiObsidian"}, ModuleType.Misc);
        super.addValues();
        this.setColor(new Color(208, 30, 142).getRGB());
    }
    @EventHandler
    public void OnUpdate(EventPreUpdate e) {
    		BlockPos obsidianpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ));
    		Block obsidianblock = mc.theWorld.getBlockState(obsidianpos).getBlock();
    		if(obsidianblock == Block.getBlockById(49)) {
                obsidianpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ));
                obsidianblock = mc.theWorld.getBlockState(obsidianpos).getBlock();
    		    if(obsidianblock != Block.getBlockById(49)) {
                    BlockUtils.updateTool(obsidianpos);
                    float[] rot = RotationUtil.getRotationFromPosition(mc.thePlayer.posX+0.5, mc.thePlayer.posY - 1+0.5, mc.thePlayer.posZ+0.5);
                    if(rot == null)return;
                    e.setYaw(rot[0]);
                    e.setPitch(rot[1]);
                    mc.playerController.onPlayerDamageBlock(obsidianpos, EnumFacing.UP);
                }else
                {
                    BlockPos block1pos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY+1, mc.thePlayer.posZ - 1));
                    BlockPos block2pos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ - 1));
                    Block block = mc.theWorld.getBlockState(block1pos).getBlock();
                    if(block != Block.getBlockById(0))
                    {
                        BlockUtils.updateTool(block1pos);
                        float[] rot = RotationUtil.getRotationFromPosition(mc.thePlayer.posX+0.5, mc.thePlayer.posY + 1+0.5, mc.thePlayer.posZ-1+0.5);
                        if(rot == null)return;
                        e.setYaw(rot[0]);
                        e.setPitch(rot[1]);
                        mc.playerController.onPlayerDamageBlock(block1pos, EnumFacing.EAST);
                    }else
                    {
                        BlockUtils.updateTool(block2pos);
                        float[] rot = RotationUtil.getRotationFromPosition(mc.thePlayer.posX+0.5, mc.thePlayer.posY + 0.5, mc.thePlayer.posZ-1+0.5);
                        if(rot == null)return;
                        e.setYaw(rot[0]);
                        e.setPitch(rot[1]);
                        mc.playerController.onPlayerDamageBlock(block2pos, EnumFacing.EAST);
                    }

                }

    		}
    	

    }


}

