package com.mitsugaru.karmiclives.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.services.ILivesCommand;

public class BuyCommand implements ILivesCommand {

   @Override
   public boolean execute(KarmicLives plugin, CommandSender sender, Command command, String label, String[] args) {
      //TODO determine amount to buy
      //TODO if no extra argument of amount is given, assume 1.
      //TODO buy lives from player for amount
      return true;
   }

}
