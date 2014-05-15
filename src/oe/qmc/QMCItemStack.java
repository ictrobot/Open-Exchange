package oe.qmc;

import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.QMCHandler;
import oe.core.util.ItemStackUtil;
import oe.qmc.QMC.Data;
import oe.qmc.QMC.DataNormal;
import oe.qmc.QMC.ID;
import oe.qmc.QMC.IDWrapper;

public class QMCItemStack extends QMCHandler {
  
  public static class ItemStackID implements ID {
    
    public final ItemStack itemstack;
    private int hashcode;
    
    public ItemStackID(ItemStack itemstack) {
      this.itemstack = itemstack;
      if (this.itemstack.stackTagCompound != null) {
        this.hashcode = itemstack.getUnlocalizedName().hashCode() ^ itemstack.getItemDamage() ^ this.itemstack.stackTagCompound.hashCode();
      } else {
        this.hashcode = itemstack.getUnlocalizedName().hashCode() ^ itemstack.getItemDamage();
      }
    }
    
    @Override
    public NBTTagCompound toNBT() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setTag("stack", itemstack.writeToNBT(new NBTTagCompound()));
      return nbt;
    }
    
    @Override
    public Class<? extends QMCHandler> getQMCHandlerClass() {
      return QMCItemStack.class;
    }
    
    public int hashCode() {
      return hashcode;
    }
    
    public boolean equals(Object o) {
      System.out.println(o + " " + itemstack);
      if (o instanceof ItemStackID) {
        ItemStack io = ((ItemStackID) o).itemstack;
        return io.getUnlocalizedName().contentEquals(itemstack.getUnlocalizedName()) & io.getItemDamage() == itemstack.getItemDamage() & ((io.stackTagCompound == null && itemstack.stackTagCompound == null) || io.stackTagCompound == itemstack.stackTagCompound);
      }
      return false;
    }
    
    public String toString() {
      return itemstack.getUnlocalizedName();
    }
  }
  
  public static class OreDictionaryID implements ID {
    
    public final String ore;
    
    public OreDictionaryID(String ore) {
      this.ore = ore;
    }
    
    @Override
    public NBTTagCompound toNBT() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setString("ore", ore);
      return nbt;
    }
    
    @Override
    public Class<? extends QMCHandler> getQMCHandlerClass() {
      return QMCItemStack.class;
    }
    
    public int hashCode() {
      return ore.hashCode();
    }
    
    public boolean equals(Object o) {
      if (o instanceof OreDictionaryID) {
        return ((OreDictionaryID) o).ore == this.ore;
      }
      return false;
    }
    
    public String toString() {
      return ore;
    }
    
  }
  
  public static class ItemStackData extends DataNormal {
    
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
      NBTTagCompound nbt = super.toNBT();
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
  public Data getData(NBTTagCompound nbt) {
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
  
  public Map<IDWrapper, Data> process(Map<IDWrapper, Data> map) {
    for (Object o : map.keySet().toArray()) {
      IDWrapper idw = (IDWrapper) o;
      ID id = idw.id;
      Data data = map.get(idw);
      if (id instanceof ItemStackID) {
        if (OreDictionary.getOreID(((ItemStackID) id).itemstack) != -1) {
          map.remove(idw);
          IDWrapper n = new IDWrapper(new OreDictionaryID(OreDictionary.getOreName(OreDictionary.getOreID(((ItemStackID) id).itemstack))), this);
          map.put(n, data);
        }
      }
    }
    return map;
  }
}
