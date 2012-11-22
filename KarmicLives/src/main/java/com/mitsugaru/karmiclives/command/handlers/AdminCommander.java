package com.mitsugaru.karmiclives.command.handlers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.command.ReloadCommand;
import com.mitsugaru.karmiclives.services.CommandHandler;

/**
 * Handles the admin sub command.
 */
public class AdminCommander extends CommandHandler {

   public AdminCommander(KarmicLives plugin) {
      super(plugin, "admin");
      registerCommand("reload", new ReloadCommand());
   }

   @Override
   public boolean noArgs(CommandSender sender, Command command, String label) {
      // TODO show admin menu here
      return true;
   }

   @Override
   public boolean unknownCommand(CommandSender sender, Command command, String label, String[] args) {
      sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Unknown command '" + ChatColor.WHITE + args[0].toLowerCase()
            + ChatColor.RED + "'");
      return true;
   }

}
