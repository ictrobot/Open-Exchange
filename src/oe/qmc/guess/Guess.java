package oe.qmc.guess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.minecraft.item.ItemStack;
import oe.api.GuessHandler;
import oe.api.GuessHandler.ActiveGuessHandler;
import oe.core.Log;
import oe.core.util.ItemStackUtil;
import oe.qmc.QMC;
import com.google.common.base.Stopwatch;

public class Guess {
  
  public static List<GuessHandler> handlers = new ArrayList<GuessHandler>();
  
  public static List<ItemStack> currentlyChecking = new ArrayList<ItemStack>();
  
  private static List<ItemStack> toGuess = new ArrayList<ItemStack>();
  
  public static void addHandler(GuessHandler h) {
    handlers.add(h);
  }
  
  public static void load() {
    Stopwatch timer = new Stopwatch();
    timer.start();
    for (Object o : handlers.toArray()) { // So list can change
      GuessHandler h = (GuessHandler) o;
      Log.debug("Initiating " + h.getClass().getSimpleName() + " " + h.type);
      h.init();
    }
    for (Object o : handlers.toArray()) { // So list can change
      GuessHandler h = (GuessHandler) o;
      if (h instanceof ActiveGuessHandler) {
        List<ItemStack> g = ((ActiveGuessHandler) h).getItemStacks();
        for (ItemStack i : g) {
          if (!toGuess.contains(i)) {
            toGuess.add(i);
          }
        }
      }
    }
    Object[] objects = toGuess.toArray();
    for (Object o : objects) {
      if (toGuess.contains(o)) {
        check((ItemStack) o);
      }
    }
    timer.stop();
    Log.info("It took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds to Guess");
    toGuess = new ArrayList<ItemStack>();
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
    if (!toGuess.contains(itemstack)) {
      return false;
    }
    if (QMC.hasQMC(itemstack)) {
      return false;
    }
    if (QMC.isBlacklisted(itemstack)) {
      return false;
    }
    if (currentlyChecking.contains(itemstack)) {
      return false;
    }
    return true;
  }
  
  public static void addToFailed(ItemStack itemstack) {
    Iterator<ItemStack> iterator = toGuess.iterator();
    while (iterator.hasNext()) {
      ItemStack check = iterator.next();
      if (ItemStackUtil.equalsIgnoreNBT(check, itemstack)) {
        iterator.remove();
      }
    }
  }
}
