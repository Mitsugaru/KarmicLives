package com.mitsugaru.karmiclives.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;

import com.mitsugaru.karmiclives.KarmicLives;

public class SetConfirmConversation extends ModifyLivesConversation {

   public SetConfirmConversation(KarmicLives plugin) {
      super(plugin);
   }

   @Override
   public String getPromptText(ConversationContext context) {
      amount = (Integer) context.getSessionData("amount");
      name = (String) context.getSessionData("name");
      return ChatColor.WHITE + " Set player " + ChatColor.GOLD + name + ChatColor.WHITE + "'s lives to " + ChatColor.AQUA + amount + ChatColor.WHITE
            + "?";
   }

}
