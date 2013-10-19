package oe.block;

import net.minecraft.block.Block;
import oe.helper.Register;

public class Blocks {
  
  public static Block condenser;
  public static Block charging;
  
  public static void Load() {
    condenser = new BlockCondenser(BlockIDs.condenserID);
    charging = new BlockCharging(BlockIDs.chargingID);
  }
  
  public static void Register() {
    Register.Block(condenser, "Condenser", "pickaxe", 2);
    Register.Block(charging, "Charging Bench", "pickaxe", 2);
  }
  
  public static String Texture(String str) {
    return "OE:block" + str;
  }
}
