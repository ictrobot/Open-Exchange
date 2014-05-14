package oe.core.util;

import java.io.File;
import net.minecraftforge.common.config.Configuration;
import oe.OpenExchange;

public class ConfigUtil {
  // Starting BLock + Item IDs
  private static int BlockID = 500;
  private static int ItemID = 10000 - 256;
  
  private static int ItemsRegistered = 0;
  private static int BlocksRegistered = 0;
  private static Configuration config;
  
  private static boolean loaded = false;
  
  private static void load() {
    if (!loaded || config == null) {
      loaded = true;
      File configfile = new File(OpenExchange.configdir, "/OpenExchange/OpenExchange.cfg");
      
      config = new Configuration(configfile);
      config.load();
    }
  }
  
  private static void save() {
    if (config.hasChanged()) {
      config.save();
    }
  }
  
  public static int item(String Name) {
    load();
    int data = config.get("Item", Name, ItemID + ItemsRegistered).getInt();
    ItemsRegistered = ItemsRegistered + 1;
    save();
    return data;
  }
  
  public static int block(String Name) {
    load();
    int data = config.get("Block", Name, BlockID + BlocksRegistered).getInt();
    BlocksRegistered = BlocksRegistered + 1;
    save();
    return data;
  }
  
  public static String other(String Name, String normal) {
    load();
    String data = config.get("Other", Name, normal).getString();
    save();
    return data;
  }
  
  public static int other(String Name, int normal) {
    load();
    int data = config.get("Other", Name, normal).getInt();
    save();
    return data;
  }
  
  public static boolean other(String Name, boolean normal) {
    load();
    boolean data = config.get("Other", Name, normal).getBoolean(false);
    save();
    return data;
  }
  
  public static String other(String Subname, String Name, String normal) {
    load();
    String data = config.get(Subname, Name, normal).getString();
    save();
    return data;
  }
  
  public static int other(String Subname, String Name, int normal) {
    load();
    int data = config.get(Subname, Name, normal).getInt();
    save();
    return data;
  }
  
  public static double other(String Subname, String Name, double normal) {
    load();
    double data = config.get(Subname, Name, normal).getDouble(normal);
    save();
    return data;
  }
  
  public static boolean other(String Subname, String Name, boolean normal) {
    load();
    boolean data = config.get(Subname, Name, normal).getBoolean(false);
    save();
    return data;
  }
}
