package oe.api;

import java.lang.reflect.Method;

/**
 * Notice:
 * All the classes in this package are not needed.
 * They are just examples for interfacing with OE
 */
public class OE {
  
  /**
   * Checks if a class extends OEInterface (Checks by looking for a method called "isOE")
   * 
   * @param c
   * @return
   */
  public static boolean isOE(Class<?> c) {
    Class<?>[] classes = c.getInterfaces();
    for (Class<?> i : classes) {
      if (i.getName().contains("OEItemInterface") || i.getName().contains("OETileInterface")) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Checks if a class extends OEGuessable (Checks by looking for a method called "isOEGuessable")
   * 
   * @param c
   * @return
   */
  public static boolean isOEGuessable(Class<?> c) {
    Class<?> e = c.getSuperclass();
    if (e == null) {
      return false;
    }
    return e.getName().contains("OEGuesser");
  }
  
  public static boolean addToolBlacklist(net.minecraft.block.Block block) {
    try {
      Method m = getMethod("add", "oe.lib.misc.QuantumToolBlackList", new Class<?>[] { net.minecraft.block.Block.class });
      m.invoke(null, block);
    } catch (Exception e) {
      return false;
    }
    return true;
  }
  
  public static boolean addQMC(Object o, double value) {
    try {
      Method m = getMethod("add", "oe.qmc.QMC", new Class<?>[] { Object.class, double.class });
      m.invoke(null, o, value);
    } catch (Exception e) {
      return false;
    }
    return true;
  }
  
  public static double getQMC(Object o) {
    try {
      Method m = getMethod("getQMC", "oe.qmc.QMC", new Class<?>[] { Object.class });
      Object r = m.invoke(null, o);
      if (r instanceof Double) {
        return (Double) r;
      }
    } catch (Exception e) {
      
    }
    return -1;
  }
  
  public static boolean hasQMC(Object o) {
    return getQMC(o) > 0;
  }
  
  public static boolean blacklistQMC(Object o) {
    try {
      Method m = getMethod("blacklist", "oe.qmc.QMC", new Class<?>[] { Object.class });
      m.invoke(null, o);
      return true;
    } catch (Exception e) {
      
    }
    return false;
  }
  
  private static Method getMethod(String name, String path, Class<?>[] parms) {
    try {
      Class<?> c = getClass(path);
      return c.getMethod(name, parms);
    } catch (Exception e) {
      
    }
    return null;
  }
  
  private static Class<?> getClass(String str) {
    try {
      return Class.forName(str);
    } catch (Exception e) {
      
    }
    return null;
  }
}
