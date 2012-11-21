package com.mitsugaru.karmiclives.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class RootConfig extends ModularConfig {

   public RootConfig(JavaPlugin plugin) {
      super(plugin);
      final ConfigurationSection config = plugin.getConfig();
      loadDefaults(config);
      plugin.saveConfig();
      reload();
   }

   @Override
   public void save() {
      plugin.reloadConfig();
      plugin.saveConfig();
   }

   @Override
   public void set(String path, Object o) {
      final ConfigurationSection config = plugin.getConfig();
      config.set(path, o);
      plugin.saveConfig();
   }

   @Override
   public void reload() {
      plugin.reloadConfig();
      loadSettings(plugin.getConfig());
      boundsCheck();
   }

   @Override
   public void loadSettings(ConfigurationSection config) {
      for(final RootConfigNode node : RootConfigNode.values()) {
         updateOption(node);
      }
   }

   @Override
   public void loadDefaults(ConfigurationSection config) {
      for(final RootConfigNode node : RootConfigNode.values()) {
         if(!config.contains(node.getPath())) {
            config.set(node.getPath(), node.getDefaultValue());
         }
      }
   }

   @Override
   public void boundsCheck() {
   }

}
