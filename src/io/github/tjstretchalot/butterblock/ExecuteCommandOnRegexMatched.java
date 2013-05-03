package io.github.tjstretchalot.butterblock;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ExecuteCommandOnRegexMatched implements OnRegexMatched {
	
	private String commandString;
	
	public ExecuteCommandOnRegexMatched(FileConfiguration config) {
		commandString = config.getString("command");
	}

	@Override
	public void onRegexMatched(PlayerMessageListener list,
			List<String> seperated, YamlConfiguration memory, Logger logger,
			AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String realCommand = commandString.replace("%player%", player.getName());
		Communications.info(logger, Communications.DISPATCH_COMMAND, realCommand, player.getName(), event.getMessage());
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), realCommand);
	}

}
