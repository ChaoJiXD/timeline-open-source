package NovClient.Module.Modules.Render;

import NovClient.Module.Module;
import NovClient.Module.ModuleType;

public class TabGui extends Module {

	public TabGui() {
		super("TabGui", new String[]{"TabGui"}, ModuleType.Render);
		this.setEnabled(true);
	}

}
