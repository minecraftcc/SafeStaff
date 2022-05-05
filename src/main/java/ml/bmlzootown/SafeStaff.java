package ml.bmlzootown;

import ml.bmlzootown.Listeners.PluginMessageReceiver;
import net.md_5.bungee.api.plugin.Plugin;

public class SafeStaff extends Plugin {

	@Override
	public void onEnable() {
		getLogger().info("SafeStaff-Bungee loaded!");
		getProxy().getPluginManager().registerListener(this, new PluginMessageReceiver(this));
		getProxy().registerChannel("bml:SafeStaff");
	}

	@Override
	public void onDisable() {
		getProxy().unregisterChannel("bml:SafeStaff");
	}
	
}
