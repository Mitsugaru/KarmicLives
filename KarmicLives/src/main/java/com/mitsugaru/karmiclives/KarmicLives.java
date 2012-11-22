package com.mitsugaru.karmiclives;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mitsugaru.karmiclives.command.handlers.Commander;
import com.mitsugaru.karmiclives.config.InventoryConfig;
import com.mitsugaru.karmiclives.config.LivesConfig;
import com.mitsugaru.karmiclives.config.RootConfig;
import com.mitsugaru.karmiclives.listeners.PlayerListener;
import com.mitsugaru.karmiclives.permissions.PermissionNode;
import com.mitsugaru.karmiclives.services.IPermissionHandler;

/**
 * Main class for the KarmicLives plugin.
 */
public class KarmicLives extends JavaPlugin implements IPermissionHandler {
   /**
    * Root config.
    */
   private RootConfig rootConfig;
   /**
    * Lives config.
    */
   private LivesConfig livesConfig;
   /**
    * Inventory config;
    */
   private InventoryConfig inventoryConfig;
   /**
    * Vault economy reference.
    */
   private Economy economy = null;
   /**
    * Plugin tag.
    */
   private static final String TAG = "[KL]";

   @Override
   public void onDisable() {
      // Save configuration
      rootConfig.reload();
      rootConfig.save();
      livesConfig.reload();
      livesConfig.save();
      inventoryConfig.reload();
      inventoryConfig.save();
   }

   @Override
   public void onEnable() {
      rootConfig = new RootConfig(this);
      livesConfig = new LivesConfig(this);
      inventoryConfig = new InventoryConfig(this);
      if(!setupEconomy()) {
         getLogger().warning("No economy found! Lives cannot be bought or sold.");
      }
      // Register events
      PluginManager pm = this.getServer().getPluginManager();
      pm.registerEvents(new PlayerListener(this), this);
      this.getCommand("lives").setExecutor(new Commander(this));
   }

   /**
    * Grab the root config.yml for this plugin.
    * 
    * @return Root config.
    */
   public RootConfig getRootConfig() {
      return rootConfig;
   }

   /**
    * Grab the config handling the player lives count.
    * 
    * @return Lives config.
    */
   public LivesConfig getLivesConfig() {
      return livesConfig;
   }
   
   public InventoryConfig getInventoryConfig() {
      return inventoryConfig;
   }

   /**
    * Get the Vault Economy reference.
    * 
    * @return Economy.
    */
   public Economy getEconomy() {
      return economy;
   }

   /**
    * Get the plugin tag.
    * 
    * @return Plugin tag.
    */
   public String getTag() {
      return TAG;
   }

   /**
    * Setup the economy, if available.
    * 
    * @return Economy.
    */
   private boolean setupEconomy() {
      // Check vault
      final RegisteredServiceProvider<Economy> economyProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
      if(economyProvider != null) {
         economy = economyProvider.getProvider();
         return true;
      }
      return false;
   }

   /**
    * Attempts to look up full name based on who's on the server Given a partial
    * name
    * 
    * @param name
    *           - Name to search for
    * @author Frigid, edited by Raphfrk and petteyg359
    * @return Matched name. Returns null if no match.
    */
   public String expandName(String name) {
      int m = 0;
      String Result = "";
      for(int n = 0; n < this.getServer().getOnlinePlayers().length; n++) {
         String str = this.getServer().getOnlinePlayers()[n].getName();
         if(str.matches("(?i).*" + name + ".*")) {
            m++;
            Result = str;
            if(m == 2) {
               return null;
            }
         }
         if(str.equalsIgnoreCase(name))
            return str;
      }
      if(m == 1)
         return Result;
      if(m > 1) {
         return null;
      }
      return name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean hasPermissionNode(CommandSender sender, PermissionNode node) {
      if(sender == null) {
         return false;
      }
      return sender.hasPermission(node.getNode());
   }
}
