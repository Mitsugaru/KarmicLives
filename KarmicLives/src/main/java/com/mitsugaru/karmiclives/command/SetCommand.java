package com.mitsugaru.karmiclives.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class SetCommand implements ILivesCommand {

   @Override
   public boolean execute(KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      // TODO grab the target player
      // TODO grab target lives
      // TODO check if player exists in config
      // TODO set lives
      return true;
   }

}