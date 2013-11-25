package oe.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import oe.api.OEItemInterface;
import oe.api.lib.OEType;
import oe.lib.util.ItemStackUtil;
import oe.lib.util.Util;
import oe.qmc.QMC;

public class ItemBuildRing extends Item implements OEItemInterface {
  
  public ItemBuildRing(int id) {
    super(id);
    setTextureName(Items.Texture("BuildRing"));
    setCreativeTab(CreativeTabs.tabTools);
    setUnlocalizedName("ItemBuildRing");
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
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    NBTTagCompound tag = itemStack.getTagCompound();
    if (Util.isServerSide()) {
      if (tag.getBoolean("Enabled")) {
        tag.setBoolean("Enabled", false);
        player.addChatMessage("\u00A73\u00A7lBuilder's Ring:\u00A7r\u00A77 Disabled");
      } else {
        tag.setBoolean("Enabled", true);
        player.addChatMessage("\u00A73\u00A7lBuilder's Ring:\u00A7r\u00A77 Enabled");
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
  public double getMaxQMC() {
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
}
