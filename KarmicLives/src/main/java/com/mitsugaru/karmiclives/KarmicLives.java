package com.mitsugaru.karmiclives;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mitsugaru.karmiclives.config.LivesConfig;
import com.mitsugaru.karmiclives.config.RootConfig;
import com.mitsugaru.karmiclives.permissions.PermissionNode;
import com.mitsugaru.karmiclives.services.IPermissionHandler;

public class KarmicLives extends JavaPlugin implements IPermissionHandler {

   private RootConfig rootConfig;
   private LivesConfig livesConfig;
   private Economy economy = null;
   private final String tag = "[KL]";

   @Override
   public void onDisable() {
      super.onDisable();
   }

   @Override
   public void onEnable() {
      rootConfig = new RootConfig(this);
      livesConfig = new LivesConfig(this);
      if(!setupEconomy()) {
         getLogger().warning("No economy found! Lives cannot be bought or sold.");
      }
   }

   public RootConfig getRootConfig() {
      return rootConfig;
   }

   public LivesConfig getLivesConfig() {
      return livesConfig;
   }

   public Economy getEconomy() {
      return economy;
   }

   public String getTag() {
      return tag;
   }

   private boolean setupEconomy() {
      // Check vault
      final RegisteredServiceProvider<Economy> economyProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
      if(economyProvider != null) {
         economy = economyProvider.getProvider();
         return true;
      }
      return false;
   }

   @Override
   public boolean has(CommandSender sender, PermissionNode node) {
      return sender.hasPermission(node.getNode());
   }
}
