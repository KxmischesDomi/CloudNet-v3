package de.dytanic.cloudnet.examples.chat;

import de.dytanic.cloudnet.ext.chat.CloudNetChatPlugin;

import java.util.function.Function;

public class CustomChatFormat {

	private final CloudNetChatPlugin cloudNetChatPlugin = CloudNetChatPlugin.getInstance();

	/**
	 * You can override the {@link Function} with {@link CloudNetChatPlugin#setFormatFunction(Function)} which returns a String which is the format like in the config file
	 */
	public void setGamemodeFormat() {

		// This would always show the players gamemode before his name
		cloudNetChatPlugin.setFormatFunction(player -> {
			return "§6" + player.getGameMode().name() + " §8┃ %display%%name% &8» §7%message%";
		});

		// This would show if the player is op or not
		cloudNetChatPlugin.setFormatFunction(player -> {
			if (player.isOp()) {
				return "§4OP §8┃ %display%%name% &8» §7%message%";
			}
			return "%display%%name% &8» §7%message%";
		});

	}

	/**
	 * If you want to reset the format to the default from the config file, just execute {@link CloudNetChatPlugin#setDefaultFormatFunction()}
	 */
	public void restoreDefaultFormat() {

		cloudNetChatPlugin.setDefaultFormatFunction();

	}

}