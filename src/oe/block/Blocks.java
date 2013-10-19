package oe.block;

import net.minecraft.block.Block;
import oe.helper.Register;
import oe.qmc.QMC;

public class Blocks {
  
  public static Block condenser;
  public static Block charging;
  public static Block extractor;
  
  public static void Load() {
    condenser = new BlockCondenser(BlockIDs.condenserID);
    charging = new BlockCharging(BlockIDs.chargingID);
    extractor = new BlockExtractor(BlockIDs.extractorID);
  }
  
  public static void Register() {
    Register.Block(condenser, QMC.name + " Condenser", "pickaxe", 2);
    Register.Block(charging, QMC.name + " Charging Bench", "pickaxe", 2);
    Register.Block(extractor, QMC.name + " Extractor", "pickaxe", 2);
  }
  
  public static String Texture(String str) {
    return "OE:block" + str;
  }
}
