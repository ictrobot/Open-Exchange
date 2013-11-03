package oe.qmc.file;

import java.io.File;
import java.io.PrintWriter;
import oe.OpenExchange;
import oe.lib.Debug;
import oe.qmc.QMC;
import oe.qmc.QMCData;
import oe.qmc.QMCType;

public class QMCValuesWriter {
  
  public static void write() {
    write(new File(OpenExchange.configdir + "/../" + QMC.name + "Values.csv"));
  }
  
  private static void write(File file) {
    PrintWriter writer;
    try {
      writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
      writer.println("Ref, Type, ID, Meta, OreDictionary, QMC");
      for (int i = 0; i < QMC.length(); i++) {
        try {
          QMCData d = QMC.getQMCData(i);
          String str = i + ", " + d.type;
          if (d.type == QMCType.Itemstack) {
            str = str + ", " + d.itemstack.itemID + ", " + d.itemstack.getItemDamage() + ", NONE";
          }
          if (d.type == QMCType.OreDictionary) {
            str = str + ", 0, 0," + d.oreDictionary;
          }
          if (d.type == QMCType.OreDictionary_Itemstack) {
            str = str + ", " + d.itemstack.itemID + ", " + d.itemstack.getItemDamage() + ", " + d.oreDictionary;
          }
          str = str + ", " + d.QMC;
          writer.println(str);
        } catch (Exception e) {
          Debug.handleException(e);
        }
      }
      writer.close();
    } catch (Exception e) {
      Debug.handleException(e);
    }
  }
}
