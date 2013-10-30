package oe.lib;

import oe.OpenExchange;

public class Debug {
  
  public static boolean debug = OpenExchange.debug;
  
  public static void handleException(Exception e) {
    Log.debug(e);
  }
}
