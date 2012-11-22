package com.mitsugaru.karmiclives.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class TradeCommand implements ILivesCommand {

   @Override
   public boolean execute(KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      //TODO grab target player in config
      //TODO bounds check on the amount of lives being sent
      //TODO send lives
      return false;
   }

}
