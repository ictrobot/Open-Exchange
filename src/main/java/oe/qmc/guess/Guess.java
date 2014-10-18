package oe.qmc.guess;

import com.google.common.base.Stopwatch;
import net.minecraft.item.ItemStack;
import oe.api.qmc.guess.GuessHandler;
import oe.api.qmc.guess.GuessHandlerFactory;
import oe.api.qmc.id.ItemStackID;
import oe.core.Debug;
import oe.core.Log;
import oe.qmc.QMC;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class Guess {

  private static List<GuessHandler> handlers = new ArrayList<GuessHandler>();
  private static List<GuessHandlerFactory> factories = new ArrayList<GuessHandlerFactory>();

  public static List<ItemStackID> currentlyChecking = new ArrayList<ItemStackID>();

  public static List<ItemStackID> guessID = new ArrayList<ItemStackID>();

  private static boolean[] failed = null;
  /**
   * ArrayList GuessHandler
   */
  private static Object[] itemstacks;

  public static void addFactory(GuessHandlerFactory f) {
    factories.add(f);
  }

  public static void load() {
    handlers.clear();
    currentlyChecking.clear();
    guessID.clear();
    Stopwatch timer = Stopwatch.createStarted();
    Stopwatch stepTimer = Stopwatch.createStarted();
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
        for (GuessHandler g : data) {
          for (ItemStack itemstack : g.itemstacks) {
            ItemStackID i = new ItemStackID(itemstack);
            if (!guessID.contains(i)) {
              guessID.add(i);
            }
          }
        }
        Log.debug("Received " + data.size() + " handlers from " + f.getClass().getSimpleName());
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    stepTimer.stop();
    Log.debug("Receiving handlers took " + stepTimer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds");
    stepTimer.reset();
    stepTimer.start();
    failed = new boolean[guessID.size()];
    itemstacks = new Object[guessID.size()];
    for (int i = 0; i < guessID.size(); i++) {
      failed[i] = false;
      itemstacks[i] = new ArrayList<GuessHandler>();
    }
    for (GuessHandler h : handlers) {
      try {
        h.init();
        for (ItemStack itemstack : h.itemstacks) {
          ItemStackID id = new ItemStackID(itemstack);
          int index = guessID.indexOf(id);
          ((ArrayList<GuessHandler>) itemstacks[index]).add(h);
        }
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    stepTimer.stop();
    Log.debug("Initiating and sorting handlers took " + stepTimer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds");
    Log.debug(itemstacks);
    stepTimer.reset();
    stepTimer.start();
    for (int i = 0; i < guessID.size(); i++) {
      try {
        check(guessID.get(i));
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    stepTimer.stop();
    timer.stop();
    Log.debug("Checking took " + stepTimer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds");
    Log.info("Guessing took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds");
    itemstacks = null;
    System.gc();
  }

  public static double check(ItemStackID itemstack) {
    if (!shouldGuess(itemstack.getItemStack())) {
      return QMC.getQMC(itemstack.getItemStack());
    }
    double qmc = -1;
    int id = guessID.indexOf(itemstack);
    currentlyChecking.add(itemstack);
    ArrayList<GuessHandler> hs = (ArrayList<GuessHandler>) itemstacks[id];
    for (GuessHandler h : hs) {
      qmc = h.check(itemstack.getItemStack());
      if (qmc > 0) {
        break;
      }
    }
    hs.clear();
    itemstacks[id] = hs;
    currentlyChecking.remove(itemstack);
    if (qmc > 0) {
      QMC.add(itemstack, qmc);
      return qmc;
    } else {
      failed[id] = true;
      return -1;
    }
  }

  public static boolean shouldGuess(ItemStack itemstack) {
    if (itemstack == null) {
      return false;
    }
    ItemStackID i = null;
    try {
      i = new ItemStackID(itemstack);
    } catch (Exception e) {
      return false;
    }
    if (failed(i)) {
      return false;
    }
    if (QMC.hasQMC(itemstack)) {
      return false;
    }
    if (currentlyChecking.contains(i)) {
      return false;
    }
    if (QMC.isBlacklisted(itemstack)) {
      return false;
    }
    return true;
  }

  public static boolean failed(ItemStackID itemstack) {
    int num = guessID.indexOf(itemstack);
    if (num == -1) {
      return true;
    }
    return failed[num];
  }

  public static List<GuessHandlerFactory> getFactories() {
    return factories;
  }

  public static List<GuessHandler> getHandlers() {
    return handlers;
  }
}
