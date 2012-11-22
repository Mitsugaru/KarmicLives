package com.mitsugaru.karmiclives.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.mitsugaru.karmiclives.KarmicLives;

public class SellConfirmConversation extends ModifyLivesConversation {
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
   public SellConfirmConversation(KarmicLives plugin) {
      super(plugin);
      // TODO Auto-generated constructor stub
   }

   @Override
   public String getPromptText(ConversationContext context) {
      amount = (Integer) context.getSessionData("amount");
      previous = (Integer) context.getSessionData("current");
      name = (String) context.getSessionData("name");
      source = (String) context.getSessionData("source");
      cost = (Double) context.getSessionData("cost");
      return ChatColor.WHITE + " Confirm selling " + ChatColor.AQUA + amount + ChatColor.WHITE + " for amount " + ChatColor.GOLD + cost
            + ChatColor.WHITE + "?";
   }

   @Override
   protected Prompt acceptValidatedInput(ConversationContext context, String in) {
      if(validAccept(in)) {
         // Take lives
         plugin.getLivesConfig().set(name, previous - amount);
         // take money
         plugin.getEconomy().depositPlayer(source, cost);
         context.getForWhom().sendRawMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.GREEN + " Success.");
         return END_OF_CONVERSATION;
      }
      return super.acceptValidatedInput(context, in);
   }
}
