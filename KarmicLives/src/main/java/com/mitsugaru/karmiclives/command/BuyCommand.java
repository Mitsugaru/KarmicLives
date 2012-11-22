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
import com.mitsugaru.karmiclives.conversations.BuyConfirmConversation;
import com.mitsugaru.karmiclives.permissions.PermissionNode;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class BuyCommand implements ILivesCommand {

   @Override
   public boolean execute(final KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      if(!(sender instanceof Player)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Cannot buy lives as a non-player.");
         return true;
      }
      // Check permissions
      if(!plugin.hasPermissionNode(sender, PermissionNode.USE)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Lack Permission: " + ChatColor.WHITE + PermissionNode.USE.getNode());
         return true;
      } else if(!plugin.hasPermissionNode(sender, PermissionNode.BUY)) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Lack Permission: " + ChatColor.WHITE + PermissionNode.BUY.getNode());
         return true;
      }
      // determine amount to buy
      int amount = plugin.getRootConfig().getInt(RootConfigNode.LIVES_BUNDLE);
      if(args.length > 0) {
         try {
            amount = Integer.parseInt(args[0]);
         } catch(NumberFormatException e) {
            sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Invalid number: " + ChatColor.WHITE + args[0]);
            return true;
         }
      }
      final int current = plugin.getLivesConfig().getLives(sender.getName());
      final int max = plugin.getRootConfig().getInt(RootConfigNode.LIVES_MAXIMUM);
      if((current + amount) > max && max > 0) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.YELLOW + " Adjusting to max limit.");
         amount = max - current;
      }
      if(amount == 0) {
         //No lives to buy
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " No lives to buy. At max.");
         return true;
      }
      // check if player ignores cost
      if(plugin.hasPermissionNode(sender, PermissionNode.IGNORE_COST)) {
         // skip to giving player amount
         plugin.getLivesConfig().set(sender.getName(), current + amount);
         return true;
      } else if(plugin.getEconomy() == null) {
         sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.DARK_RED + " Economy not found! Check your economy plugin or Vault.");
         return true;
      }
      // determine cost
      final double cost = plugin.getRootConfig().getDouble(RootConfigNode.LIVES_COST) * amount;
      // check funds
      final double result = plugin.getEconomy().getBalance(sender.getName()) - cost;
      if(result < 0) {
         sender.sendMessage(String.format(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Lack funds. " + ChatColor.AQUA + "%d" + ChatColor.RED
               + " lives costs " + ChatColor.GOLD + "%.2f", amount, cost));
         return true;
      } else {
         // confirm buy lives from player for amount
         final Map<Object, Object> map = new HashMap<Object, Object>();
         map.put("amount", amount);
         map.put("current", current);
         map.put("name", sender.getName());
         map.put("source", sender.getName());
         map.put("cost", cost);
         Conversation conv = plugin.getFactory().withFirstPrompt(new BuyConfirmConversation(plugin)).withPrefix(new ConversationPrefix() {
            @Override
            public String getPrefix(ConversationContext context) {
               return ChatColor.GRAY + plugin.getTag();
            }
         }).withInitialSessionData(map).withLocalEcho(false).buildConversation((Player) sender);
         conv.begin();
      }
      return true;
   }
}
