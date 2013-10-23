package oe.block.tile;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {
  public static void Register() {
    GameRegistry.registerTileEntity(TileCondenser.class, "OECondenser");
    GameRegistry.registerTileEntity(TileCharging.class, "OECharging");
    GameRegistry.registerTileEntity(TileExtractor.class, "OEExtractor");
    GameRegistry.registerTileEntity(TileStorage.class, "OEStorage");
    GameRegistry.registerTileEntity(TileTransfer.class, "OETransfer");
    GameRegistry.registerTileEntity(TileExperienceConsumer.class, "OEExperienceConsumer");
  }
}
