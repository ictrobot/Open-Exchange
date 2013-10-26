package oe.qmc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.FileUtils;
import oe.OpenExchange;

public class CustomValuesFileReader {
  public static void read() {
    File file = new File(OpenExchange.configdir + "/OpenExchange/CustomValues.cfg");
    try {
      if (!file.exists()) {
        file.createNewFile();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("######################");
        bw.newLine();
        bw.write("# Open-Exchange      #");
        bw.newLine();
        bw.write("# Custom Values File #");
        bw.newLine();
        bw.write("######################");
        bw.newLine();
        bw.newLine();
        bw.write("# To add a value");
        bw.newLine();
        bw.write("# a [ID] [Value]");
        bw.newLine();
        bw.write("# a [OreDictionary] [Value]");
        bw.newLine();
        bw.newLine();
        bw.write("# To remove a value");
        bw.newLine();
        bw.write("# r [ID]");
        bw.newLine();
        bw.write("# r [OreDictionary]");
        bw.newLine();
        bw.newLine();
        bw.write("# To blacklist");
        bw.newLine();
        bw.write("# b [ID]");
        bw.newLine();
        bw.write("# b [OreDictionary]");
        bw.newLine();
        bw.newLine();
        bw.close();
      }
      for (String line : FileUtils.readLines(file)) {
        if (!line.startsWith("#")) {
          String[] result = line.split("\\s");
          String command = result[0];
          if (result.length == 3) {
            if (command.startsWith("a")) {
              add(result);
            }
          } else if (result.length == 2) {
            if (command.startsWith("r")) {
              remove(result);
            } else if (command.startsWith("b")) {
              blacklist(result);
            }
          }
        }
      }
    } catch (Exception e) {
      if (OpenExchange.debug) {
        e.printStackTrace();
      }
    }
  }
  
  private static void blacklist(String[] result) {
    try {
      int d = Integer.parseInt(result[1]);
      ItemStack stack = new ItemStack(d, 1, 0);
      QMC.blacklist(stack);
    } catch (NumberFormatException e) {
      String ore = result[1];
      QMC.blacklist(ore);
    }
  }
  
  private static void add(String[] result) {
    Double value;
    try {
      value = Double.parseDouble(result[2]);
    } catch (NumberFormatException e) {
      return;
    }
    try {
      int d = Integer.parseInt(result[1]);
      ItemStack stack = new ItemStack(d, 1, 0);
      QMC.add(stack, value);
    } catch (NumberFormatException e) {
      String ore = result[1];
      QMC.add(ore, value);
    }
  }
  
  private static void remove(String[] result) {
    try {
      int d = Integer.parseInt(result[1]);
      ItemStack stack = new ItemStack(d, 1, 0);
      QMC.remove(stack);
    } catch (NumberFormatException e) {
      String ore = result[1];
      QMC.remove(ore);
    }
  }
}
