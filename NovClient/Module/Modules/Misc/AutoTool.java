package NovClient.Module.Modules.Misc;


import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPacketSend;
import NovClient.API.Events.World.EventTick;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.BlockUtils;
import net.minecraft.util.BlockPos;

public class AutoTool extends Module {
	public AutoTool() {
		super("AutoTool", new String[] {"AutoTool"},ModuleType.Misc);
    }
	public Class type() {
        return EventPacketSend.class;
    }

	@EventHandler
	    public void onEvent(EventTick event) {
	        if (!mc.gameSettings.keyBindAttack.isKeyDown()) {
	            return;
	        }
	        if (mc.objectMouseOver == null) {
	            return;
	        }
	        BlockPos pos = mc.objectMouseOver.getBlockPos();
	        if (pos == null) {
	            return;
	        }
	        BlockUtils.updateTool(pos);
	    }
	}