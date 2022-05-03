
package NovClient.Module.Modules.Legit;

import java.awt.Color;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;


public class Eagle
extends Module {

    public Eagle() {
        super("Eagle", new String[]{"Eagle"}, ModuleType.Legit);
        this.setColor(new Color(208, 30, 142).getRGB());
    }

    public Block getBlock(BlockPos pos) {
        return this.mc.theWorld.getBlockState(pos).getBlock();
    }

    public Block getBlockUnderPlayer(EntityPlayer player) {
        return this.getBlock(new BlockPos(player.posX, player.posY - 1.0, player.posZ));
    }
    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (this.getBlockUnderPlayer((EntityPlayer)this.mc.thePlayer) instanceof BlockAir) {
            if (this.mc.thePlayer.onGround) {
                KeyBinding.setKeyBindState((int)this.mc.gameSettings.keyBindSneak.getKeyCode(), (boolean)true);
            }
        } else if (this.mc.thePlayer.onGround) {
            KeyBinding.setKeyBindState((int)this.mc.gameSettings.keyBindSneak.getKeyCode(), (boolean)false);
        }
    }
    public void onEnable() {
        this.mc.thePlayer.setSneaking(false);
        super.onEnable();
    }

    public void onDisable() {
        KeyBinding.setKeyBindState((int)this.mc.gameSettings.keyBindSneak.getKeyCode(), (boolean)false);
        super.onDisable();
    }
    }


