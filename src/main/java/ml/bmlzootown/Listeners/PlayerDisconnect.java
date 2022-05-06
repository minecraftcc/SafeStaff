package ml.bmlzootown.Listeners;

import ml.bmlzootown.SafeStaff;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnect implements Listener {
    Plugin plugin;

    public PlayerDisconnect(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(PlayerDisconnectEvent event) {
        ProxiedPlayer p = event.getPlayer();
        SafeStaff.already.remove(p.getUniqueId().toString());
    }

}
