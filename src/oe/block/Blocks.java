package oe.block;

import net.minecraft.block.Block;
import oe.helper.Register;
import oe.qmc.QMC;

public class Blocks {
  
  public static Block condenser;
  public static Block charging;
  public static Block extractor;
  public static Block storage;
  public static Block transfer;
  public static Block experienceConsumer;
  
  public static void Load() {
    condenser = new BlockCondenser(BlockIDs.condenserID);
    charging = new BlockCharging(BlockIDs.chargingID);
    extractor = new BlockExtractor(BlockIDs.extractorID);
    storage = new BlockStorage(BlockIDs.storageID);
    transfer = new BlockTransfer(BlockIDs.transferID, 3);
    experienceConsumer = new BlockExperienceConsumer(BlockIDs.experienceConsumerID);
  }
  
  public static void Register() {
    Register.Block(condenser, QMC.name + " Condenser", "pickaxe", 2);
    Register.Block(charging, QMC.name + " Charging Bench", "pickaxe", 2);
    Register.Block(extractor, QMC.name + " Extractor", "pickaxe", 2);
    Register.Block(storage, QMC.name + " Storage", "pickaxe", 2);
    Register.Block(transfer, QMC.name + " Transfer", "pickaxe", 2);
    Register.Block(experienceConsumer, QMC.name + " Experienced Consumer", "pickaxe", 2);
  }
  
  public static String Texture(String str) {
    return "OE:block" + str;
  }
}
