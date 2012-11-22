package com.mitsugaru.karmiclives.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.mitsugaru.karmiclives.KarmicLives;

public class TradeConfirmConversation extends ModifyLivesConversation {
   /**
    * Source current lives amount.
    */
   private int sourcePrevious = 0;
   /**
    * Source name.
    */
   private String source = "";

   /**
    * Constructor
    * 
    * @param plugin
    */
   public TradeConfirmConversation(KarmicLives plugin) {
      super(plugin);
   }

   @Override
   public String getPromptText(ConversationContext context) {
      amount = (Integer) context.getSessionData("amount");
      previous = (Integer) context.getSessionData("current");
      name = (String) context.getSessionData("name");
      source = (String) context.getSessionData("source");
      sourcePrevious = (Integer) context.getSessionData("source.current");
      String lives = " lives";
      if(amount == 1) {
         lives = " life";
      }
      return ChatColor.WHITE + " Confirm sending " + ChatColor.AQUA + amount + ChatColor.WHITE + lives + " to " + ChatColor.GOLD + name
            + ChatColor.WHITE + "?";
   }

   @Override
   protected Prompt acceptValidatedInput(ConversationContext context, String in) {
      if(validAccept(in)) {
         plugin.getLivesConfig().set(source, sourcePrevious - amount);
         plugin.getLivesConfig().save();
      }
      return super.acceptValidatedInput(context, in);
   }
}
