package oe.block;

import oe.lib.util.ConfigUtil;

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
    ConfigUtil.load();
    
    condenser = ConfigUtil.block("condenser");
    charging = ConfigUtil.block("charging");
    extractor = ConfigUtil.block("extractor");
    storage = ConfigUtil.block("storage");
    experienceConsumer = ConfigUtil.block("experienceConsumer");
    pipe = ConfigUtil.block("pipe");
    drill = ConfigUtil.block("drill");
    drillRemote = ConfigUtil.block("drillRemote");
    drillRemoteReceiver = ConfigUtil.block("drillRemoteReceiver");
    
    ConfigUtil.save();
  }
}
