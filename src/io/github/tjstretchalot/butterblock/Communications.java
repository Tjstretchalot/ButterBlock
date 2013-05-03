package io.github.tjstretchalot.butterblock;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Communications {
	public static final int ENABLED = 0;
	public static final int DISABLED = 1;
	public static final int ON_SEPERATE_PLAYER = 2;
	public static final int USING_COMMAND = 3;
	public static final int SEPERATE_BELIEVERS = 4;
	public static final int DISPATCH_COMMAND = 5;
	public static final int TOTAL_MESSAGES = 6;

	public static final int SEPERATED_CHAT = 6;
	public static final int REGULAR_CHAT = 7;
	
	private static final String INFO_PREFIX = "";
	private static final String WARNING_PREFIX = "";
	private static final String[] MESSAGES;
	static {
		MESSAGES = new String[TOTAL_MESSAGES];
		MESSAGES[ENABLED] = INFO_PREFIX + "Enabled!";
		MESSAGES[DISABLED] = INFO_PREFIX + "Disabled!";
		MESSAGES[ON_SEPERATE_PLAYER] = WARNING_PREFIX + "%player% was seperated from the group because of " +
				"'%msg%'";
		MESSAGES[USING_COMMAND] = INFO_PREFIX + "Dispatching a command on regex match";
		MESSAGES[SEPERATE_BELIEVERS] = INFO_PREFIX + "Seperating players upon regex match";
		MESSAGES[DISPATCH_COMMAND] = WARNING_PREFIX + "Dispatched '%command%' because %player% said '%message%'";
	}

	public static void info(Logger logger, int id, Object... other) {
		switch(id) {
		case ENABLED: case DISABLED:
		case USING_COMMAND: case SEPERATE_BELIEVERS:
			logger.info(MESSAGES[id]);
			break;
		case ON_SEPERATE_PLAYER:
			if(other.length < 2) {
				throw new RuntimeException("Need at least two arguments for the message type ON_SEPERATE_PLAYER");
			}
			verify(other, Player.class, String.class);
			Player player = (Player) other[0];
			String msg = (String) other[1];
			
			String finalMessage = MESSAGES[id].replace("%player%", player.getName()).replace("%msg%", msg);
			logger.warning(finalMessage);
			break;
		case SEPERATED_CHAT:
			if(other.length < 1) {
				throw new RuntimeException("Need at least 1 argument for the message type SEPERATED_CHAT");
			}
			verify(other, AsyncPlayerChatEvent.class);
			AsyncPlayerChatEvent event = (AsyncPlayerChatEvent) other[0];
			logger.info("[SEPERATED] " + String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()));
			break;
		case REGULAR_CHAT:
			if(other.length < 1) {
				throw new RuntimeException("Need at least 1 argument for the message type REGULAR_CHAT");
			}
			verify(other, AsyncPlayerChatEvent.class);
			
			event = (AsyncPlayerChatEvent) other[0];
			logger.info(String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()));
			break;
		case DISPATCH_COMMAND:
			if(other.length < 3)
				throw new RuntimeException("Need at least 3 arguments for the message type DISPATCH_COMMAND");
			verify(other, String.class, String.class, String.class);
			
			String command = (String) other[0];
			String playerName = (String) other[1];
			String badMessage = (String) other[2];
			String toSay = MESSAGES[DISPATCH_COMMAND]
					.replace("%command%", command)
					.replace("%player%", playerName)
					.replace("%message%", badMessage);
			logger.info(toSay);
			break;
		}
	}

	@SafeVarargs
	private static void verify(Object[] got, Class<? extends Object>... expected) {
		for(int i = 0; i < got.length; i++) {
			if(!got[i].getClass().equals(expected[i])) {
				throw new RuntimeException("Expected argument " + i + " to be a " +
						expected[i].getSimpleName() + " but got a " +
						got[i].getClass().getSimpleName());
			}
		}
	}

}
