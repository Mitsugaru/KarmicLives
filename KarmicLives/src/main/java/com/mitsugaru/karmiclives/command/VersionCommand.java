package com.mitsugaru.karmiclives.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.config.nodes.RootConfigNode;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class VersionCommand implements ILivesCommand {

   @Override
   public boolean execute(KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      sender.sendMessage(ChatColor.GRAY + "=================");
      sender.sendMessage(ChatColor.GOLD + "KarmicLives " + ChatColor.AQUA + "v" + plugin.getDescription().getVersion());
      sender.sendMessage(ChatColor.GOLD + "By " + plugin.getDescription().getAuthors().get(0));
      sender.sendMessage(ChatColor.GRAY + "=================");
      sender.sendMessage(ChatColor.BLUE + "Cost: " + plugin.getRootConfig().getDouble(RootConfigNode.LIVES_COST));
      final int max = plugin.getRootConfig().getInt(RootConfigNode.LIVES_MAXIMUM);
      String maxString = "" + max;
      if(max <= 0) {
         maxString = "Infinite";
      }
      sender.sendMessage(ChatColor.BLUE + "Max: " + maxString);
      return true;
   }

}
