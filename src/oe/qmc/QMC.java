package oe.qmc;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import oe.lib.Log;
import oe.lib.helper.ConfigHelper;
import oe.qmc.file.CustomQMCValuesReader;

public class QMC {
  
  public static class Data {
    public Class<?> c;
    public Class<?>[] canHandle;
    
    public Data(Class<?> C, Class<?>[] CanHandle) {
      this.c = C;
      this.canHandle = CanHandle;
    }
  }
  
  public static String name = "QMC";
  public static String nameFull = "Quantum Matter Currency";
  public static DecimalFormat formatter = new DecimalFormat("0.00");
  public static Data[] data = new Data[0];
  
  public static void load() {
    ConfigHelper.load();
    name = ConfigHelper.other("QMC", "Name", "QMC");
    nameFull = ConfigHelper.other("QMC", "Stands For", "Quantum Matter Currency");
    ConfigHelper.save();
    CustomQMCValuesReader.read();
    NormalQMCValues.load();
  }
  
  public static void loadHandlers() {
    addHandler(QMCItemStack.class, new Class<?>[] { ItemStack.class, String.class, Block.class, Item.class });
  }
  
  public static boolean add(Object o, double Value) {
    Double d = new Double(Value);
    Class<?> c = getHandler(o);
    if (c == null) {
      return false;
    }
    try {
      Method m = c.getDeclaredMethod("add", new Class<?>[] { Object.class, Double.class });
      Object r = m.invoke(null, new Object[] { o, d });
      if (r != null && r instanceof Boolean) {
        return (Boolean) r;
      }
      return true;
    } catch (Exception e) {
    }
    return false;
  }
  
  public static double getQMC(Object o) {
    Class<?> c = getHandler(o);
    if (c == null) {
      return -1;
    }
    try {
      Method m = c.getDeclaredMethod("getQMC", Object.class);
      Object r = m.invoke(null, o);
      if (r != null && r instanceof Double) {
        return (Double) r;
      }
    } catch (Exception e) {
    }
    return -1;
  }
  
  public static boolean hasQMC(Object o) {
    return getQMC(o) > 0;
  }
  
  public static boolean blacklist(Object o) {
    Class<?> c = getHandler(o);
    if (c == null) {
      return false;
    }
    try {
      Method m = c.getDeclaredMethod("blacklist", Object.class);
      Object r = m.invoke(null, o);
      if (r != null && r instanceof Boolean) {
        return (Boolean) r;
      }
      return true;
    } catch (Exception e) {
    }
    return false;
  }
  
  public static int length() {
    int l = 0;
    for (int i = 0; i < data.length; i++) {
      Class<?> c = data[i].c;
      try {
        Method m = c.getDeclaredMethod("length");
        Object r = m.invoke(null);
        if (r != null && r instanceof Integer) {
          l = l + (Integer) r;
        }
      } catch (Exception e) {
      }
    }
    return l;
  }
  
  public static NBTTagCompound snapshot(String SnapShotName) {
    NBTTagCompound nbt = new NBTTagCompound();
    for (int i = 0; i < data.length; i++) {
      Class<?> c = data[i].c;
      try {
        Method m = c.getDeclaredMethod("snapshot");
        Object r = m.invoke(null);
        if (r != null && r instanceof NBTTagCompound) {
          nbt.setCompoundTag(c.getSimpleName(), (NBTTagCompound) r);
        }
      } catch (Exception e) {
      }
    }
    nbt.setString("Name", SnapShotName);
    return nbt;
  }
  
  public static void restoreSnapshot(NBTTagCompound nbt) {
    if (nbt == null) {
      return;
    }
    Log.debug("Restoring " + nbt.getString("Name") + " Snapshot - Currently there are " + length() + " " + name + " values");
    for (int i = 0; i < data.length; i++) {
      Class<?> c = data[i].c;
      NBTTagCompound snapshot = nbt.getCompoundTag(c.getSimpleName());
      try {
        Method m = c.getDeclaredMethod("restoreSnapshot", NBTTagCompound.class);
        m.invoke(null, snapshot);
      } catch (Exception e) {
      }
    }
    Log.debug("After restoring there are " + length() + " " + name + " values");
  }
  
  private static Class<?> getHandler(Object o) {
    for (Data d : data) {
      for (Class<?> c : d.canHandle) {
        if (c.isInstance(o)) {
          return d.c;
        }
      }
    }
    return null;
  }
  
  public static void addHandler(Class<?> handler, Class<?>[] classesCanHandle) {
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
