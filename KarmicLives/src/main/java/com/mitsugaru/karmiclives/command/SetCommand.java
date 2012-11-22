package com.mitsugaru.karmiclives.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.config.nodes.RootConfigNode;
import com.mitsugaru.karmiclives.conversations.SetConfirmConversation;
import com.mitsugaru.karmiclives.permissions.PermissionNode;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class SetCommand implements ILivesCommand {

   @Override
   public boolean execute(final KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      // Check permissions
      if(!plugin.hasPermissionNode(sender, PermissionNode.ADMIN)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Lack Permission: " + ChatColor.WHITE
               + PermissionNode.ADMIN.getNode());
         return true;
      }
      // grab the target player
      String name = plugin.expandName(args[0]);
      if(name == null) {
         // Just use as is as they might be an offline player
         name = args[0];
      }
      // check if player exists in config
      if(!plugin.getLivesConfig().playerExists(name)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Could not find player with name: " + name);
         return false;
      }
      // Get amount to set to
      int amount = 0;
      try {
         amount = Integer.parseInt(args[1]);
      } catch(NumberFormatException e) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Invalid number: " + ChatColor.WHITE + args[0]);
         return true;
      }
      final int max = plugin.getRootConfig().getInt(RootConfigNode.LIVES_MAXIMUM);
      if(amount > max && max > 0) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.YELLOW + " Adjusting to max limit.");
         amount = max;
      }
      // confirm set lives
      if(sender instanceof Player) {
         final Map<Object, Object> map = new HashMap<Object, Object>();
         map.put("amount", amount);
         map.put("name", sender.getName());
         Conversation conv = plugin.getFactory().withFirstPrompt(new SetConfirmConversation(plugin)).withPrefix(new ConversationPrefix() {
            @Override
            public String getPrefix(ConversationContext context) {
               return ChatColor.GRAY + plugin.getTag();
            }
         }).withInitialSessionData(map).withLocalEcho(false).buildConversation((Player) sender);
         conv.begin();
      } else {
         plugin.getLivesConfig().set(name, amount);
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.WHITE + " Set " + ChatColor.GOLD + name + ChatColor.WHITE + "'s lives to "
               + ChatColor.AQUA + amount);
      }
      return true;
   }

}
