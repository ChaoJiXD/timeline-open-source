/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Misc;




import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventTick;
import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.TimerUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.StatCollector;

public class ChestSteal extends Module {
	private Numbers<Double> delay = new Numbers<Double>("Delay", "delay", 80.0, 0.0, 1000.0, 10.0);
	private TimerUtil timer = new TimerUtil();

	public ChestSteal() {
		super("ChestSteal", new String[] { "ChestStealer", "chests", "stealer" }, ModuleType.Misc);
		addValues(delay);
	}

	@EventHandler
	private void onUpdate(EventTick event) {
		if (mc.thePlayer != null && mc.thePlayer.openContainer != null
				&& mc.thePlayer.openContainer instanceof ContainerChest) {
			ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
			if (!(StatCollector.translateToLocal("container.chest")
					.equalsIgnoreCase(container.getLowerChestInventory().getDisplayName().getUnformattedText())
					|| StatCollector.translateToLocal("container.chestDouble")
							.equalsIgnoreCase(container.getLowerChestInventory().getDisplayName().getUnformattedText())
					|| StatCollector.translateToLocal("container.chest").equalsIgnoreCase("LOW"))) {
				return;
			}
			int i = 0;
			while (i < container.getLowerChestInventory().getSizeInventory()) {
				if (container.getLowerChestInventory().getStackInSlot(i) != null && timer.hasReached(delay.getValue())
						&& (container.getLowerChestInventory().getStackInSlot(i).getItem() instanceof ItemArmor
								? betterCheck(container, container.getLowerChestInventory().getStackInSlot(i), i)
								: true)
						&& (container.getLowerChestInventory().getStackInSlot(i).getItem() instanceof ItemSword
								? getDamage(container.getLowerChestInventory().getStackInSlot(i)) >= this
										.bestDamage(container, i)
								: true)) {
					mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
					mc.playerController.windowClick(container.windowId, i, 1, 1, mc.thePlayer);
					timer.reset();
				}
				++i;
			}
			if (isEmpty()) {
				mc.thePlayer.closeScreen();
			}
		}
	}

	private boolean isEmpty() {
		if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
			ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
			int i = 0;
			while (i < container.getLowerChestInventory().getSizeInventory()) {
				ItemStack itemStack = container.getLowerChestInventory().getStackInSlot(i);
				if (itemStack != null && itemStack.getItem() != null) {
					if (itemStack.getItem() instanceof ItemArmor ? betterCheck(container, itemStack, i) : true)
						if (itemStack.getItem() instanceof ItemSword ? getDamage(itemStack) >= bestDamage(container, i)
								: true)
							return false;
				}
				++i;
			}
		}
		return true;
	}

	private boolean betterCheck(ContainerChest c, ItemStack item, int slot) {
		double item1 = ((ItemArmor) item.getItem()).damageReduceAmount + getProtectionValue(item);
		double item2 = 0;
		int bestslot = 0;
		if (item.getUnlocalizedName().contains("helmet")) {
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && mc.thePlayer.inventoryContainer
						.getSlot(i).getStack().getItem().getUnlocalizedName().contains("helmet")) {
					double temp = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount
							+ getProtectionValue((mc.thePlayer.inventoryContainer.getSlot(i).getStack()));
					if (temp > item2) {
						item2 = temp;
						bestslot = i;
					}
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null
						&& c.getLowerChestInventory().getStackInSlot(i).getUnlocalizedName().contains("helmet")) {
					double temp = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount
							+ getProtectionValue((c.getLowerChestInventory().getStackInSlot(i)));
					if (temp > item2) {
						item2 = temp;
						bestslot = i;
					}
				}
			}
		} // Í·¿ø
		if (item.getUnlocalizedName().contains("chestplate")) {
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && mc.thePlayer.inventoryContainer
						.getSlot(i).getStack().getItem().getUnlocalizedName().contains("chestplate")) {
					double temp = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount
							+ getProtectionValue((mc.thePlayer.inventoryContainer.getSlot(i).getStack()));
					if (temp > item2) {
						item2 = temp;
						bestslot = i;
					}
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null
						&& c.getLowerChestInventory().getStackInSlot(i).getUnlocalizedName().contains("chestplate")) {
					double temp = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount
							+ getProtectionValue((c.getLowerChestInventory().getStackInSlot(i)));
					if (temp > item2) {
						item2 = temp;
						bestslot = i;
					}
				}
			}
		} // ÐØ¼×
		if (item.getUnlocalizedName().contains("leggings")) {
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && mc.thePlayer.inventoryContainer
						.getSlot(i).getStack().getItem().getUnlocalizedName().contains("leggings")) {
					double temp = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount
							+ getProtectionValue((mc.thePlayer.inventoryContainer.getSlot(i).getStack()));
					if (temp > item2) {
						item2 = temp;
						bestslot = i;
					}
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null
						&& c.getLowerChestInventory().getStackInSlot(i).getUnlocalizedName().contains("leggings")) {
					double temp = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount
							+ getProtectionValue((c.getLowerChestInventory().getStackInSlot(i)));
					if (temp > item2) {
						item2 = temp;
						bestslot = i;
					}
				}
			}
		} // »¤ÍÈ
		if (item.getUnlocalizedName().contains("boots")) {
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && mc.thePlayer.inventoryContainer
						.getSlot(i).getStack().getItem().getUnlocalizedName().contains("boots")) {
					double temp = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount
							+ getProtectionValue((mc.thePlayer.inventoryContainer.getSlot(i).getStack()));
					if (temp > item2) {
						item2 = temp;
						bestslot = i;
					}
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null
						&& c.getLowerChestInventory().getStackInSlot(i).getUnlocalizedName().contains("boots")) {
					double temp = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount
							+ getProtectionValue((c.getLowerChestInventory().getStackInSlot(i)));
					if (temp > item2) {
						item2 = temp;
						bestslot = i;
					}
				}
			}
		} // Ð¬×Ó
		return item1 >= item2 && c.getLowerChestInventory().getStackInSlot(bestslot) == item;
	}

	private double getProtectionValue(ItemStack stack) {
		return stack.getItem() instanceof ItemArmor ? (double) ((ItemArmor) stack.getItem()).damageReduceAmount
				+ (double) ((100 - ((ItemArmor) stack.getItem()).damageReduceAmount)
						* EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D
				: 0.0D;
	}

	private double bestDamage(ContainerChest container, int slot) {
		double bestDamage = 0;

		for (int i = 0; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (is.getItem() instanceof ItemSword && getDamage(is) > bestDamage) {
					bestDamage = getDamage(is);
				}
			}
		}
		for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
			if (container.getLowerChestInventory().getStackInSlot(i) != null) {
				ItemStack is = container.getLowerChestInventory().getStackInSlot(i);
				if (i != slot && is.getItem() instanceof ItemSword && getDamage(is) > bestDamage) {
					bestDamage = getDamage(is);
				}
			}
		}

		return bestDamage;
	}

	private float getDamage(ItemStack stack) {
		float damage = 0;
		Item item = stack.getItem();
		if (item instanceof ItemTool) {
			damage += getSpeed(stack);
		}
		if (item instanceof ItemSword) {
			damage += getAttackDamage(stack);
		} else {
			damage += 1;
		}
		return damage;
	}

	private float getAttackDamage(final ItemStack itemStack) {
		float damage = ((ItemSword) itemStack.getItem()).getDamageVsEntity();
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
		return damage;
	}

	private float getSpeed(ItemStack stack) {
		return ((ItemTool) stack.getItem()).getToolMaterial().getEfficiencyOnProperMaterial();
	}
}
