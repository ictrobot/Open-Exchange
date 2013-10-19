package oe.block.tile;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {
  public static void Register() {
    GameRegistry.registerTileEntity(TileCondenser.class, "OECondenser");
    GameRegistry.registerTileEntity(TileCharging.class, "OECharging");
  }
}
