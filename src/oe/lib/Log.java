package oe.lib;

import java.util.logging.Level;
import java.util.logging.Logger;
import oe.OpenExchange;

public class Log {
  
  static Logger log = Logger.getLogger(Reference.MOD_ID);
  
  public static void output(Level lvl, Object o) {
    log.log(lvl, o.toString());
  }
  
  public static void severe(Object o) {
    output(Level.SEVERE, o);
  }
  
  public static void info(Object o) {
    output(Level.INFO, o);
  }
  
  public static void warning(Object o) {
    output(Level.WARNING, o);
  }
  
  public static void debug(Object o) {
    if (OpenExchange.debug) {
      output(Level.INFO, "[DEBUG]: " + o.toString());
    }
  }
}
