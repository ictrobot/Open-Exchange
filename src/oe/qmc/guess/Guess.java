package oe.qmc.guess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import oe.api.GuessHandler;
import oe.core.Log;
import oe.core.util.ConfigUtil;
import oe.core.util.ItemStackUtil;
import oe.core.util.Util;
import oe.qmc.QMC;
import com.google.common.base.Stopwatch;

public class Guess {
  
  public static List<GuessHandler> handlers = new ArrayList<GuessHandler>();
  
  private static List<ItemStack> currentlyChecking = new ArrayList<ItemStack>();
  
  private static HashMap<Integer, List<Integer>> guessFailed = new HashMap<Integer, List<Integer>>();
  
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
    boolean displayMsg;
    if (Util.isClient()) {
      ConfigUtil.load();
      displayMsg = ConfigUtil.other("QMC", "Show QMC Guessing Loading Messages", true);
      ConfigUtil.save();
    } else {
      displayMsg = false;
    }
    for (int i = 1; i < 32000; i++) { // Length of ItemList Array
      if (ItemStackUtil.isBlock(i) || ItemStackUtil.isItem(i)) {
        for (int m : meta(i)) {
          ItemStack check = new ItemStack(i, 1, m);
          if (check != null && !QMC.hasQMC(check)) {
            currentlyChecking = new ArrayList<ItemStack>();
            try {
              check(check);
            } catch (Exception e) {
              Log.info("Error occured while guessing " + check.getDisplayName() + " (ID:" + check.itemID + " Meta:" + check.getItemDamage() + ")");
              e.printStackTrace();
              break;
            }
          }
        }
      }
      if (i % 320 == 0) {
        int percent = i / 320;
        if (percent % 5 == 0) { // Every 5% info
          Log.info("Guessing " + percent + "% done, time elapsed: " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliSeconds");
          if (displayMsg) {
            try {
              MinecraftServer.class.getField("userMessage").set(MinecraftServer.getServer(), "Open Exchange - Guessing " + percent + "%");
            } catch (Exception e) {
              
            }
          }
        } else { // Every other % debug
          Log.debug("Guessing " + percent + "% done, time elapsed: " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliSeconds");
        }
      }
    }
    timer.stop();
    Log.info("It took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds to Guess");
    guessFailed = new HashMap<Integer, List<Integer>>();
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
    if (guessFailed.get(itemstack.itemID) != null) {
      if (guessFailed.get(itemstack.itemID).contains(itemstack.getItemDamage())) {
        return -1;
      }
    }
    for (ItemStack checking : currentlyChecking) {
      if (ItemStackUtil.equals(itemstack, checking)) {
        return -1;
      }
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
      List<Integer> list;
      if (guessFailed.get(itemstack.itemID) != null) {
        list = guessFailed.get(itemstack.itemID);
        guessFailed.remove(itemstack.itemID);
      } else {
        list = new ArrayList<Integer>();
      }
      list.add(itemstack.getItemDamage());
      guessFailed.put(itemstack.itemID, list);
      return -1;
    }
  }
}
