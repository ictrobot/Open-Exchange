package oe.block;

import oe.helper.ConfigHelper;

public class BlockIDs {
  
  public static int condenserID;
  
  public static void Load() {
    ConfigHelper.load();
    
    condenserID = ConfigHelper.block("condenser");
    
    ConfigHelper.save();
  }
}
