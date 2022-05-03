package NovClient.Util;

import java.util.List;

import NovClient.Client;
import NovClient.API.Events.World.EventMove;
import NovClient.Module.Modules.Move.TargetStrafe;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class SigmaBhop {
	
    private double speed;
    private double lastDist;
    public static int stage;
    Minecraft mc = Minecraft.getMinecraft();
	public void onEnable()
	{
        boolean player = mc.thePlayer == null;
        if (mc.thePlayer != null) {
            speed = MoveUtils.defaultSpeed();
        }
        lastDist = 0.0;
        stage = 2;
	}
	public void onMove(EventMove e)
	{
        if (mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f) {
            speed = MoveUtils.defaultSpeed();
        }
        if (stage == 1 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
            speed = 1.35 + MoveUtils.defaultSpeed() - 0.01;
        }
        if (!BlockUtils.isInLiquid() && stage == 2 && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01) && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
            if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump))
                e.setY(mc.thePlayer.motionY = 0.41999998688698 + (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1);
            else
                e.setY(mc.thePlayer.motionY = 0.41999998688698);
            mc.thePlayer.jump();
            speed *= 1.533D;
        } else if (stage == 3) {
            final double difference = 0.66 * (lastDist - MoveUtils.defaultSpeed());
            speed = lastDist - difference;
        } else {
            final List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0));
            if ((collidingList.size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                stage = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
            }
            speed = lastDist - lastDist / 159.0;
        }
        speed = Math.max(speed, MoveUtils.defaultSpeed());

        //Stage checks if you're greater than 0 as step sets you -6 stage to make sure the player wont flag.
        if (stage > 0) {
            //Set strafe motion.
            if (BlockUtils.isInLiquid())
                speed = 0.1;
            L:
            if(MoveUtils.isMoving())
            {
            if(Client.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled()) {
            	TargetStrafe.onStrafe(e, speed);
            break L;
            }
            	setMotion(e, speed);
            }    
        }
        //If the player is moving, step the stage up.
        if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
            ++stage;
        }
	}
    private void setMotion(EventMove em, double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            em.setX(0.0D);
            em.setZ(0.0D);
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }
}
