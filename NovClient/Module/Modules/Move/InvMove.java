package NovClient.Module.Modules.Move;


import java.awt.Color;


import org.lwjgl.input.Keyboard;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPacketSend;
import NovClient.API.Events.World.EventTick;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.util.BlockPos;

public class InvMove extends Module {

	public static Option<Boolean> Carry = new Option("Carry", "Carry", Boolean.valueOf(false));
    boolean inInventory = false;
    public InvMove() {
        super("InvMove", new String[]{"InvMove", "crit"}, ModuleType.Move);
        this.setColor(new Color(235, 194, 138).getRGB());
        this.addValues(this.Carry);
    }
    @EventHandler
    public void onPacket(EventPacketSend event) {
    	EventPacketSend ep = (EventPacketSend) event;
        Packet packet = ep.getPacket();
        if(this.Carry.getValue())
        if (packet instanceof C0DPacketCloseWindow) {
            ep.setCancelled(true);
        }


    }
    @EventHandler
    public void onTick(EventTick event) {
        if (mc.currentScreen instanceof GuiChat) {
            return;
        }
    	if (mc.currentScreen != null) {
				KeyBinding[] moveKeys = new KeyBinding[]{
					mc.gameSettings.keyBindForward,
					mc.gameSettings.keyBindBack,
					mc.gameSettings.keyBindLeft,
					mc.gameSettings.keyBindRight,
					mc.gameSettings.keyBindJump					
				};
				for (KeyBinding bind : moveKeys){
					KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
				}
				if(!inInventory){

					inInventory = !inInventory;
				}
			

        }else{
        	if(inInventory){

				inInventory = !inInventory;
			}
        }
    }
}
