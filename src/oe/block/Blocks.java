package oe.block;

import net.minecraft.block.Block;
import oe.core.data.RemoteDrillData;
import oe.core.util.ConfigUtil;
import oe.core.util.RegisterUtil;

public class Blocks {
  
  public static Block condenser;
  public static Block charging;
  public static Block extractor;
  public static Block storage;
  public static Block experienceConsumer;
  public static Block pipe;
  public static Block drill;
  public static Block drillRemote;
  public static Block drillRemoteReceiver;
  
  public static void Load() {
    ConfigUtil.load();
    if (ConfigUtil.other("block", "condenserEnabled", true)) {
      condenser = new BlockCondenser(BlockIDs.condenser);
    }
    if (ConfigUtil.other("block", "chargingEnabled", true)) {
      charging = new BlockCharging(BlockIDs.charging);
    }
    if (ConfigUtil.other("block", "extractorEnabled", true)) {
      extractor = new BlockExtractor(BlockIDs.extractor);
    }
    if (ConfigUtil.other("block", "storageEnabled", true)) {
      storage = new BlockStorage(BlockIDs.storage);
    }
    if (ConfigUtil.other("block", "experienceConsumerEnabled", true)) {
      experienceConsumer = new BlockExperienceConsumer(BlockIDs.experienceConsumer);
    }
    if (ConfigUtil.other("block", "pipeEnabled", true)) {
      pipe = new BlockPipe(BlockIDs.pipe);
    }
    if (ConfigUtil.other("block", "drillEnabled", true)) {
      RemoteDrillData.init();
      drill = new BlockDrill(BlockIDs.drill);
    }
    if (ConfigUtil.other("block", "drillRemoteEnabled", true)) {
      drillRemote = new BlockDrillRemote(BlockIDs.drillRemote);
      drillRemoteReceiver = new BlockDrillRemoteReceiver(BlockIDs.drillRemoteReceiver);
    }
    ConfigUtil.save();
  }
  
  public static void Register() {
    ConfigUtil.load();
    if (ConfigUtil.other("block", "condenserEnabled", true)) {
      RegisterUtil.Block(condenser, "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "chargingEnabled", true)) {
      RegisterUtil.Block(charging, "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "extractorEnabled", true)) {
      RegisterUtil.Block(extractor, "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "storageEnabled", true)) {
      RegisterUtil.Block(storage, "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "experienceConsumerEnabled", true)) {
      RegisterUtil.Block(experienceConsumer, "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "pipeEnabled", true)) {
      RegisterUtil.Block(pipe, "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "drillEnabled", true)) {
      RegisterUtil.Block(drill, "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "drillRemoteEnabled", true)) {
      RegisterUtil.Block(drillRemote, "pickaxe", 2);
      RegisterUtil.Block(drillRemoteReceiver, "pickaxe", 2);
    }
    ConfigUtil.save();
  }
  
  public static String Texture(String str) {
    return "OE:block" + str;
  }
}
