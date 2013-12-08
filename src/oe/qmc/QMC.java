package oe.qmc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import oe.api.QMCHandler;
import oe.core.Log;
import oe.core.util.Util;
import oe.qmc.file.CustomQMCValuesReader;

public class QMC {
  
  // Name
  public static final String name = "QMC";
  public static final String nameFull = "Quantum Matter Currency";
  // Formatter
  public static DecimalFormat formatter = new DecimalFormat("0.00");
  // Handlers
  public static HashMap<Class<?>, QMCHandler> handlers = new HashMap<Class<?>, QMCHandler>();
  public static List<QMCHandler> handlersList = new ArrayList<QMCHandler>();
  // Built in handlers
  public static QMCItemStack itemstackHandler = new QMCItemStack();
  public static QMCFluid fluidHandler = new QMCFluid();
  
  public static void load() {
    CustomQMCValuesReader.read();
    QMCValues.load();
  }
  
  public static void loadHandlers() {
    addHandler(itemstackHandler, new Class<?>[] { ItemStack.class, String.class, Block.class, Item.class });
    addHandler(fluidHandler, new Class<?>[] { Fluid.class, FluidStack.class, QMCFluid.FluidItemStack.class });
  }
  
  public static boolean add(Object o, double Value) {
    QMCHandler h = getHandler(o);
    if (h == null) {
      return false;
    }
    return h.add(o, Value);
  }
  
  public static double getQMC(Object o) {
    QMCHandler h = getHandler(o);
    if (h == null) {
      return -1;
    }
    return h.getQMC(o);
  }
  
  public static boolean hasQMC(Object o) {
    return getQMC(o) > 0;
  }
  
  public static boolean blacklist(Object o) {
    QMCHandler h = getHandler(o);
    if (h == null) {
      return false;
    }
    return h.blacklist(o);
  }
  
  public static boolean isBlacklisted(Object o) {
    QMCHandler h = getHandler(o);
    if (h == null) {
      return false;
    }
    return h.isBlacklisted(o);
  }
  
  public static int length() {
    int l = 0;
    for (QMCHandler handler : handlersList) {
      l += handler.length();
    }
    return l;
  }
  
  public static NBTTagCompound snapshot(String SnapShotName) {
    Log.info("Taking " + SnapShotName + " " + name + " Snapshot");
    NBTTagCompound nbt = new NBTTagCompound();
    for (QMCHandler handler : handlersList) {
      NBTTagCompound snapshot = handler.snapshot();
      if (snapshot != null && !snapshot.hasNoTags()) {
        nbt.setCompoundTag(handler.getClass().getSimpleName(), snapshot);
      }
    }
    nbt.setString("Name", SnapShotName);
    nbt.setString("Date", Util.getDate());
    nbt.setString("Time", Util.getTime());
    return nbt;
  }
  
  public static void restoreSnapshot(NBTTagCompound nbt) {
    if (nbt == null) {
      return;
    }
    Log.info("Restoring " + nbt.getString("Name") + " " + name + " Snapshot");
    Log.debug("Currently there are " + length() + " " + name + " values");
    for (QMCHandler handler : handlersList) {
      handler.restoreSnapshot(nbt.getCompoundTag(handler.getClass().getSimpleName()));
    }
    Log.debug("After restoring there are " + length() + " " + name + " values");
  }
  
  public static NBTTagCompound postInitSnapshot = new NBTTagCompound();
  
  public static void takePostInitSnapshot() {
    postInitSnapshot = snapshot("Post-Init");
  }
  
  public static QMCHandler getHandler(Object o) {
    for (Class<?> c : handlers.keySet()) {
      if (c.isInstance(o)) {
        return handlers.get(c);
      }
    }
    return null;
  }
  
  public static void addHandler(QMCHandler handler, Class<?> canHandle) {
    addHandler(handler, new Class<?>[] { canHandle });
  }
  
  public static void addHandler(QMCHandler handler, Class<?>[] canHandle) {
    if (handler == null) {
      return;
    }
    if (!handlersList.contains(handler)) {
      handlersList.add(handler);
    }
    for (Class<?> c : canHandle) {
      if (c != null) {
        handlers.put(c, handler);
      }
    }
  }
}
