package com.mitsugaru.karmiclives.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.config.nodes.RootConfigNode;

public class PlayerListener implements Listener {

   private KarmicLives plugin;

   private final Map<String, HashMap<Integer, ItemStack>> items = new HashMap<String, HashMap<Integer, ItemStack>>();

   private final Map<String, List<ItemStack>> armor = new HashMap<String, List<ItemStack>>();

   public PlayerListener(KarmicLives plugin) {
      this.plugin = plugin;
   }

   @EventHandler()
   public void playerJoin(final PlayerJoinEvent event) {
      final Player player = event.getPlayer();
      if(!plugin.getLivesConfig().playerExists(player.getName())) {
         // TODO add to player with default amount
         plugin.getLivesConfig().set(player.getName(), plugin.getRootConfig().getInt(RootConfigNode.LIVES_START));
      }
   }

   @EventHandler()
   public void playerDeath(final PlayerDeathEvent event) {
      // Grab player
      final Player player = event.getEntity();
      if(player == null) {
         return;
      }
      // Check if we should save the inventory
      if(!shouldSaveOnDeath(player.getName())) {
         return;
      }
      // Grab inventory
      PlayerInventory inventory = player.getInventory();
      // TODO move this to the inventory config.
      ListIterator<ItemStack> iterator = inventory.iterator();
      plugin.getLogger().info(player.getName() + " died: ");
      while(iterator.hasNext()) {
         int index = iterator.nextIndex();
         ItemStack item = iterator.next();
         if(item != null) {
            plugin.getLogger().info(index + " - " + item.toString());
            final Map<String, Object> seralized = item.serialize();
         }
      }
      final ItemStack[] armor = inventory.getArmorContents();
      for(ItemStack item : armor) {
         if(item != null) {
            plugin.getLogger().info("* " + item.toString());
         }
      }
   }

   private boolean shouldSaveOnDeath(String name) {
      int lives = plugin.getLivesConfig().getLives(name);
      if(lives <= 0) {
         // They have no lives, don't save inventory
         // TODO send message
         return false;
      } else {
         // Decrement lives
         lives -= plugin.getRootConfig().getInt(RootConfigNode.LIVES_ADJUST);
         if(lives < 0) {
            // Not enough lives to pay for the amount.
            // TODO send message
            return false;
         }
         plugin.getLivesConfig().set(name, lives);
         plugin.getLivesConfig().save();
      }
      return true;
   }
}
