package com.mitsugaru.karmiclives.config.nodes;

public enum RootConfigNode implements ConfigNode {
   /**
    * Lives
    */
   LIVES_MAXIMUM("lives.max", VarType.INTEGER, 0),
   LIVES_COST("lives.cost", VarType.DOUBLE, 100.0),
   LIVES_START("lives.start", VarType.INTEGER, 3),
   LIVES_ADJUST("lives.adjust", VarType.INTEGER, 1),
   LIVES_NOTIFY("lives.notify", VarType.BOOLEAN, true),
   LIVES_BUNDLE("lives.bundle", VarType.INTEGER, 1),
   /**
    * Cooldown
    */
   COOLDOWN_USE("cooldown.use", VarType.BOOLEAN, false),
   COOLDOWN_TIME("cooldown.time", VarType.INTEGER, 30),
   COOLDOWN_PERMISSION("cooldown.permission", VarType.BOOLEAN, true),
   /**
    * Debug.
    */
   DEBUG_CONFIG("debug.config", VarType.BOOLEAN, false),
   DEBUG_ECONOMY("debug.economy", VarType.BOOLEAN, false),
   /**
    * Version.
    */
   VERSION("version", VarType.STRING, "0.1");

   /**
    * Config path.
    */
   private String path;
   /**
    * Default value.
    */
   private Object def;
   /**
    * Variable type.
    */
   private VarType vartype;

   /**
    * Private constructor
    * 
    * @param path
    *           - Config path.
    * @param vartype
    *           - Variable type.
    * @param def
    *           - Default value.
    */
   private RootConfigNode(String path, VarType vartype, Object def) {
      this.path = path;
      this.vartype = vartype;
      this.def = def;
   }

   /**
    * Get the config path.
    * 
    * @return Config path.
    */
   public String getPath() {
      return this.path;
   }

   /**
    * Get the variable type.
    * 
    * @return Variable type.
    */
   public VarType getVarType() {
      return this.vartype;
   }

   /**
    * Get the default value.
    * 
    * @return Default value.
    */
   public Object getDefaultValue() {
      return this.def;
   }

}
