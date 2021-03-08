package de.dytanic.cloudnet.ext.simplenametags.manager;

import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface ICloudNetTabManager {

	void onUpdate(@Nonnull Player player);

	void onUpdateUser(@Nonnull IPermissionUser user);

	void onUpdateGroup(@Nonnull IPermissionGroup group);

	void onDeactivate();

}