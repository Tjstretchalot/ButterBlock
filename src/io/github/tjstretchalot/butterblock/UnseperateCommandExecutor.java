package io.github.tjstretchalot.butterblock;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnseperateCommandExecutor implements CommandExecutor {
	private List<String> seperated;
	private Logger logger;
	
	public UnseperateCommandExecutor(List<String> seperated, Logger logger) {
		this.seperated = seperated;
		this.logger = logger;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(args.length != 1) {
			sender.sendMessage("You need exactly 1 argument for this command");
			return false;
		}
		
		Player target = (Bukkit.getServer().getPlayer(args[0]));
		if(target == null) {
			sender.sendMessage("Player not found or not online");
			return false;
		}
		
		synchronized(seperated) {
			if(!seperated.contains(target.getName())) {
				sender.sendMessage("Player is not seperated!");
				return false;
			}else {
				seperated.remove(target.getName());
				sender.sendMessage(target.getName() + " is no longer seperated");
				if(sender instanceof Player)
					logger.info(sender.getName() + " unseperated " + target.getName());
			}
		}
		
		return true;
	}

}
