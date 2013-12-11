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
  
  private static ItemStack currentlyChecking;
  
  private static ArrayList<ItemStack> guessFailed = new ArrayList<ItemStack>();
  
  private static boolean guessChecking;
  
  public static void addHandler(GuessHandler h) {
    handlers.add(h);
  }
  
  public static void load() {
    Stopwatch timer = new Stopwatch();
    timer.start();
    for (GuessHandler h : handlers) {
      Log.debug("Initiating " + h.getClass() + " Guess Handler");
      h.init();
    }
    checkLoop();
    timer.stop();
    Log.info("It took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds to Guess");
    Log.debug(guessFailed.size() + " Items to not have a value");
    guessFailed = new ArrayList<ItemStack>();
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
            guessChecking = true;
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
    if (guessFailed.contains(itemstack)) {
      return -1;
    }
    if (guessChecking) {
      currentlyChecking = itemstack;
      guessChecking = false;
    } else if (ItemStackUtil.equalsIgnoreNBT(itemstack, currentlyChecking) || ItemStackUtil.oreDictionary(itemstack, currentlyChecking)) {
      return -1;
    }
    double qmc = -1;
    for (GuessHandler h : handlers) {
      qmc = h.check(itemstack);
      if (qmc > 0) {
        break;
      }
    }
    if (qmc > 0) {
      QMC.add(itemstack, qmc);
      return qmc;
    } else {
      guessFailed.add(itemstack);
      return -1;
    }
  }
}
