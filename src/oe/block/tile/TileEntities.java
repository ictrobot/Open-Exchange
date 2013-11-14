package oe.block.tile;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {
  public static void Register() {
    GameRegistry.registerTileEntity(TileCondenser.class, "OECondenser");
    GameRegistry.registerTileEntity(TileCharging.class, "OECharging");
    GameRegistry.registerTileEntity(TileExtractor.class, "OEExtractor");
    GameRegistry.registerTileEntity(TileStorage.class, "OEStorage");
    GameRegistry.registerTileEntity(TilePipe.class, "OEPipe");
    GameRegistry.registerTileEntity(TileDrill.class, "OEDrill");
    GameRegistry.registerTileEntity(TileExperienceConsumer.class, "OEExperienceConsumer");
  }
}
