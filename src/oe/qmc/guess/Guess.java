package oe.qmc.guess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.minecraft.item.ItemStack;
import oe.api.GuessHandler;
import oe.core.Log;
import oe.core.util.ItemStackUtil;
import oe.qmc.QMC;
import com.google.common.base.Stopwatch;

public class Guess {
  
  public static List<GuessHandler> handlers = new ArrayList<GuessHandler>();
  
  private static int recursions = 0;
  private static int recursionLimit = 25;
  private static boolean recursionNotified = false;
  private static ItemStack stackCheck;
  
  public static void addHandler(GuessHandler h) {
    handlers.add(h);
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
  
  public static List<Integer> meta(int ID) {
    List<Integer> data = new ArrayList<Integer>();
    for (GuessHandler h : handlers) {
      for (int i : h.meta(ID)) {
        if (!data.contains(i)) {
          data.add(i);
        }
      }
    }
    return data;
  }
  
  private static void checkLoop() {
    for (int i = 0; i < 32000; i++) { // Length of ItemList Array
      if (ItemStackUtil.isBlock(i) || ItemStackUtil.isItem(i)) {
        for (int m : meta(i)) {
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
    if (recursions > recursionLimit) {
      if (!recursionNotified) {
        Log.debug("ItemStack " + stackCheck.toString() + " (ID: " + stackCheck.itemID + ") is recurring to many times");
        recursionNotified = true;
      }
      return -1;
    }
    if (recursions == 0) {
      stackCheck = itemstack;
    } else if (ItemStackUtil.equalsIgnoreNBT(itemstack, stackCheck) || ItemStackUtil.oreDictionary(itemstack, stackCheck)) {
      return -1;
    }
    double qmc = -1;
    for (GuessHandler h : handlers) {
      qmc = h.check(itemstack);
      if (qmc > 0) {
        break;
      }
    }
    if (qmc > -1) {
      QMC.add(itemstack, qmc);
    }
    return -1;
  }
}
