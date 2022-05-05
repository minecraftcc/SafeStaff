package ml.bmlzootown;

import ml.bmlzootown.Listeners.CommandEvent;
import ml.bmlzootown.Listeners.PluginMessageReceiver;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SafeStaff extends Plugin {

	public static List<String> players;
	public static Logger logger;

	@Override
	public void onEnable() {
		players = new ArrayList<>();
		logger = getLogger();
		logger.info("SafeStaff-Bungee loaded!");
		getProxy().getPluginManager().registerListener(this, new PluginMessageReceiver(this));
		getProxy().getPluginManager().registerListener(this, new CommandEvent(this));
		getProxy().registerChannel("bml:safestaff");
	}

	@Override
	public void onDisable() {
		getProxy().unregisterChannel("bml:safestaff");
	}
	
}
