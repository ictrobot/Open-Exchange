package oe.qmc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.QMCHandler;
import oe.api.QMCProcessor;
import oe.core.util.ItemStackUtil;

public class QMCItemStack extends QMCHandler {
  public QMCItemStack() {
    super(new Class<?>[] { ItemStack.class, String.class });
  }
  
  public double decode(Object o, NBTTagCompound nbt) {
    double qmc = nbt.getDouble("qmc");
    if (o instanceof ItemStack && ItemStackUtil.isValidTool((ItemStack) o) && ((ItemStack) o).getItemDamage() != 0) {
      int maxDamage = ((ItemStack) o).getMaxDamage() + 1;
      int damage = ((ItemStack) o).getItemDamage();
      qmc = qmc - (qmc / maxDamage) * damage;
    }
    return qmc;
  }
  
  public NBTTagCompound getID(Object o) {
    NBTTagCompound nbt = new NBTTagCompound();
    if (o instanceof ItemStack) {
      if (OreDictionary.getOreID((ItemStack) o) != -1) {
        return getID(OreDictionary.getOreName(OreDictionary.getOreID((ItemStack) o)));
      }
      nbt.setInteger("ID", ((ItemStack) o).itemID);
      if (!ItemStackUtil.isValidTool((ItemStack) o)) {
        nbt.setInteger("meta", ((ItemStack) o).getItemDamage());
      }
      if (((ItemStack) o).stackTagCompound != null) {
        nbt.setCompoundTag("compound", ((ItemStack) o).stackTagCompound);
      }
    } else if (o instanceof String) {
      nbt.setString("ore", (String) o);
    }
    return nbt;
  }
  
  public static class OreProcessor extends QMCProcessor {
    @Override
    public void process(QMCProcessor.Data d) {
      NBTTagCompound id = d.getID();
      NBTTagCompound data = d.getData();
      if (id.getName().equals("QMCItemStack")) {
        if (!data.hasKey("string")) {
          ItemStack itemstack = new ItemStack(id.getInteger("ID"), 1, id.getInteger("meta"));
          if (id.hasKey("compound")) {
            itemstack.stackTagCompound = data.getCompoundTag("compound");
          }
          if (OreDictionary.getOreID(itemstack) != -1) {
            id = new NBTTagCompound();
            id.setString("ore", OreDictionary.getOreName(OreDictionary.getOreID(itemstack)));
            d.setID(id);
          }
        }
      }
    }
  }
}
