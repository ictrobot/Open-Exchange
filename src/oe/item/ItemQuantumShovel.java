package oe.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import oe.api.OEItemInterface;
import oe.api.lib.OEType;
import oe.core.util.NetworkUtil;
import oe.core.util.NetworkUtil.Channel;
import oe.core.util.Util;

public class ItemQuantumShovel extends ItemSpade implements OEItemInterface {
  
  public ItemQuantumShovel() {
    super(OEItems.quantumToolMaterial);
    setTextureName(OEItems.Texture(this.getClass().getSimpleName().substring(4).trim()));
    setUnlocalizedName(this.getClass().getSimpleName());
    setCreativeTab(CreativeTabs.tabTools);
    setMaxDamage(0);
    setNoRepair();
  }
  
  @Override
  public EnumRarity getRarity(ItemStack par1ItemStack) {
    return EnumRarity.rare;
  }
  
  @Override
  public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
    return false;
  }
  
  @Override
  public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
    checkNBT(itemstack);
  }
  
  @Override
  public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block block, int par3, int par4, int par5, EntityLivingBase entity) {
    if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)) {
      decreaseQMC(1, par1ItemStack);
    }
    return super.onBlockDestroyed(par1ItemStack, par2World, block, par3, par4, par5, entity);
  }
  
  private void checkNBT(ItemStack itemstack) {
    if (itemstack.getTagCompound() == null) {
      itemstack.setTagCompound(new NBTTagCompound());
      itemstack.getTagCompound().setDouble("Value", 0);
    }
  }
  
  @Override
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
    if (Util.isClientSide()) {
      NetworkUtil.sendMouseOverToServer(Channel.QuantumDestruction);
    }
    return itemstack;
  }
  
  @Override
  public float getDigSpeed(ItemStack stack, Block block, int meta) {
    if (getQMC(stack) < 1) {
      return 0.5F;
    }
    return super.getDigSpeed(stack, block, meta);
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
}
