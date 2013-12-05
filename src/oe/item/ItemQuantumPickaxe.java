package oe.item;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import oe.api.OEItemInterface;
import oe.api.lib.OEType;
import oe.core.Debug;
import oe.core.util.Util;

public class ItemQuantumPickaxe extends ItemPickaxe implements OEItemInterface {
  
  public ItemQuantumPickaxe(int id) {
    super(id, Items.quantum);
    setTextureName(Items.Texture(this.getClass().getSimpleName().substring(4).trim()));
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
  public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase entity) {
    if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)) {
      decreaseQMC(1, par1ItemStack);
    }
    return super.onBlockDestroyed(par1ItemStack, par2World, par3, par4, par5, par6, entity);
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
      if (Minecraft.getMinecraft().objectMouseOver != null) {
        int x = Minecraft.getMinecraft().objectMouseOver.blockX;
        int y = Minecraft.getMinecraft().objectMouseOver.blockY;
        int z = Minecraft.getMinecraft().objectMouseOver.blockZ;
        packet(x, y, z, player);
      }
    }
    return itemstack;
  }
  
  private void packet(int x, int y, int z, EntityPlayer tmpplayer) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try {
      outputStream.writeInt(x);
      outputStream.writeInt(y);
      outputStream.writeInt(z);
    } catch (Exception e) {
      Debug.handleException(e);
    }
    
    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = "oeQD";
    packet.data = bos.toByteArray();
    packet.length = bos.size();
    EntityClientPlayerMP player = (EntityClientPlayerMP) tmpplayer;
    player.sendQueue.addToSendQueue(packet);
  }
  
  @Override
  public float getStrVsBlock(ItemStack stack, Block block, int meta) {
    if (getQMC(stack) < 1) {
      return 0.5F;
    }
    return super.getStrVsBlock(stack, block, meta);
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
    return 100000;
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
