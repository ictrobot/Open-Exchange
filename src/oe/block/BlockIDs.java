package oe.block;

import oe.helper.ConfigHelper;

public class BlockIDs {
  
  public static int condenserID;
  public static int chargingID;
  public static int extractorID;
  public static int storageID;
  public static int transferID;
  
  public static void Load() {
    ConfigHelper.load();
    
    condenserID = ConfigHelper.block("condenser");
    chargingID = ConfigHelper.block("charging");
    extractorID = ConfigHelper.block("extractor");
    storageID = ConfigHelper.block("storage");
    transferID = ConfigHelper.block("transfer");
    
    ConfigHelper.save();
  }
}
