package oe.block.tile;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {
  public static void Register() {
    GameRegistry.registerTileEntity(TileCondenser.class, "Condenser");
  }
}
