package NovClient.Module.Modules.Misc;

import java.util.ArrayList;

import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Move.Scaffold;
import NovClient.Util.TimerUtil;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Mode;

/*
 * Made by LeakedPvP
 */
public class InvCleaner extends Module {
private Numbers<Double> BlockCap = new Numbers("BlockCap", "BlockCap", Double.valueOf(128.0D), Double.valueOf(0.0D), Double.valueOf(256.0D), Double.valueOf(8.0D));
private Numbers<Double> Delay = new Numbers("Delay", "Delay", Double.valueOf(1.0D), Double.valueOf(0.0D), Double.valueOf(10.0D), Double.valueOf(1.0D));
private Option<Boolean> Food = new Option("Food", "Food", Boolean.valueOf(true));
private Option<Boolean> sort = new Option("sort", "sort", Boolean.valueOf(true));
private Option<Boolean> Archery = new Option("Archery", "Archery", Boolean.valueOf(true));
private Option<Boolean> Sword = new Option("Sword", "Sword", Boolean.valueOf(true));
private Mode<Enum> Mode = new Mode("Mode", "Mode", (Enum[]) EMode.values(), (Enum) EMode.Basic);
private Option<Boolean> InvCleaner = new Option("InvCleaner", "InvCleaner", Boolean.valueOf(true));
private Option<Boolean> UHC = new Option("UHC", "UHC", Boolean.valueOf(false));
    
    public InvCleaner() {   	   
		super("InvCleaner", new String[] { "InvCleaner" }, ModuleType.Misc);
		this.addValues(BlockCap, Delay, Food, Archery, Sword,Mode,InvCleaner,sort,UHC);
    }
    
    public static int weaponSlot = 36, pickaxeSlot = 37, axeSlot = 38, shovelSlot = 39;
    TimerUtil timer = new TimerUtil();
    ArrayList<Integer>whitelistedItems = new ArrayList<>();

    public void onEnable() {
        super.onEnable();
    }
    @EventHandler
    public void onEvent(EventPreUpdate event) {
    	if(mc.thePlayer.openContainer instanceof ContainerChest && mc.currentScreen instanceof GuiContainer)return;
    	InvCleaner i3 = (InvCleaner)Client.instance.getModuleManager().getModuleByClass(InvCleaner.class);
        long delay = this.Delay.getValue().longValue()*50;
        EventPreUpdate em =(EventPreUpdate)event;
            this.setSuffix(this.Mode.getValue());
            long Adelay = AutoArmor.DELAY.getValue().longValue()*50;
            
            if(timer.hasReached(Adelay) && i3.isEnabled()){
            	 if(!(i3.Mode.getValue() == AutoArmor.EMode.OpenInv) || mc.currentScreen instanceof GuiInventory){
                     if(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat){
                     }
                 }
            }
            if( i3.isEnabled())
            for(int type = 1; type < 5; type++){
        		if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()){
        			ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
        			if(!AutoArmor.isBestArmor(is, type)){

        			}
        		}else if(invContainsType(type-1)){

        		}
            }
            if(this.Mode.getValue() == EMode.OpenInv && !(mc.currentScreen instanceof GuiInventory)){
            	return;
            }
            
            if(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat){
            	if(timer.hasReached(delay) && weaponSlot >= 36){         

            		if (!mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()){

                	
            		}else{
            			if(!isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())){           			

                		}
            		}
            	}
            	if(sort.getValue()) {
                	if(timer.hasReached(delay) && pickaxeSlot >= 36){
                		getBestPickaxe(pickaxeSlot);
                	}
                	if(timer.hasReached(delay) && shovelSlot >= 36){
                		getBestShovel(shovelSlot);
                	}
                	if(timer.hasReached(delay) && axeSlot >= 36){
                		getBestAxe(axeSlot);
                	}
            	}

