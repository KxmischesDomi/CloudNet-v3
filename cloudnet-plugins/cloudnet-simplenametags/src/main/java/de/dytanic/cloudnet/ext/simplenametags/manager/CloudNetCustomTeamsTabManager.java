package de.dytanic.cloudnet.ext.simplenametags.manager;

import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.simplenametags.models.CloudNetTabProperties;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class CloudNetCustomTeamsTabManager implements ICloudNetTabManager {

	private final Function<Player, CloudNetTabProperties> propertiesFunction;

	public CloudNetCustomTeamsTabManager(@Nonnull Function<Player, CloudNetTabProperties> propertiesFunction) {
		this.propertiesFunction = propertiesFunction;
	}

	@Override
	public void onUpdate(@NotNull Player player) {
		handleTabUpdate(player);
	}

	@Override
	public void onUpdateUser(@NotNull IPermissionUser user) {
		Bukkit.getOnlinePlayers().stream()
				.filter(player -> player.getUniqueId().equals(user.getUniqueId()))
				.findFirst()
				.ifPresent(this::handleTabUpdate);
	}

	@Override
	public void onUpdateGroup(@NotNull IPermissionGroup event) {
		Bukkit.getOnlinePlayers().forEach(this::handleTabUpdate);
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

	private void handleTabUpdate(@Nonnull Player player) {

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			Scoreboard scoreboard = onlinePlayer.getScoreboard();
			Team team = scoreboard.getEntryTeam(player.getName());
			if (team == null) continue;
			team.removeEntry(player.getName());
			if (team.getEntries().isEmpty()) {
				team.unregister();
			}
		}

		CloudNetTabProperties properties = propertiesFunction.apply(player);
		player.setDisplayName(ChatColor.translateAlternateColorCodes('&', properties.getDisplay() + player.getName()));

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

			properties = propertiesFunction.apply(onlinePlayer);

			for (Player onlinePlayer2 : Bukkit.getOnlinePlayers()) {
				Team team = createTeam(properties, onlinePlayer2.getScoreboard());
				team.addEntry(onlinePlayer.getName());
			}
		}
	}

	protected Team createTeam(@Nonnull CloudNetTabProperties properties, Scoreboard scoreboard) {
		String teamName = getTeamName(properties);
		Team team = scoreboard.getTeam(teamName);

		if (team == null) {
			team = scoreboard.registerNewTeam(teamName);
		}

		team.setPrefix(properties.getPrefix() == null ? "" : ChatColor.translateAlternateColorCodes('&', properties.getPrefix()));
		team.setSuffix(properties.getSuffix() == null ? "" : ChatColor.translateAlternateColorCodes('&', properties.getSuffix()));
		setColor(team, properties.getColor(), properties.getPrefix());

		return team;
	}

	protected void setColor(@Nonnull Team team, @Nullable String color, @Nullable String prefix) {

		try {
			Method method = team.getClass().getDeclaredMethod("setColor", ChatColor.class);
			method.setAccessible(true);

			if (color != null && !color.isEmpty()) {
				ChatColor chatColor = ChatColor.getByChar(color.replaceAll("&", "").replaceAll("§", ""));
				if (chatColor != null) {
					method.invoke(team, chatColor);
				}
			} else if (prefix != null && !prefix.isEmpty()) {
				color = ChatColor.getLastColors(prefix.replace('&', '§'));
				if (!color.isEmpty()) {
					ChatColor chatColor = ChatColor.getByChar(color.replaceAll("&", "").replaceAll("§", ""));
					if (chatColor != null) {
						method.invoke(team, chatColor);
					}
				}
			}
		} catch (NoSuchMethodException ignored) {
		} catch (IllegalAccessException | InvocationTargetException exception) {
			exception.printStackTrace();
		}
	}

	private String getTeamName(@Nonnull CloudNetTabProperties properties) {
		return properties.getSortId() + properties.getName();
	}

}