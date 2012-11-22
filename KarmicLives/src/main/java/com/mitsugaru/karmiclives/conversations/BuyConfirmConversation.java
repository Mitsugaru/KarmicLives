package com.mitsugaru.karmiclives.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.mitsugaru.karmiclives.KarmicLives;

public class BuyConfirmConversation extends ModifyLivesConversation {
   /**
    * Cost to withdraw from the source player.
    */
   private double cost = 0;
   /**
    * Source player.
    */
   private String source = "";

   /**
    * Constructor.
    * 
    * @param plugin
    *           - Plugin.
    */
   public BuyConfirmConversation(KarmicLives plugin) {
      super(plugin);
   }

   @Override
   public String getPromptText(ConversationContext context) {
      amount = (Integer) context.getSessionData("amount");
      previous = (Integer) context.getSessionData("current");
      name = (String) context.getSessionData("name");
      source = (String) context.getSessionData("source");
      cost = (Double) context.getSessionData("cost");
      return ChatColor.WHITE + " Confirm buying " + ChatColor.AQUA + amount + ChatColor.WHITE + " for amount "
            + ChatColor.GOLD + cost + ChatColor.WHITE + "?";
   }

   @Override
   protected Prompt acceptValidatedInput(ConversationContext context, String in) {
      if(validAccept(in)) {
         // take money
         plugin.getEconomy().withdrawPlayer(source, cost);
      }
      return super.acceptValidatedInput(context, in);
   }
}
