package oe.qmc;

import java.text.DecimalFormat;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import oe.api.QMCHandler;
import oe.lib.Log;
import oe.lib.util.Util;
import oe.qmc.file.CustomQMCValuesReader;

public class QMC {
  
  public static class Data {
    public QMCHandler handler;
    public Class<?>[] canHandle;
    
    public Data(QMCHandler Handler, Class<?>[] CanHandle) {
      this.handler = Handler;
      this.canHandle = CanHandle;
    }
  }
  
  public static final String name = "QMC";
  public static final String nameFull = "Quantum Matter Currency";
  public static DecimalFormat formatter = new DecimalFormat("0.00");
  public static Data[] data = new Data[0];
  
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
    for (int i = 0; i < data.length; i++) {
      QMCHandler h = data[i].handler;
      l += h.length();
    }
    return l;
  }
  
  public static NBTTagCompound snapshot(String SnapShotName) {
    Log.info("Taking " + SnapShotName + " " + name + " Snapshot");
    NBTTagCompound nbt = new NBTTagCompound();
    for (int i = 0; i < data.length; i++) {
      QMCHandler h = data[i].handler;
      NBTTagCompound snapshot = h.snapshot();
      if (snapshot != null) {
        nbt.setCompoundTag(h.getClass().getSimpleName(), snapshot);
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
    for (int i = 0; i < data.length; i++) {
      QMCHandler h = data[i].handler;
      h.restoreSnapshot(nbt.getCompoundTag(h.getClass().getSimpleName()));
    }
    Log.debug("After restoring there are " + length() + " " + name + " values");
  }
  
  public static QMCHandler getHandler(Object o) {
    for (Data d : data) {
      for (Class<?> c : d.canHandle) {
        if (c.isInstance(o)) {
          return d.handler;
        }
      }
    }
    return null;
  }
  
  public static void addHandler(QMCHandler handler, Class<?>[] classesCanHandle) {
    Data[] tmp = new Data[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
    data[data.length - 1] = new Data(handler, classesCanHandle);
  }
  
  public static NBTTagCompound postInitSnapshot = new NBTTagCompound();
  
  public static void takePostInitSnapshot() {
    postInitSnapshot = snapshot("Post-Init");
  }
}
