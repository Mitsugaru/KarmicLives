package com.mitsugaru.karmiclives.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.permissions.PermissionNode;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class ReloadCommand implements ILivesCommand {

   @Override
   public boolean execute(KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      if(!plugin.hasPermissionNode(sender, PermissionNode.ADMIN)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Lack permission: " + ChatColor.WHITE + PermissionNode.ADMIN.getNode());
         return true;
      }
      plugin.getRootConfig().reload();
      plugin.getLivesConfig().reload();
      plugin.getInventoryConfig().reload();
      sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.GREEN + " Configuration reloaded.");
      return true;
   }

}
