package com.mitsugaru.karmiclives.command.handlers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.command.ReloadCommand;
import com.mitsugaru.karmiclives.command.ResetCommand;
import com.mitsugaru.karmiclives.command.SetCommand;
import com.mitsugaru.karmiclives.permissions.PermissionNode;
import com.mitsugaru.karmiclives.services.CommandHandler;

/**
 * Handles the admin sub command.
 */
public class AdminCommander extends CommandHandler {

   public AdminCommander(KarmicLives plugin) {
      super(plugin, "admin");
      registerCommand("reload", new ReloadCommand());
      registerCommand("reset", new ResetCommand());
      registerCommand("set", new SetCommand());
   }

   @Override
   public boolean noArgs(CommandSender sender, Command command, String label) {
      sender.sendMessage(ChatColor.GRAY + "=======" + ChatColor.GOLD + "[KarmicLives] " + ChatColor.RED + "ADMIN" + ChatColor.GRAY + "=======");
      if(plugin.hasPermissionNode(sender, PermissionNode.ADMIN)) {
         sender.sendMessage(ChatColor.WHITE + "/lives admin " + ChatColor.GRAY + "set " + ChatColor.ITALIC + "<player> <lives> " + ChatColor.RESET
               + "- Force set the player's lives");
         sender.sendMessage(ChatColor.WHITE + "/lives admin " + ChatColor.GRAY + "reset " + ChatColor.ITALIC + "<player> " + ChatColor.RESET
               + "- Reset the player's lives");
         sender.sendMessage(ChatColor.WHITE + "/lives admin " + ChatColor.GRAY + "reload " + ChatColor.RESET + "- Reset the player's lives");
      }
      return true;
   }

   @Override
   public boolean unknownCommand(CommandSender sender, Command command, String label, String[] args) {
      sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Unknown command '" + ChatColor.WHITE + args[0].toLowerCase()
            + ChatColor.RED + "'");
      return true;
   }

}
