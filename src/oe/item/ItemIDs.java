package oe.item;

import oe.lib.helper.ConfigHelper;

public class ItemIDs {
  
  public static int buildRing;
  public static int reader;
  public static int pickaxe;
  public static int axe;
  public static int shovel;
  public static int transmutation;
  
  public static void Load() {
    ConfigHelper.load();
    
    buildRing = ConfigHelper.item("buildRing");
    reader = ConfigHelper.item("reader");
    pickaxe = ConfigHelper.item("pickaxe");
    axe = ConfigHelper.item("axe");
    shovel = ConfigHelper.item("shovel");
    transmutation = ConfigHelper.item("transmutation");
    
    ConfigHelper.save();
  }
}
