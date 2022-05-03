/*    */ package NovClient.Module.Modules.Move;
/*    */ import java.awt.Color;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventMove;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
/*    */ 
/*    */ public class AntiFall extends Module {
/*    */   private boolean saveMe;
/*    */   
/* 29 */   private TimerUtil timer = new TimerUtil();
/*    */   
/* 30 */   private Mode<Enum> mode = new Mode("Mode", "Mode", AntiMode.values(), AntiMode.Motion);
/*    */   
/* 31 */   private Option<Boolean> ov = new Option("OnlyVoid", "OnlyVoid", Boolean.valueOf(true));
/*    */   
/* 32 */   private static Numbers<Double> distance = new Numbers("Distance", "Distance", Double.valueOf(5.0D), Double.valueOf(1.0D), Double.valueOf(25.0D), Double.valueOf(1.0D));
/*    */   
/*    */   public AntiFall() {
/* 35 */     super("AntiFall", new String[] { "AntiFall", "antifall" }, ModuleType.Move);
/* 36 */     setColor((new Color(223, 233, 233)).getRGB());
/* 37 */     addValues( this.ov, distance, this.mode );
/*    */   }
/*    */   
/*    */   private boolean isBlockUnder() {
/* 41 */     if (mc.thePlayer.posY < 0.0D)
/* 42 */       return false; 
/* 43 */     for (int off = 0; off < (int)mc.thePlayer.posY + 2; off += 2) {
/* 44 */       AxisAlignedBB bb = mc.thePlayer.boundingBox.offset(0.0D, -off, 0.0D);
/* 45 */       if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty())
/* 46 */         return true; 
/*    */     } 
/* 49 */     return false;
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   private void onMove(EventMove e) {
/* 54 */     if (mc.thePlayer.fallDistance > ((Double)distance.getValue()).doubleValue())
/* 55 */       if ( !Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled() && (
/* 56 */         !((Boolean)this.ov.getValue()).booleanValue() || !isBlockUnder())) {
/* 57 */         if (!this.saveMe) {
/* 58 */           this.saveMe = true;
/* 59 */           this.timer.reset();
/*    */         } 
/* 61 */         //mc.thePlayer.fallDistance = 0.0F;
/* 62 */         if (this.mode.getValue() == AntiMode.Hypixel) {
/* 63 */           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 
/* 64 */                 mc.thePlayer.posY + 10.0D, mc.thePlayer.posZ, false));
/* 65 */         } else if (this.mode.getValue() == AntiMode.Motion) {
	e.setY(2.0);
	//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + distance.getValue() + 1, mc.thePlayer.posZ);
/*    */         } 
/*    */       }  

/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   private void onUpdate(EventPreUpdate e) {
/* 77 */     setSuffix(this.mode.getValue());
/* 78 */     if ((this.saveMe && this.timer.delay(150.0F)) || mc.thePlayer.isCollidedVertically) {
/* 79 */       this.saveMe = false;
/* 80 */       this.timer.reset();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onEnable() {

/*    */   }
/*    */   
/*    */   enum AntiMode {
/* 92 */     Motion, Hypixel;
/*    */   }
/*    */ }


/* Location:              C:\Users\Administrator\Documents\Tencent Files\1719993693\FileRecv\MobileFile\lac.LEFgangaDEV.LacClient ÎÞ»ìÏý 100%½âÃÜ.jar!\lac.LEFgangaDEV.LacClient\Module\Modules\Move\AntiFall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */