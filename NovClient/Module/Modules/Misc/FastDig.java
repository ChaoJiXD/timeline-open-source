package NovClient.Module.Modules.Misc;

import java.awt.*;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventDamageBlock;
import NovClient.API.Events.World.EventPacketSend;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;

public class FastDig extends Module
{

    public static Numbers<Double> speed = new Numbers<Double>("Speed", "Speed", 0.7, 0.0, 1.0, 0.1);
    public static Numbers<Double> Pot = new Numbers<Double>("Potion", "Potion", 1.0, 0.0, 4.0, 1.0);
    public static Mode<Enum> mode = new Mode("Mode", "mode", AutoPlayMode.values(), AutoPlayMode.Packet);
    private boolean bzs = false;
    private float bzx = 0.0f;
    public BlockPos blockPos;
    public EnumFacing facing;
    public static Option<Boolean> SendPacket = new Option<Boolean>("SendPacket", "SendPacket", false);
    public FastDig() {
    	
        super("FastDig", new String[] { "SpeedMine", "antifall" }, ModuleType.Misc);
        this.setColor(new Color(223, 233, 233).getRGB());
        super.addValues(speed,mode,Pot,SendPacket);
    }
	public Block getBlock(double x,double y,double z){
		BlockPos bp = new BlockPos(x, y, z);
		return mc.theWorld.getBlockState(bp).getBlock();
	}
    @EventHandler
    private void onUpdate(EventDamageBlock e) {
    	
        if(SendPacket.getValue())mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, e.getpos(), e.getfac()));
    	if(Pot.getValue().intValue()!=0) {
    		mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(),100,Pot.getValue().intValue()-1));
    	}else
    	{
    		mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
    	}
    	if(this.mode.getValue() == AutoPlayMode.Packet) {
    		mc.playerController.blockHitDelay = 0;
            if (mc.playerController.curBlockDamageMP >= FastDig.speed.getValue()) {
            	mc.playerController.curBlockDamageMP = 1.0f;
            }

    	}
    	if(this.mode.getValue() == AutoPlayMode.NewPacket2) {
 		   if(mc.playerController.curBlockDamageMP == 0.2f) {
 			  mc.playerController.curBlockDamageMP += 0.1f;
 		   }
 		   if(mc.playerController.curBlockDamageMP == 0.4f) {
 			  mc.playerController.curBlockDamageMP += 0.1f;
 		   }
 		   if(mc.playerController.curBlockDamageMP == 0.6f) {
 			  mc.playerController.curBlockDamageMP += 0.1f;
 		   }
 		   if(mc.playerController.curBlockDamageMP == 0.8f) {
 			  mc.playerController.curBlockDamageMP += 0.1f;
 		   }
 		   
 	}
 	
		   
	
    	if(this.mode.getValue() == AutoPlayMode.NewPacket) {
    		   if(mc.playerController.curBlockDamageMP == 0.1f) {
    			   mc.playerController.curBlockDamageMP += 0.1f;
    		   }
    		   if(mc.playerController.curBlockDamageMP == 0.4f) {
    			   mc.playerController.curBlockDamageMP += 0.1f;
    		   }
    		   if(mc.playerController.curBlockDamageMP == 0.7f) {
    			   mc.playerController.curBlockDamageMP += 0.1f;
    		   }

    		   
    	}
    	
 		   
 	
    	

            
 		   

    	
    }


    @EventHandler
    public void onDamageBlock(EventPacketSend event) {
    	
    	if(this.mode.getValue() == AutoPlayMode.Remix) {
        if (event.packet instanceof C07PacketPlayerDigging && !mc.playerController.extendedReach() && mc.playerController != null) {
            C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging)event.packet;
            if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                this.bzs = true;
                this.blockPos = c07PacketPlayerDigging.getPosition();
                this.facing = c07PacketPlayerDigging.getFacing();
                this.bzx = 0.0f;
            } else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.bzs = false;
                this.blockPos = null;
                this.facing = null;
            }
        }
    	}
    }
    
    @EventHandler
    public void onUpdate(EventPreUpdate event) {
    	this.setSuffix(mode.getValue());
    	if(this.mode.getValue() == AutoPlayMode.Remix) {
        if (mc.playerController.extendedReach()) {
            mc.playerController.blockHitDelay = 0;
        } else if (this.bzs) {
            Block block = this.mc.theWorld.getBlockState(this.blockPos).getBlock();
            this.bzx += (float)((double)block.getPlayerRelativeBlockHardness(mc.thePlayer, this.mc.theWorld, this.blockPos) * 1.4);
            if (this.bzx >= 1.0f) {
                this.mc.theWorld.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing));
                this.bzx = 0.0f;
                this.bzs = false;
            }
        }
    	}
    }
    @Override
    public void onDisable() {
        super.onDisable();
    }
    /*     */  public static enum AutoPlayMode {
    	/* 379 */     Packet,NewPacket,NewPacket2,Remix;
    	/*     */   }
}
