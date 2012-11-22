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
import com.mitsugaru.karmiclives.conversations.TradeConfirmConversation;
import com.mitsugaru.karmiclives.permissions.PermissionNode;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class TradeCommand implements ILivesCommand {

   @Override
   public boolean execute(final KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      if(!(sender instanceof Player)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Must be a player to trade lives.");
         return true;
      }
      // Check permissions
      if(!plugin.hasPermissionNode(sender, PermissionNode.USE)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Lack Permission: " + ChatColor.WHITE + PermissionNode.USE.getNode());
         return true;
      } else if(!plugin.hasPermissionNode(sender, PermissionNode.TRADE)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Lack Permission: " + ChatColor.WHITE
               + PermissionNode.TRADE.getNode());
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
      // bounds check on the amount of lives being sent
      int source = plugin.getLivesConfig().getLives(sender.getName());
      if(amount > source) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.YELLOW + " Lack amount. Setting to max.");
         amount = source;
      }
      // Bounds check on amount of lives received
      int max = plugin.getRootConfig().getInt(RootConfigNode.LIVES_MAXIMUM);
      int target = plugin.getLivesConfig().getLives(name);
      if((target + amount) > max && max > 0) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.YELLOW + " Adjusting receiving amount to lives limit.");
         amount = max - target;
      }
      // Check amount after adjustments
      if(amount <= 0) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " No amount of lives to trade after adjustment. Cancelling trade.");
         return true;
      }
      // confirm send lives
      final Map<Object, Object> map = new HashMap<Object, Object>();
      map.put("amount", amount);
      map.put("current", target);
      map.put("name", name);
      map.put("source", sender.getName());
      map.put("source.current", source);
      Conversation conv = plugin.getFactory().withFirstPrompt(new TradeConfirmConversation(plugin)).withPrefix(new ConversationPrefix() {
         @Override
         public String getPrefix(ConversationContext context) {
            return ChatColor.GRAY + plugin.getTag();
         }
      }).withInitialSessionData(map).withLocalEcho(false).buildConversation((Player) sender);
      conv.begin();
      return true;
   }

}
