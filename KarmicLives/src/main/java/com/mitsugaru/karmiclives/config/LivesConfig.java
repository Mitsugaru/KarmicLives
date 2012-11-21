package com.mitsugaru.karmiclives.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LivesConfig extends ModularConfig {

   private File file;
   private YamlConfiguration config;

   public LivesConfig(JavaPlugin plugin) {
      super(plugin);
      file = new File(plugin.getDataFolder().getAbsolutePath() + "/lives.yml");
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
   
   public int getLives(String name) {
      return config.getInt(name, 0);
   }

   @Override
   public void reload() {
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
   }

   @Override
   public void loadDefaults(ConfigurationSection config) {
   }

   @Override
   public void boundsCheck() {
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
