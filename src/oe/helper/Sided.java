package oe.helper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Sided {
  public static boolean isServer() {
    Side side = FMLCommonHandler.instance().getEffectiveSide();
    return side.isServer();
  }
  
  public static boolean isClient() {
    Side side = FMLCommonHandler.instance().getEffectiveSide();
    return side.isClient();
  }
}
