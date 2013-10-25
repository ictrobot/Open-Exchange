package oe.lib.helper;

import java.io.File;
import net.minecraftforge.common.Configuration;
import oe.OpenExchange;

public class ConfigHelper {
  // Starting BLock + Item IDs
  static int BlockID = 500;
  static int ItemID = 10000 - 256;
  
  static int ItemsRegistered = 0;
  static int BlocksRegistered = 0;
  
  static String Module;
  static Configuration config;
  
  public static void load() {
    File configfile = new File(OpenExchange.configdir, "OpenExchange.cfg");
    
    config = new Configuration(configfile);
    config.load();
    
    ItemsRegistered = 0;
    BlocksRegistered = 0;
  }
  
  public static void save() {
    if (config.hasChanged()) {
      config.save();
    }
  }
  
  public static int item(String Name) {
    int id = config.get("Item", Name, ItemID + ItemsRegistered).getInt();
    ItemsRegistered = ItemsRegistered + 1;
    return id;
  }
  
  public static int block(String Name) {
    int id = config.get("Block", Name, BlockID + BlocksRegistered).getInt();
    BlocksRegistered = BlocksRegistered + 1;
    return id;
  }
  
  public static String other(String Name, String normal) {
    String str = config.get("Other", Name, normal).getString();
    return str;
  }
  
  public static int other(String Name, int normal) {
    int num = config.get("Other", Name, normal).getInt();
    return num;
  }
  
  public static boolean other(String Name, boolean normal) {
    boolean num = config.get("Other", Name, normal).getBoolean(false);
    return num;
  }
  
  public static String other(String Subname, String Name, String normal) {
    String str = config.get(Subname, Name, normal).getString();
    return str;
  }
  
  public static int other(String Subname, String Name, int normal) {
    int num = config.get(Subname, Name, normal).getInt();
    return num;
  }
  
  public static double other(String Subname, String Name, double normal) {
    double num = config.get(Subname, Name, normal).getDouble(normal);
    return num;
  }
  
  public static boolean other(String Subname, String Name, boolean normal) {
    boolean num = config.get(Subname, Name, normal).getBoolean(false);
    return num;
  }
  
  public static boolean module(String Name, boolean normal) {
    boolean enabled = config.get("Module", Name, normal).getBoolean(normal);
    return enabled;
  }
}
