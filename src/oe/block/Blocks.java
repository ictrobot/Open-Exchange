package oe.block;

import net.minecraft.block.Block;
import oe.lib.misc.RemoteDrillData;
import oe.lib.util.ConfigUtil;
import oe.lib.util.RegisterUtil;
import oe.qmc.QMC;

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
      RegisterUtil.Block(condenser, QMC.name + " Condenser", "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "chargingEnabled", true)) {
      RegisterUtil.Block(charging, QMC.name + " Charging Bench", "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "extractorEnabled", true)) {
      RegisterUtil.Block(extractor, QMC.name + " Extractor", "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "storageEnabled", true)) {
      RegisterUtil.Block(storage, QMC.name + " Storage", "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "experienceConsumerEnabled", true)) {
      RegisterUtil.Block(experienceConsumer, QMC.name + " Experience Consumer", "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "pipeEnabled", true)) {
      RegisterUtil.Block(pipe, QMC.name + " Pipe", "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "drillEnabled", true)) {
      RegisterUtil.Block(drill, QMC.name + " Drill", "pickaxe", 2);
    }
    if (ConfigUtil.other("block", "drillRemoteEnabled", true)) {
      RegisterUtil.Block(drillRemote, QMC.name + " Remote Drill", "pickaxe", 2);
      RegisterUtil.Block(drillRemoteReceiver, QMC.name + " Remote Drill Receiver", "pickaxe", 2);
    }
    ConfigUtil.save();
  }
  
  public static String Texture(String str) {
    return "OE:block" + str;
  }
}
