package com.mitsugaru.karmiclives.permissions;

public enum PermissionNode {

   ALL("KarmicLives.all"),
   BUY("KarmicLives.buy"),
   SELL("KarmicLives.sell"),
   TRADE("KarmicLives.trade"),
   FREE("KarmicLives.free"),
   USE("KarmicLives.use"),
   VIEW("KarmicLives.view"),
   ADMIN("KarmicLives.admin"),
   COOLDOWN("KarmicLives.cooldown"),
   IGNORE_COST("KarmicLives.ignore.cost"),
   IGNORE_DEATH("KarmicLives.ignore.death"),
   IGNORE_COOLDOWN("KarmicLives.ignore.cooldown");

   private String node;

   private PermissionNode(String node) {
      this.node = node;
   }

   public String getNode() {
      return node;
   }

}
