package com.mitsugaru.karmiclives.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class VersionCommand implements ILivesCommand {

   @Override
   public boolean execute(KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      sender.sendMessage(ChatColor.GRAY + "=================");
      sender.sendMessage(ChatColor.GOLD + "KarmicLives " + ChatColor.AQUA + "v" + plugin.getDescription().getVersion());
      sender.sendMessage(ChatColor.GOLD + "By " + plugin.getDescription().getAuthors().get(0));
      sender.sendMessage(ChatColor.GRAY + "=================");
      return true;
   }

}
