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
import com.mitsugaru.karmiclives.tasks.CooldownTask;

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
      if(checkCooldown(player.getName())) {
         return;
      }
      // Check permissions
      if(!plugin.hasPermissionNode(player, PermissionNode.USE)) {
         return;
      } else if(plugin.getRootConfig().getBoolean(RootConfigNode.LIVES_NOTIFY)) {
         player.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.WHITE + " Lives remaining: " + ChatColor.GOLD
               + plugin.getLivesConfig().getLives(player.getName()));
      }
      // Set the cooldown
      setCooldown(player);
      // Restore inventory if possible.
      plugin.getInventoryConfig().restorePlayerStorage(player);
      // Clear stored inventory
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
      // Grab player
      final Player player = event.getEntity();
      if(player == null) {
         return;
      }
      // Check permissions
      if(!plugin.hasPermissionNode(player, PermissionNode.USE)) {
         return;
      }
      if(checkCooldown(player.getName())) {
         // tell player that their cooldown is still in effect
         player.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.RED + " Cooldown still in effect. Inventory not saved.");
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
      if(plugin.hasPermissionNode(player, PermissionNode.IGNORE_DEATH)) {
         return true;
      }
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

   private boolean checkCooldown(String name) {
      if(plugin.getRootConfig().getBoolean(RootConfigNode.COOLDOWN_USE)) {
         return plugin.getCooldowns().containsKey(name);
      }
      return false;
   }

   private void setCooldown(Player player) {
      if(!plugin.getRootConfig().getBoolean(RootConfigNode.COOLDOWN_USE) || plugin.hasPermissionNode(player, PermissionNode.IGNORE_COOLDOWN)) {
         return;
      } else if(plugin.getRootConfig().getBoolean(RootConfigNode.COOLDOWN_PERMISSION) && !plugin.hasPermissionNode(player, PermissionNode.COOLDOWN)) {
         return;
      }
      final CooldownTask task = new CooldownTask(plugin, player.getName());
      final int time = plugin.getRootConfig().getInt(RootConfigNode.COOLDOWN_TIME);
      final long delay = (long) time * 20;
      int id = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, delay);
      if(id == -1) {
         plugin.getLogger().severe("Could not schedule cooldown task for " + player.getName());
      } else {
         player.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.WHITE + " Cooldown in effect for " + ChatColor.GOLD + time + ChatColor.WHITE
               + " seconds.");
         plugin.getCooldowns().put(player.getName(), id);
      }
   }

}
