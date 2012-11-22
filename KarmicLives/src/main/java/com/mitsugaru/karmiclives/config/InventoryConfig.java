package com.mitsugaru.karmiclives.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.mitsugaru.karmiclives.KarmicLives;
import com.mitsugaru.karmiclives.config.nodes.ConfigNode;
import com.mitsugaru.karmiclives.services.ModularConfig;

public class InventoryConfig extends ModularConfig<KarmicLives> {
   private File file;
   private YamlConfiguration config;

   public InventoryConfig(KarmicLives plugin) {
      super(plugin);
      file = new File(plugin.getDataFolder().getAbsolutePath() + "/savedinventories.yml");
      config = YamlConfiguration.loadConfiguration(file);
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
   
   public void savePlayerStorage(PlayerInventory inventory) {
      
   }
   
   public void restorePlayerStorage(PlayerInventory inventory) {
      
   }

   /**
    * Get the stored main inventory for the given name.
    * 
    * @param name
    *           - Name of player.
    * @return Map of slots and item stacks for the given player.
    */
   @SuppressWarnings("unchecked")
   public Map<Integer, ItemStack> getMainInventory(String name) {
      final Map<Integer, ItemStack> inventory = new HashMap<Integer, ItemStack>();
      final ConfigurationSection invSection = config.getConfigurationSection(name + ".main");
      final Set<String> invKeys = invSection.getKeys(false);
      for(String key : invKeys) {
         List<Map<?, ?>> itemMapList = invSection.getMapList(key);
         Map<String, Object> itemMap = (Map<String, Object>) itemMapList.get(0);
         // TODO catch any exceptions and log to console
         final ItemStack item = ItemStack.deserialize(itemMap);
         inventory.put(Integer.valueOf(key), item);
      }
      return inventory;
   }

   /**
    * Get the stored armor for the given name.
    * 
    * @param name
    *           - Name of player.
    * @return Array of armor items.
    */
   @SuppressWarnings("unchecked")
   public ItemStack[] getArmorInventory(String name) {
      final List<ItemStack> armor = new ArrayList<ItemStack>();
      ConfigurationSection invSection = config.getConfigurationSection(name + ".armor");
      for(ArmorKey armorKey : ArmorKey.values()) {
         List<Map<?, ?>> itemMapList = invSection.getMapList("" + armorKey.getIndex());
         Map<String, Object> itemMap = (Map<String, Object>) itemMapList.get(0);
         final ItemStack item = ItemStack.deserialize(itemMap);
         armor.add(item);
      }
      return armor.toArray(new ItemStack[0]);
   }

   private enum ArmorKey {
      FIRST(0),
      SECOND(1),
      THIRD(2),
      FOURTH(3);

      private int index;

      private ArmorKey(int index) {
         this.index = index;
      }

      public int getIndex() {
         return index;
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
