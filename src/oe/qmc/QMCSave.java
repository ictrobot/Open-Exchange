package oe.qmc;

import java.io.File;
import java.util.HashMap;
import oe.core.Debug;
import oe.core.util.Util;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class QMCSave {
  
  public final HashMap<String, String> mods;
  public final NBTTagCompound QMCSnapshot;
  private final NBTTagCompound save;
  public static final HashMap<String, String> loadedMods = getLoadedModData();
  
  public QMCSave(NBTTagCompound save) {
    this.save = save;
    this.mods = restoreModData(save.getCompoundTag("mods"));
    this.QMCSnapshot = save.getCompoundTag("snapshot");
  }
  
  public QMCSave() {
    this.mods = getLoadedModData();
    this.QMCSnapshot = QMC.snapshot("QMCSave");
    this.save = new NBTTagCompound();
    save.setCompoundTag("mods", getNBTModData(mods));
    save.setCompoundTag("snapshot", QMCSnapshot);
    save.setString("time", Util.getTime() + " " + Util.getDate());
    
  }
  
  public static HashMap<String, String> getLoadedModData() { // ModID, Version
    HashMap<String, String> data = new HashMap<String, String>();
    for (ModContainer mod : Loader.instance().getModList()) {
      data.put(mod.getModId(), mod.getVersion());
    }
    return data;
  }
  
  public static NBTTagCompound getNBTLoadedModData() {
    return getNBTModData(getLoadedModData());
  }
  
  public static NBTTagCompound getNBTModData(HashMap<String, String> data) {
    NBTTagCompound nbt = new NBTTagCompound();
    int i = 0;
    for (String modID : data.keySet()) {
      i++;
      NBTTagCompound mod = new NBTTagCompound();
      mod.setString("modID", modID);
      mod.setString("version", data.get(modID));
      nbt.setCompoundTag(i + "", mod);
    }
    nbt.setInteger("number", data.size());
    return nbt;
  }
  
  public static HashMap<String, String> restoreModData(NBTTagCompound nbt) {
    HashMap<String, String> data = new HashMap<String, String>();
    for (int i = 1; i <= nbt.getInteger("number"); i++) {
      NBTTagCompound mod = nbt.getCompoundTag(i + "");
      data.put(mod.getString("modID"), mod.getString("version"));
    }
    return data;
  }
  
  public boolean isValid() {
    return mods.equals(loadedMods);
  }
  
  public String getTimeStamp() {
    return save.getString("time");
  }
  
  public boolean writeToFile(File file) {
    try {
      CompressedStreamTools.write(save, file);
      return true;
    } catch (Exception e) {
      Debug.handleException(e);
    }
    return false;
  }
  
  public static QMCSave readFromFile(File file) {
    try {
      NBTTagCompound nbt = CompressedStreamTools.read(file);
      return new QMCSave(nbt);
    } catch (Exception e) {
      Debug.handleException(e);
    }
    return null;
  }
}
