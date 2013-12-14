package oe.item;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import oe.api.OEItemMode;
import oe.core.Debug;
import oe.core.util.Util;
import oe.qmc.InWorldQMC;

public class ItemInfo extends Item implements OEItemMode {
  
  public ItemInfo(int id) {
    super(id);
    setTextureName(Items.Texture(this.getClass().getSimpleName().substring(4).trim()));
    setUnlocalizedName(this.getClass().getSimpleName());
    setCreativeTab(CreativeTabs.tabTools);
    setMaxStackSize(1);
  }
  
  @Override
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
    checkNBT(itemstack);
    if (Util.isClientSide() && Minecraft.getMinecraft().objectMouseOver != null) {
      int x = Minecraft.getMinecraft().objectMouseOver.blockX;
      int y = Minecraft.getMinecraft().objectMouseOver.blockY;
      int z = Minecraft.getMinecraft().objectMouseOver.blockZ;
      if (itemstack.getTagCompound().getBoolean("Server")) {
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
        packet.channel = "OpenExchangeT";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        ((EntityClientPlayerMP) player).sendQueue.addToSendQueue(packet);
      } else {
        InWorldQMC.info(player, x, y, z);
      }
    }
    return itemstack;
  }
  
  private void checkNBT(ItemStack itemstack) {
    if (itemstack.getTagCompound() == null) {
      itemstack.setTagCompound(new NBTTagCompound());
      itemstack.stackTagCompound.setBoolean("Server", true);
    }
  }
  
  @Override
  public String getMode(ItemStack itemstack) {
    checkNBT(itemstack);
    NBTTagCompound tag = itemstack.getTagCompound();
    if (tag.getBoolean("Server")) {
      return "Server";
    } else {
      return "Client";
    }
  }
  
  @Override
  public String switchMode(ItemStack itemstack) {
    checkNBT(itemstack);
    NBTTagCompound tag = itemstack.getTagCompound();
    if (tag.getBoolean("Server")) {
      tag.setBoolean("Server", false);
    } else {
      tag.setBoolean("Server", true);
    }
    return getMode(itemstack);
  }
}
