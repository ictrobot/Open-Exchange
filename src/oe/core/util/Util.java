package oe.core.util;

import java.util.HashMap;
import java.util.Set;
import oe.OpenExchange;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Util {
  
  public static boolean isServerSide() {
    return getSide().isServer();
  }
  
  public static boolean isClientSide() {
    return getSide().isClient();
  }
  
  public static boolean isClient() {
    return OpenExchange.proxy.isClient();
  }
  
  public static Side getSide() {
    return FMLCommonHandler.instance().getEffectiveSide();
  }
  
  public static String getTime() {
    return new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime());
  }
  
  public static String getDate() {
    return new java.text.SimpleDateFormat("dd,MM,yyyy").format(java.util.Calendar.getInstance().getTime());
  }
  
  private static HashMap<String, Integer> priority;
  
  public static void setMaxPriority() {
    priority = new HashMap<String, Integer>();
    Set<Thread> threads = Thread.getAllStackTraces().keySet();
    for (Thread t : threads) {
      priority.put(t.getName(), t.getPriority());
      if (t.getPriority() < 6) { // Don't Change the High Importance Threads
        t.setPriority(Thread.MIN_PRIORITY);
      }
    }
    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
  }
  
  public static void restorePriority() {
    Set<Thread> threads = Thread.getAllStackTraces().keySet();
    for (Thread t : threads) {
      t.setPriority(priority.get(t.getName()));
    }
  }
}
