package oe.block;

import oe.lib.helper.ConfigHelper;

public class BlockIDs {
  
  public static int condenser;
  public static int charging;
  public static int extractor;
  public static int storage;
  public static int experienceConsumer;
  public static int pipe;
  public static int drill;
  public static int drillRemote;
  public static int drillRemoteReceiver;
  
  public static void Load() {
    ConfigHelper.load();
    
    condenser = ConfigHelper.block("condenser");
    charging = ConfigHelper.block("charging");
    extractor = ConfigHelper.block("extractor");
    storage = ConfigHelper.block("storage");
    experienceConsumer = ConfigHelper.block("experienceConsumer");
    pipe = ConfigHelper.block("pipe");
    drill = ConfigHelper.block("drill");
    drillRemote = ConfigHelper.block("drillRemote");
    drillRemoteReceiver = ConfigHelper.block("drillRemoteReceiver");
    
    ConfigHelper.save();
  }
}
