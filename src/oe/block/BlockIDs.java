package oe.block;

import oe.helper.ConfigHelper;

public class BlockIDs {
  
  public static int condenserID;
  public static int chargingID;
  public static int extractorID;
  
  public static void Load() {
    ConfigHelper.load();
    
    condenserID = ConfigHelper.block("condenser");
    chargingID = ConfigHelper.block("charging");
    extractorID = ConfigHelper.block("extractor");
    
    ConfigHelper.save();
  }
}
