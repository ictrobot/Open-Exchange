package oe.qmc.guess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oe.Log;
import oe.OpenExchange;
import oe.api.OE_API;
import oe.qmc.QMC;

public class Guess {
  
  public static Class<?>[] classes = new Class[0];
  
  // To Stop recursions
  private static int recursions = 0;
  private static int recursionLimit = 50;
  private static boolean recursionNotified = false;
  private static ItemStack stackCheck;
  
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
    checkLoop();
  }
  
  private static void checkLoop() {
    for (int i = 0; i < Block.blocksList.length; i++) {
      if (Block.blocksList[i] != null) {
        if (!Block.blocksList[i].getUnlocalizedName().contains("tile.ForgeFiller")) {
          List<ItemStack> subBlocks = new ArrayList<ItemStack>();
          Block.blocksList[i].getSubBlocks(0, Block.blocksList[i].getCreativeTabToDisplayOn(), subBlocks);
          for (int m = 0; m < subBlocks.size(); m++) {
            ItemStack stack = subBlocks.get(m);
            stack.itemID = i;
            if (!QMC.hasValue(stack)) {
              recursions = -1;
              recursionNotified = false;
              check(stack);
            }
          }
        }
      }
    }
    for (int i = 0; i < Item.itemsList.length; i++) {
      if (Item.itemsList[i] != null) {
        List<ItemStack> subItems = new ArrayList<ItemStack>();
        Item.itemsList[i].getSubItems(0, Item.itemsList[i].getCreativeTab(), subItems);
        for (int m = 0; m < subItems.size(); m++) {
          ItemStack stack = subItems.get(m);
          stack.itemID = i;
          if (!QMC.hasValue(stack)) {
            recursions = -1;
            recursionNotified = false;
            check(stack);
          }
        }
      }
    }
  }
  
  public static double check(ItemStack itemstack) {
    recursions++;
    if (recursions == 0) {
      stackCheck = itemstack;
    }
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
      if (itemstack.isItemStackDamageable()) {
        toAdd.setItemDamage(0);
        QMC.addMeta(toAdd, v);
      }
      if (itemstack.itemID == 35) {
        System.out.println();
      }
      List<ItemStack> subItems = new ArrayList<ItemStack>();
      toAdd.getItem().getSubItems(toAdd.getItemDamage(), toAdd.getItem().getCreativeTab(), subItems);
      if (subItems.size() > 1) {
        QMC.addMeta(toAdd, v);
      }
      QMC.add(toAdd, v);
    }
    return v;
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
