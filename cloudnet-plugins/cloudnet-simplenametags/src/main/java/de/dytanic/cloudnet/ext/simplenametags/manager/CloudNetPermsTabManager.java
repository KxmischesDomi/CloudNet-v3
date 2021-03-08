package de.dytanic.cloudnet.ext.simplenametags.manager;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class CloudNetPermsTabManager implements ICloudNetTabManager {

	private final JavaPlugin plugin;

	public CloudNetPermsTabManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onUpdate(@NotNull Player player) {
		Bukkit.getScheduler().runTask(plugin, () -> BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player));
	}

	@Override
	public void onUpdateUser(@NotNull IPermissionUser user) {
		Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getOnlinePlayers().stream()
				.filter(player -> player.getUniqueId().equals(user.getUniqueId()))
				.findFirst()
				.ifPresent(value -> BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(value)));
	}

	@Override
	public void onUpdateGroup(@NotNull IPermissionGroup group) {
		Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getOnlinePlayers().forEach(player -> {
			IPermissionUser permissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());

			if (permissionUser != null && permissionUser.inGroup(group.getName())) {
				BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);
			}

		}));
	}

	@Override
	public void onDeactivate() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setDisplayName((player.getGameMode() == GameMode.SPECTATOR ? "§7" : "") + player.getName());
			for (Team team : new ArrayList<>(player.getScoreboard().getTeams())) {
				team.unregister();
			}
		}
	}

}