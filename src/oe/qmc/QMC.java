package oe.qmc;

import java.io.File;
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
import oe.OpenExchange;
import oe.api.QMCHandler;
import oe.core.Log;
import oe.core.util.Util;
import oe.qmc.file.CustomQMCValuesReader;
import oe.qmc.file.QMCCustomAction;
import oe.qmc.guess.Guess;

public class QMC {
  
  // Name
  public static final String name = "QMC";
  public static final String nameFull = "Quantum Matter Currency";
  // Formatter
  public static final DecimalFormat formatter = new DecimalFormat("0.00");
  // Handlers
  public static HashMap<Class<?>, QMCHandler> handlers = new HashMap<Class<?>, QMCHandler>();
  public static List<QMCHandler> handlersList = new ArrayList<QMCHandler>();
  // Built in handlers
  public static final QMCItemStack itemstackHandler = new QMCItemStack();
  public static final QMCFluid fluidHandler = new QMCFluid();
  // Custom Actions
  public static List<QMCCustomAction> actions = new ArrayList<QMCCustomAction>();
  // QMCSave
  private static final File saveFile = new File(OpenExchange.configdir.toString() + "\\..\\QMCSave.dat");
  private static QMCSave save;
  private static NBTTagCompound loadedSnapshot = new NBTTagCompound();
  
  public static void load() {
    actions = CustomQMCValuesReader.actions();
    QMCSave saveFromFile = QMCSave.readFromFile(saveFile);
    if (saveFromFile != null && saveFromFile.checkMods() && saveFromFile.checkActions()) {
      save = saveFromFile;
      Log.info("Using QMCSave from " + save.getTimeStamp());
    } else {
      if (saveFromFile == null) {
        regenerateSave("No QMCSave file");
      } else if (!saveFromFile.checkMods()) {
        regenerateSave("Mods have changed");
      } else if (!saveFromFile.checkActions()) {
        regenerateSave("Custom Actions have changed");
      } else {
        regenerateSave("UNKNOWN");
      }
    }
  }
  
  public static QMCSave getSave() {
    return save;
  }
  
  public static void regenerateSave(String reason) {
    Log.info("Building QMC database - " + reason);
    Log.info("Scheduled Guessing");
    for (QMCHandler h : handlersList) {
      h.restoreSnapshot(new NBTTagCompound()); // Try to wipe
    }
    for (QMCCustomAction action : actions) {
      Log.debug("Executing Custom Action - " + action);
      action.execute();
    }
    QMCValues.load();
  }
  
  public static void serverStarted() {
    if (save == null) {
      Log.debug("Guessing QMC Values");
      Guess.load();
      save = new QMCSave();
    } else if (!loadedSnapshot.equals(save.QMCSnapshot)) {
      restoreSnapshot(save.QMCSnapshot);
    }
    save.writeToFile(saveFile);
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
    loadedSnapshot = nbt;
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
