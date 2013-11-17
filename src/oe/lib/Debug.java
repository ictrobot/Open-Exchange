package oe.lib;

import java.lang.reflect.Field;
import oe.OpenExchange;

public class Debug {
  
  public static boolean debug = OpenExchange.debug;
  public static boolean rawCode = Reference.VERSION_NUMBER == "@VERSION@";
  
  public static void handleException(Exception e) {
    if (debug || rawCode) {
      e.printStackTrace();
    }
  }
  
  public static void message(Object o) {
    if (debug || rawCode) {
      Log.debug(o);
    }
  }
  
  public static void printObject(Object o) {
    if (debug && rawCode) {
      prevPrint = null;
      printObject(o, "", 0);
    }
  }
  
  private static Object prevPrint = null;
  
  private static void printObject(Object o, String fieldName, int i) {
    if (prevPrint != null && prevPrint.equals(o)) {
      return;
    }
    if (i > 9) {
      return;
    }
    prevPrint = o;
    try {
      String prefix = "";
      for (int x = 0; x < i; x++) {
        prefix = prefix + " ";
      }
      String fieldType = o.getClass().getSimpleName();
      Boolean isArray = fieldType.endsWith("[]");
      String display = " " + o.toString();
      if (isArray) {
        display = "";
      }
      Log.debug(prefix + fieldName + " " + fieldType + display + ":");
      if (o.getClass().getName().startsWith("java.")) {
        return;
      }
      if (isArray) {
        Object[] a = (Object[]) o;
        if (a != null) {
          for (int x = 0; x < i; x++) {
            if (a[x] != null) {
              printObject(a[x], x + "", i + 1);
            }
          }
        }
      }
      for (Field field : o.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        Object value = null;
        try {
          value = field.get(o);
          printObject(value, field.getName(), i + 1);
        } catch (Exception e) {
          
        }
      }
    } catch (Exception e) {
      return;
    }
  }
}
