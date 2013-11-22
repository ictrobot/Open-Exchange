package oe.lib.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Util {
  
  public static boolean isServer() {
    return getSide().isServer();
  }
  
  public static boolean isClient() {
    return getSide().isClient();
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
}
