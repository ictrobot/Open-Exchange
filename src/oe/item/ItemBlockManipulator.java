package oe.item;

import java.util.List;
import net.minecraft.block.Block;
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

public class ItemBlockManipulator extends Item implements OEItemInterface, OEItemMode {
  
  public ItemBlockManipulator(int id) {
    super(id);
    setTextureName(Items.Texture(this.getClass().getSimpleName().substring(4).trim()));
    setUnlocalizedName(this.getClass().getSimpleName());
    setCreativeTab(CreativeTabs.tabTools);
    setMaxStackSize(1);
  }
  
  @Override
  public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
    checkNBT(itemstack);
  }
  
  private void checkNBT(ItemStack itemstack) {
    if (itemstack.getTagCompound() == null) {
      itemstack.setTagCompound(new NBTTagCompound());
      itemstack.stackTagCompound.setCompoundTag("mode", new NBTTagCompound());
      itemstack.stackTagCompound.getCompoundTag("mode").setBoolean("copy", true);
      itemstack.stackTagCompound.setBoolean("Enabled", false);
    }
  }
  
  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
    if (itemStack.getTagCompound() != null) {
      // list.add(QMC.name + ": " + itemStack.stackTagCompound.getDouble("Value")); Because Charging
      // currently does nothing
      if (itemStack.stackTagCompound.getBoolean("hasBlock")) {
        NBTTagCompound nbtBlock = itemStack.stackTagCompound.getCompoundTag("block");
        if (nbtBlock != null) {
          Block block = Block.blocksList[nbtBlock.getInteger("BlockID")];
          list.add("Currently Holding: " + block.getLocalizedName());
        }
      }
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
    if (itemstack.stackTagCompound.getCompoundTag("mode").getBoolean("copy")) {
      return "Copy";
    } else {
      return "Move";
    }
  }
  
  @Override
  public String switchMode(ItemStack itemstack) {
    checkNBT(itemstack);
    if (itemstack.stackTagCompound.getCompoundTag("mode").getBoolean("copy")) {
      itemstack.stackTagCompound.getCompoundTag("mode").removeTag("copy");
      itemstack.stackTagCompound.getCompoundTag("mode").setBoolean("move", true);
    } else {
      itemstack.stackTagCompound.getCompoundTag("mode").removeTag("move");
      itemstack.stackTagCompound.getCompoundTag("mode").setBoolean("copy", true);
    }
    return getMode(itemstack);
  }
}
