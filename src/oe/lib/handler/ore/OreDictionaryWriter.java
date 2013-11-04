package oe.lib.handler.ore;

import java.io.File;
import java.io.PrintWriter;
import net.minecraft.item.ItemStack;
import oe.OpenExchange;
import oe.lib.Debug;
import oe.qmc.QMC;

public class OreDictionaryWriter {
  
  public static void write() {
    write(new File(OpenExchange.configdir + "/../" + "OreDictionary.csv"));
  }
  
  private static void write(File file) {
    PrintWriter writer;
    try {
      writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
      writer.println("OreDictionary, UnlocalizedName, ID, Meta, QMC");
      for (OreData od : OreDictionaryHelper.oreDataArray()) {
        for (ItemStack stack : od.itemstacks) {
          try {
            String str = od.ore + ", " + stack.getUnlocalizedName() + ", " + stack.itemID + ", " + stack.getItemDamage() + ", " + QMC.getQMC(od.ore);
            writer.println(str);
          } catch (Exception e) {
            Debug.handleException(e);
          }
        }
      }
      writer.close();
    } catch (Exception e) {
      Debug.handleException(e);
    }
  }
}
