package oe.lib.helper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Sided {
  
  public static boolean isServer() {
    return getSide().isServer();
  }
  
  public static boolean isClient() {
    return getSide().isClient();
  }
  
  public static Side getSide() {
    return FMLCommonHandler.instance().getEffectiveSide();
  }
}
