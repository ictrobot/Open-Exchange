package oe.qmc.guess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.minecraft.item.ItemStack;
import oe.api.GuessHandler;
import oe.api.GuessHandlerFactory;
import oe.core.Debug;
import oe.core.Log;
import oe.core.util.Util;
import oe.qmc.QMC;
import com.google.common.base.Stopwatch;

public class Guess {
  
  private static List<GuessHandler> handlers = new ArrayList<GuessHandler>();
  private static List<GuessHandlerFactory> factories = new ArrayList<GuessHandlerFactory>();
  
  public static List<ItemStack> currentlyChecking = new ArrayList<ItemStack>();
  
  private static HashMap<ItemStack, List<GuessHandler>> toGuess = new HashMap<ItemStack, List<GuessHandler>>();
  
  public static void addFactory(GuessHandlerFactory f) {
    factories.add(f);
  }
  
  public static void load() {
    Stopwatch timer = new Stopwatch();
    timer.start();
    for (GuessHandlerFactory f : factories) {
      try {
        Log.debug("Initiating " + f.getClass().getSimpleName() + " GuessHandlerFactory");
        f.init();
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    for (GuessHandlerFactory f : factories) {
      try {
        List<GuessHandler> data = f.handlers();
        getHandlers().addAll(data);
        int num = 0;
        for (GuessHandler h : data) {
          for (ItemStack i : h.itemstacks) {
            List<GuessHandler> list;
            if (toGuess.containsKey(i)) {
              list = toGuess.get(i);
              toGuess.remove(i);
            } else {
              list = new ArrayList<GuessHandler>();
            }
            list.add(h);
            toGuess.put(i, list);
            num++;
          }
        }
        Log.debug("Received " + num + " Handlers from " + f.getClass().getSimpleName());
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    for (GuessHandler h : handlers) {
      try {
        h.init();
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    for (Object o : toGuess.keySet().toArray()) {
      try {
        check((ItemStack) o);
      } catch (Exception e) {
        Debug.handleException(e);
      }
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
      double q = h.check(itemstack);
      if (q > 0) {
        qmc = Util.minPos(qmc, q);
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
  
  public static List<GuessHandlerFactory> getFactories() {
    return factories;
  }
  
  public static List<GuessHandler> getHandlers() {
    return handlers;
  }
}
