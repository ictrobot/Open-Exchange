package oe.block;

import net.minecraft.block.Block;
import oe.lib.helper.ConfigHelper;
import oe.lib.helper.Register;
import oe.lib.util.RemoteDrillData;
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
    ConfigHelper.load();
    if (ConfigHelper.other("block", "condenserEnabled", true)) {
      condenser = new BlockCondenser(BlockIDs.condenser);
    }
    if (ConfigHelper.other("block", "chargingEnabled", true)) {
      charging = new BlockCharging(BlockIDs.charging);
    }
    if (ConfigHelper.other("block", "extractorEnabled", true)) {
      extractor = new BlockExtractor(BlockIDs.extractor);
    }
    if (ConfigHelper.other("block", "storageEnabled", true)) {
      storage = new BlockStorage(BlockIDs.storage);
    }
    if (ConfigHelper.other("block", "experienceConsumerEnabled", true)) {
      experienceConsumer = new BlockExperienceConsumer(BlockIDs.experienceConsumer);
    }
    if (ConfigHelper.other("block", "pipeEnabled", true)) {
      pipe = new BlockPipe(BlockIDs.pipe);
    }
    if (ConfigHelper.other("block", "drillEnabled", true)) {
      RemoteDrillData.init();
      drill = new BlockDrill(BlockIDs.drill);
    }
    if (ConfigHelper.other("block", "drillRemoteEnabled", true)) {
      drillRemote = new BlockDrillRemote(BlockIDs.drillRemote);
      drillRemoteReceiver = new BlockDrillRemoteReceiver(BlockIDs.drillRemoteReceiver);
    }
    ConfigHelper.save();
  }
  
  public static void Register() {
    ConfigHelper.load();
    if (ConfigHelper.other("block", "condenserEnabled", true)) {
      Register.Block(condenser, QMC.name + " Condenser", "pickaxe", 2);
    }
    if (ConfigHelper.other("block", "chargingEnabled", true)) {
      Register.Block(charging, QMC.name + " Charging Bench", "pickaxe", 2);
    }
    if (ConfigHelper.other("block", "extractorEnabled", true)) {
      Register.Block(extractor, QMC.name + " Extractor", "pickaxe", 2);
    }
    if (ConfigHelper.other("block", "storageEnabled", true)) {
      Register.Block(storage, QMC.name + " Storage", "pickaxe", 2);
    }
    if (ConfigHelper.other("block", "experienceConsumerEnabled", true)) {
      Register.Block(experienceConsumer, QMC.name + " Experience Consumer", "pickaxe", 2);
    }
    if (ConfigHelper.other("block", "pipeEnabled", true)) {
      Register.Block(pipe, QMC.name + " Pipe", "pickaxe", 2);
    }
    if (ConfigHelper.other("block", "drillEnabled", true)) {
      Register.Block(drill, QMC.name + " Drill", "pickaxe", 2);
    }
    if (ConfigHelper.other("block", "drillRemoteEnabled", true)) {
      Register.Block(drillRemote, QMC.name + " Remote Drill", "pickaxe", 2);
      Register.Block(drillRemoteReceiver, QMC.name + " Remote Drill Receiver", "pickaxe", 2);
    }
    ConfigHelper.save();
  }
  
  public static String Texture(String str) {
    return "OE:block" + str;
  }
}
