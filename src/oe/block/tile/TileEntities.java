package oe.block.tile;

import oe.core.data.TileSync.OETileSync;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class TileEntities {
  public static void Register() {
    GameRegistry.registerTileEntity(TileCondenser.class, "OECondenser");
    GameRegistry.registerTileEntity(TileCharging.class, "OECharging");
    GameRegistry.registerTileEntity(TileExtractor.class, "OEExtractor");
    GameRegistry.registerTileEntity(TileStorage.class, "OEStorage");
    GameRegistry.registerTileEntity(TilePipe.class, "OEPipe");
    GameRegistry.registerTileEntity(TileDrill.class, "OEDrill");
    GameRegistry.registerTileEntity(TileDrillRemote.class, "OEDrillRemote");
    GameRegistry.registerTileEntity(TileDrillRemoteReceiver.class, "OEDrillRemoteReceiver");
    GameRegistry.registerTileEntity(TileExperienceConsumer.class, "OEExperienceConsumer");
    
    TickRegistry.registerTickHandler(new OETileSync(), Side.CLIENT);
    TickRegistry.registerTickHandler(new OETileSync(), Side.SERVER);
  }
}
