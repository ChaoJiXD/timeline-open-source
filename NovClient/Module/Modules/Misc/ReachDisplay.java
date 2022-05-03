package NovClient.Module.Modules.Misc;

import java.awt.Color;
import java.awt.Toolkit;
import java.text.DecimalFormat;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Events.World.EventAttack;
import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Legit.Reach;
import NovClient.Module.Modules.Render.HUD;
import NovClient.UI.fontRenderer.UnicodeFontRenderer;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ReachDisplay extends Module {
	private UnicodeFontRenderer Font = Client.instance.FontLoaders.Chinese18;
	private long lastAttack;
	private String rangeText;
	 private Numbers<Double> X = new Numbers<Double>("X", "X", 5.0, 0.0, Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 10.0);
	    private Numbers<Double> Y = new Numbers<Double>("Y", "Y", 150.0, 0.0, Toolkit.getDefaultToolkit().getScreenSize().getHeight(), 10.0);
	public ReachDisplay() {
		super("ReachDisplay", new String[]{"ReachDisplay"}, ModuleType.Legit);
		super.addValues(X,Y);
	}
	public void onEnable() {
		Client.instance.build = HUD.中文.getValue() ? "战斗优化客户端" : "Cheat Breaker";
	}
	public void onDisable() {
		Client.instance.build = Client.instance.Lbuild;
	}
	@EventHandler
	public void onAttack(EventAttack e)
	{

        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && this.mc.objectMouseOver.entityHit.getEntityId() == e.getEntity().getEntityId()) {
            Vec3 vec3 = this.mc.getRenderViewEntity().getPositionEyes(1.0f);
            double range = this.mc.objectMouseOver.hitVec.distanceTo(vec3)*(3.0/ (Client.instance.getModuleManager().getModuleByClass(Reach.class).isEnabled() ? Reach.CombatReach.getValue() : 3.0));
            if(range>3.0)range = 3.0;
            this.rangeText = new DecimalFormat(".##").format(range)  + (HUD.中文.getValue() ? "格" : " blocks");
            
            
        } else {
            this.rangeText = "Not on target?";
        }
        this.lastAttack = System.currentTimeMillis();
	}
	@EventHandler
	public void onRender(EventRender2D e)
	{
        if (System.currentTimeMillis() - this.lastAttack > 2000L) {
            this.rangeText = HUD.中文.getValue() ? "攻击距离显示" : "Reach Display";
        }
        
		RenderUtil.drawFastRoundedRect(this.X.getValue().intValue(), this.Y.getValue().floatValue(), this.X.getValue().intValue()+Font.getStringWidth(rangeText)+4,this.Y.getValue().floatValue() + 12.0f,2f, new Color(0,0,0,120).getRGB());
		Font.drawStringWithShadow(rangeText, this.X.getValue().floatValue()+3.0f, this.Y.getValue().floatValue()+3.0f, new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue()).getRGB(), 255);
	}
}
