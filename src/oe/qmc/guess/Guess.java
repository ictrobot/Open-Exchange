package oe.qmc.guess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.minecraft.item.ItemStack;
import oe.api.GuessHandler;
import oe.api.GuessHandler.ActiveGuessHandler;
import oe.core.Log;
import oe.qmc.QMC;
import com.google.common.base.Stopwatch;

public class Guess {
  
  public static List<GuessHandler> handlers = new ArrayList<GuessHandler>();
  
  public static List<ItemStack> currentlyChecking = new ArrayList<ItemStack>();
  
  private static HashMap<ItemStack, List<GuessHandler>> toGuess = new HashMap<ItemStack, List<GuessHandler>>();
  
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
          List<GuessHandler> list;
          if (toGuess.containsKey(i)) {
            list = toGuess.get(i);
            toGuess.remove(i);
          } else {
            list = new ArrayList<GuessHandler>();
          }
          list.add(h);
          toGuess.put(i, list);
        }
      }
    }
    for (Object o : toGuess.keySet().toArray()) {
      check((ItemStack) o);
    }
    timer.stop();
    Log.info("It took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds to Guess");
    toGuess = new HashMap<ItemStack, List<GuessHandler>>();
    System.gc();
  }
  
  public static double check(ItemStack itemstack) {
    if (!shouldGuess(itemstack)) {
      return QMC.getQMC(itemstack);
    }
    currentlyChecking.add(itemstack);
    double qmc = -1;
    for (GuessHandler h : toGuess.get(itemstack)) {
      qmc = h.check(itemstack);
      if (qmc > 0) {
        break;
      }
    }
    currentlyChecking.remove(itemstack);
    if (qmc > 0) {
      QMC.add(itemstack, qmc);
      return qmc;
    } else {
      toGuess.remove(itemstack);
      return -1;
    }
  }
  
  public static boolean shouldGuess(ItemStack itemstack) {
    if (itemstack == null) {
      return false;
    }
    if (!toGuess.keySet().contains(itemstack)) {
      return false;
    }
    if (currentlyChecking.contains(itemstack)) {
      return false;
    }
    if (QMC.hasQMC(itemstack)) {
      return false;
    }
    if (QMC.isBlacklisted(itemstack)) {
      return false;
    }
    return true;
  }
}
