package oe.qmc;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import oe.OpenExchange;
import oe.api.QMCHandler;
import oe.core.Log;
import oe.core.util.ConfigUtil;
import oe.core.util.Util;
import oe.qmc.file.CustomActionReader;
import oe.qmc.file.QMCCustomAction;
import oe.qmc.guess.Guess;

public final class QMC {
  
  public static interface ID {
    public NBTTagCompound toNBT();
    
    public Class<? extends QMCHandler> getQMCHandlerClass();
  }
  
  public final static class IDWrapper {
    public final ID id;
    public final QMCHandler handler;
    
    public IDWrapper(ID id, QMCHandler handler) {
      this.id = id;
      this.handler = handler;
    }
    
    public IDWrapper(NBTTagCompound nbt) {
      this.handler = QMC.getHandlerFromString(nbt.getString("handler"));
      this.id = handler.getIDFromNBT(nbt.getCompoundTag("id"));
    }
    
    public NBTTagCompound getNBT() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setTag("id", id.toNBT());
      nbt.setString("handler", handler.getClass().getSimpleName());
      return nbt;
    }
  }
  
  public static interface Data {
    public NBTTagCompound toNBT();
  }
  
  public static class DataNormal implements Data {
    
    public final double qmc;
    
    public DataNormal(double qmc) {
      this.qmc = qmc;
    }
    
    @Override
    public NBTTagCompound toNBT() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setDouble("qmc", qmc);
      return nbt;
    }
    
    public static Data getData(NBTTagCompound nbt) {
      return new DataNormal(nbt.getDouble("qmc"));
    }
    
  }
  
  // Name
  public static final String name = "QMC";
  public static final String nameFull = "Quantum Matter Currency";
  // Database
  private static HashMap<IDWrapper, Data> database = new HashMap<IDWrapper, Data>();
  private static ArrayList<ID> blacklist = new ArrayList<ID>();
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
      ID id = h.getID(o);
      if (id != null && !blacklist.contains(id)) {
        database.put(new IDWrapper(id, h), h.getData(qmc, id, o));
        return true;
      }
    }
    return false;
  }
  
  public static double getQMC(Object o) {
    QMCHandler h = getHandler(o);
    if (h != null) {
      ID id = h.getID(o);
      Data data = database.get(id);
      if (id != null && data != null) {
        return h.getQMC(data, id, o);
      }
    }
    return 0;
  }
  
  public static boolean hasQMC(Object o) {
    QMCHandler h = getHandler(o);
    if (h != null) {
      ID id = h.getID(o);
      return database.containsKey(id);
    }
    return false;
  }
  
  public static boolean blacklist(Object o) {
    QMCHandler h = getHandler(o);
    if (h != null) {
      ID id = h.getID(o);
      if (id != null) {
        blacklist.add(id);
        return true;
      }
    }
    return false;
  }
  
  public static boolean isBlacklisted(Object o) {
    QMCHandler h = getHandler(o);
    if (h != null) {
      ID id = h.getID(o);
      if (id != null) {
        return blacklist.contains(id);
      }
    }
    return false;
  }
  
  public static int size() {
    return database.size();
  }
  
  public static NBTTagCompound snapshot(String snapshotName) {
    Log.info("Taking " + snapshotName + " " + name + " Snapshot");
    NBTTagCompound nbt = new NBTTagCompound();
    NBTTagList list = new NBTTagList();
    for (IDWrapper id : database.keySet()) {
      if (id == null) {
        continue;
      }
      NBTTagCompound snapshot = new NBTTagCompound();
      snapshot.setTag("data", database.get(id).toNBT());
      snapshot.setTag("id", id.getNBT());
      list.appendTag(snapshot);
    }
    nbt.setTag("data", list);
    nbt.setString("name", snapshotName);
    nbt.setString("date", Util.getDate());
    nbt.setString("time", Util.getTime());
    return nbt;
  }
  
  public static void restoreSnapshot(NBTTagCompound nbt) {
    if (nbt == null) {
      return;
    }
    Log.info("Restoring " + nbt.getString("name") + " " + name + " Snapshot");
    Log.debug("Currently there are " + size() + " " + name + " values");
    NBTTagList list = nbt.getTagList("data", 10);
    for (int i = 0; i < list.tagCount(); i++) {
      NBTTagCompound snapshot = list.getCompoundTagAt(i);
      IDWrapper id = new IDWrapper(snapshot.getCompoundTag("id"));
      database.put(id, id.handler.getData(snapshot.getCompoundTag("data")));
    }
    Log.debug("After restoring there are " + size() + " " + name + " values");
    loadedSnapshot = nbt;
  }
  
  public static void process(QMCHandler handler) {
    HashMap<IDWrapper, Data> map = new HashMap<IDWrapper, Data>();
    for (Object o : database.keySet().toArray()) {
      IDWrapper id = (IDWrapper) o;
      if (id == null) {
        continue;
      }
      if (id.handler == handler) {
        map.put(id, database.get(id));
        database.remove(id);
      }
    }
    database.putAll(handler.process(map));
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
  
  public static QMCHandler getHandlerFromString(String s) {
    for (QMCHandler handler : handlers) {
      if (handler.getClass().getSimpleName().equals(s)) {
        return handler;
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
