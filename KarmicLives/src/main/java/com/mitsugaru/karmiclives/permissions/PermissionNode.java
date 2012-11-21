package com.mitsugaru.karmiclives.permissions;

public enum PermissionNode {

   ALL("KarmicLives.all"),
   BUY("KarmicLives.buy"),
   SELL("KarmicLives"),
   TRADE("KarmicLives.trade"),
   FREE("KarmicLives.free"),
   USE("KarmicLives.use"),
   ADMIN("KarmicLives.admin");

   private String node;

   private PermissionNode(String node) {
      this.node = node;
   }

   public String getNode() {
      return node;
   }

}
