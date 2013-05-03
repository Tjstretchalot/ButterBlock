package io.github.tjstretchalot.butterblock;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SeperateOnRegexMatched implements OnRegexMatched {

	@Override
	public void onRegexMatched(PlayerMessageListener list,
			List<String> seperated, YamlConfiguration memory, Logger logger,
			AsyncPlayerChatEvent event) {
		synchronized(seperated) {
			Communications.info(logger, Communications.ON_SEPERATE_PLAYER, event.getPlayer(), event.getMessage());
			seperated.add(event.getPlayer().getName());
			
			if(list.remember()) {
				memory.set("memory.seperated", seperated);
			}
		}
		list.sendToSeperated(event);
	}

}
