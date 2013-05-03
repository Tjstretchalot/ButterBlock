package io.github.tjstretchalot.butterblock;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface OnRegexMatched {
	public void onRegexMatched(PlayerMessageListener playerMessageListener, List<String> seperated,
			YamlConfiguration memory, Logger logger, AsyncPlayerChatEvent event);
}
