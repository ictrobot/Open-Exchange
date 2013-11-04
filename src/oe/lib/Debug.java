package oe.lib;

import java.io.PrintStream;
import oe.OpenExchange;

public class Debug {
  
  public static boolean debug = OpenExchange.debug;
  
  public static void handleException(Exception e) {
    try {
      String str = "";
      PrintStream ps = new PrintStream(str);
      e.printStackTrace(ps);
    } catch (Exception ex) {
      
    }
  }
}
