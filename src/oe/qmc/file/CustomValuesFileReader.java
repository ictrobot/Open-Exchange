package oe.qmc.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import net.minecraft.item.ItemStack;
import oe.OpenExchange;
import oe.lib.Debug;
import oe.qmc.QMC;
import org.apache.commons.io.FileUtils;

public class CustomValuesFileReader {
  
  public static void read() {
    read(new File(OpenExchange.configdir + "/OpenExchange/CustomValues.cfg"), true);
  }
  
  public static void read(File file) {
    read(file, false);
  }
  
  private static void read(File file, boolean shouldCreate) {
    try {
      if (!file.exists()) {
        if (shouldCreate) {
          file.createNewFile();
          FileWriter fw = new FileWriter(file.getAbsoluteFile());
          BufferedWriter bw = new BufferedWriter(fw);
          bw.write("############################");
          bw.newLine();
          bw.write("#                          #");
          bw.newLine();
          bw.write("#    Open-Exchange         #");
          bw.newLine();
          bw.write("#    Custom Values File    #");
          bw.newLine();
          bw.write("#                          #");
          bw.newLine();
          bw.write("############################");
          bw.newLine();
          bw.newLine();
          bw.write("# To add a value");
          bw.newLine();
          bw.write("# a [ID] [Meta] [Value]");
          bw.newLine();
          bw.write("# a [OreDictionary] [Value]");
          bw.newLine();
          bw.newLine();
          bw.write("# To remove a value");
          bw.newLine();
          bw.write("# r [ID] [Meta]");
          bw.newLine();
          bw.write("# r [OreDictionary]");
          bw.newLine();
          bw.newLine();
          bw.write("# To blacklist");
          bw.newLine();
          bw.write("# b [ID] [Meta]");
          bw.newLine();
          bw.write("# b [OreDictionary]");
          bw.newLine();
          bw.newLine();
          bw.close();
        } else {
          return;
        }
      }
      for (String line : FileUtils.readLines(file)) {
        if (!line.startsWith("#")) {
          String[] result = line.split("\\s");
          String command = result[0];
          if (result.length == 2) {
            if (command.startsWith("r")) {
              removeOre(result);
            } else if (command.startsWith("b")) {
              blacklistOre(result);
            }
          } else if (result.length == 3) {
            if (command.startsWith("a")) {
              addOre(result);
            }
            if (command.startsWith("r")) {
              remove(result);
            } else if (command.startsWith("b")) {
              blacklist(result);
            }
          } else if (result.length == 4) {
            if (command.startsWith("a")) {
              add(result);
            }
          }
        }
      }
    } catch (Exception e) {
      Debug.handleException(e);
    }
  }
  
  private static void add(String[] result) {
    Double value;
    int id;
    int meta;
    try {
      value = Double.parseDouble(result[3]);
      id = Integer.parseInt(result[1]);
      meta = Integer.parseInt(result[2]);
    } catch (NumberFormatException e) {
      return;
    }
    ItemStack stack = new ItemStack(id, 1, meta);
    QMC.add(stack, value);
  }
  
  private static void addOre(String[] result) {
    Double value;
    try {
      value = Double.parseDouble(result[2]);
    } catch (NumberFormatException e) {
      return;
    }
    String ore = result[1];
    QMC.add(ore, value);
  }
  
  private static void remove(String[] result) {
    int id;
    int meta;
    try {
      id = Integer.parseInt(result[1]);
      meta = Integer.parseInt(result[2]);
    } catch (NumberFormatException e) {
      return;
    }
    ItemStack stack = new ItemStack(id, 1, meta);
    QMC.remove(stack);
  }
  
  private static void removeOre(String[] result) {
    String ore = result[1];
    QMC.remove(ore);
  }
  
  private static void blacklist(String[] result) {
    int id;
    int meta;
    try {
      id = Integer.parseInt(result[1]);
      meta = Integer.parseInt(result[2]);
    } catch (NumberFormatException e) {
      return;
    }
    ItemStack stack = new ItemStack(id, 1, meta);
    QMC.blacklist(stack);
  }
  
  private static void blacklistOre(String[] result) {
    String ore = result[1];
    QMC.blacklist(ore);
  }
}
