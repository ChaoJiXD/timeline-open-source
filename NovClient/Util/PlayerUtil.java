package NovClient.Util;

import java.util.*;
import org.lwjgl.util.vector.*;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.*;
import net.minecraft.util.*;

public class PlayerUtil
{
    private static Minecraft mc;
    
    static {
        PlayerUtil.mc = Minecraft.getMinecraft();
    }
	public static double getIncremental(final double val, final double inc) {
        final double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }
    public static float getDirection() {
        float yaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }
    public static void blockHit(Entity en, boolean value) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
        if(mc.thePlayer.getCurrentEquippedItem() != null && en != null && value && stack.getItem() instanceof ItemSword && (double)mc.thePlayer.swingProgress > 0.2D) {
           KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
        }

     }


    public static boolean isInWater() {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)).getBlock().getMaterial() == Material.water;
    }
    
    public static void toFwd(final double speed) {
        final float yaw = mc.thePlayer.rotationYaw * 0.017453292f;
        final EntityPlayerSP thePlayer = mc.thePlayer;
        thePlayer.motionX -= MathHelper.sin(yaw) * speed;
        final EntityPlayerSP thePlayer2 = mc.thePlayer;
        thePlayer2.motionZ += MathHelper.cos(yaw) * speed;
    }
    
    public static void setSpeed(final double speed) {
        mc.thePlayer.motionX = -Math.sin(getDirection()) * speed;
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }
    
    public static double getSpeed() {
        Minecraft.getMinecraft();
        final double motionX = mc.thePlayer.motionX;
        Minecraft.getMinecraft();
        final double n = motionX * mc.thePlayer.motionX;
        Minecraft.getMinecraft();
        final double motionZ = mc.thePlayer.motionZ;
        Minecraft.getMinecraft();
        return Math.sqrt(n + motionZ * mc.thePlayer.motionZ);
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer) {
        return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
    }
    
    public static Block getBlock(final BlockPos pos) {
        Minecraft.getMinecraft();
        return mc.theWorld.getBlockState(pos).getBlock();
    }
    
    public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x, final double y, final double z) {
        return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }
    
    public static ArrayList<Vector3f> vanillaTeleportPositions(final double tpX, final double tpY, final double tpZ, final double speed) {
        final ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        final Minecraft mc = Minecraft.getMinecraft();
        final double posX = tpX - mc.thePlayer.posX;
        final double posY = tpY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() + 1.1);
        final double posZ = tpZ - mc.thePlayer.posZ;
        final float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
        final float pitch = (float)(-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0 / 3.141592653589793);
        double tmpX = mc.thePlayer.posX;
        double tmpY = mc.thePlayer.posY;
        double tmpZ = mc.thePlayer.posZ;
        double steps = 1.0;
        for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY, tpZ); d += speed) {
            ++steps;
        }
        for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY, tpZ); d += speed) {
            tmpX = mc.thePlayer.posX - Math.sin(getDirection(yaw)) * d;
            tmpZ = mc.thePlayer.posZ + Math.cos(getDirection(yaw)) * d;
            positions.add(new Vector3f((float)tmpX, (float)(tmpY -= (mc.thePlayer.posY - tpY) / steps), (float)tmpZ));
        }
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }
    
    public static float getDirection(float yaw) {
        Minecraft.getMinecraft();
        if (mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        Minecraft.getMinecraft();
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else {
            Minecraft.getMinecraft();
            if (mc.thePlayer.moveForward > 0.0f) {
                forward = 0.5f;
            }
        }
        Minecraft.getMinecraft();
        if (mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        Minecraft.getMinecraft();
        if (mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }
    
    public static double getDistance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double d0 = x1 - x2;
        final double d2 = y1 - y2;
        final double d3 = z1 - z2;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
    }
    
    public static boolean MovementInput() {
        return PlayerUtil.mc.gameSettings.keyBindForward.pressed || PlayerUtil.mc.gameSettings.keyBindLeft.pressed || PlayerUtil.mc.gameSettings.keyBindRight.pressed || PlayerUtil.mc.gameSettings.keyBindBack.pressed;
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static boolean isMoving2() {
        return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
    }
    
    public static boolean isInLiquid() {
        if(mc.thePlayer == null)return false;
    	if (mc.thePlayer.isInWater()) {
           return true;
        } else {
           boolean var1 = false;
           int var2 = (int)mc.thePlayer.getEntityBoundingBox().minY;

           for(int var3 = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); var3 < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++var3) {
              for(int var4 = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); var4 < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++var4) {
                 Block var5 = mc.theWorld.getBlockState(new BlockPos(var3, var2, var4)).getBlock();
                 if (var5 != null && var5.getMaterial() != Material.air) {
                    if (!(var5 instanceof BlockLiquid)) {
                       return false;
                    }

                    var1 = true;
                 }
              }
           }

           return var1;
        }
     }
    
    public static BlockPos getHypixelBlockpos(String str){
    	int val = 89;
    	if(str != null && str.length() > 1){
    		char[] chs = str.toCharArray();
        	
        	int lenght = chs.length;
        	for(int i = 0; i < lenght; i++)
        		val += (int)chs[i] * str.length()* str.length() + (int)str.charAt(0) + (int)str.charAt(1);
        	val/=str.length();
    	}
    	return new BlockPos(val, -val%255, val);
    }
    public static boolean isOnLiquid() {
        AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox();
        if (boundingBox == null) {
            return false;
        }
        boundingBox = boundingBox.contract(0.01D, 0.0D, 0.01D).offset(0.0D, -0.01D, 0.0D);
        boolean onLiquid = false;
        int y = (int) boundingBox.minY;
        for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper
                .floor_double(boundingBox.maxX + 1.0D); x++) {
            for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper
                    .floor_double(boundingBox.maxZ + 1.0D); z++) {
                Block block = mc.theWorld.getBlockState((new BlockPos(x, y, z))).getBlock();
                if (block != Blocks.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }

    public static void blinkToPos(final double[] startPos, final BlockPos endPos, final double slack, final double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        try {
            final double endX = endPos.getX() + 0.5;
            final double endY = endPos.getY() + 1.0;
            final double endZ = endPos.getZ() + 0.5;

            double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            int count = 0;
            while (distance > slack) {
                distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
                if (count > 120) {
                    break;
                }
                final boolean next = false;
                final double diffX = curX - endX;
                final double diffY = curY - endY;
                final double diffZ = curZ - endZ;
                final double offset = ((count & 0x1) == 0x0) ? pOffset[0] : pOffset[1];
                if (diffX < 0.0) {
                    if (Math.abs(diffX) > offset) {
                        curX += offset;
                    } else {
                        curX += Math.abs(diffX);
                    }
                }
                if (diffX > 0.0) {
                    if (Math.abs(diffX) > offset) {
                        curX -= offset;
                    } else {
                        curX -= Math.abs(diffX);
                    }
                }
                if (diffY < 0.0) {
                    if (Math.abs(diffY) > 0.25) {
                        curY += 0.25;
                    } else {
                        curY += Math.abs(diffY);
                    }
                }
                if (diffY > 0.0) {
                    if (Math.abs(diffY) > 0.25) {
                        curY -= 0.25;
                    } else {
                        curY -= Math.abs(diffY);
                    }
                }
                if (diffZ < 0.0) {
                    if (Math.abs(diffZ) > offset) {
                        curZ += offset;
                    } else {
                        curZ += Math.abs(diffZ);
                    }
                }
                if (diffZ > 0.0) {
                    if (Math.abs(diffZ) > offset) {
                        curZ -= offset;
                    } else {
                        curZ -= Math.abs(diffZ);
                    }
                }
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
                ++count;
            }
        } catch (Exception e) {

        }
    }
    public static void hypixelTeleport(final double[] startPos, final BlockPos endPos){

    	double distx = startPos[0] - endPos.getX()+ 0.5;
    	double disty = startPos[1] - endPos.getY();
    	double distz = startPos[2] - endPos.getZ()+ 0.5;
    	double dist = Math.sqrt(mc.thePlayer.getDistanceSq(endPos));
    	double distanceEntreLesPackets = 0.31 + MoveUtils.getSpeedEffect()/20;
    	double xtp, ytp, ztp = 0;
    	if(dist> distanceEntreLesPackets){
    		
    		double nbPackets = Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1;
    	
    		xtp = mc.thePlayer.posX;
    		ytp = mc.thePlayer.posY;
    		ztp = mc.thePlayer.posZ;		
    		double count = 0;
    		for (int i = 1; i < nbPackets;i++){		
    			double xdi = (endPos.getX() - mc.thePlayer.posX)/( nbPackets);	
    			 xtp += xdi;
    			 
    			double zdi = (endPos.getZ() - mc.thePlayer.posZ)/( nbPackets);	
    			 ztp += zdi;
    			 
    			double ydi = (endPos.getY() - mc.thePlayer.posY)/( nbPackets);	
    			ytp += ydi;
    			   count ++;
    			   
    			   if(!mc.theWorld.getBlockState(new BlockPos(xtp, ytp-1, ztp)).getBlock().isFullCube()){  
    			   if (count <= 2) {
                      ytp += 2E-8;
                   } else if (count >= 4) {
                       count = 0;
                   }
    			   }
    				C03PacketPlayer.C04PacketPlayerPosition Packet= new C03PacketPlayer.C04PacketPlayerPosition(xtp, ytp, ztp, false);
    				mc.thePlayer.sendQueue.addToSendQueue(Packet);
    		}
    	
    			mc.thePlayer.setPosition(endPos.getX() + 0.5, endPos.getY(), endPos.getZ() + 0.5);
    		
    	}else{
    			mc.thePlayer.setPosition(endPos.getX(), endPos.getY(), endPos.getZ());
    		
    		}
    }
    public static void teleport(final double[] startPos, final BlockPos endPos){
    	double distx = startPos[0] - endPos.getX()+ 0.5;
    	double disty = startPos[1] - endPos.getY();
    	double distz = startPos[2] - endPos.getZ()+ 0.5;
    	double dist = Math.sqrt(mc.thePlayer.getDistanceSq(endPos));
    	double distanceEntreLesPackets = 5;
    	double xtp, ytp, ztp = 0;
    	
    	if(dist> distanceEntreLesPackets){
    		double nbPackets = Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1;
    		xtp = mc.thePlayer.posX;
    		ytp = mc.thePlayer.posY;
    		ztp = mc.thePlayer.posZ;		
    		double count = 0;
    		for (int i = 1; i < nbPackets;i++){		
    			double xdi = (endPos.getX() - mc.thePlayer.posX)/( nbPackets);	
    			xtp += xdi;
    			 
    			double zdi = (endPos.getZ() - mc.thePlayer.posZ)/( nbPackets);	
    			ztp += zdi;
    			 
    			double ydi = (endPos.getY() - mc.thePlayer.posY)/( nbPackets);	
    			ytp += ydi;
    			count ++;
    			C03PacketPlayer.C04PacketPlayerPosition Packet= new C03PacketPlayer.C04PacketPlayerPosition(xtp, ytp, ztp, true);
    			
    			mc.thePlayer.sendQueue.addToSendQueue(Packet);
    		}
    		
    		mc.thePlayer.setPosition(endPos.getX() + 0.5, endPos.getY(), endPos.getZ() + 0.5);
    	}else{
    		mc.thePlayer.setPosition(endPos.getX(), endPos.getY(), endPos.getZ());
    	}
    }
    public static boolean isMoving() {
        if ((!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking())) {
            return ((mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F));
        }
        return false;
    }



}



