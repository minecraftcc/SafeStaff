package ml.bmlzootown;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ml.bmlzootown.Listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;


public class SafeStaff extends JavaPlugin implements PluginMessageListener {
	public static Plugin plugin;
	Logger log = Logger.getLogger("Minecraft");
	public PlayerListener pl = new PlayerListener(this);
	public HashMap<String, String> notLoggedIn = new HashMap<String, String>();
	public HashMap<String, Integer> attempts = new HashMap<String, Integer>();

	public void onEnable() {
		this.log.info("[SafeStaff] is now enabled!");
		plugin = this;
		File file = new File(getDataFolder() + File.separator + "config.yml");
		if (!file.exists()) {
			this.log.info("No config found! Making one for you...");
			getConfig().addDefault("mod_login", "pasta");
			getConfig().addDefault("admin_login", "hac1sHax0r");
			getConfig().addDefault("maxAttempts", 3);
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.pl, this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "bml:safestaff");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "bml:safestaff", this);
	}
	
	public void onDisable() {
		this.log.info("[SafeStaff] is now disabled!");
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("bml:safestaff")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subChannel = in.readUTF();
		if (subChannel.equalsIgnoreCase("already")) {
			String data = in.readUTF();
			Player p = Bukkit.getPlayer(UUID.fromString(data));
			if (p != null) {
				if (SafeStaff.hasPerm(p, "SafeStaff.use")) {
					this.notLoggedIn.remove(p.getName());
					p.sendMessage(ChatColor.GREEN + "You are already logged in -- welcome back!");
				}
			}
		}
	}

	public static void sendPluginMessage(Plugin plugin, String channelName, String subChannelName, String data) {
		if (Bukkit.getServer().getOnlinePlayers().size() < 1) {
			return;
		}

		ByteArrayDataOutput o = ByteStreams.newDataOutput();
		// Channel = SafeStaff
		o.writeUTF(subChannelName);
		o.writeUTF(data);

		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						Bukkit.getServer().sendPluginMessage(plugin, channelName, o.toByteArray());
					}
				},
				1000
		);
	}
	
	 public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((sender instanceof Player)) {
		 Player p = (Player)sender;
		 if (cmd.getName().equalsIgnoreCase("safestaff")) {
			if (args.length == 0) {
				p.sendMessage(ChatColor.GOLD + "----SafeStaff----");
				p.sendMessage(ChatColor.GOLD + "Commands:");
				p.sendMessage(ChatColor.GOLD + "/safestaff setpassword [admin/mod] [password]");
				p.sendMessage(ChatColor.GOLD + "/login [password]");
				p.sendMessage(ChatColor.GOLD + "--------------------");
			}
			if (args.length == 1 || args.length == 2) {
				if (args[0].equalsIgnoreCase("setpassword")) {
					p.sendMessage(ChatColor.RED + "Correct Usage: /safestaff setpassword [admin/mod] [password]");
				} else if (args[0].equalsIgnoreCase("reload")) {
					reloadConfig();
					p.sendMessage(ChatColor.GOLD + "[SafeStaff] Config reloaded!");
				} else {
					p.sendMessage(ChatColor.RED + "Incorrect Usage: See /safestaff");
				}
			}
			if (args.length == 3 && args[0].equalsIgnoreCase("setpassword")) {
				if (SafeStaff.hasPerm(p, "SafeStaff.admin")) {
					if (args[1].equalsIgnoreCase("admin")) {
						getConfig().set("admin_login", args[2]);
						saveConfig();
						p.sendMessage(ChatColor.GOLD + "[SafeStaff] Admin password changed to: " + args[2]);
						reloadConfig();
						p.sendMessage(ChatColor.GOLD + "[SafeStaff] Config reloaded!");
						return true;
					} else if (args[1].equalsIgnoreCase("mod")) {
						getConfig().set("mod_login", args[2]);
						saveConfig();
						p.sendMessage(ChatColor.GOLD + "[SafeStaff] Mod password changed to: " + args[2]);
						reloadConfig();
						p.sendMessage(ChatColor.GOLD + "[SafeStaff] Config reloaded!");
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "Incorrect Usage: /safestaff setpassword [admin/mod] [password]");
					}
				}
				p.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				return true;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("login")) {
			if (args.length == 1) {
				if (SafeStaff.hasPerm(p, "SafeStaff.use")) {
					if (((Integer)this.attempts.get(p.getName())).intValue() != 1) {
			            if (SafeStaff.hasPerm(p, "SafeStaff.admin")) {
							if (args[0].equals(getConfig().get("admin_login"))) {
			            		p.sendMessage(ChatColor.RED + "You are now logged in!");
			            		this.notLoggedIn.remove(p.getName());
								sendPluginMessage(plugin,"bml:safestaff", "authenticated", p.getUniqueId().toString());
			            		return true;
			            	}
			            }
			            
			            	if (SafeStaff.hasPerm(p, "SafeStaff.mod")) {
			            		if (args[0].equals(getConfig().get("mod_login"))) {
			            			p.sendMessage(ChatColor.GREEN + "You are now logged in!");
			            			this.notLoggedIn.remove(p.getName());
									sendPluginMessage(plugin,"bml:safestaff", "authenticated", p.getUniqueId().toString());
			            			return true;
			            		}
			            	}
			            
			            int oldAttempts = ((Integer)this.attempts.get(p.getName())).intValue();
			            int newAttempts = oldAttempts - 1;
			            if (oldAttempts != 0) {
			              this.attempts.put(p.getName(), Integer.valueOf(newAttempts));
			            }
			            p.sendMessage(ChatColor.RED + "Wrong password!");
			            p.sendMessage(ChatColor.RED + "Attempts left: " + this.attempts.get(p.getName()));
			            return true;
			          }
			          p.kickPlayer(ChatColor.RED + "You entered the wrong password too many times!");
				} else {
					p.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				}
			} else {
				p.sendMessage(ChatColor.RED + "Incorrect Usage: /login [password]");
			}
		}
	} else if (sender instanceof ConsoleCommandSender) {
		if (cmd.getName().equalsIgnoreCase("safestaff")) {
			if (args.length == 0) {
				this.log.info("Commands:");
				this.log.info("safestaff setpassword [admin/mod] [password]");
				this.log.info("safestaff reload");
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					reloadConfig();
					this.log.info("[SafeStaff] Config reloaded!");
					return true;
				} else {
					this.log.info("Incorrect Usage: See /safestaff");
				}
			}
			if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setpassword")) {
					if (args[1].equalsIgnoreCase("admin")) {
						getConfig().set("admin_login", args[2]);
						saveConfig();
					} 
					if (args[1].equalsIgnoreCase("mod")) {
						getConfig().set("mod_login", args[2]);
						saveConfig();
					}
				}
			}
		}
	}
		 return false;
	 }
	 
	 public static boolean hasPerm(Player player, String perm) {
		 Permission p = new Permission(perm,PermissionDefault.FALSE);
		 return player.hasPermission(p);
	 }
	
}
