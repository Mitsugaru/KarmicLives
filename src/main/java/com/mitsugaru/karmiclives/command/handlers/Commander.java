package com.mitsugaru.karmiclives.command.handlers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.command.BuyCommand;
import com.mitsugaru.karmiclives.command.HelpCommand;
import com.mitsugaru.karmiclives.command.SellCommand;
import com.mitsugaru.karmiclives.command.TradeCommand;
import com.mitsugaru.karmiclives.command.VersionCommand;
import com.mitsugaru.karmiclives.command.ViewCommand;
import com.mitsugaru.karmiclives.services.CommandHandler;

/**
 * Handles the root command, lives.
 */
public class Commander extends CommandHandler {

   private final ViewCommand view = new ViewCommand();

   public Commander(KarmicLives plugin) {
      super(plugin, "lives");
      final HelpCommand help = new HelpCommand();
      registerCommand("help", help);
      registerCommand("?", help);
      final VersionCommand version = new VersionCommand();
      registerCommand("version", version);
      registerCommand("about", version);
      final TradeCommand trade = new TradeCommand();
      registerCommand("trade", trade);
      registerCommand("send", trade);
      registerCommand("give", trade);
      final BuyCommand buy = new BuyCommand();
      registerCommand("buy", buy);
      registerCommand("get", buy);
      final SellCommand sell = new SellCommand();
      registerCommand("sell", sell);
      registerHandler(new AdminCommander(plugin));
   }

   @Override
   public boolean noArgs(CommandSender sender, Command command, String label) {
      final int lives = plugin.getLivesConfig().getLives(sender.getName());
      ChatColor status = ChatColor.GOLD;
      if(plugin.getCooldowns().containsKey(sender.getName())) {
         status = ChatColor.DARK_RED;
      }
      sender.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.WHITE + " Lives: " + status + lives);
      return true;
   }

   @Override
   public boolean unknownCommand(CommandSender sender, Command command, String label, String[] args) {
      if(!view.execute(plugin, sender, command, label, args)) {
         // Bad command
      }
      return true;
   }

}
