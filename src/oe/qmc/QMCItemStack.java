package oe.qmc;

import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.qmc.Data;
import oe.api.qmc.ID;
import oe.api.qmc.QMCHandler;
import oe.api.qmc.QMCIDWrapper;
import oe.api.qmc.SimpleData;
import oe.api.qmc.id.ItemStackID;
import oe.api.qmc.id.OreDictionaryID;
import oe.core.util.ItemStackUtil;

public class QMCItemStack extends QMCHandler {
  
  public static class ItemStackData extends SimpleData {
    
    public final boolean metaSensitive;
    public final int maxMeta;
    
    public ItemStackData(double qmc) {
      this(qmc, false, -1);
    }
    
    public ItemStackData(double qmc, boolean metaSensitive, int maxMeta) {
      super(qmc);
      this.metaSensitive = metaSensitive;
      this.maxMeta = maxMeta;
    }
    
    @Override
    public NBTTagCompound toNBT() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setTag("qmc", super.toNBT());
      nbt.setBoolean("meta", metaSensitive);
      nbt.setInteger("maxMeta", maxMeta);
      return nbt;
    }
  }
  
  public static QMCItemStack instance;
  
  public QMCItemStack() {
    super(new Class<?>[] { ItemStack.class, String.class });
    QMCItemStack.instance = this;
  }
  
  @Override
  public ID getID(Object o) {
    if (o instanceof String) {
      return new OreDictionaryID((String) o);
    } else {
      if (OreDictionary.getOreID((ItemStack) o) != -1) {
        return new OreDictionaryID(OreDictionary.getOreName(OreDictionary.getOreID((ItemStack) o)));
      }
      return new ItemStackID((ItemStack) o);
    }
  }
  
  @Override
  public ID getIDFromNBT(NBTTagCompound nbt) {
    if (nbt.hasKey("ore")) {
      return new OreDictionaryID(nbt.getString("ore"));
    } else {
      return new ItemStackID(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("stack")));
    }
  }
  
  @Override
  public Data getData(double qmc, ID id, Object o) {
    if (o instanceof ItemStack) {
      ItemStack itemstack = (ItemStack) o;
      if (ItemStackUtil.isValidTool(itemstack)) {
        return new ItemStackData(qmc, true, itemstack.getMaxDamage());
      }
    }
    return new ItemStackData(qmc);
  }
  
  @Override
  public Data getData(NBTBase n) {
    NBTTagCompound nbt = (NBTTagCompound) n;
    return new ItemStackData(nbt.getDouble("qmc"), nbt.getBoolean("meta"), nbt.getInteger("maxMeta"));
  }
  
  @Override
  public double getQMC(Data d, ID id, Object o) {
    ItemStackData data = (ItemStackData) d;
    double qmc = data.qmc;
    if (data.metaSensitive) {
      qmc = qmc / (data.maxMeta + 1) * ((data.maxMeta + 1) - ((ItemStack) o).getItemDamage());
    }
    return qmc;
  }
  
  public Map<QMCIDWrapper, Data> process(Map<QMCIDWrapper, Data> map) {
    for (Object o : map.keySet().toArray()) {
      QMCIDWrapper idw = (QMCIDWrapper) o;
      ID id = idw.id;
      Data data = map.get(idw);
      if (id instanceof ItemStackID) {
        if (OreDictionary.getOreID(((ItemStackID) id).getItemStack()) != -1) {
          map.remove(idw);
          QMCIDWrapper n = new QMCIDWrapper(new OreDictionaryID(OreDictionary.getOreName(OreDictionary.getOreID(((ItemStackID) id).getItemStack()))), this);
          map.put(n, data);
        }
      }
    }
    return map;
  }
}
