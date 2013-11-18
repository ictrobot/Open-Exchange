package oe.qmc;

import java.text.DecimalFormat;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.OEItemInterface;
import oe.api.OE_API;
import oe.lib.Log;
import oe.lib.helper.ConfigHelper;
import oe.lib.helper.OreDictionaryHelper;
import oe.qmc.file.CustomQMCValuesReader;
import oe.qmc.guess.GuessReturn;

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
    data = new QMCData[0];
    ConfigHelper.load();
    name = ConfigHelper.other("QMC", "Name", "QMC");
    nameFull = ConfigHelper.other("QMC", "Stands For", "Quantum Matter Currency");
    ConfigHelper.save();
    CustomQMCValuesReader.read();
    NormalQMCValues.load();
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
    double value = -1;
    if (itemstack == null) {
      return -1;
    }
    int r = getReference(itemstack);
    if (r != -1) {
      value = data[r].QMC;
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
                value = qmc * percent;
              }
            }
          }
        }
      }
    }
    if (OE_API.isOE(itemstack.getItem().getClass()) && value != -1) {
      OEItemInterface oe = (OEItemInterface) itemstack.getItem();
      value = value + oe.getQMC(itemstack);
    }
    return value;
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
    if (!hasQMC(stack)) {
      stack.stackSize = 1;
      increase();
      data[data.length - 1] = new QMCData(stack, Value);
    }
  }
  
  /**
   * Adds an OreDictionary string to the database
   */
  public static void add(String ore, double Value) {
    if (!hasQMC(ore)) {
      increase();
      data[data.length - 1] = new QMCData(ore, Value);
    }
  }
  
  /**
   * Adds a guessed value to the database;
   */
  public static void addGuessed(ItemStack stack, GuessReturn guessdata) {
    if (guessdata == null) {
      return;
    }
    increase();
    data[data.length - 1] = new QMCData(stack, guessdata);
  }
  
  // Check for QMC value
  /**
   * @return If the Itemstack has a QMC value in the database
   */
  public static boolean hasQMC(ItemStack stack) {
    return getQMC(stack) >= 0;
  }
  
  /**
   * @return If the OreDictionary value has a QMC value in the database
   */
  public static boolean hasQMC(String ore) {
    return getQMC(ore) >= 0;
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
    if (hasQMC(ore)) {
      remove(getReference(ore));
    }
  }
  
  /**
   * Removes stack
   */
  public static void remove(ItemStack stack) {
    if (hasQMC(stack)) {
      remove(getReference(stack));
    }
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
          if (OreDictionary.getOreID(check.oreDictionary) == oreID) {
            return i;
          }
        }
      }
    }
    return -1;
  }
  
  /**
   * @return Reference for the itemstack that is damaged in the database, E.G a damaged wood shovel
   *         would still return the wood shovel reference;
   */
  public static int getReferenceDamaged(ItemStack itemstack) {
    if (!itemstack.isItemStackDamageable()) {
      return getReference(itemstack);
    }
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
  
  /**
   * @return ItemStacks with the QMC value
   */
  public static ItemStack[] getItemStacksFromQMC(Double value) {
    ItemStack[] toReturn = new ItemStack[0];
    for (QMCData d : data) {
      if (d.QMC == value) {
        if (d.type != QMCType.OreDictionary) {
          ItemStack[] tmp = new ItemStack[toReturn.length + 1];
          System.arraycopy(toReturn, 0, tmp, 0, toReturn.length);
          toReturn = tmp;
          toReturn[toReturn.length - 1] = d.itemstack;
        }
        if (d.type != QMCType.Itemstack) {
          ItemStack[] ore = OreDictionaryHelper.getItemStacks(d.oreDictionary);
          if (ore != null) {
            int curLength = data.length;
            ItemStack[] tmp = new ItemStack[toReturn.length + ore.length];
            System.arraycopy(toReturn, 0, tmp, 0, toReturn.length);
            toReturn = tmp;
            for (int i = curLength; i < data.length; i++) {
              toReturn[i] = ore[curLength - i];
            }
          }
        }
      }
    }
    return toReturn;
  }
  
  /**
   * Update Ore Dictionary Values
   */
  public static void updateOreDictionary() {
    Log.debug("Updating Ore Dictionary Values in QMC Database");
    for (QMCData d : data) {
      if (d.type != QMCType.OreDictionary) {
        int oreID = OreDictionary.getOreID(d.itemstack);
        if (oreID != -1) {
          d.oreDictionary = OreDictionary.getOreName(oreID);
          d.type = QMCType.OreDictionary_Itemstack;
        } else {
          d.oreDictionary = "NONE";
          d.type = QMCType.Itemstack;
        }
      } else {
        ItemStack[] stacks = OreDictionaryHelper.getItemStacks(d.oreDictionary);
        if (stacks != null) {
          d.itemstack = stacks[0];
          d.type = QMCType.OreDictionary_Itemstack;
        }
      }
    }
  }
  
  /**
   * @return The whole database
   */
  public static QMCData[] getDataBase() {
    return data;
  }
  
  public static void removeGuessed() {
    for (int i = 0; i < data.length; i++) {
      GuessReturn gd = data[i].guess;
      if (gd != null) {
        remove(i);
        removeGuessed();
        break;
      }
    }
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
  private static void remove(int i) {
    data = ArrayUtils.removeElement(data, data[i]);
  }
}
