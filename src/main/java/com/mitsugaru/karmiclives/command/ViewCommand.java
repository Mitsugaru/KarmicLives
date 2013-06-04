package com.mitsugaru.karmiclives.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.permissions.PermissionNode;
import com.mitsugaru.karmiclives.services.ILivesCommand;

/**
 * View command executor.
 */
public class ViewCommand implements ILivesCommand {

   @Override
   public boolean execute(KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      String name = plugin.expandName(args[0]);
      if(name == null) {
         // Just use as is, as they might be an offline player
         name = args[0];
      }
      if(!plugin.hasPermissionNode(sender, PermissionNode.VIEW) && !sender.getName().equals(name)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Lack permission: " + PermissionNode.VIEW.getNode());
         return true;
      }
      if(!plugin.getLivesConfig().playerExists(name)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Could not find player with name: " + name);
         return false;
      }
      final int lives = plugin.getLivesConfig().getLives(name);
      ChatColor status = ChatColor.GOLD;
      if(plugin.getCooldowns().containsKey(name)) {
         status = ChatColor.DARK_RED;
      }
      sender.sendMessage(ChatColor.GRAY + plugin.getTag() + " " + ChatColor.AQUA + name + ChatColor.WHITE + "'s lives: " + status + lives);
      return true;
   }

}
