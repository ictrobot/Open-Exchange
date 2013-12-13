package oe.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import oe.api.OEItemInterface;
import oe.api.OEItemMode;
import oe.api.lib.OEType;
import oe.core.util.ItemStackUtil;
import oe.core.util.Util;
import oe.qmc.QMC;

public class ItemBuildRing extends Item implements OEItemInterface, OEItemMode {
  
  public ItemBuildRing(int id) {
    super(id);
    setTextureName(Items.Texture(this.getClass().getSimpleName().substring(4).trim()));
    setUnlocalizedName(this.getClass().getSimpleName());
    setCreativeTab(CreativeTabs.tabTools);
    setMaxStackSize(1);
  }
  
  @Override
  public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    checkNBT(itemStack);
    if (Util.isServerSide()) {
      if (itemStack.getTagCompound().getBoolean("Enabled")) {
        if (entity instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer) entity;
          ItemStack held = player.getHeldItem();
          if (held != null) {
            if (ItemStackUtil.isBlock(held.itemID)) {
              if (held.stackSize == 1) {
                if (QMC.hasQMC(held)) {
                  double v = itemStack.getTagCompound().getDouble("Value");
                  if (v >= QMC.getQMC(held)) {
                    itemStack.getTagCompound().setDouble("Value", itemStack.getTagCompound().getDouble("Value") - QMC.getQMC(held));
                    held.stackSize++;
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
      if (current + value > getMaxQMC(stack)) {
        current = getMaxQMC(stack);
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
  public double getMaxQMC(ItemStack itemstack) {
    return 50000;
  }
  
  @Override
  public int getTier(ItemStack itemstack) {
    return 2;
  }
  
  @Override
  public OEType getType(ItemStack itemstack) {
    return OEType.Consumer;
  }
  
  @Override
  public String getMode(ItemStack itemstack) {
    checkNBT(itemstack);
    NBTTagCompound tag = itemstack.getTagCompound();
    if (tag.getBoolean("Enabled")) {
      return "Enabled";
    } else {
      return "Disabled";
    }
  }
  
  @Override
  public String switchMode(ItemStack itemstack) {
    checkNBT(itemstack);
    NBTTagCompound tag = itemstack.getTagCompound();
    if (tag.getBoolean("Enabled")) {
      tag.setBoolean("Enabled", false);
    } else {
      tag.setBoolean("Enabled", true);
    }
    return getMode(itemstack);
  }
}
