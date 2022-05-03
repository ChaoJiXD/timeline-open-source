package NovClient.Module.Modules.Move;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPostUpdate;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Combat.KillAura;
import NovClient.Util.PlayerUtil;
import NovClient.Util.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class NoSlow extends Module {

	public Option<Boolean> Ground = new Option("Ground", "Ground", Boolean.valueOf(true));
	private Mode<Enum> mode = new Mode("Mode", "Mode", (Enum[]) JMode.values(), (Enum) JMode.Vanilla);
	public TimerUtil timer = new TimerUtil();
	public static Numbers<Double> Speed = new Numbers<Double>("Speed", "Speed", 1.0, 0.0, 1.0, 0.1);
	public NoSlow() {
		super("NoSlow", new String[] { "NoSlow", "float" }, ModuleType.Move);
		this.setColor(new Color(188, 233, 248).getRGB());
		this.addValues(this.mode,this.Ground,Speed);
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@EventHandler
	public void onPost(EventPostUpdate event) {
		if(Ground.getValue())if(!mc.thePlayer.onGround)return;
		if(this.mode.getValue() == JMode.Vanilla) {
			return;
		}
		if(this.mode.getValue() == JMode.NCP) {
			if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving()  && ( KillAura.blockTarget == null )){
				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
			}
		}
		if(this.mode.getValue() == JMode.NCP2) {
			return;
		}


		if(this.mode.getValue() == JMode.AAC) {
			if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving()  && ( KillAura.blockTarget == null )){
				if(timer.delay(65)){
					mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
					timer.reset();
				}

			}
		}
	}

	@EventHandler
	public void onPre(EventPreUpdate e) {
		this.setSuffix(this.mode.getValue());
		if(Ground.getValue())if(!mc.thePlayer.onGround)return;
		if(this.mode.getValue() == JMode.Vanilla) {
			return;
		}

		if(this.mode.getValue() == JMode.NCP) {
			if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving() ){
				mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));

			}
		}
		if(this.mode.getValue() == JMode.NCP2) {
			if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving() ){
				e.setCancelled(true);
			}
		}
		if(this.mode.getValue() == JMode.AAC) {
			if(mc.thePlayer.onGround || isOnGround(0.5))
				mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		}
	}



	/*     */   public static boolean isOnGround(double height) {
		/* 293 */     Minecraft.getMinecraft().getMinecraft().getMinecraft(); if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
			/* 294 */       return true;
			/*     */     }
		/* 296 */     return false;
		/*     */   }







	static enum JMode {
		NCP, NCP2, Vanilla, AAC;
	}
}
