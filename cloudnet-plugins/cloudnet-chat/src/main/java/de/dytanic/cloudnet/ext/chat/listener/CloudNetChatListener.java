package de.dytanic.cloudnet.ext.chat.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.chat.CloudNetChatPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.annotation.Nonnull;

public class CloudNetChatListener implements Listener {

	private final CloudNetChatPlugin plugin;

	public CloudNetChatListener(CloudNetChatPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void handleChat(AsyncPlayerChatEvent event) {
		if (plugin.getFormatFunction() == null) return;

		Player player = event.getPlayer();

		IPermissionUser user = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());

		if (user == null) {
			return;
		}

		IPermissionGroup group = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(user);

		String message = event.getMessage().replace("%", "%%");
		if (player.hasPermission("cloudnet.chat.color")) {
			message = ChatColor.translateAlternateColorCodes('&', message);
		}

		if (ChatColor.stripColor(message).trim().isEmpty()) {
			event.setCancelled(true);
			return;
		}
		

		String format = plugin.getFormatFunction().apply(player)
				.replace("%name%", player.getName())
				.replace("%uniqueId%", player.getUniqueId().toString());

		if (group != null) {
			format = ChatColor.translateAlternateColorCodes('&',
					format
							.replace("%group%", group.getName())
							.replace("%display%", group.getDisplay())
							.replace("%prefix%", group.getPrefix())
							.replace("%suffix%", group.getSuffix())
							.replace("%color%", group.getColor())
			);
		} else {
			format = ChatColor.translateAlternateColorCodes('&',
					format
							.replace("%group%", "")
							.replace("%display%", "")
							.replace("%prefix%", "")
							.replace("%suffix%", "")
							.replace("%color%", "")
			);
		}

		event.setFormat(format.replace("%message%", message));
	}

}