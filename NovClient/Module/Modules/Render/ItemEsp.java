package NovClient.Module.Modules.Render;


import org.lwjgl.opengl.GL11;

import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;

public class ItemEsp extends Module {
	public static Option outlinedboundingBox = new Option("OutlinedBoundingBox", "OutlinedBoundingBox", true);
	public static Option boundingBox = new Option("BoundingBox", "BoundingBox", true);
    public static Mode<Enum> heigh = new Mode("Height", "Height", (Enum[])height.values(), (Enum)height.High);
	public ItemEsp() {
		super("ItemESP", new String[]{"ItemESP"}, ModuleType.Render);
        this.addValues(this.outlinedboundingBox,this.boundingBox,this.heigh);
	}
	
	@EventHandler
	public void onRender(EventRender3D event) {
		for (Object o : mc.theWorld.loadedEntityList) {
    		if (!(o instanceof EntityItem)) continue;
    		EntityItem item = (EntityItem)o;
 		   	double var10000 = item.posX;
 		   	Minecraft.getMinecraft().getRenderManager();
 		   	double x = var10000 - RenderManager.renderPosX;
 		   	var10000 = item.posY + 0.5D;
 		   	Minecraft.getMinecraft().getRenderManager();
 		   	double y = var10000 - RenderManager.renderPosY;
 		   	var10000 = item.posZ;
 		   	Minecraft.getMinecraft().getRenderManager();
 		   	double z = var10000 - RenderManager.renderPosZ;
 		   	GL11.glEnable(3042);
 		   	GL11.glLineWidth(2.0F);
 		   	GL11.glColor4f(1, 1, 1, .75F);
 		   	GL11.glDisable(3553);
 		   	GL11.glDisable(2929);
 		   	GL11.glDepthMask(false);
            if(((Boolean) this.outlinedboundingBox.getValue()).booleanValue()&&this.heigh.getValue()==height.High) {
 	   		RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - .2D, y-0.05, z - .2D, x + .2D, y - 0.45d, z + .2D));
 	   		}
            if(((Boolean) this.outlinedboundingBox.getValue()).booleanValue()&&this.heigh.getValue()==height.Low) {
 	   		RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - .2D, y - 0.3d, z - .2D, x + .2D, y - 0.4d, z + .2D));
 	   		}
 	   		GL11.glColor4f(1, 1, 1, 0.15f);
            if(((Boolean) this.boundingBox.getValue()).booleanValue()&&this.heigh.getValue()==height.High) {
 	   		RenderUtil.drawBoundingBox(new AxisAlignedBB(x - .2D, y-0.05, z - .2D, x + .2D, y - 0.45d, z + .2D));
 	   		}
 	   		GL11.glColor4f(1, 1, 1, 0.15f);
            if(((Boolean) this.boundingBox.getValue()).booleanValue()&&this.heigh.getValue()==height.Low) {
 	   		RenderUtil.drawBoundingBox(new AxisAlignedBB(x - .2D, y - 0.3d, z - .2D, x + .2D, y - 0.4d, z + .2D));
 	   		}
 	   		GL11.glEnable(3553);
 	   		GL11.glEnable(2929);
 	   		GL11.glDepthMask(true);
 	   		GL11.glDisable(3042);
    	}
	}
	enum height{
		High,
		Low;
	}
}
