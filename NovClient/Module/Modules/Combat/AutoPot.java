/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Combat;

import java.awt.Color;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.Timer;
import NovClient.Util.Math.RotationUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;



public class AutoPot
extends Module {
	private Option<Boolean> REGEN = new Option<Boolean>("REGEN", "REGEN", true);
	private Option<Boolean> SPEED = new Option<Boolean>("SPEED", "SPEED", true);
	private Option<Boolean> PREDICT = new Option<Boolean>("PREDICT", "PREDICT", true);
	private Numbers<Double> HEALTH = new Numbers<Double>("HEALTH", "HEALTH", 6.0, 0.5, 10.0, 0.5);
	public AutoPot() {
        super("AutoPot", new String[]{"AutoPot"}, ModuleType.Combat);
        super.addValues(this.REGEN,this.SPEED,this.PREDICT,this.HEALTH);
        this.setColor(new Color(208, 30, 142).getRGB());
    }
    public static boolean potting;
    Timer timer = new Timer();
    @Override
    public void onEnable() {
        super.onEnable();
    }
    public static boolean isPotting(){
    	return potting;
    }
    @EventHandler
    private void onUpdate(EventPreUpdate em) {
        if(mc.thePlayer.onGround == false)return;
        final boolean speed = this.SPEED.getValue();
        final boolean regen = this.REGEN.getValue();

        if(timer.delay(200)){
        	if(potting)
        		potting = false;
        }
        int spoofSlot = getBestSpoofSlot();
        int pots[] = {6,-1,-1};
        if(regen)
        	pots[1] = 10;
        if(speed)
        	pots[2] = 1;
        
        for(int i = 0; i < pots.length; i ++){
        	if(pots[i] == -1)
        		continue;
        	if(pots[i] == 6 || pots[i] == 10){
        		if(timer.delay(900) && !mc.thePlayer.isPotionActive(pots[i])){
            		if(mc.thePlayer.getHealth() < this.HEALTH.getValue().doubleValue()*2){
            			getBestPot(spoofSlot, pots[i]);
            		}
        		}
        	}else
        	if(timer.delay(1000) && !mc.thePlayer.isPotionActive(pots[i])){
        		getBestPot(spoofSlot, pots[i]);               		
        	}
        }       
    }
    public void swap(int slot1, int hotbarSlot){
    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
    }
    float[] getRotations(){    	
        double movedPosX = mc.thePlayer.posX + mc.thePlayer.motionX * 26.0D;
        double movedPosY = mc.thePlayer.boundingBox.minY - 3.6D;
        double movedPosZ = mc.thePlayer.posZ + mc.thePlayer.motionZ * 26.0D;	
        if(this.PREDICT.getValue())
        	return RotationUtil.getRotationFromPosition(movedPosX, movedPosZ, movedPosY);
        else
        	return new float[]{mc.thePlayer.rotationYaw, 90};
    }
    int getBestSpoofSlot(){  	
    	int spoofSlot = 5;
    	for (int i = 36; i < 45; i++) {       		
    		if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
     			spoofSlot = i - 36;
     			break;
            }else if(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemPotion) {
            	spoofSlot = i - 36;
     			break;
            }
        }
    	return spoofSlot;
    }
    void getBestPot(int hotbarSlot, int potID){
    	if(KillAura.blockTarget !=null)return;
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() &&(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(is.getItem() instanceof ItemPotion){
              	  ItemPotion pot = (ItemPotion)is.getItem();
              	  if(pot.getEffects(is).isEmpty())
              		  return;
              	  PotionEffect effect = (PotionEffect) pot.getEffects(is).get(0);              	  
                  int potionID = effect.getPotionID();
                  if(potionID == potID)
              	  if(ItemPotion.isSplash(is.getItemDamage()) && isBestPot(pot, is)){
              		  if(36 + hotbarSlot != i)
              			  swap(i, hotbarSlot);
              		  timer.reset();
              		  boolean canpot = true;
              		  int oldSlot = mc.thePlayer.inventory.currentItem;
              		  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(hotbarSlot));
          			  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations()[0], getRotations()[1], mc.thePlayer.onGround));
          			  mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
          			  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));
          			  potting = true;
          			  break;
              	  }               	  
                }              
            }
        }
    }
    
    boolean isBestPot(ItemPotion potion, ItemStack stack){
    	if(potion.getEffects(stack) == null || potion.getEffects(stack).size() != 1)
    		return false;
        PotionEffect effect = (PotionEffect) potion.getEffects(stack).get(0);
        int potionID = effect.getPotionID();
        int amplifier = effect.getAmplifier(); 
        int duration = effect.getDuration();
    	for (int i = 9; i < 45; i++) {    		
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {           	
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(is.getItem() instanceof ItemPotion){
                	ItemPotion pot = (ItemPotion)is.getItem();
                	 if (pot.getEffects(is) != null) {
                         for (Object o : pot.getEffects(is)) {
                             PotionEffect effects = (PotionEffect) o;
                             int id = effects.getPotionID();
                             int ampl = effects.getAmplifier(); 
                             int dur = effects.getDuration();
                             if (id == potionID && ItemPotion.isSplash(is.getItemDamage())){
                            	 if(ampl > amplifier){
                            		 return false;
                            	 }else if (ampl == amplifier && dur > duration){
                            		 return false;
                            	 }
                             }                            
                         }
                     }
                }
            }
        }
    	return true;
    }
}

