package oe.qmc.guess;

import java.lang.reflect.Method;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oe.OpenExchange;
import oe.api.OE_API;
import oe.qmc.QMC;

public class Guess {
  
  public static Class<?>[] classes = new Class[0];
  
  public static boolean add(Class<?> c) {
    if (OE_API.isOEGuessable(c)) {
      increaseClasses();
      classes[classes.length - 1] = c;
      return true;
      
    }
    return false;
  }
  
  public static void load() {
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
    check();
  }
  
  private static void check() {
    for (int i = 0; i < Block.blocksList.length; i++) {
      if (Block.blocksList[i] != null) {
        if (!Block.blocksList[i].getUnlocalizedName().contains("tile.ForgeFiller")) {
          ItemStack stack = new ItemStack(Block.blocksList[i]);
          if (!QMC.hasValue(stack)) {
            double d = checkClasses(stack);
            if (d > 0) {
              QMC.add(Block.blocksList[i], stack, d);
            }
          }
        }
      }
    }
    for (int i = 0; i < Item.itemsList.length; i++) {
      if (Item.itemsList[i] != null) {
        ItemStack stack = new ItemStack(Item.itemsList[i]);
        if (!QMC.hasValue(stack)) {
          double d = checkClasses(stack);
          if (d > 0) {
            QMC.add(stack, d);
          }
        }
      }
    }
  }
  
  public static double checkClasses(ItemStack itemstack) {
    if (QMC.hasValue(itemstack)) {
      return QMC.getQMC(itemstack);
    }
    double d = 0;
    for (Class<?> c : classes) {
      try {
        Method m = c.getMethod("check", ItemStack.class);
        Object r = m.invoke(null, itemstack);
        double value = Double.parseDouble(r.toString());
        if (value > 0) {
          d = d + value;
        }
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
