package ml.bmlzootown.Listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import ml.bmlzootown.SafeStaff;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageReceiver implements Listener {
    Plugin p;

    public PluginMessageReceiver(Plugin plugin) {
        this.p = plugin;
    }

    @EventHandler
    public void on(PluginMessageEvent event) {
        if ( !event.getTag().equalsIgnoreCase("bml:safestaff") ) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();

        if (subChannel.equalsIgnoreCase("joined")) {
            if (event.getReceiver() instanceof ProxiedPlayer) {
                //ProxiedPlayer receiver = (ProxiedPlayer) event.getReceiver();
                String UUID = in.readUTF();
                //p.getLogger().info("Joined: " + UUID);
                if (!SafeStaff.players.contains(UUID)) {
                    //p.getLogger().info("Added player to list!");
                    if (SafeStaff.already.contains(UUID)) return;
                    SafeStaff.players.add(UUID);
                }
            }
        } else if (subChannel.equalsIgnoreCase("authenticated")) {
            String UUID = in.readUTF();
            //p.getLogger().info("Authenticated: " + UUID);
            SafeStaff.players.remove(UUID);
            SafeStaff.already.add(UUID);
            //p.getLogger().info("Removed player from list!");
        }
    }



}
