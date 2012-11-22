package com.mitsugaru.karmiclives.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.permissions.PermissionNode;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class HelpCommand implements ILivesCommand {

   @Override
   public boolean execute(KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      sender.sendMessage(ChatColor.GRAY + "=======" + ChatColor.GOLD + "[KarmicLives]" + ChatColor.GRAY + "=======");
      if(plugin.hasPermissionNode(sender, PermissionNode.USE)) {
         sender.sendMessage(ChatColor.WHITE + "/lives " + ChatColor.GRAY + ChatColor.ITALIC + "[player] " + ChatColor.RESET
               + "- See current lives of self or given player");
      }
      if(plugin.hasPermissionNode(sender, PermissionNode.BUY)) {
         sender.sendMessage(ChatColor.WHITE + "/lives " + ChatColor.GRAY + "buy " + ChatColor.ITALIC + "<amount> " + ChatColor.RESET + "- Buy lives");
      }
      if(plugin.hasPermissionNode(sender, PermissionNode.SELL)) {
         sender.sendMessage(ChatColor.WHITE + "/lives " + ChatColor.GRAY + "sell " + ChatColor.ITALIC + "<amount> " + ChatColor.RESET
               + "- Sell lives");
      }
      sender.sendMessage(ChatColor.WHITE + "/lives " + ChatColor.GRAY + "version " + ChatColor.RESET + "- Check version and settings");
      return true;
   }

}
