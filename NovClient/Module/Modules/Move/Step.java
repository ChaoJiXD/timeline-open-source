package NovClient.Module.Modules.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventStep;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.PlayerUtil;
import NovClient.Util.StepTimer;
import NovClient.Util.Timer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Step extends Module {
	private Mode<Enum> SMODE = new Mode( "Step Mode", "Step Mode", (Enum[])SMode.values(), (Enum) SMode.NCP );
	private Numbers<Double> STEP = new Numbers<Double>( "HEIGHT", "HEIGHT", 0.5, 0.0, 10.0, 0.1 );
	private Numbers<Double> NCPHEIGHT = new Numbers<Double>( "NCPHEIGHT", "NCPHEIGHT",  0.5, 0.0, 1.5, 0.1 );
	private Numbers<Double> TIMER = new Numbers<Double>( "TIMER", "TIMER", 0.6, 0.1, 0.6, 0.10 );
	private Numbers<Double> DELAY = new Numbers<Double>( "DELAY", "DELAY", 0.5, 0.0, 2.0, 0.1 );
	boolean resetTimer;
	StepTimer time = new StepTimer();
	public static Timer lastStep = new Timer();
    public Step() {
    	super("Step", new String[] {"Step","autojump"}, ModuleType.Move);
    	this.addValues(this.SMODE,this.STEP,this.NCPHEIGHT,this.TIMER,this.DELAY);
    	
    }

    @Override
    public void onEnable(){
    	resetTimer = false;
    	super.onEnable();
    }
    @Override
    public void onDisable(){
    	Minecraft.getMinecraft().timer.timerSpeed = 1;
    	super.onDisable();
    }
    @EventHandler
    public void onEvent(EventStep event) {
    	if(mc.thePlayer == null)return;
    	//if(!event.isPre())System.out.println("111");
    	EventStep es = (EventStep)event;
    	double stepValue = 1.5D;
    	final float timer = TIMER.getValue().floatValue();
    	final float delay = DELAY.getValue().floatValue() * 1000;
    	
    	if(SMODE.getValue().toString().equalsIgnoreCase("Vanilla") || SMODE.getValue().toString().equalsIgnoreCase("Cubecraft"))
    		stepValue = STEP.getValue().doubleValue();
    	if(SMODE.getValue().toString().equalsIgnoreCase("NCP"))
    		stepValue = NCPHEIGHT.getValue().doubleValue();
    	
    	if(resetTimer){
    		resetTimer = !resetTimer;
    		mc.timer.timerSpeed = 1;
    	}
    	if(!PlayerUtil.isInLiquid())
    	if (es.isPre()) {
    		if(mc.thePlayer.isCollidedVertically && !mc.gameSettings.keyBindJump.isKeyDown() && time.delay(delay)){
    			es.setStepHeight(stepValue);		
                es.setActive(true);
                
    		}
    		
        }else{
       
        	double rheight = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
        	boolean canStep = rheight >= 0.625;
			//System.out.println(rheight);
    		switch(SMODE.getValue().toString()){
    		case"NCP": 		
    			if(canStep){
    				mc.timer.timerSpeed = timer - (rheight >= 1 ? Math.abs(1-(float)rheight)*((float)timer*0.55f) : 0);
    				//System.out.println(mc.timer.timerSpeed);
    				if(mc.timer.timerSpeed <= 0.05f){
    					mc.timer.timerSpeed = 0.05f;
    				}
        			resetTimer = true;
        			ncpStep(rheight);	
    			}	
    			break;
    		case"AAC":
    			if(canStep){
    				if(rheight < 1.1){
    					mc.timer.timerSpeed = 0.5F;
    					resetTimer = true;
    				}else{
    					mc.timer.timerSpeed = 1 - (float)rheight*0.57f;
    					resetTimer = true;
    				}
        			aacStep(rheight);
        		}
    			break;
    		case "Vanilla":
    			
    			return;
    		}	


        }
    }
        
    
    void cubeStep(double height){
    	double posX = Minecraft.getMinecraft().thePlayer.posX; double posZ = Minecraft.getMinecraft().thePlayer.posZ;
    	double y = Minecraft.getMinecraft().thePlayer.posY;
    	double first = 0.42;
    	double second = 0.75;
    	Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
    }
    void ncpStep(double height){
    	List<Double>offset = Arrays.asList(0.42,0.333,0.248,0.083,-0.078);
    	double posX = Minecraft.getMinecraft().thePlayer.posX; double posZ = Minecraft.getMinecraft().thePlayer.posZ;
    	double y = Minecraft.getMinecraft().thePlayer.posY;
    	if(height < 1.1){
    		double first = 0.42;
    		double second = 0.75;
    		if(height != 1){
    			first *= height;
    			second *= height;
        		if(first > 0.425){
        			first = 0.425;
        		}
        		if(second > 0.78){
        			second = 0.78;
        		}
        		if(second < 0.49){
        			second = 0.49;
        		}
    		}
    		if(first == 0.42)
    			first = 0.41999998688698;
    		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
    		if(y+second < y + height)
    		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
    		return;
    	}else if(height <1.6){
    		for(int i = 0; i < offset.size(); i++){
        		double off = offset.get(i);
        		y += off;
        		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
        	}
    	}else if(height < 2.1){
    		double[] heights = {0.425,0.821,0.699,0.599,1.022,1.372,1.652,1.869};
			for(double off : heights){
        		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
        	}
    	}else{
        	double[] heights = {0.425,0.821,0.699,0.599,1.022,1.372,1.652,1.869,2.019,1.907};
        	for(double off : heights){
        		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
        	}
    	}
    	
    }
    void aacStep(double height){
    	double posX = Minecraft.getMinecraft().thePlayer.posX; double posY = Minecraft.getMinecraft().thePlayer.posY; double posZ = Minecraft.getMinecraft().thePlayer.posZ;
    	if(height < 1.1){
    		double first = 0.42;
    		double second = 0.75;

    		if(height > 1){
    			first *= height;
    			second *= height;
        		if(first > 0.4349){
        			first = 0.4349;
        		}else if(first < 0.405){
        			first = 0.405;
        		} 
    		}
    		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + first, posZ, false));
    		if(posY+second < posY + height)
    		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + second, posZ, false));
    		return;
    	}
    	List<Double> offset = Arrays.asList(0.434999999999998,0.360899999999992,0.290241999999991,0.220997159999987,0.13786084000003104,0.055);
    	double y = Minecraft.getMinecraft().thePlayer.posY;
    	for(int i = 0; i < offset.size(); i++){
    		double off = offset.get(i);
    		y += off;
    		if(y > Minecraft.getMinecraft().thePlayer.posY + height){
    			double x = Minecraft.getMinecraft().thePlayer.posX; double z = Minecraft.getMinecraft().thePlayer.posZ;
    			double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
    			double strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
   	         	float YAW = Minecraft.getMinecraft().thePlayer.rotationYaw;
   	         	double speed = 0.3;
   	         	if(forward != 0 && strafe != 0)
   	         		speed -= 0.09;
   	         	x += (forward * speed * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(YAW + 90.0f))) *1;
   	         	z += (forward * speed * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(YAW + 90.0f))) *1;
   	         	Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
   	         			x, y,z, false));	
   	        
   	         	break;
   	         	
    		}
    		if(i== offset.size() - 1){			
    			double x = Minecraft.getMinecraft().thePlayer.posX; double z = Minecraft.getMinecraft().thePlayer.posZ;
    			double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
    			double strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
   	         	float YAW = Minecraft.getMinecraft().thePlayer.rotationYaw;
   	         	double speed = 0.3;
   	         	if(forward != 0 && strafe != 0)
   	         		speed -= 0.09;
   	         	x += (forward * speed * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(YAW + 90.0f))) *1;
   	         	z += (forward * speed * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(YAW + 90.0f))) *1;
   	         	Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
   	         			x, y,z, false));	
    		}else{
    			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
    		}
    	}
    }
    enum SMode{
    	NCP,
    	Vanilla,
    	AAC,
    }
}
