package oe.lib.misc;

import java.io.File;
import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import oe.OpenExchange;
import oe.api.lib.Location;
import oe.lib.util.ConfigUtil;

public class RemoteDrillData {
  public static double mineCost = 256;
  public static double moveCost = 8096;
  public static int range = 8;
  public static int rangeX2 = range * 2;
  public static int delayTicks = 50;
  private static NBTTagCompound nbt = new NBTTagCompound();
  private static String path;
  
  public static void init() {
    ConfigUtil.load();
    mineCost = ConfigUtil.other("block", "DrillRemote Mine Block Cost", 256.0);
    moveCost = ConfigUtil.other("block", "DrillRemote Move Block Cost", 8096.0);
    delayTicks = ConfigUtil.other("block", "DrillRemote Tick Delay", 50);
    range = ConfigUtil.other("block", "DrillRemote Range", 8);
    ConfigUtil.save();
    rangeX2 = range * 2;
  }
  
  public static void loadNBT() {
    path = OpenExchange.configdir.toString() + "\\." + MinecraftServer.getServer().worldServerForDimension(0).getChunkSaveLocation().toString();
    path = path + "\\OERemoteDrillData.dat";
    File file = new File(path);
    try {
      if (!file.exists()) {
        saveNBT();
      }
      nbt = CompressedStreamTools.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static void saveNBT() {
    File file = new File(path);
    try {
      CompressedStreamTools.write(nbt, file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static int getNewID() {
    int d = 0;
    while (true) {
      d++;
      if (!nbt.hasKey(d + "")) {
        nbt.setCompoundTag(d + "", new NBTTagCompound());
        return d;
      }
    }
  }
  
  public static boolean isReady(int id) {
    return !nbt.getCompoundTag(id + "").hasKey("items");
  }
  
  public static void addItemStack(int id, ItemStack toAdd) {
    NBTTagCompound tag = nbt.getCompoundTag(id + "");
    if (!tag.hasKey("items")) {
      tag.setCompoundTag("items", new NBTTagCompound());
      tag.getCompoundTag("items").setInteger("itemsNum", 0);
    }
    int num = tag.getCompoundTag("items").getInteger("itemsNum") + 1;
    tag.getCompoundTag("items").setInteger("itemsNum", num);
    NBTTagCompound item = new NBTTagCompound();
    toAdd.writeToNBT(item);
    tag.getCompoundTag("items").setCompoundTag(num + "", item);
  }
  
  public static ItemStack[] getItemStacks(int id) {
    if (!nbt.getCompoundTag(id + "").hasKey("items")) {
      return new ItemStack[0];
    }
    ItemStack[] toReturn = new ItemStack[nbt.getCompoundTag(id + "").getCompoundTag("items").getInteger("itemsNum")];
    for (int i = 1; i <= nbt.getCompoundTag(id + "").getCompoundTag("items").getInteger("itemsNum"); i++) {
      toReturn[i - 1] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(id + "").getCompoundTag("items").getCompoundTag(i + ""));
    }
    nbt.getCompoundTag(id + "").removeTag("items");
    return toReturn;
  }
  
  public static double getQMC(int id) {
    return nbt.getCompoundTag(id + "").getDouble("QMC");
  }
  
  public static double setQMC(int id, double qmc) {
    nbt.getCompoundTag(id + "").setDouble("QMC", qmc);
    return getQMC(id);
  }
  
  public static void setLocationDrill(int id, Location loc) {
    nbt.getCompoundTag(id + "").setDouble("drillX", loc.x);
    nbt.getCompoundTag(id + "").setDouble("drillY", loc.y);
    nbt.getCompoundTag(id + "").setDouble("drillZ", loc.z);
    nbt.getCompoundTag(id + "").setInteger("drillDim", loc.worldID);
  }
  
  public static Location getLocationDrill(int id) {
    double x = nbt.getCompoundTag(id + "").getDouble("drillX");
    double y = nbt.getCompoundTag(id + "").getDouble("drillY");
    double z = nbt.getCompoundTag(id + "").getDouble("drillZ");
    int world = nbt.getCompoundTag(id + "").getInteger("drillDim");
    return new Location(x, y, z, world);
  }
}
