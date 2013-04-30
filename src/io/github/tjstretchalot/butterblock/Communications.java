package io.github.tjstretchalot.butterblock;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Communications {
	public static final int ENABLED = 0;
	public static final int DISABLED = 1;
	public static final int ON_SEPERATE_PLAYER = 2;
	public static final int TOTAL_MESSAGES = 3;

	public static final int SEPERATED_CHAT = 3;
	public static final int REGULAR_CHAT = 4;
	
	private static final String INFO_PREFIX = "";
	private static final String WARNING_PREFIX = "";
	private static final String[] MESSAGES;
	static {
		MESSAGES = new String[TOTAL_MESSAGES];
		MESSAGES[ENABLED] = INFO_PREFIX + "Enabled!";
		MESSAGES[DISABLED] = INFO_PREFIX + "Disabled!";
		MESSAGES[ON_SEPERATE_PLAYER] = WARNING_PREFIX + "%player% was seperated from the group because of " +
				"'%msg%'";
	}

	public static void info(Logger logger, int id, Object... other) {
		switch(id) {
		case ENABLED: case DISABLED:
			logger.info(MESSAGES[id]);
			break;
		case ON_SEPERATE_PLAYER:
			if(other.length < 2) {
				throw new RuntimeException("Need at least two arguments for the message type ON_SEPERATE_PLAYER");
			}
			if(!(other[0] instanceof Player) || !(other[1] instanceof String)) {
				throw new RuntimeException("Invalid arguments, expected a " +
						"Player and a String, got a " + other[0].getClass().getSimpleName() +
						" and " + other[1].getClass().getSimpleName());
			}
			Player player = (Player) other[0];
			String msg = (String) other[1];
			
			String finalMessage = MESSAGES[id].replace("%player%", player.getName()).replace("%msg%", msg);
			logger.warning(finalMessage);
			break;
		case SEPERATED_CHAT:
			if(other.length < 1) {
				throw new RuntimeException("Need at least 1 argument for the message type SEPERATED_CHAT");
			}
			if(!(other[0] instanceof AsyncPlayerChatEvent)) {
				throw new RuntimeException("Expected an AsyncPlayerChatEvent, got " + other[0].getClass().getSimpleName());
			}
			AsyncPlayerChatEvent event = (AsyncPlayerChatEvent) other[0];
			logger.info("[SEPERATED] " + String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()));
			break;
		case REGULAR_CHAT:
			if(other.length < 1) {
				throw new RuntimeException("Need at least 1 argument for the message type REGULAR_CHAT");
			}
			if(!(other[0] instanceof AsyncPlayerChatEvent)) {
				throw new RuntimeException("Expected an AsyncPlayerChatEvent, got " + other[0].getClass().getSimpleName());
			}
			event = (AsyncPlayerChatEvent) other[0];
			logger.info(String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()));
			break;
		}
	}

}
