package oe.qmc.guess;

import java.util.concurrent.TimeUnit;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.GuessHandler;
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
  
  public static GuessHandler[] handlers = new GuessHandler[0];
  
  // To Stop recursions
  private static int recursions = 0;
  private static int recursionLimit = 25;
  private static boolean recursionNotified = false;
  private static ItemStack stackCheck;
  
  public static boolean addHandler(GuessHandler h) {
    GuessHandler[] tmp = new GuessHandler[handlers.length + 1];
    System.arraycopy(handlers, 0, tmp, 0, handlers.length);
    handlers = tmp;
    handlers[handlers.length - 1] = h;
    return true;
  }
  
  public static void load() {
    Stopwatch timer = new Stopwatch();
    timer.start();
    for (GuessHandler h : handlers) {
      h.init();
    }
    checkLoop();
    timer.stop();
    Log.info("It took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds to Guess");
  }
  
  public static int[] meta(int ID) {
    int[] possible = new int[] { 0 };
    for (GuessHandler h : handlers) {
      int[] meta = h.meta(ID);
      for (int i : meta) {
        boolean exists = false;
        for (int e : possible) {
          if (e == i) {
            exists = true;
            break;
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
    if (QMC.isBlacklisted(itemstack)) {
      return -1;
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
    for (GuessHandler h : handlers) {
      try {
        Data d = h.check(itemstack);
        if (d != null) {
          return d;
        }
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    return null;
  }
}
