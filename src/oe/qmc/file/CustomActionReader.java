package oe.qmc.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import oe.OpenExchange;
import oe.core.Debug;
import oe.qmc.QMC;
import org.apache.commons.io.FileUtils;

public class CustomActionReader {
  
  public static List<QMCCustomAction> actions() {
    return read(new File(OpenExchange.configdir + "/OpenExchange/CustomItemStackValues.cfg"), true, false);
  }
  
  private static List<QMCCustomAction> read(File file, boolean shouldCreate, boolean read) {
    List<QMCCustomAction> data = new ArrayList<QMCCustomAction>();
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
          bw.write("#      Open-Exchange       #");
          bw.newLine();
          bw.write("#     Custom ItemStack     #");
          bw.newLine();
          bw.write("#     " + QMC.name + " Values File      #");
          bw.newLine();
          bw.write("#                          #");
          bw.newLine();
          bw.write("############################");
          bw.newLine();
          bw.newLine();
          bw.write("# To add a value");
          bw.newLine();
          bw.write("# add [Value] [ID] [Meta]");
          bw.newLine();
          bw.write("# add [Value] [OreDictionary]");
          bw.newLine();
          bw.newLine();
          bw.write("# To blacklist");
          bw.newLine();
          bw.write("# blacklist [ID] [Meta]");
          bw.newLine();
          bw.write("# blacklist [OreDictionary]");
          bw.newLine();
          bw.newLine();
          bw.close();
        }
      }
      for (String line : FileUtils.readLines(file)) {
        if (!line.startsWith("#")) {
          QMCCustomAction action = read(line);
          if (action != null && action.isValid()) {
            data.add(action);
          }
        }
      }
    } catch (Exception e) {
      Debug.handleException(e);
    }
    return data;
  }
  
  public static QMCCustomAction read(String str) {
    String[] result = str.split("\\s");
    String command = result[0];
    if (command.startsWith("a")) {
      return add(result);
    } else if (command.startsWith("b")) {
      return blacklist(result);
    }
    return null;
  }
  
  private static QMCCustomAction add(String[] result) {
    Double value;
    try {
      value = Double.parseDouble(result[1]);
    } catch (NumberFormatException e) {
      return null;
    }
    Object o = read(result, 2);
    return new QMCCustomAction(o, value);
  }
  
  private static QMCCustomAction blacklist(String[] result) {
    Object o = read(result, 1);
    return new QMCCustomAction(o);
  }
  
  private static Object read(String[] str, int starting) { // String or ItemStack
    try {
      return new ItemStack(Integer.parseInt(str[starting]), 1, Integer.parseInt(str[starting + 1]));
    } catch (Exception e) {
      return str[starting];
    }
  }
}
