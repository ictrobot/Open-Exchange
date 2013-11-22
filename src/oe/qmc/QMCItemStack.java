package oe.qmc;

import oe.lib.helper.OreDictionaryHelper;
import oe.lib.util.FluidUtil;
import oe.lib.util.ItemStackUtil;
import oe.qmc.QMCFluid.FluidItemStack;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class QMCItemStack {
  public static enum Type {
    Itemstack, // Itemstack
    OreDictionary, // Ore Dictionary
    OreDictionary_Itemstack, // Both
  }
  
  public static class Data {
    public double QMC; // QMC Value
    public ItemStack itemstack; // Itemstack
    public String oreDictionary; // Ore Dictionary
    public Type type;
    
    public Data(ItemStack stack, double Value) {
      this.itemstack = stack;
      this.QMC = Value;
      int oreID = OreDictionary.getOreID(this.itemstack);
      if (oreID != -1) {
        this.oreDictionary = OreDictionary.getOreName(oreID);
        this.type = Type.OreDictionary_Itemstack;
      } else {
        this.oreDictionary = "NONE";
        this.type = Type.Itemstack;
      }
    }
    
    public Data(String ore, double Value) {
      this.type = Type.OreDictionary;
      this.QMC = Value;
      this.itemstack = null;
      this.oreDictionary = ore;
    }
  }
  
  private static Data[] data = new Data[0];
  
  public static Double getQMC(Object o) {
    // Fluid Storing Itemstacks
    if (FluidUtil.storesFluid((ItemStack) o)) {
      ItemStack container = FluidUtil.getEmpty((ItemStack) o);
      if (container != null) {
        double containerQMC = getQMC(container);
        if (containerQMC >= 0 && FluidUtil.getFluidStack((ItemStack) o) != null) {
          FluidItemStack f = new FluidItemStack(container, containerQMC, FluidUtil.getFluidStack((ItemStack) o));
          double qmc = QMC.getQMC(f);
          if (qmc >= 0) {
            return new Double(qmc);
          }
        }
      }
    }
    // Other Itemstacks
    if (o instanceof ItemStack) {
      ItemStack itemstack = (ItemStack) o;
      for (int i = 0; i < data.length; i++) {
        Data check = data[i];
        if (check.type != Type.OreDictionary) {
          if (check.itemstack.itemID == ((ItemStack) o).itemID) {
            if ((((ItemStack) o).stackTagCompound == null && check.itemstack.stackTagCompound == null) || (((ItemStack) o).stackTagCompound == itemstack.stackTagCompound)) {
              if (check.itemstack.getItemDamage() == ((ItemStack) o).getItemDamage()) {
                return new Double(check.QMC);
              } else if (ItemStackUtil.isValidTool((ItemStack) o) && check.itemstack.getItemDamage() == 0) {
                double q = check.QMC - (check.QMC / check.itemstack.getMaxDamage() * ((ItemStack) o).getItemDamage());
                if (q > 0) {
                  return new Double(q);
                }
              }
            }
          }
        }
        if (check.type != Type.Itemstack) {
          int oreID = OreDictionary.getOreID(itemstack);
          if (oreID != -1) {
            if (OreDictionary.getOreID(check.oreDictionary) == oreID) {
              return new Double(check.QMC);
            }
          }
        }
      }
    } else if (o instanceof Block) {
      return getQMC(new ItemStack((Block) o));
    } else if (o instanceof Item) {
      return getQMC(new ItemStack((Item) o));
    } else if (o instanceof String) {
      ItemStack[] stacks = OreDictionaryHelper.getItemStacks((String) o);
      if (stacks != null) {
        return getQMC(stacks[0]);
      }
    }
    return new Double(-1);
  }
  
  public static Boolean add(Object o, Double Value) {
    if (getReference(o) != -1) {
      return false;
    }
    if (o instanceof ItemStack) {
      increase();
      data[data.length - 1] = new Data((ItemStack) o, Value);
      return true;
    } else if (o instanceof String) {
      increase();
      data[data.length - 1] = new Data((String) o, Value);
      return true;
    } else if (o instanceof Block) {
      return add(new ItemStack((Block) o), Value);
    } else if (o instanceof Item) {
      return add(new ItemStack((Item) o), Value);
    }
    return false;
  }
  
  public static Boolean blacklist(Object o) {
    int r = getReference(o);
    if (r >= 0) {
      remove(r);
    }
    add(o, -1.0);
    return true;
  }
  
  public static void updateOreDictionary() {
    for (Data d : data) {
      if (d.type != Type.OreDictionary) {
        int oreID = OreDictionary.getOreID(d.itemstack);
        if (oreID != -1) {
          d.oreDictionary = OreDictionary.getOreName(oreID);
          d.type = Type.OreDictionary_Itemstack;
        } else {
          d.oreDictionary = "NONE";
          d.type = Type.Itemstack;
        }
      } else {
        ItemStack[] stacks = OreDictionaryHelper.getItemStacks(d.oreDictionary);
        if (stacks != null) {
          d.itemstack = stacks[0];
          d.type = Type.OreDictionary_Itemstack;
        }
      }
    }
  }
  
  public static NBTTagCompound snapshot() {
    NBTTagCompound nbt = new NBTTagCompound();
    for (int i = 0; i < data.length; i++) {
      NBTTagCompound o = new NBTTagCompound();
      o.setDouble("QMC", data[i].QMC);
      if (data[i].type == Type.Itemstack) {
        NBTTagCompound item = new NBTTagCompound();
        data[i].itemstack.writeToNBT(item);
        o.setCompoundTag("item", item);
      } else {
        o.setString("ore", data[i].oreDictionary);
      }
      nbt.setCompoundTag(i + "", o);
    }
    nbt.setInteger("Length", data.length);
    return nbt;
  }
  
  public static void restoreSnapshot(NBTTagCompound nbt) {
    data = new Data[0];
    for (int i = 0; i < nbt.getInteger("Length"); i++) {
      NBTTagCompound o = nbt.getCompoundTag(i + "");
      double value = o.getDouble("QMC");
      if (o.hasKey("ore")) {
        String ore = o.getString("ore");
        add(ore, value);
      } else {
        ItemStack item = ItemStack.loadItemStackFromNBT(o.getCompoundTag("item"));
        add(item, value);
      }
    }
    updateOreDictionary();
  }
  
  private static void increase() {
    Data[] tmp = new Data[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
  }
  
  private static void remove(int i) {
    data = ArrayUtils.removeElement(data, data[i]);
  }
  
  public static int getReference(Object o) {
    if (o instanceof ItemStack) {
      ItemStack itemstack = (ItemStack) o;
      for (int i = 0; i < data.length; i++) {
        Data check = data[i];
        if (check.type != Type.OreDictionary) {
          if (check.itemstack.itemID == ((ItemStack) o).itemID) {
            if ((((ItemStack) o).stackTagCompound == null && check.itemstack.stackTagCompound == null) || (((ItemStack) o).stackTagCompound == itemstack.stackTagCompound)) {
              if (check.itemstack.getItemDamage() == ((ItemStack) o).getItemDamage()) {
                return i;
              } else if (ItemStackUtil.isValidTool((ItemStack) o) && check.itemstack.getItemDamage() == 0) {
                return i;
              }
            }
          }
        }
        if (check.type != Type.Itemstack) {
          int oreID = OreDictionary.getOreID(itemstack);
          if (oreID != -1) {
            if (OreDictionary.getOreID(check.oreDictionary) == oreID) {
              return i;
            }
          }
        }
      }
    } else if (o instanceof Block) {
      return getReference(new ItemStack((Block) o));
    } else if (o instanceof Item) {
      return getReference(new ItemStack((Item) o));
    } else if (o instanceof String) {
      ItemStack[] stacks = OreDictionaryHelper.getItemStacks((String) o);
      if (stacks != null) {
        return getReference(stacks[0]);
      }
    }
    return -1;
  }
  
  public static Integer length() {
    return data.length;
  }
}
