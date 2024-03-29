package NovClient.Module.Modules.Move;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Misc.EventRotation;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Events.World.EventPostUpdate;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Render.Crosshair;
import NovClient.Module.Modules.Render.HUD;
import NovClient.UI.fontRenderer.UnicodeFontRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {
	private BlockData blockData;
	private timeHelper timer2 = new timeHelper();
	private Option<Boolean> tower = new Option<Boolean>("Tower", "Tower", true);
	private Option<Boolean> movetower = new Option<Boolean>("MoveTower", "MoveTower", false);
	private Option<Boolean> noSwing = new Option<Boolean>("NoSwing", "NoSwing", true);
	private Option<Boolean> KeepRotation = new Option<Boolean>("KeepRotation", "KeepRotation", true);
	private List<Block> blacklisted = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
			Blocks.flowing_lava, Blocks.enchanting_table, Blocks.ender_chest, Blocks.yellow_flower, Blocks.carpet,
			Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.crafting_table, Blocks.snow_layer,
			Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch,
			Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore,
			Blocks.lit_redstone_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate,
			Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button,
			Blocks.wooden_button, Blocks.cactus, Blocks.lever, Blocks.activator_rail, Blocks.rail, Blocks.detector_rail,
			Blocks.golden_rail, Blocks.furnace, Blocks.ladder, Blocks.oak_fence, Blocks.redstone_torch,
			Blocks.iron_trapdoor, Blocks.trapdoor, Blocks.tripwire_hook, Blocks.hopper, Blocks.acacia_fence_gate,
			Blocks.birch_fence_gate, Blocks.dark_oak_fence_gate, Blocks.jungle_fence_gate, Blocks.spruce_fence_gate,
			Blocks.oak_fence_gate, Blocks.dispenser, Blocks.sapling, Blocks.tallgrass, Blocks.deadbush, Blocks.web,
			Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.nether_brick_fence, Blocks.vine,
			Blocks.double_plant, Blocks.flower_pot, Blocks.beacon, Blocks.pumpkin, Blocks.lit_pumpkin);
	public static List<Block> blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water,
			Blocks.lava, Blocks.flowing_lava, Blocks.ender_chest, Blocks.enchanting_table, Blocks.stone_button,
			Blocks.wooden_button, Blocks.crafting_table, Blocks.beacon);
	public static Option<Boolean> Lag = new Option<Boolean>("SpeedCheck","SpeedCheck", true);
    public static Option<Boolean> auracheck = new Option<Boolean>("AuraCheck","AuraCheck", true);
    public static Option<Boolean> safe = new Option<Boolean>("SafeWalk","SafeWalk", true);
	int slot;
	static final int[] $SwitchMap$net$minecraft$util$EnumFacing = new int[EnumFacing.values().length];

	public Scaffold() {
		super("Scaffold", new String[] { "ScaffoldWalk", "Scaffold" }, ModuleType.Move);
        super.addValues(tower,movetower,KeepRotation,noSwing,auracheck,Lag,safe);
	}
	   public static int getBlockCount() {
		   
		     int blockCount = 0;
		     for (int i = 0; i < 45; i++) {
		       if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
		         ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
		         Item item = is.getItem();
		         if (is.getItem() instanceof ItemBlock && isValid(item)) {
		           blockCount += is.stackSize;
		         }
		       } 
		     } 
		     return blockCount;
		   }
	   private static boolean isValid(Item item) {
		     if (!(item instanceof ItemBlock)) {
		       return false;
		     }
		     ItemBlock iBlock = (ItemBlock)item;
		     Block block = iBlock.getBlock();
		     if (blacklistedBlocks.contains(block)) {
		       return false;
		     }
		     
		     return true;
		   }
	   UnicodeFontRenderer font = Client.instance.FontLoaders.Chinese18;
	   @EventHandler
	   public void onRender2D(EventRender2D event) {
		    ScaledResolution sr = new ScaledResolution(mc);
		    int color = new Color(255, 0, 0, 150).getRGB();
		    if (this.getBlockCount() >= 64 && 128 > this.getBlockCount()) {
		        color = new Color(255, 255, 0, 150).getRGB();
		    } else if (this.getBlockCount() >= 128) {
		        color = new Color(0, 255, 0, 150).getRGB();
		    }
		    Crosshair Cross = (Crosshair)Client.instance.getModuleManager().getModuleByClass(Crosshair.class);
		    GlStateManager.enableBlend();
		    if(this.getBlockCount()==0) {
		    	font.drawStringWithShadow((HUD.中文.getValue()?"你没方块了" :"You do not have any blocks"), sr.getScaledWidth() / 2 - font.getStringWidth(HUD.中文.getValue()?"你没方块了" :"You do not have any blocks") / 2, sr.getScaledHeight() / 2 - 12 - (Cross.isEnabled() ? (Crosshair.GAP.getValue().floatValue() + Crosshair.SIZE.getValue().floatValue()):0f), color,255);	
		    }else {
		    font.drawStringWithShadow((HUD.中文.getValue()?"你还有 " :"")+this.getBlockCount() + (HUD.中文.getValue()?" 个方块":" Block"), sr.getScaledWidth() / 2 - font.getStringWidth((HUD.中文.getValue()?"你还有 " :"")+this.getBlockCount() + (HUD.中文.getValue()?" 个方块":" Block")) / 2, sr.getScaledHeight() / 2 - 12 - (Cross.isEnabled() ? (Crosshair.GAP.getValue().floatValue() + Crosshair.SIZE.getValue().floatValue()):0f), color,255);
		    }
		    GlStateManager.disableBlend();
		}
   
	    
	@EventHandler
	public void onPre(EventPreUpdate event) {
		super.setSuffix((getBlockCount() == 0)?(HUD.中文.getValue()?"你没方块了" :"You do not have any blocks"):getBlockCount());
		double x = mc.thePlayer.posX;
		double y = mc.thePlayer.posY - 1.0;
		double z = mc.thePlayer.posZ;
		BlockPos blockBelow = new BlockPos(x, y, z);
		if (mc.thePlayer != null) {
			this.blockData = this.getBlockData(blockBelow, blacklistedBlocks);
			if (this.blockData == null) {
				this.blockData = this.getBlockData(blockBelow.offset(EnumFacing.DOWN), blacklistedBlocks);
			}
			if (this.tower.getValue().booleanValue()) {
				if (this.movetower.getValue().booleanValue()) {
					if (mc.gameSettings.keyBindJump.isKeyDown()) {
						if (this.isMoving2()) {
							if (this.isOnGround(0.76) && !this.isOnGround(0.75) && mc.thePlayer.motionY > 0.23
									&& mc.thePlayer.motionY < 0.25) {
								mc.thePlayer.motionY = (double) Math.round(mc.thePlayer.posY) - mc.thePlayer.posY;
							}
							if (this.isOnGround(1.0E-4)) {
								mc.thePlayer.motionY = 0.42;
								mc.thePlayer.motionX *= 0.9;
								mc.thePlayer.motionZ *= 0.9;
							} else if (mc.thePlayer.posY >= (double) Math.round(mc.thePlayer.posY) - 1.0E-4
									&& mc.thePlayer.posY <= (double) Math.round(mc.thePlayer.posY) + 1.0E-4) {
								mc.thePlayer.motionY = 0.0;
							}
						} else {
							mc.thePlayer.motionX = 0.0;
							mc.thePlayer.motionZ = 0.0;
							mc.thePlayer.jumpMovementFactor = 0.0f;
							blockBelow = new BlockPos(x, y, z);
							if (mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air
									&& this.blockData != null) {
								mc.thePlayer.motionY = 0.4196;
								mc.thePlayer.motionX *= 0.75;
								mc.thePlayer.motionZ *= 0.75;
							}
						}
					}
				} else if (!this.isMoving2() && mc.gameSettings.keyBindJump.isKeyDown()) {
					mc.thePlayer.motionX = 0.0;
					mc.thePlayer.motionZ = 0.0;
					mc.thePlayer.jumpMovementFactor = 0.0f;
					blockBelow = new BlockPos(x, y, z);
					if (mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air && this.blockData != null) {
						mc.thePlayer.motionY = 0.4196;
						mc.thePlayer.motionX *= 0.75;
						mc.thePlayer.motionZ *= 0.75;
					}
				}
			}
			float random = 0;
			//System.out.println(random);
			Client.Yaw = mc.thePlayer.rotationYaw;
			Client.Pitch = mc.thePlayer.rotationPitch;
			if(KeepRotation.getValue())
			{
				float[] rot = this.getRotationsBlock(BlockData.position, BlockData.face);
				event.setPitch(rot[1]);
				event.setYaw(getYaw());
				Client.Yaw = getYaw();
				Client.Pitch = rot[1];
				Client.RenderRotate(getYaw());
			}
			if (mc.theWorld.getBlockState(blockBelow = new BlockPos(x, y, z)).getBlock() == Blocks.air) {
				if (this.blockData != null) {
					if(!KeepRotation.getValue())
					{
						float[] rot = this.getRotationsBlock(BlockData.position, BlockData.face);
						
						event.setPitch(rot[1]);
						event.setYaw(getYaw());
						Client.Yaw = getYaw();
						Client.Pitch = rot[1];
						Client.RenderRotate(getYaw());
					}
					if (this.noSwing.getValue().booleanValue()) {
						mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
					} else {
						mc.thePlayer.swingItem();
					}
				}
			}
		}
	}
    public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }
	public boolean isOnGround(double height) {
		if (!mc.theWorld
				.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0))
				.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isMoving2() {
		return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
	}
	public static float getYaw() {
		float yaw = 0;

		if (mc.gameSettings.keyBindForward.isKeyDown())
			yaw = mc.thePlayer.rotationYaw + 180;
		if (mc.gameSettings.keyBindLeft.isKeyDown())
			yaw = mc.thePlayer.rotationYaw + 90;
		if (mc.gameSettings.keyBindRight.isKeyDown())
			yaw = mc.thePlayer.rotationYaw - 90;
		if (mc.gameSettings.keyBindBack.isKeyDown())
			yaw = mc.thePlayer.rotationYaw;

		if (mc.gameSettings.keyBindForward.isKeyDown() && mc.gameSettings.keyBindLeft.isKeyDown())
			yaw = mc.thePlayer.rotationYaw + 90 + 45;
		if (mc.gameSettings.keyBindForward.isKeyDown() && mc.gameSettings.keyBindRight.isKeyDown())
			yaw = mc.thePlayer.rotationYaw - 90 - 45;

		if (mc.gameSettings.keyBindBack.isKeyDown() && mc.gameSettings.keyBindLeft.isKeyDown())
			yaw = mc.thePlayer.rotationYaw + 90 - 45;
		if (mc.gameSettings.keyBindBack.isKeyDown() && mc.gameSettings.keyBindRight.isKeyDown())
			yaw = mc.thePlayer.rotationYaw - 90 + 45;

		if (!mc.thePlayer.isMoving())
			yaw = mc.thePlayer.rotationYaw + 180;

		return yaw;
	}

    public static float[] getRotationsBlock(BlockPos block, EnumFacing face) {
        double x = block.getX() + 0.5 - mc.thePlayer.posX +  (double) face.getFrontOffsetX()/2;
        double z = block.getZ() + 0.5 - mc.thePlayer.posZ +  (double) face.getFrontOffsetZ()/2;
        double y = (block.getY() + 0.5);
        double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);
        if (yaw < 0.0F) {
            yaw += 360f;
        }
        return new float[]{yaw, pitch};
    }
	@EventHandler
	public void onSafe(EventPostUpdate event) {
		int i;
		for (i = 36; i < 45; ++i) {
			Item item;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| !((item = (mc.thePlayer.inventoryContainer.getSlot(i).getStack())
							.getItem()) instanceof ItemBlock)
					|| this.blacklisted.contains(((ItemBlock) item).getBlock())
					|| ((ItemBlock) item).getBlock().getLocalizedName().toLowerCase().contains("chest")
					|| this.blockData == null)
				continue;
			int currentItem = mc.thePlayer.inventory.currentItem;
			mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 36));
			mc.thePlayer.inventory.currentItem = i - 36;
			mc.playerController.updateController();
			try {
				mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
						BlockData.position, BlockData.face,
						new Vec3(BlockData.access$2(this.blockData)).addVector(0.5, 0.5, 0.5)
								.add(scale(new Vec3(BlockData.access$3(this.blockData).getDirectionVec()), 0.5)));
			} catch (Exception exception) {
				// empty catch block
			}
			mc.thePlayer.inventory.currentItem = currentItem;
			mc.playerController.updateController();
			return;
		}
		if (this.invCheck()) {
			for (i = 9; i < 36; ++i) {
				Item item;
				if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
						|| !((item = mc.thePlayer.inventoryContainer.getSlot(i).getStack()
								.getItem()) instanceof ItemBlock)
						|| this.blacklisted.contains(((ItemBlock) item).getBlock())
						|| ((ItemBlock) item).getBlock().getLocalizedName().toLowerCase().contains("chest"))
					continue;
				this.swap(i, 7);
				break;
			}
		}
	}

	public static float randomFloat(long seed) {
		seed = System.currentTimeMillis() + seed;
		return 0.3f + (float) new Random(seed).nextInt(70000000) / 1.0E8f + 1.458745E-8f;
	}

	protected void swap(int slot, int hotbarNum) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
	}

	private boolean invCheck() {
		for (int i = 36; i < 45; ++i) {
			Item item;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| !((item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock)
					|| this.blacklisted.contains(((ItemBlock) item).getBlock()))
				continue;
			return false;
		}
		return true;
	}

	public Vec3 scale(Vec3 t, double p_186678_1_) {
		return new Vec3(t.xCoord * p_186678_1_, t.yCoord * p_186678_1_, t.zCoord * p_186678_1_);
	}

	private BlockData getBlockData(BlockPos pos, List list) {
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(-1, 0, 0), mc.gameSettings.keyBindSprint.isKeyDown() && mc.thePlayer.onGround
					&& mc.thePlayer.fallDistance == 0.0f
					&& mc.theWorld
							.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ))
							.getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.EAST,
					this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(1, 0, 0), mc.gameSettings.keyBindSprint.isKeyDown() && mc.thePlayer.onGround
					&& mc.thePlayer.fallDistance == 0.0f
					&& mc.theWorld
							.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ))
							.getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.WEST,
					this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
			return new BlockData(pos.add(0, 0, -1), mc.gameSettings.keyBindSprint.isKeyDown() && mc.thePlayer.onGround
					&& mc.thePlayer.fallDistance == 0.0f
					&& mc.theWorld
							.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ))
							.getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.SOUTH,
					this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
			return new BlockData(pos.add(0, 0, 1), mc.gameSettings.keyBindSprint.isKeyDown() && mc.thePlayer.onGround
					&& mc.thePlayer.fallDistance == 0.0f
					&& mc.theWorld
							.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ))
							.getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.NORTH,
					this.blockData);
		}
		BlockPos add = pos.add(-1, 0, 0);
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
			return new BlockData(add.add(1, 0, 0), EnumFacing.WEST, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
			return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
			return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
		}
		BlockPos add2 = pos.add(1, 0, 0);
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
			return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
			return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
			return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
		}
		BlockPos add3 = pos.add(0, 0, -1);
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
			return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
			return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
			return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
		}
		BlockPos add4 = pos.add(0, 0, 1);
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
			return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
			return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
		}
		if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
			return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
		}
		return null;
	}

	public boolean isAirBlock(Block block) {
		return block.getMaterial().isReplaceable()
				&& (!(block instanceof BlockSnow) || block.getBlockBoundsMaxY() <= 0.125);
	}

	public Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
		if (face == EnumFacing.NORTH) {
			return new Vec3(pos.getX(), pos.getY(), (double) pos.getZ() - 0.5);
		}
		if (face == EnumFacing.EAST) {
			return new Vec3((double) pos.getX() + 0.5, pos.getY(), pos.getZ());
		}
		if (face == EnumFacing.SOUTH) {
			return new Vec3(pos.getX(), pos.getY(), (double) pos.getZ() + 0.5);
		}
		if (face == EnumFacing.WEST) {
			return new Vec3((double) pos.getX() - 0.5, pos.getY(), pos.getZ());
		}
		return new Vec3(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void onEnable() {
		Client.Yaw = 0.0f;
		Client.Pitch = 0.0f;
		super.onEnable();
		this.timer2.reset();
	}

	@Override
	public void onDisable() {
		Client.Yaw = 0.0f;
		Client.Pitch = 0.0f;
		super.onDisable();
		mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
	}

	static {
		try {
			Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.UP.ordinal()] = 1;
		} catch (NoSuchFieldError noSuchFieldError) {
			// empty catch block
		}
		try {
			Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.SOUTH.ordinal()] = 2;
		} catch (NoSuchFieldError noSuchFieldError) {
			// empty catch block
		}
		try {
			Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.NORTH.ordinal()] = 3;
		} catch (NoSuchFieldError noSuchFieldError) {
			// empty catch block
		}
		try {
			Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.EAST.ordinal()] = 4;
		} catch (NoSuchFieldError noSuchFieldError) {
			// empty catch block
		}
		try {
			Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.WEST.ordinal()] = 5;
		} catch (NoSuchFieldError noSuchFieldError) {
			// empty catch block
		}
	}

	public class timeHelper {
		private long prevMS = 0L;

		public boolean delay(float milliSec) {
			return (float) (this.getTime() - this.prevMS) >= milliSec;
		}

		public void reset() {
			this.prevMS = this.getTime();
		}

		public long getTime() {
			return System.nanoTime() / 1000000L;
		}

		public long getDifference() {
			return this.getTime() - this.prevMS;
		}

		public void setDifference(long difference) {
			this.prevMS = this.getTime() - difference;
		}
	}

	private static class BlockData {
		public static BlockPos position;
		public static EnumFacing face;

		public BlockData(BlockPos position, EnumFacing face, BlockData blockData) {
			BlockData.position = position;
			BlockData.face = face;
		}

		static BlockPos access$2(BlockData var0) {
			return position;
		}

		static EnumFacing access$3(BlockData var0) {
			return face;
		}
	}

}
