package de.dytanic.cloudnet.examples.simplenametags;

import de.dytanic.cloudnet.driver.event.events.permission.PermissionUpdateGroupEvent;
import de.dytanic.cloudnet.driver.event.events.permission.PermissionUpdateUserEvent;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.simplenametags.manager.ICloudNetTabManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Examples for plugins to customize the tablist
 */
public class OwnTabManager implements ICloudNetTabManager {

	/**
	 * Called when a player joined and when the tab manager is set as current {@link ICloudNetTabManager}
	 *
	 * @param player the player to update the tablist for
	 */
	@Override
	public void onUpdate(@NotNull Player player) {

	}

	/**
	 * Called when the {@link PermissionUpdateUserEvent} is called
	 *
	 * @param user the user that has been updated
	 */
	@Override
	public void onUpdateUser(@NotNull IPermissionUser user) {

	}

	/**
	 * Called when the {@link PermissionUpdateGroupEvent} is called
	 *
	 * @param group the group that has been updated
	 */
	@Override
	public void onUpdateGroup(@NotNull IPermissionGroup group) {

	}

	/**
	 * Called when the tab manager is not longer the current tab manager
	 * Should be used to reset the tab list
	 */
	@Override
	public void onDeactivate() {

	}

}