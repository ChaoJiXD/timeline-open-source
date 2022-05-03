package NovClient.Command.Commands;

import NovClient.Command.Command;
import NovClient.Util.Helper;
import net.minecraft.client.Minecraft;

public class Help extends Command {
	public Help() {
		super("Help", new String[] { "list" }, "", "sketit");
	}

	@Override
	public String execute(String[] args) {
		if (args.length == 0) {
			Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
			Helper.sendMessageWithoutPrefix("\u00a7b\u00a7lNov Reload Client");
            Helper.sendMessageWithoutPrefix("\u00a7b.help >\u00a77 list commands");
            Helper.sendMessageWithoutPrefix("\u00a7b.t >\u00a77 toggle a module on/off");
            Helper.sendMessageWithoutPrefix("\u00a7b.modlist >\u00a77 list all modules");
			Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
		} else {
			Helper.sendMessage("Correct usage .help");
		}
		return null;
	}
}
