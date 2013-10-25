package oe.qmc.guess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Stopwatch;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.OpenExchange;
import oe.api.OE_API;
import oe.lib.Log;
import oe.qmc.QMC;

public class Guess {
  
  public static Class<?>[] classes = new Class[0];
  public static ItemStack[] noValue = new ItemStack[0];
  
  // To Stop recursions
  private static int recursions = 0;
  private static int recursionLimit = 25;
  private static boolean recursionNotified = false;
  private static ItemStack stackCheck;
  
  // To make meta sensitive (Currently unused)
  @SuppressWarnings("unused")
  private static boolean multipleMeta;
  
  public static boolean add(Class<?> c) {
    if (OE_API.isOEGuessable(c)) {
      increaseClasses();
      classes[classes.length - 1] = c;
      return true;
      
    }
    return false;
  }
  
  public static void load() {
    Stopwatch timer = new Stopwatch();
    timer.start();
    for (Class<?> c : classes) {
      try {
        Method m = c.getMethod("init");
        m.invoke(null);
      } catch (Exception e) {
        if (OpenExchange.debug) {
          e.printStackTrace();
        }
      }
    }
    checkLoop();
    timer.stop();
    Log.info("It took " + timer.elapsed(TimeUnit.SECONDS) + " seconds to Guess");
  }
  
  public static int[] meta(int ID) {
    int[] possible = new int[0];
    for (Class<?> c : classes) {
      int[] meta;
      try {
        Method m = c.getMethod("meta", int.class);
        Object r = m.invoke(null, ID);
        if (r != null) {
          if (r instanceof int[]) {
            meta = (int[]) r;
            for (int i : meta) {
              boolean exists = false;
              for (int e : possible) {
                if (e == i) {
                  exists = true;
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
        }
      } catch (Exception e) {
        if (OpenExchange.debug) {
          e.printStackTrace();
        }
      }
    }
    return possible;
  }
  
  private static void checkLoop() {
    
    int r = QMC.length();
    
    for (int i = 0; i < Block.blocksList.length; i++) {
      if (Block.blocksList[i] != null) {
        if (!Block.blocksList[i].getUnlocalizedName().contains("tile.ForgeFiller")) {
          int[] Meta = meta(i);
          if (Meta.length > 1) {
            multipleMeta = true;
          } else {
            multipleMeta = false;
          }
          for (int m : Meta) {
            ItemStack check = new ItemStack(Block.blocksList[i], 0, m);
            if (!QMC.hasValue(check)) {
              recursions = -1;
              recursionNotified = false;
              check(check);
            }
          }
        }
      }
    }
    for (int i = 0; i < Item.itemsList.length; i++) {
      if (Item.itemsList[i] != null) {
        int[] Meta = meta(i);
        if (Meta.length > 1) {
          multipleMeta = true;
        } else {
          multipleMeta = false;
        }
        for (int m : Meta) {
          ItemStack check = new ItemStack(Item.itemsList[i], 0, m);
          if (!QMC.hasValue(check)) {
            recursions = -1;
            recursionNotified = false;
            check(check);
          }
        }
      }
    }
    
    int reg = QMC.length() - r;
    Log.info(reg + " " + QMC.nameFull + " Values Guessed");
    Log.debug(noValue.length + " Items/Blocks have no value");
  }
  
  public static double check(ItemStack itemstack) {
    if (QMC.hasValue(itemstack)) {
      return QMC.getQMC(itemstack);
    }
    recursions++;
    int ore = OreDictionary.getOreID(itemstack);
    // Stops itemstacks from being checked that have alreading been checked and returned no value;
    for (ItemStack stack : noValue) {
      if (itemstack.itemID == stack.itemID && itemstack.getItemDamage() == stack.getItemDamage() || (ore != -1 && ore == OreDictionary.getOreID(stack))) {
        return -1;
      }
    }
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
    double v = checkClasses(itemstack);
    if (v > -1) {
      ItemStack toAdd = itemstack.copy();
      QMC.add(toAdd, v);
    } else {
      ItemStack[] tmp = new ItemStack[noValue.length + 1];
      System.arraycopy(noValue, 0, tmp, 0, noValue.length);
      noValue = tmp;
      noValue[noValue.length - 1] = itemstack;
    }
    return v;
  }
  
  public static double checkClasses(ItemStack itemstack) {
    double d = 0;
    for (Class<?> c : classes) {
      try {
        Method m = c.getMethod("check", ItemStack.class);
        Object r = m.invoke(null, itemstack);
        double value = Double.parseDouble(r.toString());
        if (value > 0) {
          d = d + value;
          break;
        }
      } catch (InvocationTargetException e) {
        e.getTargetException().printStackTrace();
      } catch (Exception e) {
        if (OpenExchange.debug) {
          e.printStackTrace();
        }
      }
    }
    if (d == 0) {
      return -1;
    } else {
      return d;
    }
  }
  
  private static void increaseClasses() {
    Class<?>[] tmp = new Class<?>[classes.length + 1];
    System.arraycopy(classes, 0, tmp, 0, classes.length);
    classes = tmp;
  }
}
