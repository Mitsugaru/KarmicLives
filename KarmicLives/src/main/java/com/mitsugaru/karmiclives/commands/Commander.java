package com.mitsugaru.karmiclives.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class Commander implements CommandExecutor {

   private final Map<String, ILivesCommand> registeredCommands = new HashMap<String, ILivesCommand>();

   private KarmicLives plugin;
   
   public Commander(KarmicLives plugin) {
      this.plugin = plugin;
      // TODO Register commands
      registeredCommands.put("help", new HelpCommand());
   }

   @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if(args.length == 0) {
         //Show player's lives
         final int lives = plugin.getLivesConfig().getLives(sender.getName());
         sender.sendMessage(plugin.getTag() + " Lives: " + lives);
      } else {
         final String cmd = args[0].toLowerCase();
         final ILivesCommand subCommand = registeredCommands.get(cmd);
         if(subCommand == null) {
            //TODO notify player of bad command
            return true;
         }
         //Execute command
         subCommand.execute(plugin, sender, command, label, args);
      }
      return true;
   }

}
