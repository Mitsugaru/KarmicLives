package com.mitsugaru.karmiclives.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

import com.mitsugaru.karmiclives.KarmicLives;

/**
 * Common class for conversations that modify a target player's lives.
 */
public abstract class ModifyLivesConversation extends ValidatingPrompt {
   /**
    * Plugin.
    */
   protected KarmicLives plugin;
   /**
    * Cancel options.
    */
   protected static final String[] cancel = { "no", "n", "end", "stop", "cancel", "false" };
   /**
    * Accepting options.
    */
   protected static final String[] approve = { "yes", "y", "accept", "confirm", "true" };
   /**
    * Amount of change.
    */
   protected int amount = 0;
   /**
    * Previous amount to append change to.
    */
   protected int previous = 0;
   /**
    * Name of target player.
    */
   protected String name = "";

   /**
    * Constructor.
    * 
    * @param plugin
    *           - Root plugin.
    */
   public ModifyLivesConversation(KarmicLives plugin) {
      this.plugin = plugin;
   }

   @Override
   protected Prompt acceptValidatedInput(ConversationContext context, String in) {
      if(validAccept(in)) {
         plugin.getLivesConfig().set(name, previous + amount);
         context.getForWhom().sendRawMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.GREEN + " Success.");
         plugin.getLivesConfig().save();
         return END_OF_CONVERSATION;
      } else if(validCancel(in)) {
         context.getForWhom().sendRawMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.YELLOW + " Transaction cancelled.");
         return END_OF_CONVERSATION;
      }
      return this;
   }

   @Override
   protected boolean isInputValid(ConversationContext context, String in) {
      return (validCancel(in) || validAccept(in));
   }

   /**
    * Check if it is a valid cancel input.
    * 
    * @param in
    *           - Input to check.
    * @return True if valid cancel. Else false.
    */
   protected boolean validCancel(String in) {
      final String lower = in.toLowerCase();
      for(String c : cancel) {
         if(lower.equals(c)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Check if it is a valid accept input.
    * 
    * @param in
    *           - Input to check.
    * @return True if valid accept. Else false.
    */
   protected boolean validAccept(String in) {
      final String lower = in.toLowerCase();
      for(String a : approve) {
         if(lower.equals(a)) {
            return true;
         }
      }
      return false;
   }
}
