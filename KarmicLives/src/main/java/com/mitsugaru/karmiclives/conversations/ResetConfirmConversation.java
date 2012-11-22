package com.mitsugaru.karmiclives.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.mitsugaru.karmiclives.KarmicLives;

public class ResetConfirmConversation extends ModifyLivesConversation {

   public ResetConfirmConversation(KarmicLives plugin) {
      super(plugin);
   }

   @Override
   public String getPromptText(ConversationContext context) {
      amount = (Integer) context.getSessionData("amount");
      name = (String) context.getSessionData("name");
      return ChatColor.WHITE + " Reset lives and clear stored inventory for " + ChatColor.GOLD + name + ChatColor.WHITE + "?";
   }

   @Override
   protected Prompt acceptValidatedInput(ConversationContext context, String in) {
      if(validAccept(in)) {
         plugin.getInventoryConfig().clearPlayerStorage(name);
         plugin.getInventoryConfig().save();
      }
      return super.acceptValidatedInput(context, in);
   }
}
