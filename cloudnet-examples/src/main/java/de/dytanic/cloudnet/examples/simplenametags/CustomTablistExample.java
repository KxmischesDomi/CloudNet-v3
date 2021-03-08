package de.dytanic.cloudnet.examples.simplenametags;

import de.dytanic.cloudnet.ext.simplenametags.CloudNetSimpleNameTagsPlugin;
import de.dytanic.cloudnet.ext.simplenametags.manager.CloudNetCustomTeamsTabManager;
import de.dytanic.cloudnet.ext.simplenametags.manager.CloudNetPermsTabManager;
import de.dytanic.cloudnet.ext.simplenametags.manager.ICloudNetTabManager;
import de.dytanic.cloudnet.ext.simplenametags.models.CloudNetTabProperties;

import java.util.function.Function;

/**
 * Examples for plugins to customize the tablist
 */
public class CustomTablistExample {

	private final CloudNetSimpleNameTagsPlugin simpleNameTagsPlugin = CloudNetSimpleNameTagsPlugin.getInstance();

	/**
	 * You can override the {@link ICloudNetTabManager} with the {@link CloudNetCustomTeamsTabManager} to customize the appearance of the players
	 *
	 * {@link CloudNetCustomTeamsTabManager} has a {@link Function} with a player in its constructor which returns an instance of {@link CloudNetTabProperties}
	 * With returning the right properties in this object you can display players in any way you want
	 */
	public void overrideCloudTab() {

		simpleNameTagsPlugin.setCloudTabManager(new CloudNetCustomTeamsTabManager(player -> {
			//                                Team name /  prefix     / display / color / suffix / sortId
			return new CloudNetTabProperties("spectator", "§7Spec §8»", null    , null,   null   , 0);
		}));

	}

	/**
	 * To restore the default tab you can override the {@link ICloudNetTabManager} with the {@link CloudNetPermsTabManager}
	 */
	public void restoreCloudPermsTab() {

		simpleNameTagsPlugin.setCloudTabManager(new CloudNetPermsTabManager(simpleNameTagsPlugin));

	}

	/**
	 * To reset the tablist to the vanilla tablist, just set the {@link ICloudNetTabManager} to null
	 */
	public void removeTab() {

		simpleNameTagsPlugin.setCloudTabManager(null);

	}

}