package oe.qmc;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import oe.OpenExchange;
import oe.api.QMCHandler;
import oe.api.QMCProcessor;
import oe.core.Log;
import oe.core.util.ConfigUtil;
import oe.core.util.Util;
import oe.qmc.file.CustomActionReader;
import oe.qmc.file.QMCCustomAction;
import oe.qmc.guess.Guess;

public final class QMC {
  
  // Name
  public static final String name = "QMC";
  public static final String nameFull = "Quantum Matter Currency";
  // Database (HashMap of getID() to decode())
  private static HashMap<NBTTagCompound, NBTTagCompound> data = new HashMap<NBTTagCompound, NBTTagCompound>();
  // Formatter
  public static final DecimalFormat formatter = new DecimalFormat("0.00");
  // Handlers
  public static List<QMCHandler> handlers = new ArrayList<QMCHandler>();
  // Custom Actions
  public static List<QMCCustomAction> actions = new ArrayList<QMCCustomAction>();
  // QMCSave
  private static final File saveFile = new File(OpenExchange.configdir.toString() + "\\..\\QMCSave.dat");
  private static QMCSave save;
  private static NBTTagCompound loadedSnapshot = new NBTTagCompound();
  private static boolean QMCSaveEnabled = true;
  
  // TODO: Itemstacks criteria (containing fluids), Guessing containing itemstacks in something with
  // hashcode
  public static void load() {
    actions = CustomActionReader.actions();
    QMCSave saveFromFile = QMCSave.readFromFile(saveFile);
    QMCSaveEnabled = ConfigUtil.other("QMC", "Enable QMCSave", true);
    if (QMCSaveEnabled && saveFromFile != null && saveFromFile.checkMods() && saveFromFile.checkActions() && saveFromFile.checkGuessHandlers()) {
      save = saveFromFile;
      Log.info("Using QMCSave from " + save.getTimeStamp());
    } else {
      if (!QMCSaveEnabled) {
        regenerateSave("QMCSave disabled");
      } else if (saveFromFile == null) {
        regenerateSave("No QMCSave file");
      } else if (!saveFromFile.checkMods()) {
        regenerateSave("Mods have changed");
      } else if (!saveFromFile.checkActions()) {
        regenerateSave("Custom Actions have changed");
      } else if (!saveFromFile.checkGuessHandlers()) {
        regenerateSave("Guess Handlers have changed");
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
    save = null;
    saveFile.delete();
    for (QMCCustomAction action : actions) {
      Log.debug("Executing Custom Action - " + action);
      action.execute();
    }
    QMCValues.load();
  }
  
  public static void serverStarted() {
    if (save == null) {
      Log.info("Guessing QMC Values");
      Guess.load();
      save = new QMCSave();
    } else if (!loadedSnapshot.equals(save.QMCSnapshot)) {
      restoreSnapshot(save.QMCSnapshot);
    }
    if (!save.saved && QMCSaveEnabled) {
      save.writeToFile(saveFile);
    }
  }
  
  public static void loadHandlers() {
    addHandler(new QMCItemStack());
    addHandler(new QMCFluid());
  }
  
  public static boolean add(Object o, double qmc) {
    QMCHandler h = getHandler(o);
    if (h != null) {
      NBTTagCompound id = getID(o, h);
      if (id != null) {
        data.put(id, h.encode(o, qmc));
        return true;
      }
    }
    return false;
  }
  
  public static double getQMC(Object o) {
    QMCHandler h = getHandler(o);
    if (h != null) {
      NBTTagCompound id = getID(o, h);
      NBTTagCompound nbt = data.get(id);
      if (id != null && nbt != null) {
        return h.decode(o, nbt);
      }
    }
    return 0;
  }
  
  public static boolean hasQMC(Object o) {
    return getQMC(o) > 0;
  }
  
  public static boolean blacklist(Object o) {
    QMCHandler h = getHandler(o);
    if (h != null) {
      NBTTagCompound id = getID(o, h);
      if (id != null) {
        data.put(id, h.blacklist(o));
        return true;
      }
    }
    return false;
  }
  
  public static boolean isBlacklisted(Object o) {
    QMCHandler h = getHandler(o);
    if (h != null) {
      NBTTagCompound id = getID(o, h);
      NBTTagCompound nbt = data.get(id);
      if (id != null && nbt != null) {
        return h.isBlacklisted(o, nbt);
      }
    }
    return false;
  }
  
  public static int length() {
    return data.size();
  }
  
  public static NBTTagCompound snapshot(String SnapshotName) {
    Log.info("Taking " + SnapshotName + " " + name + " Snapshot");
    NBTTagCompound nbt = new NBTTagCompound();
    int i = 0;
    for (NBTTagCompound id : data.keySet()) {
      if (id == null) {
        continue;
      }
      NBTTagCompound snapshot = new NBTTagCompound();
      snapshot.setCompoundTag("data", (NBTTagCompound) data.get(id).copy());
      snapshot.setCompoundTag("ID", (NBTTagCompound) id.copy());
      i++;
      nbt.setCompoundTag(i + "", snapshot);
    }
    nbt.setInteger("length", i);
    nbt.setString("name", SnapshotName);
    nbt.setString("date", Util.getDate());
    nbt.setString("time", Util.getTime());
    return nbt;
  }
  
  public static void restoreSnapshot(NBTTagCompound nbt) {
    if (nbt == null) {
      return;
    }
    Log.info("Restoring " + nbt.getString("name") + " " + name + " Snapshot");
    Log.debug("Currently there are " + length() + " " + name + " values");
    for (int i = 1; i <= nbt.getInteger("length"); i++) {
      NBTTagCompound snapshot = nbt.getCompoundTag(i + "");
      data.put(snapshot.getCompoundTag("ID"), snapshot.getCompoundTag("data"));
    }
    Log.debug("After restoring there are " + length() + " " + name + " values");
    loadedSnapshot = nbt;
  }
  
  public static void process(QMCProcessor processor) {
    for (Object o : data.keySet().toArray()) {
      NBTTagCompound id = (NBTTagCompound) o;
      if (id == null) {
        continue;
      }
      QMCProcessor.Data d = new QMCProcessor.Data(id, data.get(id));
      processor.process(d);
      if (d.changed()) {
        data.remove(id);
        d.setID((NBTTagCompound) d.getID().setName(id.getName()));
        data.put(d.getID(), d.getData());
      }
    }
  }
  
  public static NBTTagCompound getID(Object o, QMCHandler handler) {
    NBTTagCompound nbt = handler.getID(o);
    nbt.setName(handler.getClass().getSimpleName());
    return nbt;
  }
  
  public static QMCHandler getHandler(Object o) {
    for (QMCHandler handler : handlers) {
      for (Class<?> c : handler.handles) {
        if (c.isInstance(o)) {
          return handler;
        }
      }
    }
    return null;
  }
  
  public static void addHandler(QMCHandler handler) {
    if (handler == null) {
      return;
    }
    handlers.add(handler);
  }
}
