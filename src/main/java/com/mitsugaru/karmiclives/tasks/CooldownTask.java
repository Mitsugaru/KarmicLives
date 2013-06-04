package com.mitsugaru.karmiclives.tasks;

import com.mitsugaru.karmiclives.KarmicLives;

/**
 * Cooldown task counter.
 */
public class CooldownTask implements Runnable {
   /**
    * Pugin reference.
    */
   private KarmicLives plugin;
   /**
    * Player name.
    */
   private String name;

   /**
    * Constructor.
    * 
    * @param plugin
    *           - KarmicLives plugin.
    * @param name
    *           - Player name.
    */
   public CooldownTask(KarmicLives plugin, String name) {
      this.plugin = plugin;
      this.name = name;
   }

   @Override
   public void run() {
      // Remove player from known cooldowns.
      plugin.getCooldowns().remove(name);
   }

}
