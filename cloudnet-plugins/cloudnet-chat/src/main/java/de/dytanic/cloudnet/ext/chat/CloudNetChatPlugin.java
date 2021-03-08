package de.dytanic.cloudnet.ext.chat;

import de.dytanic.cloudnet.ext.chat.listener.CloudNetChatListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class CloudNetChatPlugin extends JavaPlugin {

    private static CloudNetChatPlugin instance;

    private String defaultFormat;
    private Function<Player, String> formatFunction = player -> defaultFormat;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        this.defaultFormat = getConfig().getString("format");

        getServer().getPluginManager().registerEvents(new CloudNetChatListener(this), this);
    }

    @Nonnull
    @CheckReturnValue
    public String getDefaultFormat() {
        return defaultFormat;
    }

    @Nullable
    @CheckReturnValue
    public Function<Player, String> getFormatFunction() {
        return formatFunction;
    }

    public void setDefaultFormatFunction() {
        this.formatFunction = player -> defaultFormat;
    }

    public void setFormatFunction(@Nullable Function<Player, String> formatFunction) {
        this.formatFunction = formatFunction;
    }

    @Nonnull
    @CheckReturnValue
    public static CloudNetChatPlugin getInstance() {
        return instance;
    }

}
