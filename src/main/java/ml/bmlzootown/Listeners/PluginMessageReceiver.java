package ml.bmlzootown.Listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
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
                p.getLogger().info("Joined: " + UUID);
            }
        }
    }

}
