package de.dytanic.cloudnet.ext.simplenametags;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.simplenametags.listener.CloudNetSimpleNameTagsListener;
import de.dytanic.cloudnet.ext.simplenametags.manager.CloudNetPermsTabManager;
import de.dytanic.cloudnet.ext.simplenametags.manager.ICloudNetTabManager;
import de.dytanic.cloudnet.wrapper.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CloudNetSimpleNameTagsPlugin extends JavaPlugin {

    private static CloudNetSimpleNameTagsPlugin instance;

    private ICloudNetTabManager cloudTabManager = new CloudNetPermsTabManager(this);

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Listener listener = new CloudNetSimpleNameTagsListener(this);

        this.getServer().getPluginManager().registerEvents(listener, this);
        CloudNetDriver.getInstance().getEventManager().registerListener(listener);
    }

    @Override
    public void onDisable() {
        CloudNetDriver.getInstance().getEventManager().unregisterListeners(this.getClass().getClassLoader());
        Wrapper.getInstance().unregisterPacketListenersByClassLoader(this.getClass().getClassLoader());
    }

    @Nullable
    @CheckReturnValue
    public ICloudNetTabManager getCloudTabManager() {
        return cloudTabManager;
    }

    public void setCloudTabManager(@Nullable ICloudNetTabManager cloudTabManager) {
        if (this.cloudTabManager != null && this.cloudTabManager != cloudTabManager) this.cloudTabManager.onDeactivate();
        this.cloudTabManager = cloudTabManager;
        if (cloudTabManager != null) Bukkit.getOnlinePlayers().forEach(cloudTabManager::onUpdate);
    }

    @Nonnull
    @CheckReturnValue
    public static CloudNetSimpleNameTagsPlugin getInstance() {
        return instance;
    }

}