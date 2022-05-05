package ml.bmlzootown.Listeners;

import ml.bmlzootown.SafeStaff;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class CommandEvent implements Listener {
    Plugin plugin;

    public CommandEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(ChatEvent event) {
        if (event.isCommand()) {
            if (event.getSender() instanceof ProxiedPlayer) {
                ProxiedPlayer p = (ProxiedPlayer) event.getSender();
                if (SafeStaff.players.contains(p.getUniqueId().toString())) {
                    if (!event.getMessage().contains("/login")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
