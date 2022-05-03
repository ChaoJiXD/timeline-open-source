
package NovClient.Module.Modules.Legit;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Combat.KillAura;
import NovClient.Util.PlayerUtil;
import NovClient.Util.TimeHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;


public class AutoClicker
extends Module {

    public TimeHelper time = new TimeHelper();
    private long nextDelay = 0L;
    long lastMSInventory = -1L;
    public static Numbers<Double> cpsmin = new Numbers("CPSMin","CPSMin", 8.0, 2.0, 20.0, 1.0);
    public static Numbers<Double> cpsmax = new Numbers("CPSMax","CPSMax", 8.0, 2.0, 20.0, 1.0);
    protected Random r = new Random();
    protected long lastMS = -1L;
    public Option<Boolean> ab = new Option<Boolean>("BlockHit","BlockHit", true);
    public Option<Boolean> BreakBlock = new Option<Boolean>("BreakBlock","BreakBlock", true);
    public Option<Boolean> InvClicker = new Option<Boolean>("Inventory","Inventory", false);
    public static int Click;
    public static boolean Clicked;
    private double delay = 0.0;
    private TimeHelper time2 = new TimeHelper();
    private TimeHelper time3 = new TimeHelper();
    public AutoClicker() {
        super("AutoClicker", new String[]{"AutoClicker"}, ModuleType.Legit);
        this.setColor(new Color(208, 30, 142).getRGB());
        super.addValues(cpsmin,cpsmax,ab,InvClicker,BreakBlock);
    }
    private void delay() {
        float minCps = ((Double)cpsmin.getValue()).floatValue();
        float maxCps = ((Double)cpsmax.getValue()).floatValue();
        float minDelay = 1000.0f / minCps;
        float maxDelay = 1000.0f / maxCps;
        this.delay = (double)maxDelay + this.r.nextDouble() * (double)(minDelay - maxDelay);
    }


    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        boolean isblock;
        BlockPos bp = mc.thePlayer.rayTrace(6.0, 0.0f).getBlockPos();
        boolean bl = isblock = mc.theWorld.getBlockState(bp).getBlock() != Blocks.air && mc.objectMouseOver.typeOfHit != MovingObjectType.ENTITY;
        if(!BreakBlock.getValue())isblock=false;
        if(this.time2.delay(this.delay) && !this.time2.delay(this.delay + this.delay/2))Clicked = true;
        if(this.time2.delay(this.delay + this.delay-1.0))
        	{
        	Clicked = false;
        	time2.reset();
        	}

        if (!Client.instance.getModuleManager().getModuleByClass(KillAura.class).isEnabled() && Mouse.isButtonDown((int)0) && this.time.delay(this.delay) && mc.currentScreen == null &&  !isblock) {
            PlayerUtil.blockHit((Entity)mc.objectMouseOver.entityHit, (boolean)((Boolean)this.ab.getValue()));
            mc.leftClickCounter = 0;
            mc.clickMouse();
            this.delay();
            this.time.reset();
        }
    }

    @EventHandler
    private void invClicks(EventPreUpdate event) {
        if (!Keyboard.isKeyDown((int)42)) {
            return;
        }
        if (mc.currentScreen instanceof GuiContainer && ((Boolean)this.InvClicker.getValue()).booleanValue()) {
            float invClickDelay = 1000.0f / ((Double)cpsmax.getValue()).floatValue() + (float)this.r.nextInt(50);
            if (Mouse.isButtonDown((int)0) && this.time3.delay((double)invClickDelay)) {
                try {
                    mc.currentScreen.InventoryClicks();
                    this.time3.reset();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }
    }


