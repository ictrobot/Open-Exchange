package oe.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import oe.api.OEItemInterface;
import oe.api.lib.OEType;
import oe.lib.helper.Sided;
import oe.qmc.QMC;

public class ItemRepair extends Item implements OEItemInterface {
  
  public ItemRepair(int id) {
    super(id);
    setTextureName(Items.Texture("Repair"));
    setCreativeTab(CreativeTabs.tabTools);
    setUnlocalizedName("ItemRepair");
  }
  
  public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
    checkNBT(itemstack);
    if (Sided.isServer()) {
      if (itemstack.getTagCompound().getBoolean("Enabled")) {
        if (entity instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer) entity;
          for (ItemStack stack : player.inventory.mainInventory) {
            if (stack != null) {
              if (stack.isItemStackDamageable() && stack.getMaxStackSize() == 1) {
                if (stack.getItemDamage() > 0) {
                  ItemStack normal = new ItemStack(stack.itemID, 1, 0);
                  double repairCost = (QMC.getQMC(normal) - QMC.getQMC(stack)) / stack.getItemDamage();
                  if (getQMC(itemstack) >= repairCost) {
                    decreaseQMC(repairCost, itemstack);
                    stack.setItemDamage(stack.getItemDamage() - 1);
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  private void checkNBT(ItemStack itemstack) {
    if (itemstack.getTagCompound() == null) {
      itemstack.setTagCompound(new NBTTagCompound());
      itemstack.getTagCompound().setDouble("Value", 0);
    }
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
    if (itemStack.getTagCompound() != null) {
      if (itemStack.getTagCompound().getBoolean("Enabled")) {
        list.add("\u00A77Enabled");
      } else {
        list.add("\u00A77Disabled");
      }
      list.add(QMC.name + ": " + itemStack.getTagCompound().getDouble("Value"));
    }
  }
  
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    NBTTagCompound tag = itemStack.getTagCompound();
    if (Sided.isServer()) {
      if (tag.getBoolean("Enabled")) {
        tag.setBoolean("Enabled", false);
        player.addChatMessage("\u00A73\u00A7lRepair:\u00A7r\u00A77 Disabled");
      } else {
        tag.setBoolean("Enabled", true);
        player.addChatMessage("\u00A73\u00A7lRepair:\u00A7r\u00A77 Enabled");
      }
    }
    return itemStack;
  }
  
  @Override
  public double getQMC(ItemStack stack) {
    if (stack.getTagCompound() != null) {
      return stack.getTagCompound().getDouble("Value");
    } else {
      return -1;
    }
  }
  
  @Override
  public void setQMC(double qmc, ItemStack stack) {
    if (stack.getTagCompound() != null) {
      stack.getTagCompound().setDouble("Value", qmc);
    }
  }
  
  @Override
  public void increaseQMC(double value, ItemStack stack) {
    if (stack.getTagCompound() != null) {
      double current = stack.getTagCompound().getDouble("Value");
      if (current + value > getMaxQMC()) {
        current = getMaxQMC();
      } else {
        current = current + value;
      }
      stack.getTagCompound().setDouble("Value", current);
    }
  }
  
  @Override
  public void decreaseQMC(double value, ItemStack stack) {
    if (stack.getTagCompound() != null) {
      double current = stack.getTagCompound().getDouble("Value");
      if (current - value < 0) {
        current = 0;
      } else {
        current = current - value;
      }
      stack.getTagCompound().setDouble("Value", current);
    }
  }
  
  @Override
  public int getMaxQMC() {
    return 10000;
  }
  
  @Override
  public int getTier() {
    return 2;
  }
  
  @Override
  public OEType getType() {
    return OEType.Consumer;
  }
  
  @Override
  public void isOE() {
  }
}
