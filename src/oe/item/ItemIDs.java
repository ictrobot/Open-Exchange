package oe.item;

import oe.lib.helper.ConfigHelper;

public class ItemIDs {
  
  public static int buildRingID;
  public static int readerID;
  
  public static void Load() {
    ConfigHelper.load();
    
    buildRingID = ConfigHelper.item("buildRing");
    readerID = ConfigHelper.item("reader");
    
    ConfigHelper.save();
  }
}
