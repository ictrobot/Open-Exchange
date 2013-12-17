package oe.qmc.guess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.minecraft.item.ItemStack;
import oe.api.GuessHandler;
import oe.api.GuessHandler.ActiveGuessHandler;
import oe.core.Log;
import oe.core.util.ItemStackUtil;
import oe.core.util.Util;
import oe.qmc.QMC;
import com.google.common.base.Stopwatch;

public class Guess {
  
  public static List<GuessHandler> handlers = new ArrayList<GuessHandler>();
  
  public static List<ItemStack> currentlyChecking = new ArrayList<ItemStack>();
  
  private static HashMap<Integer, List<Integer>> guessFailed = new HashMap<Integer, List<Integer>>();
  
  public static void addHandler(GuessHandler h) {
    handlers.add(h);
  }
  
  public static void load() {
    Util.setMaxPriority();
    Stopwatch timer = new Stopwatch();
    timer.start();
    for (GuessHandler h : handlers) {
      String type = "Passive Guess Handler";
      if (h instanceof ActiveGuessHandler) {
        type = "Active Guess Handler";
      }
      Log.debug("Initiating " + h.getClass().getSimpleName() + " " + type);
      h.init();
    }
    for (GuessHandler h : handlers) {
      String type = "Passive Guess Handler";
      if (h instanceof ActiveGuessHandler) {
        type = "Active Guess Handler";
      }
      Log.debug("Initiating " + h.getClass().getSimpleName() + " " + type);
      h.beforeGuess();
    }
    for (GuessHandler h : handlers) {
      if (h instanceof ActiveGuessHandler) {
        ActiveGuessHandler a = (ActiveGuessHandler) h;
        Log.debug("Guessing " + a.getClass().getSimpleName() + " Active Guess Handler");
        a.guess();
      }
    }
    timer.stop();
    Log.info("It took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds to Guess");
    guessFailed = new HashMap<Integer, List<Integer>>();
    Util.restorePriority();
    System.gc();
  }
  
  public static double check(ItemStack itemstack) {
    if (!shouldGuess(itemstack)) {
      return QMC.getQMC(itemstack);
    }
    currentlyChecking.add(itemstack);
    ItemStack toCheck = itemstack.copy();
    double qmc = -1;
    for (GuessHandler h : handlers) {
      qmc = h.check(toCheck);
      if (qmc > 0) {
        break;
      }
    }
    currentlyChecking.remove(itemstack);
    if (qmc > 0) {
      QMC.add(itemstack, qmc);
      return qmc;
    } else {
      addToFailed(itemstack);
      return -1;
    }
  }
  
  public static boolean shouldGuess(ItemStack itemstack) {
    if (itemstack == null) {
      return false;
    }
    if (QMC.hasQMC(itemstack)) {
      return false;
    }
    if (QMC.isBlacklisted(itemstack)) {
      return false;
    }
    if (guessFailed.get(itemstack.itemID) != null) {
      if (guessFailed.get(itemstack.itemID).contains(itemstack.getItemDamage())) {
        return false;
      }
    }
    for (ItemStack stack : currentlyChecking) {
      if (ItemStackUtil.equals(stack, itemstack)) {
        return false;
      }
    }
    return true;
  }
  
  public static void addToFailed(ItemStack itemstack) {
    List<Integer> list;
    if (guessFailed.get(itemstack.itemID) != null) {
      list = guessFailed.get(itemstack.itemID);
      guessFailed.remove(itemstack.itemID);
    } else {
      list = new ArrayList<Integer>();
    }
    for (int i : list) {
      if (i == itemstack.getItemDamage()) {
        return;
      }
    }
    list.add(itemstack.getItemDamage());
    guessFailed.put(itemstack.itemID, list);
  }
}
