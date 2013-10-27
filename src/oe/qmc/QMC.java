package oe.qmc;

import java.text.DecimalFormat;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.lib.helper.ConfigHelper;
import oe.qmc.file.CustomValuesFileReader;

public class QMC {
  
  public static boolean loaded = false;
  
  public static String name = "QMC";
  public static String nameFull = "Quantum Matter Currency";
  
  private static QMCData[] data = new QMCData[0];
  public static DecimalFormat formatter = new DecimalFormat("0.00");
  
  /**
   * Load
   */
  public static void load() {
    ConfigHelper.load();
    name = ConfigHelper.other("QMC", "Name", "QMC");
    nameFull = ConfigHelper.other("QMC", "Stands For", "Quantum Matter Currency");
    ConfigHelper.save();
    NormalQMCValues.load();
    CustomValuesFileReader.read();
    loaded = true;
  }
  
  // Get QMC Values
  /**
   * @return QMC value of the item
   */
  public static double getQMC(Item item) {
    return getQMC(new ItemStack(item));
  }
  
  /**
   * @return QMC value of the block
   */
  public static double getQMC(Block block) {
    return getQMC(new ItemStack(block));
  }
  
  /**
   * @return QMC value of the itemstack
   */
  public static double getQMC(ItemStack itemstack) {
    if (itemstack == null) {
      return -1;
    }
    int r = getReference(itemstack);
    if (r != -1) {
      return data[r].QMC;
    } else {
      // Damaged Items
      if (itemstack.isItemStackDamageable()) {
        for (int i = 0; i < data.length; i++) {
          QMCData check = data[i];
          if (check.type != QMCType.OreDictionary) {
            if (check.itemstack.isItemStackDamageable()) {
              if (check.itemstack.itemID == itemstack.itemID) {
                double qmc = data[i].QMC;
                double maxDamage = itemstack.getMaxDamage();
                double damage = itemstack.getItemDamage();
                double totalDamage = maxDamage - damage;
                double percent = totalDamage / maxDamage;
                return qmc * percent;
              }
            }
          }
        }
      }
    }
    return -1;
  }
  
  /**
   * @return QMC value of the OreDictionary value
   */
  public static double getQMC(String ore) {
    int r = getReference(ore);
    if (r != -1) {
      return data[r].QMC;
    }
    return -1;
  }
  
  // Add QMC Value
  /**
   * Adds an Item to the database
   */
  public static void add(Item item, double Value) {
    add(new ItemStack(item), Value);
  }
  
  /**
   * Adds an Block to the database
   */
  public static void add(Block block, double Value) {
    add(new ItemStack(block), Value);
  }
  
  /**
   * Adds an Itemstack to the database
   */
  public static void add(ItemStack stack, double Value) {
    stack.stackSize = 1;
    increase();
    data[data.length - 1] = new QMCData(stack, Value);
  }
  
  /**
   * Adds an OreDictionary string to the database
   */
  public static void add(String ore, double Value) {
    increase();
    data[data.length - 1] = new QMCData(ore, Value);
  }
  
  // Check for QMC value
  /**
   * @return If the Itemstack has a QMC value in the database
   */
  public static boolean hasValue(ItemStack stack) {
    return getQMC(stack) >= 0;
  }
  
  // BlackList
  /**
   * BlackLists ore
   */
  public static void blacklist(String ore) {
    remove(ore);
    add(ore, -1);
  }
  
  /**
   * BlackLists stack
   */
  public static void blacklist(ItemStack stack) {
    remove(stack);
    add(stack, -1);
  }
  
  // Remove
  /**
   * Removes ore
   */
  public static void remove(String ore) {
    if (hasValue(ore)) {
      remove(getReference(ore));
    }
  }
  
  /**
   * Removes stack
   */
  public static void remove(ItemStack stack) {
    if (hasValue(stack)) {
      remove(getReference(stack));
    }
  }
  
  /**
   * @return If the OreDictionary value has a QMC value in the database
   */
  public static boolean hasValue(String ore) {
    return getQMC(ore) >= 0;
  }
  
  // Advanced access to database
  /**
   * @return Number of QMCData entries
   */
  public static int length() {
    return data.length;
  }
  
  /**
   * @return Reference for the itemstack in the database
   */
  public static int getReference(ItemStack itemstack) {
    for (int i = 0; i < data.length; i++) {
      QMCData check = data[i];
      if (check.type != QMCType.OreDictionary) {
        if (check.itemstack.isItemEqual(itemstack)) {
          return i;
        }
      }
      if (check.type != QMCType.Itemstack) {
        int oreID = OreDictionary.getOreID(itemstack);
        if (oreID != -1) {
          if (check.oreDictionary == OreDictionary.getOreName(oreID)) {
            return i;
          }
        }
      }
    }
    return -1;
  }
  
  /**
   * @return Reference for the OreDictionary value in the database
   */
  public static int getReference(String ore) {
    for (int i = 0; i < data.length; i++) {
      QMCData check = data[i];
      if (check.type != QMCType.Itemstack) {
        if (check.oreDictionary == ore) {
          return i;
        }
      }
    }
    return -1;
  }
  
  /**
   * @return QMCData for OreDictionary from the database
   */
  public static QMCData getQMCData(int Reference) {
    if (Reference >= 0 && Reference < data.length) {
      return data[Reference];
    }
    return null;
  }
  
  // Private
  /**
   * Increase database size
   */
  private static void increase() {
    QMCData[] tmp = new QMCData[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
  }
  
  /**
   * Removes reference from database
   */
  private static void remove(int Reference) {
    QMCData[] tmp = new QMCData[data.length - 1];
    if (Reference > 0) {
      System.arraycopy(data, 0, tmp, 0, Reference - 1);
    }
    if (Reference < data.length - 1) {
      System.arraycopy(data, Reference + 1, tmp, Reference + 1, data.length - Reference - 1);
    }
    data = tmp;
  }
}
