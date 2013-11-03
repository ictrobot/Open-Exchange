package oe.block;

import net.minecraft.block.Block;
import oe.lib.helper.ConfigHelper;
import oe.lib.helper.Register;
import oe.qmc.QMC;

public class Blocks {
  
  public static Block condenser;
  public static Block charging;
  public static Block extractor;
  public static Block storage;
  public static Block experienceConsumer;
  
  public static void Load() {
    ConfigHelper.load();
    if (ConfigHelper.other("block", "condenserEnabled", true)) {
      condenser = new BlockCondenser(BlockIDs.condenserID);
    }
    if (ConfigHelper.other("block", "chargingEnabled", true)) {
      charging = new BlockCharging(BlockIDs.chargingID);
    }
    if (ConfigHelper.other("block", "extractorEnabled", true)) {
      extractor = new BlockExtractor(BlockIDs.extractorID);
    }
    if (ConfigHelper.other("block", "storageEnabled", true)) {
      storage = new BlockStorage(BlockIDs.storageID);
    }
    if (ConfigHelper.other("block", "experienceConsumerEnabled", true)) {
      experienceConsumer = new BlockExperienceConsumer(BlockIDs.experienceConsumerID);
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
    ConfigHelper.save();
  }
  
  public static String Texture(String str) {
    return "OE:block" + str;
  }
}
