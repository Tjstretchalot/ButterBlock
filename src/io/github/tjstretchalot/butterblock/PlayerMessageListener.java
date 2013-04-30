package io.github.tjstretchalot.butterblock;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerMessageListener implements Listener {
	private String[] regexes;
	private boolean remember;
	private YamlConfiguration memory;
	private List<String> seperated; 
	private Logger logger;

	public PlayerMessageListener(JavaPlugin plugin, YamlConfiguration memory, String[] regexes, 
			boolean remember, List<String> seperated2) {
		this.seperated = seperated2;
		this.regexes = regexes;
		this.remember = remember;
		this.memory = memory;
		this.logger = plugin.getLogger(); 
		
		logger.info("regexes[0]: " + this.regexes[0]);
	}
	
	@EventHandler(ignoreCancelled = true) 
	public void playerLogin(PlayerJoinEvent event) {
		if(!remember)
			return;
		
	}
	
	@EventHandler(ignoreCancelled = true) 
	public void playerLogout(PlayerQuitEvent event) {
		if(remember)
			return;
		synchronized(seperated) {
			if(seperated.contains(event.getPlayer().getName())) {
				logger.info("Removing: '" + event.getPlayer().getName() + "'");
				seperated.remove(event.getPlayer().getName());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) // everyone else needs a chance to cancel this event
	public void playerMessage(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		if(player.hasPermission("butterblock.ignore")) {
			sendToRegular(event);
			return;
		}
		boolean sepContains = false;
		synchronized(seperated) {
			if(seperated.contains(event.getPlayer().getName())) {
				sepContains = true;
			}
		}
		if(sepContains) {
			sendToSeperated(event);
			return;
		}
		
		String msg = event.getMessage();
		if(shouldSeperate(msg)) {
			synchronized(seperated) {
				Communications.info(logger, Communications.ON_SEPERATE_PLAYER, player, msg);
				seperated.add(event.getPlayer().getName());
				
				if(remember) {
					memory.set("memory.seperated", seperated);
				}
			}
			sendToSeperated(event);
		}else {
			sendToRegular(event);
		}
	}
	
	private boolean shouldSeperate(String msg) {
		msg = msg.toLowerCase();
		Pattern pattern = null;
		Matcher matcher = null;
		for(int i = 0; i < regexes.length; i++) {
			pattern = Pattern.compile(regexes[i]);
			matcher = pattern.matcher(msg);
			if(matcher.find()) {
				logger.info("found " + matcher.group());
				return true;
			}
		}
		return false;
	}

	private void sendToRegular(AsyncPlayerChatEvent event) {
		Communications.info(logger, Communications.REGULAR_CHAT, event);
		synchronized(seperated) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				if(!seperated.contains(player.getName())) {
					player.sendMessage(String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()));
				}
			}
		}
	}

	private void sendToSeperated(AsyncPlayerChatEvent event) {
		Communications.info(logger, Communications.SEPERATED_CHAT, event);
		synchronized(seperated) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				if(seperated.contains(player.getName())) {
					player.sendMessage(String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()));
				}else if(player.hasPermission("butterblock.seeall")) {
					player.sendMessage(ChatColor.GOLD + "[SEPERATED] " + ChatColor.GRAY + "<" + event.getPlayer().getDisplayName()
							+ "> " + event.getMessage());
				}
			}
		}
	}

	public void setRegexes(String[] regexes) {
		this.regexes = regexes;
	}
	
	public void setRemember(boolean remember) {
		this.remember = remember;
	}
}
