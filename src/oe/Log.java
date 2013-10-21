package oe;

import oe.helper.ConfigHelper;

public class Log {
  
  static String prefix = "OPEN EXCHANGE: ";
  
  public Log(String str) {
    System.out.println(prefix + str);
  }
  
  public static void error(String str) {
    System.out.println(prefix + "ERROR: " + str);
  }
  
  public static void info(String str) {
    System.out.println(prefix + "INFO: " + str);
  }
  
  public static void warning(String str) {
    System.out.println(prefix + "WARNING: " + str);
  }
  
  public static void debug(String str) {
    ConfigHelper.load();
    if (OpenExchange.debug) {
      System.out.println(prefix + "DEBUG: " + str);
    }
    ConfigHelper.save();
  }
}
