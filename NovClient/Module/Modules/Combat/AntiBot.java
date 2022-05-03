package NovClient.Module.Modules.Combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPacketReceive;
import NovClient.API.Events.World.EventPacketSend;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.TimeHelper;
import NovClient.Util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.main.Main;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.util.Timer;


public class AntiBot extends Module {
    private TimerUtil Packettimer = new TimerUtil();
   public Mode mode;
   public static ArrayList Bots = new ArrayList();
   private TimerUtil timer;
   int bots;
   private TimeHelper timert = new TimeHelper();
   private static List invalid = new ArrayList();
   private List<Packet> packetList = new CopyOnWriteArrayList();
   public static ArrayList bot = new ArrayList();
   private static ArrayList playersInGame = new ArrayList();
   private static List removed = new ArrayList();
   public TimeHelper lastRemoved = new TimeHelper();
   public TimeHelper timer2 = new TimeHelper();
   public TimeHelper timer3 = new TimeHelper();
   private final HashMap<Packet<?>, Long> packetsMap = new HashMap<>();

   public static List onAirInvalid = new ArrayList();
   //private Option<Boolean> Hypixel = new Option("Hypixel", "Hypixel", Boolean.valueOf(true));
   public AntiBot() {
      super("AntiBot", new String[]{"AntiBot"}, ModuleType.Combat);
      this.mode = new Mode("Mode", "Mode", AntiMode.values(), AntiMode.Hypixel);
      this.timer = new TimerUtil();
      this.bots = 0;
      
      this.addValues(this.mode);
   }
   public static boolean isInTablist(EntityPlayer player) {
	      if (Minecraft.getMinecraft().isSingleplayer()) {
	         return true;
	      } else {
	         Minecraft.getMinecraft();
	         Iterator var2 = mc.getNetHandler().getPlayerInfoMap().iterator();

	         while(var2.hasNext()) {
	            Object o = var2.next();
	            NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)o;
	            if (playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) {
	               return true;
	            }
	         }

	         return false;
	      }
	   }
   @EventHandler
   public void onUpdate(EventPreUpdate event) {
      this.setSuffix(this.mode.getValue());
      Minecraft var10000;
      if (this.mode.getValue() == AntiMode.Hypixel2) {
         var10000 = mc;
         Iterator var3 = mc.theWorld.playerEntities.iterator();

         Minecraft var10001;
         while(var3.hasNext()) {
            EntityPlayer entity = (EntityPlayer)var3.next();
            var10001 = mc;
            if (entity != mc.thePlayer && entity != null && entity instanceof EntityLivingBase) {
               var10001 = mc;
               if (entity != mc.thePlayer && entity instanceof EntityPlayer && !isInTablist(entity) && !entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc") && entity.getDisplayName().getFormattedText().startsWith("\247")) {
                  var10000 = mc;
                  mc.theWorld.removeEntity(entity);
               }
            }
         }

         if ( !removed.isEmpty() && this.lastRemoved.isDelayComplete(1000L)) {
            this.lastRemoved.reset();
            removed.clear();
         }



         var10000 = mc;
         var3 = mc.theWorld.getLoadedEntityList().iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            if (o instanceof EntityPlayer) {
               EntityPlayer ent = (EntityPlayer)o;
               var10001 = mc;
               if (ent != mc.thePlayer && !invalid.contains(ent)) {
                  String formated = ent.getDisplayName().getFormattedText();
                  String custom = ent.getCustomNameTag();
                  String name = ent.getName();
                  if (ent.isInvisible() && !formated.startsWith("¡ìc") && formated.endsWith("¡ìr") && custom.equals(name)) {
                     var10001 = mc;
                     double diffX = Math.abs(ent.posX - mc.thePlayer.posX);
                     var10001 = mc;
                     double diffY = Math.abs(ent.posY - mc.thePlayer.posY);
                     var10001 = mc;
                     double diffZ = Math.abs(ent.posZ - mc.thePlayer.posZ);
                     double diffH = Math.sqrt(diffX * diffX + diffZ * diffZ);
                     if (diffY < 13.0D && diffY > 10.0D && diffH < 3.0D) {
                        List list = getTabPlayerList();
                        if (!list.contains(ent)) {
                              this.lastRemoved.reset();
                              removed.add(ent);
                              var10000 = mc;
                              mc.theWorld.removeEntity(ent);
                           

                           invalid.add(ent);
                        }
                     }
                  }

                  if (!formated.startsWith("\247") && formated.endsWith("¡ìr")) {
                     invalid.add(ent);
                  }

                  if (!isInTablist(ent)) {
                     invalid.add(ent);
                  }




                  if (ent.isInvisible() && !custom.equalsIgnoreCase("") && custom.toLowerCase().contains("¡ìc¡ìc") && name.contains("¡ìc")) {
                        this.lastRemoved.reset();
                        removed.add(ent);
                        var10000 = mc;
                        mc.theWorld.removeEntity(ent);
                     

                     invalid.add(ent);
                  }

                  if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("¡ìc") && custom.toLowerCase().contains("¡ìr")) {
                        this.lastRemoved.reset();
                        removed.add(ent);
                        var10000 = mc;
                        mc.theWorld.removeEntity(ent);
                     

                     invalid.add(ent);
                  }

                  if (formated.contains("¡ì8[NPC]")) {
                     invalid.add(ent);
                  }

                  if (!formated.contains("¡ìc") && !custom.equalsIgnoreCase("")) {
                     invalid.add(ent);
                  }
               }
            }
         }

      }
      if(this.mode.getValue() == AntiMode.Hypixel) {
         Minecraft mc2 = AntiBot.mc;
         Iterator var4 = mc.theWorld.loadedEntityList.iterator();

         label63:
         while(true) {
            Object entity2;
            EntityPlayer entityPlayer;
            EntityPlayer ent;
            do {
               if(!var4.hasNext()) {
                  this.bots = 0;
                  var4 = mc.theWorld.getLoadedEntityList().iterator();

                  while(var4.hasNext()) {
                     Entity entity21 = (Entity)var4.next();
                     if(entity21 instanceof EntityPlayer) {
                        ent = entityPlayer = (EntityPlayer)entity21;
                        if(entityPlayer != mc.thePlayer && ent.isInvisible() && ent.ticksExisted > 105) {
                           if(getTabPlayerList().contains(ent)) {
                              if(ent.isInvisible()) {
                                 ent.setInvisible(false);
                              }
                           } else {
                              ent.setInvisible(false);
                              ++this.bots;
                              mc.theWorld.removeEntity(ent);
                           }
                        }
                     }
                  }
                  break label63;
               }

               entity2 = var4.next();
            } while(!(entity2 instanceof EntityPlayer));

            ent = entityPlayer = (EntityPlayer)entity2;
            if(entityPlayer != mc.thePlayer) {
               Minecraft mc4 = AntiBot.mc;
               if(mc.thePlayer.getDistanceToEntity(ent) < 10.0F && (!ent.getDisplayName().getFormattedText().contains("à¸¢à¸‡") || ent.isInvisible() || ent.getDisplayName().getFormattedText().toLowerCase().contains("npc") || ent.getDisplayName().getFormattedText().toLowerCase().contains("Â§r"))) {
                  Bots.add(ent);
               }
            }

            if(Bots.contains(ent)) {
               Bots.remove(ent);
            }
         }
      }

   }
   @EventHandler
   public void onReceivePacket(EventPacketReceive event) {
      if (event.getPacket() instanceof S14PacketEntity) {
         S14PacketEntity packet = (S14PacketEntity)event.getPacket();
         Minecraft var10001 = mc;
         Entity entity;
         if ((entity = packet.getEntity(mc.theWorld)) instanceof EntityPlayer && !packet.getOnGround() && !onAirInvalid.contains(entity.getEntityId())) {
            onAirInvalid.add(entity.getEntityId());
         }
      }

   }
   public boolean isInGodMode(Entity entity) {
      return this.isEnabled() && this.mode.getValue() == AntiMode.Hypixel && entity.ticksExisted <= 25;
   }

   public boolean isServerBot(Entity entity) {
      if(this.isEnabled()) {
          if (this.mode.getValue() == AntiMode.Hypixel2) {
              return invalid.contains(entity) || !onAirInvalid.contains(entity.getEntityId());
           }
         if(this.mode.getValue() == AntiMode.Hypixel) {
            if(entity.getDisplayName().getFormattedText().startsWith("¡ì") && !(entity instanceof EntityArmorStand) && !entity.getDisplayName().getFormattedText().contains("[NPC]") && !this.isInGodMode(entity)) {
               return false;
            }

            return true;
         }

         if(this.mode.getValue() == AntiMode.Mineplex) {
            Minecraft mc = AntiBot.mc;
            Iterator var4 = mc.theWorld.playerEntities.iterator();

            while(var4.hasNext()) {
               Object object = var4.next();
               EntityPlayer entityPlayer = (EntityPlayer)object;
               if(entityPlayer != null) {
                  Minecraft mc2 = AntiBot.mc;
                  if(entityPlayer != mc.thePlayer && (entityPlayer.getName().startsWith("Body #") || entityPlayer.getMaxHealth() == 20.0F)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public void onEnable() {
      Bots.clear();
   }
   


   public void onDisable() {
      Bots.clear();
   }

   public static List getTabPlayerList() {
      Minecraft mc = AntiBot.mc;
      NetHandlerPlayClient var4 = mc.thePlayer.sendQueue;
      ArrayList list = new ArrayList();
      List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(var4.getPlayerInfoMap());
      Iterator var5 = players.iterator();

      while(var5.hasNext()) {
         Object o = var5.next();
         NetworkPlayerInfo info = (NetworkPlayerInfo)o;
         if(info != null) {
            Minecraft mc2 = AntiBot.mc;
            list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
         }
      }

      return list;
   }
   

enum AntiMode {
   Hypixel,
   Mineplex, Hypixel2;

}

}
