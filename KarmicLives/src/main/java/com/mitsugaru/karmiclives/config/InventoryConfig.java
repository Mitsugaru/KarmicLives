package com.mitsugaru.karmiclives.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.services.ConfigNode;
import com.mitsugaru.karmiclives.services.ModularConfig;

/**
 * Configuration class for the inventory storage.
 */
public class InventoryConfig extends ModularConfig<KarmicLives> {
   /**
    * File.
    */
   private File file;
   /**
    * Configuration.
    */
   private YamlConfiguration config;

   /**
    * Constructor.
    * 
    * @param plugin
    *           - Plugin.
    */
   public InventoryConfig(KarmicLives plugin) {
      super(plugin);
      file = new File(plugin.getDataFolder().getAbsolutePath() + "/savedinventories.yml");
      config = YamlConfiguration.loadConfiguration(file);
      reload();
      save();
   }

   @Override
   public void save() {
      try {
         config.save(file);
      } catch(IOException e) {
         plugin.getLogger().severe("File I/O Exception on saving player lives");
         e.printStackTrace();
      }
   }

   @Override
   public void set(String path, Object o) {
      config.set(path, o);
   }

   /**
    * Clears the section for the given player.
    * 
    * @param name
    *           - Player name.
    */
   public void clearPlayerStorage(String name) {
      config.set(name, null);
      save();
   }

   /**
    * Has player section.
    * 
    * @param name
    *           - Player name to check.
    * @return True if there is a storage section.
    */
   public boolean hasPlayer(String name) {
      return config.contains(name);
   }

   /**
    * Save the player's inventory to storage.
    * 
    * @param player
    *           - Player to grab info from.
    */
   public void savePlayerStorage(Player player) {
      if(hasPlayer(player.getName())) {
         // Clear previous entry
         clearPlayerStorage(player.getName());
      }
      // save inventory
      final PlayerInventory inventory = player.getInventory();
      ListIterator<ItemStack> iterator = inventory.iterator();
      while(iterator.hasNext()) {
         int index = iterator.nextIndex();
         ItemStack item = iterator.next();
         if(item != null && !item.getType().equals(Material.AIR)) {
            set(player.getName() + ".main." + index, item);
         }
      }
      // Save armor
      final ItemStack boots = player.getInventory().getBoots();
      if(boots != null && !boots.getType().equals(Material.AIR)) {
         set(player.getName() + ".boots", boots);
      }
      final ItemStack chestplate = player.getInventory().getChestplate();
      if(chestplate != null && !chestplate.getType().equals(Material.AIR)) {
         set(player.getName() + ".chest", chestplate);
      }
      final ItemStack leggings = player.getInventory().getLeggings();
      if(leggings != null && !leggings.getType().equals(Material.AIR)) {
         set(player.getName() + ".leggings", leggings);
      }
      final ItemStack helmet = player.getInventory().getHelmet();
      if(helmet != null && !helmet.getType().equals(Material.AIR)) {
         set(player.getName() + ".helmet", helmet);
      }
      // Save cursor item
      final ItemStack cursor = player.getItemOnCursor();
      if(cursor != null && !cursor.getType().equals(Material.AIR)) {
         set(player.getName() + ".cursor", cursor);
      }
      save();
   }

   /**
    * Restore the player's inventory from storage.0
    * 
    * @param player
    *           - Player to restore inventory
    */
   public void restorePlayerStorage(Player player) {
      if(!hasPlayer(player.getName())) {
         // Player does not have a stored inventory, ignore.
         return;
      }
      // Restore main
      final ConfigurationSection mainSection = config.getConfigurationSection(player.getName() + ".main");
      if(mainSection != null) {
         final Set<String> mainKeys = mainSection.getKeys(false);
         boolean error = false;
         for(String mainKey : mainKeys) {
            try {
               final int index = Integer.parseInt(mainKey);
               final ItemStack item = mainSection.getItemStack(mainKey);
               if(item != null) {
                  player.getInventory().setItem(index, item);
               }
            } catch(NumberFormatException e) {
               error = true;
            }
         }
         if(error) {
            player.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.YELLOW + " Could not reteive some items. Bad configuration.");
         }
      }

      // armor
      if(config.contains(player.getName() + ".boots")) {
         final ItemStack item = config.getItemStack(player.getName() + ".boots");
         if(item != null) {
            player.getInventory().setBoots(item);
         }
      }
      if(config.contains(player.getName() + ".chest")) {
         final ItemStack item = config.getItemStack(player.getName() + ".chest");
         if(item != null) {
            player.getInventory().setChestplate(item);
         }
      }
      if(config.contains(player.getName() + ".leggings")) {
         final ItemStack item = config.getItemStack(player.getName() + ".leggings");
         if(item != null) {
            player.getInventory().setLeggings(item);
         }
      }
      if(config.contains(player.getName() + ".helmet")) {
         final ItemStack item = config.getItemStack(player.getName() + ".helmet");
         if(item != null) {
            player.getInventory().setHelmet(item);
         }
      }
      // cursor item
      if(config.contains(player.getName() + ".cursor")) {
         final ItemStack cursor = config.getItemStack(player.getName() + ".cursor");
         HashMap<Integer, ItemStack> remainder = player.getInventory().addItem(cursor);
         if(!remainder.isEmpty()) {
            // TODO drop item at player
            for(ItemStack item : remainder.values()) {
               player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
            player.sendMessage(ChatColor.GRAY + plugin.getTag() + ChatColor.YELLOW + " Some items have been dropped. No room in your inventory.");
         }
      }
   }

   @Override
   public void reload() {
      // Save so that we don't lose any changes.
      save();
      // Reload config from file.
      try {
         config.load(file);
      } catch(FileNotFoundException e) {
         e.printStackTrace();
      } catch(IOException e) {
         e.printStackTrace();
      } catch(InvalidConfigurationException e) {
         e.printStackTrace();
      }
   }

   /*
    * Invalid operations below.
    */

   @Override
   public void loadSettings(ConfigurationSection config) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void loadDefaults(ConfigurationSection config) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void boundsCheck() {
      throw new UnsupportedOperationException();
   }

   @Override
   public void updateOption(ConfigNode node) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void set(ConfigNode node, Object o) {
      throw new UnsupportedOperationException();
   }

   @Override
   public int getInt(ConfigNode node) {
      throw new UnsupportedOperationException();
   }

   @Override
   public String getString(ConfigNode node) {
      throw new UnsupportedOperationException();
   }

   @Override
   public List<String> getStringList(ConfigNode node) {
      throw new UnsupportedOperationException();
   }

   @Override
   public double getDouble(ConfigNode node) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean getBoolean(ConfigNode node) {
      throw new UnsupportedOperationException();
   }
}
