package de.dytanic.cloudnet.ext.simplenametags.listener;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.permission.PermissionUpdateGroupEvent;
import de.dytanic.cloudnet.driver.event.events.permission.PermissionUpdateUserEvent;
import de.dytanic.cloudnet.ext.simplenametags.CloudNetSimpleNameTagsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class CloudNetSimpleNameTagsListener implements Listener {

    private final CloudNetSimpleNameTagsPlugin plugin;

    public CloudNetSimpleNameTagsListener(CloudNetSimpleNameTagsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(PlayerJoinEvent event) {
        if (plugin.getCloudTabManager() == null) return;
        plugin.getCloudTabManager().onUpdate(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(PlayerQuitEvent event) {
        if (plugin.getCloudTabManager() == null) return;
        plugin.getCloudTabManager().onUpdate(event.getPlayer());
    }

    @EventListener
    public void handle(PermissionUpdateUserEvent event) {
        if (plugin.getCloudTabManager() == null) return;
        plugin.getCloudTabManager().onUpdateUser(event.getPermissionUser());
    }

    @EventListener
    public void handle(PermissionUpdateGroupEvent event) {
        if (plugin.getCloudTabManager() == null) return;
        plugin.getCloudTabManager().onUpdateGroup(event.getPermissionGroup());
    }

}