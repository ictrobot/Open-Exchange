package oe.qmc.guess;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.OE;
import oe.lib.Debug;
import oe.lib.Log;
import oe.lib.util.ItemStackUtil;
import oe.qmc.QMC;
import com.google.common.base.Stopwatch;

public class Guess {
  
  public static class Data {
    public ItemStack[] input; // Input ItemStacks
    public double QMC; // Total QMC
    public int outputNum; // Number of Items Made
    
    public Data(ItemStack[] Input, double value, int OutputNum) {
      this.input = Input;
      this.QMC = value;
      this.outputNum = OutputNum;
    }
    
    public Data(ItemStack Input, double value, int OutputNum) {
      this.input = new ItemStack[1];
      this.input[0] = Input;
      this.QMC = value;
      this.outputNum = OutputNum;
    }
  }
  
  public static Class<?>[] classes = new Class[0];
  
  // To Stop recursions
  private static int recursions = 0;
  private static int recursionLimit = 25;
  private static boolean recursionNotified = false;
  private static ItemStack stackCheck;
  
  public static boolean add(Class<?> c) {
    if (OE.isOEGuessable(c)) {
      increaseClasses();
      classes[classes.length - 1] = c;
      return true;
      
    }
    return false;
  }
  
  public static void load() {
    Stopwatch timer = new Stopwatch();
    timer.start();
    for (Class<?> c : classes) {
      try {
        Method m = c.getMethod("init");
        m.invoke(null);
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    checkLoop();
    timer.stop();
    Log.info("It took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds to Guess");
  }
  
  public static int[] meta(int ID) {
    int[] possible = new int[] { 0 };
    for (Class<?> c : classes) {
      int[] meta;
      try {
        Method m = c.getMethod("meta", int.class);
        Object r = m.invoke(null, ID);
        if (r != null) {
          if (r instanceof int[]) {
            meta = (int[]) r;
            for (int i : meta) {
              boolean exists = false;
              for (int e : possible) {
                if (e == i) {
                  exists = true;
                }
              }
              if (!exists) {
                int[] tmp = new int[possible.length + 1];
                System.arraycopy(possible, 0, tmp, 0, possible.length);
                possible = tmp;
                possible[possible.length - 1] = i;
              }
            }
          }
        }
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    return possible;
  }
  
  private static void checkLoop() {
    for (int i = 0; i < 32000; i++) { // Length of ItemList Array
      if (ItemStackUtil.isBlock(i) || ItemStackUtil.isItem(i)) {
        int[] Meta = meta(i);
        for (int m : Meta) {
          ItemStack check = new ItemStack(i, 0, m);
          if (check != null && !QMC.hasQMC(check)) {
            recursions = -1;
            recursionNotified = false;
            check(check);
          }
        }
      }
    }
  }
  
  public static double check(ItemStack itemstack) {
    Data data;
    if (itemstack == null) {
      return -1;
    }
    if (QMC.hasQMC(itemstack)) {
      return QMC.getQMC(itemstack);
    }
    recursions++;
    int ore = OreDictionary.getOreID(itemstack);
    // Tries to stop recursions before they happen
    if (recursions == 0) {
      stackCheck = itemstack;
    } else if ((itemstack.itemID == stackCheck.itemID && itemstack.getItemDamage() == stackCheck.getItemDamage()) || (ore != -1 && ore == OreDictionary.getOreID(stackCheck))) {
      return -1;
    }
    // Stops recursions from carrying on
    if (recursions > recursionLimit) {
      if (!recursionNotified) {
        Log.debug("ItemStack " + stackCheck.toString() + " (ID: " + stackCheck.itemID + ") is recurring to many times");
        recursionNotified = true;
      }
      return -1;
    }
    data = checkClasses(itemstack);
    if (data != null) {
      if (data.QMC > -1) {
        ItemStack toAdd = itemstack.copy();
        QMC.add(toAdd, data.QMC);
      }
    }
    if (data != null) {
      return data.QMC;
    }
    return -1;
  }
  
  public static Data checkClasses(ItemStack itemstack) {
    for (Class<?> c : classes) {
      try {
        Method m = c.getMethod("check", ItemStack.class);
        Object r = m.invoke(null, itemstack);
        if (r instanceof Data) {
          return (Data) r;
        }
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    return null;
  }
  
  private static void increaseClasses() {
    Class<?>[] tmp = new Class<?>[classes.length + 1];
    System.arraycopy(classes, 0, tmp, 0, classes.length);
    classes = tmp;
  }
}
