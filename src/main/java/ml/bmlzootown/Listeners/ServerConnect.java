package ml.bmlzootown.Listeners;

import ml.bmlzootown.SafeStaff;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class ServerConnect implements Listener {
    Plugin plugin;

    public ServerConnect(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(ServerSwitchEvent event) {
        if (event.getFrom() != null && !event.getPlayer().getServer().getInfo().getName().equalsIgnoreCase(event.getFrom().getName())) {
            SafeStaff.sendCustomData(event.getPlayer(), "already", event.getPlayer().getUniqueId().toString());
        }
    }
}
