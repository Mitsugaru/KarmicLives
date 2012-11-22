package com.mitsugaru.karmiclives.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.config.nodes.RootConfigNode;
import com.mitsugaru.karmiclives.permissions.PermissionNode;

/**
 * Listener class for player events.
 */
public class PlayerListener implements Listener {
   /**
    * Root plugin reference.
    */
   private KarmicLives plugin;

   /**
    * Constructor.
    * 
    * @param plugin
    *           - Plugin reference.
    */
   public PlayerListener(KarmicLives plugin) {
      this.plugin = plugin;
   }

   /**
    * Listens to player joins and checks to see if the player needs to have
    * lives added.
    * 
    * @param event
    */
   @EventHandler()
   public void playerJoin(final PlayerJoinEvent event) {
      final Player player = event.getPlayer();
      if(player == null) {
         return;
      }
      if(!plugin.hasPermissionNode(player, PermissionNode.USE)) {
         return;
      }
      if(!plugin.getLivesConfig().playerExists(player.getName())) {
         // add to player with default amount
         plugin.getLivesConfig().set(player.getName(), plugin.getRootConfig().getInt(RootConfigNode.LIVES_START));
      }
   }

   /**
    * Listens for player respawn events.
    * 
    * Set to lowest to assure that the player's inventory is prepared before any
    * other plugins interfere.
    * 
    * @param event
    *           - Player respawn event.
    */
   @EventHandler(priority = EventPriority.LOWEST)
   public void playerRespawn(final PlayerRespawnEvent event) {
      // Grab player
      final Player player = event.getPlayer();
      if(player == null) {
         return;
      }
      // Check permissions
      if(!plugin.hasPermissionNode(player, PermissionNode.USE)) {
         return;
      } else if(plugin.getRootConfig().getBoolean(RootConfigNode.LIVES_NOTIFY)) {
         player.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.WHITE + " Lives remaining: "
               + plugin.getLivesConfig().getLives(player.getName()));
      }
      // Restore inventory if possible.
      plugin.getInventoryConfig().restorePlayerStorage(player);
      plugin.getInventoryConfig().clearPlayerStorage(player.getName());
   }

   /**
    * Listens for player death events.
    * 
    * Using lowest to assure that if the inventory does get modified, other
    * plugins do not interfere.
    * 
    * @param event
    *           - Player Death Event
    */
   @EventHandler(priority = EventPriority.LOWEST)
   public void playerDeath(final PlayerDeathEvent event) {
      plugin.getLogger().info("PlayerDeath event");
      // Grab player
      final Player player = event.getEntity();
      if(player == null) {
         return;
      }
      plugin.getLogger().info("player: " + player.getName());
      // Check permissions
      if(!plugin.hasPermissionNode(player, PermissionNode.USE)) {
         return;
      }
      // Check if we should save the inventory
      if(!shouldSaveOnDeath(player)) {
         return;
      }
      // Save inventory
      plugin.getInventoryConfig().savePlayerStorage(player);
      // Clear inventory
      PlayerInventory inventory = player.getInventory();
      inventory.clear();
      player.setItemOnCursor(null);
      event.getDrops().clear();
   }

   /**
    * Check if the player's inventory should be saved. Also decrements the
    * player lives count.
    * 
    * @param name
    *           - Player name.
    * @return True if we should save the inventory. Else false.
    */
   private boolean shouldSaveOnDeath(Player player) {
      int lives = plugin.getLivesConfig().getLives(player.getName());
      if(lives <= 0) {
         // They have no lives, don't save inventory
         player.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " No lives available.");
         return false;
      } else {
         // Decrement lives
         lives -= plugin.getRootConfig().getInt(RootConfigNode.LIVES_ADJUST);
         if(lives < 0) {
            // Not enough lives to pay for the amount.
            player.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Not enough lives.");
            return false;
         }
         plugin.getLivesConfig().set(player.getName(), lives);
         plugin.getLivesConfig().save();
      }
      return true;
   }
}
