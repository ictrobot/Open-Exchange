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
import oe.qmc.QMC;
import com.google.common.base.Stopwatch;

public class Guess {
  
  public static class HashStack {
    public final ItemStack itemstack;
    private int hashcode;
    
    public HashStack(ItemStack itemstack) {
      if (itemstack == null) {
        this.itemstack = null;
        this.hashcode = 0;
        return;
      }
      this.itemstack = itemstack.copy();
      this.itemstack.stackSize = 1;
      this.hashcode = itemstack.getDisplayName().hashCode() ^ itemstack.itemID ^ (itemstack.getItemDamage());
      if (this.itemstack.stackTagCompound != null) {
        this.hashcode = this.hashcode ^ this.itemstack.stackTagCompound.hashCode();
      }
    }
    
    public int hashCode() {
      return this.hashcode;
    }
    
    public boolean equals(Object o) {
      if (!(o instanceof HashStack)) {
        return false;
      }
      return this.hashcode == ((HashStack) o).hashCode();
    }
    
    public String toString() {
      return this.getClass().getSimpleName();
    }
  }
  
  private static List<GuessHandler> handlers = new ArrayList<GuessHandler>();
  private static List<GuessHandlerFactory> factories = new ArrayList<GuessHandlerFactory>();
  
  public static List<ItemStack> currentlyChecking = new ArrayList<ItemStack>();
  
  private static HashMap<HashStack, List<GuessHandler>> itemstacks = new HashMap<HashStack, List<GuessHandler>>();
  
  public static void addFactory(GuessHandlerFactory f) {
    factories.add(f);
  }
  
  public static void load() {
    Stopwatch timer = new Stopwatch();
    Stopwatch stepTimer = new Stopwatch();
    timer.start();
    stepTimer.start();
    for (GuessHandlerFactory f : factories) {
      try {
        Log.debug("Initiating " + f.getClass().getSimpleName() + " GuessHandlerFactory");
        f.init();
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    stepTimer.stop();
    Log.debug("Initiating factories took " + stepTimer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds");
    stepTimer.reset();
    stepTimer.start();
    for (GuessHandlerFactory f : factories) {
      try {
        List<GuessHandler> data = f.handlers();
        handlers.addAll(data);
        int num = 0;
        for (GuessHandler h : data) {
          for (ItemStack i : h.itemstacks) {
            if (i == null) {
              continue;
            }
            HashStack hash = new HashStack(i);
            List<GuessHandler> list;
            if (itemstacks.containsKey(hash)) {
              list = itemstacks.get(hash);
              itemstacks.remove(hash);
            } else {
              list = new ArrayList<GuessHandler>();
            }
            list.add(h);
            itemstacks.put(hash, list);
            num++;
          }
        }
        Log.debug("Received " + num + " Handlers from " + f.getClass().getSimpleName());
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    stepTimer.stop();
    Log.debug("Receiving handlers took " + stepTimer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds");
    stepTimer.reset();
    stepTimer.start();
    for (List<GuessHandler> list : itemstacks.values()) {
      for (GuessHandler h : list) {
        try {
          h.init();
        } catch (Exception e) {
          Debug.handleException(e);
        }
      }
    }
    stepTimer.stop();
    Log.debug("Initiating handlers took " + stepTimer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds");
    Log.debug(itemstacks);
    stepTimer.reset();
    stepTimer.start();
    for (Object o : itemstacks.keySet().toArray()) {
      try {
        check(((HashStack) o).itemstack);
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    stepTimer.stop();
    timer.stop();
    Log.debug("Checking took " + stepTimer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds");
    Log.info("Guessing took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds");
    itemstacks = new HashMap<HashStack, List<GuessHandler>>();
    System.gc();
  }
  
  public static double check(ItemStack itemstack) {
    if (!shouldGuess(itemstack)) {
      return QMC.getQMC(itemstack);
    }
    HashStack hash = new HashStack(itemstack);
    currentlyChecking.add(itemstack);
    double qmc = -1;
    for (GuessHandler h : itemstacks.get(hash)) {
      qmc = h.check(itemstack);
      if (qmc > 0) {
        break;
      }
    }
    currentlyChecking.remove(itemstack);
    itemstacks.remove(hash);
    if (qmc > 0) {
      QMC.add(itemstack, qmc);
      return qmc;
    } else {
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
    if (!itemstacks.containsKey(new HashStack(itemstack))) {
      return false;
    }
    if (currentlyChecking.contains(itemstack)) {
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
