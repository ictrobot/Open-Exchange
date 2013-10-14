package oe.block;

import net.minecraft.block.Block;
import oe.helper.Register;

public class Blocks {
  
  public static Block condenser;
  
  public static void Load() {
    condenser = new BlockCondenser(BlockIDs.condenserID);
  }
  
  public static void Register() {
    Register.Block(condenser, "Condenser", "pickaxe", 1);
  }
  
  public static String Texture(String str) {
    return "OE:block" + str;
  }
}
