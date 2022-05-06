package ml.bmlzootown;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ml.bmlzootown.Listeners.CommandEvent;
import ml.bmlzootown.Listeners.PlayerDisconnect;
import ml.bmlzootown.Listeners.PluginMessageReceiver;
import ml.bmlzootown.Listeners.ServerConnect;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class SafeStaff extends Plugin {

	public static List<String> players;
	public static List<String> already;
	public static Logger logger;

	@Override
	public void onEnable() {
		players = new ArrayList<>();
		already = new ArrayList<>();
		logger = getLogger();
		logger.info("SafeStaff-Bungee loaded!");
		getProxy().getPluginManager().registerListener(this, new PluginMessageReceiver(this));
		getProxy().getPluginManager().registerListener(this, new CommandEvent(this));
		getProxy().getPluginManager().registerListener(this, new ServerConnect(this));
		getProxy().getPluginManager().registerListener(this, new PlayerDisconnect(this));
		getProxy().registerChannel("bml:safestaff");
	}

	@Override
	public void onDisable() {
		getProxy().unregisterChannel("bml:safestaff");
	}

	public static void sendCustomData(ProxiedPlayer player, String subchannel, String uuid) {
		Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
		if (networkPlayers == null || networkPlayers.isEmpty()) {
			return;
		}
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(subchannel);
		out.writeUTF(uuid);

		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						player.getServer().getInfo().sendData("bml:safestaff", out.toByteArray());
					}
				},
				2000
		);
	}
	
}
