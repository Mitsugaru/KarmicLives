package com.mitsugaru.karmiclives.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class ViewCommand implements ILivesCommand {

   @Override
   public boolean execute(KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      String name = plugin.expandName(args[0]);
      if(name == null) {
         // Just use as is as they might be an offline player
         name = args[0];
      }
      if(!plugin.getLivesConfig().playerExists(name)) {
         // TODO message player
         return false;
      }
      final int lives = plugin.getLivesConfig().getLives(name);
      sender.sendMessage(plugin.getTag() + " " + ChatColor.AQUA + name + ChatColor.WHITE + "'s lives: " + ChatColor.GOLD + lives);
      return true;
   }

}
