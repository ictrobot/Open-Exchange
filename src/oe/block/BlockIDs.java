package oe.block;

import oe.helper.ConfigHelper;

public class BlockIDs {
  
  public static int condenserID;
  public static int chargingID;
  
  public static void Load() {
    ConfigHelper.load();
    
    condenserID = ConfigHelper.block("condenser");
    chargingID = ConfigHelper.block("charging");
    
    ConfigHelper.save();
  }
}