            	if(timer.hasReached(delay) && this.InvCleaner.getValue())
                for (int i = 9; i < 45; i++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if(shouldDrop(is, i)){
                        	drop(i);
                        	timer.reset();
                        	if(delay > 0)
                        	break;
                        }
                    }
                }              
            }
    	
    }
    public void shiftClick(int slot){
    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
    }
    public void swap(int slot1, int hotbarSlot){
    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
    }
    public void drop(int slot){
    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
    }
    public boolean isBestWeapon(ItemStack stack){
    	float damage = getDamage(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getDamage(is) > damage && (is.getItem() instanceof ItemSword || !this.Sword.getValue()))
                	return false;
            }
        }
    	if((stack.getItem() instanceof ItemSword || !this.Sword.getValue())){
    		return true;
    	}else{
    		return false;
    	}
    	
    }

    public void getBestWeapon(int slot){
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(isBestWeapon(is) && getDamage(is) > 0 && (is.getItem() instanceof ItemSword || !this.Sword.getValue())){
                	swap(i, slot - 36);
            		timer.reset();
                	break;
                }
            }
        }
    }
 
    private float getDamage(ItemStack stack) {
    	float damage = 0;
    	Item item = stack.getItem();
    	if(item instanceof ItemTool){
    		ItemTool tool = (ItemTool)item;
    		damage += tool.getMaxDamage();
    	}
    	if(item instanceof ItemSword){
    		ItemSword sword = (ItemSword)item;
    		damage += sword.getDamageVsEntity();
    	}
    	damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + 
    			EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
        return damage;
    }
    public boolean shouldDrop(ItemStack stack, int slot){
    	
    	if(stack.getDisplayName().contains("µã»÷")){
    		return false;
    	}
    	if(stack.getDisplayName().contains("ÓÒ¼ü")){
    		return false;
    	}
    	if(stack.getDisplayName().toLowerCase().contains("(right click)")){
    		return false;
    	}
    	if(UHC.getValue()) {
        	if(stack.getDisplayName().toLowerCase().contains("Í·")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("apple")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("head")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("gold")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("crafting table")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("stick")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("and") && stack.getDisplayName().toLowerCase().contains("ril")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("axe of perun")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("barbarian")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("bloodlust")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("dragonchest")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("dragon sword")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("dragon armor")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("excalibur")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("exodus")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("fusion armor")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("hermes boots")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("hide of leviathan")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("scythe")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("seven-league boots")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("shoes of vidar")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("apprentice")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("master")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("vorpal")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("enchanted")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("spiked")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("tarnhelm")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("philosopher")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("anvil")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("panacea")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("fusion")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("excalibur")){
        		return false;
        	}
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	if(stack.getDisplayName().toLowerCase().contains("Ñ§Í½")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("´óÊ¦ÂÞÅÌ")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("Õ¶Ê×Ö®½£")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("¸½Ä§")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("¾ÞÁúÖ®½£")){
        		return false;
        	}
        	
        	if(stack.getDisplayName().toLowerCase().contains("¾ÞÁúÖ®¼×")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÈÐ¼×")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("Æß¹úÕ½Ñ¥")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("±ù¶·ºþ")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÕÜÈË")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÌúÕè")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("Æ»¹û")){
        		return false;
        	}
        	
        	if(stack.getDisplayName().toLowerCase().contains("½ð")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÓÀÉúÖ®¾Æ")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("Çð±ÈÌØÖ®¹­")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("¶ÍÂ¯")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("backpack")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("¾Û±äÖ®¼×")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("±³°ü")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÔÂÉñ")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÓÀÉú")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("³±Ï«")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("À×¸«")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÍõÕßÖ®½£")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("°²¶¼Èð¶û")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ËÀÉñÁ­µ¶")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("·áÈÄÖ®½Ç")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("Î¬´ïÕ½Ñ¥")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("¶á»êÖ®ÈÐ")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÂùÈËÖ®¼×")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÇÔÔôÖ®Ñ¥")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("hermes")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("barbarian")){
        		return false;
        	}
    	}


    	
    	if((slot == weaponSlot && isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) ||
    			(slot == pickaxeSlot && isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack()) && pickaxeSlot >= 0) ||
    			(slot == axeSlot && isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack()) && axeSlot >= 0) ||
    			(slot == shovelSlot && isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack()) && shovelSlot >= 0) ){
    		return false;
    	}
    	if(stack.getItem() instanceof ItemArmor){
    		for(int type = 1; type < 5; type++){
    			if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()){
        			ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
        			if(AutoArmor.isBestArmor(is, type)){
        				continue;
        			}
        		}
    			if(AutoArmor.isBestArmor(stack, type)){
    				return false;
    			}
    		}
    	}
    	if (this.BlockCap.getValue().intValue()!=0 && stack.getItem() instanceof ItemBlock &&
    			(getBlockCount() > this.BlockCap.getValue().intValue() || 
    					Scaffold.blacklistedBlocks.contains(((ItemBlock)stack.getItem()).getBlock()))){
    		return true;
    	}
    	if(stack.getItem() instanceof ItemPotion){
    		if(isBadPotion(stack)){
    			return true;
    		}
    	}
    	if(stack.getItem() instanceof ItemFood && this.Food.getValue() && !(stack.getItem() instanceof ItemAppleGold)){
    		return true;
    	}
    	if(stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor){
    		return true;
    	}
    	
    	if(((stack.getItem().getUnlocalizedName().contains("tnt")) ||
                        (stack.getItem().getUnlocalizedName().contains("stick")) ||
                        (stack.getItem().getUnlocalizedName().contains("egg")) ||
                        (stack.getItem().getUnlocalizedName().contains("string")) ||
                        (stack.getItem().getUnlocalizedName().contains("cake")) ||
                        (stack.getItem().getUnlocalizedName().contains("mushroom")) ||
                        (stack.getItem().getUnlocalizedName().contains("flint")) ||
                        (stack.getItem().getUnlocalizedName().contains("compass")) ||
                        (stack.getItem().getUnlocalizedName().contains("dyePowder")) ||
                        (stack.getItem().getUnlocalizedName().contains("feather")) ||
                        (stack.getItem().getUnlocalizedName().contains("bucket")) ||
                        (stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) ||
                        (stack.getItem().getUnlocalizedName().contains("snow")) ||
                        (stack.getItem().getUnlocalizedName().contains("fish")) ||
                        (stack.getItem().getUnlocalizedName().contains("enchant")) ||
                        (stack.getItem().getUnlocalizedName().contains("exp")) ||
                        (stack.getItem().getUnlocalizedName().contains("shears")) ||
                        (stack.getItem().getUnlocalizedName().contains("anvil")) ||
                        (stack.getItem().getUnlocalizedName().contains("torch")) ||
                        (stack.getItem().getUnlocalizedName().contains("seeds")) ||
                        (stack.getItem().getUnlocalizedName().contains("leather")) ||
                        (stack.getItem().getUnlocalizedName().contains("reeds")) ||
                        (stack.getItem().getUnlocalizedName().contains("skull")) ||
                        (stack.getItem().getUnlocalizedName().contains("record")) ||
                        (stack.getItem().getUnlocalizedName().contains("snowball")) ||
                        (stack.getItem() instanceof ItemGlassBottle) ||
                        (stack.getItem().getUnlocalizedName().contains("piston")))){
    		return true;
    	}            
    	
    	return false;
    }
    public ArrayList<Integer>getWhitelistedItem(){
    	return whitelistedItems;
    }
    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !Scaffold.blacklistedBlocks.contains(((ItemBlock) item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }
    
    private void getBestPickaxe(int slot){
    	for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				
				if(isBestPickaxe(is) && pickaxeSlot != i){
					if(!isBestWeapon(is))
					if(!mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()){	
						swap(i, pickaxeSlot - 36);
						timer.reset();
    					if(this.Delay.getValue().longValue() > 0)
    						return;
					}else if(!isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())){
						swap(i, pickaxeSlot - 36);
						timer.reset();
    					if(this.Delay.getValue().longValue() > 0)
    						return;
					}
				
				}
			}
    	}
    }
    private void getBestShovel(int slot){
    	for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				
				if(isBestShovel(is) && shovelSlot != i){
					if(!isBestWeapon(is))
					if(!mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()){
						swap(i, shovelSlot - 36);
						timer.reset();
    					if(this.Delay.getValue().longValue() > 0)
    						return;
					}else if(!isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())){	
						swap(i, shovelSlot - 36);
						timer.reset();
    					if(this.Delay.getValue().longValue() > 0)
    						return;
					}
				
				}
			}
    	}
    }
    private void getBestAxe(int slot){

    	for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				
				if(isBestAxe(is) && axeSlot != i){
					if(!isBestWeapon(is))
					if(!mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()){
						swap(i, axeSlot - 36);
						timer.reset();
    					if(this.Delay.getValue().longValue() > 0)
    						return;
					}else if(!isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())){
						swap(i, axeSlot - 36);
						timer.reset();
    					if(this.Delay.getValue().longValue() > 0)
    						return;
					}
				
				}
			}
    	}
    }
    private boolean isBestPickaxe(ItemStack stack){
     	Item item = stack.getItem();
    	if(!(item instanceof ItemPickaxe))
    		return false;
    	float value = getToolEffect(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe){                	
                	return false;
                }
                	
            }
        }
    	return true;
    }
    private boolean isBestShovel(ItemStack stack){
    	Item item = stack.getItem();
    	if(!(item instanceof ItemSpade))
    		return false;
    	float value = getToolEffect(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getToolEffect(is) > value && is.getItem() instanceof ItemSpade){                	
                	return false;
                }
                	
            }
        }
    	return true;
    }
    private boolean isBestAxe(ItemStack stack){
    	Item item = stack.getItem();
    	if(!(item instanceof ItemAxe))
    		return false;
    	float value = getToolEffect(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)){                
                	return false;
                }
                	
            }
        }
    	return true;
    }
    private float getToolEffect(ItemStack stack){
    	Item item = stack.getItem();
    	if(!(item instanceof ItemTool))
    		return 0;
    	String name = item.getUnlocalizedName();
    	ItemTool tool = (ItemTool)item;
    	float value = 1;
    	if(item instanceof ItemPickaxe){
    		value = tool.getStrVsBlock(stack, Blocks.stone);
    		if(name.toLowerCase().contains("gold")){
    			value -= 5;
    		}
    	}else if(item instanceof ItemSpade){
    		value = tool.getStrVsBlock(stack, Blocks.dirt);
    		if(name.toLowerCase().contains("gold")){
    			value -= 5;
    		}
    	}else if(item instanceof ItemAxe){
    		value = tool.getStrVsBlock(stack, Blocks.log);
    		if(name.toLowerCase().contains("gold")){
    			value -= 5;
    		}
    	}else
    		return 1f;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)/100d;
    	return value;
    }
    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if(potion.getEffects(stack) == null)
            	return true;
            for (final Object o : potion.getEffects(stack)) {
                final PotionEffect effect = (PotionEffect) o;
                if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                	return true;
                }
            }
        }
        return false;
    }
    boolean invContainsType(int type){
    	
    	for(int i = 9; i < 45; i++){
    		if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = is.getItem();
				if(item instanceof ItemArmor){
					ItemArmor armor = (ItemArmor)item;
					if(type == armor.armorType){
						return true;
					}	
				}
    		}
    	}
    	return false;
    }
    public void getBestArmor(){
    	for(int type = 1; type < 5; type++){
    		if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()){
    			ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
    			if(AutoArmor.isBestArmor(is, type)){
    				continue;
    			}else{
    				drop(4 + type);
    			}
    		}
    		for (int i = 9; i < 45; i++) {
    			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
    				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
    				if(AutoArmor.isBestArmor(is, type) && AutoArmor.getProtection(is) > 0){
    					shiftClick(i);
    					timer.reset();
    					if(this.Delay.getValue().longValue() > 0)
    						return;
    				}
    			}
            }
        }
    }
    static enum EMode {
    	Basic, OpenInv;
    }
}
