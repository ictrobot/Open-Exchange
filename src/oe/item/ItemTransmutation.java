package oe.item;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import oe.lib.Debug;
import oe.lib.helper.Sided;

public class ItemTransmutation extends Item {
  
  public ItemTransmutation(int id) {
    super(id);
    setTextureName(Items.Texture("Transmutation"));
    setCreativeTab(CreativeTabs.tabTools);
    setUnlocalizedName("ItemTransmutation");
    setFull3D();
  }
  
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
    checkNBT(itemstack);
    if (Sided.isClient()) {
      if (Minecraft.getMinecraft().objectMouseOver != null) {
        int x = Minecraft.getMinecraft().objectMouseOver.blockX;
        int y = Minecraft.getMinecraft().objectMouseOver.blockY;
        int z = Minecraft.getMinecraft().objectMouseOver.blockZ;
        packet(x, y, z, player);
        itemstack.getTagCompound().setDouble("particleX", x);
        itemstack.getTagCompound().setDouble("particleY", y + 1);
        itemstack.getTagCompound().setDouble("particleZ", z);
        itemstack.getTagCompound().setInteger("particleTick", 0);
        
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
    packet.channel = "oeTransmutation";
    packet.data = bos.toByteArray();
    packet.length = bos.size();
    EntityClientPlayerMP player = (EntityClientPlayerMP) tmpplayer;
    player.sendQueue.addToSendQueue(packet);
  }
  
  public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
    checkNBT(itemstack);
    int tick = itemstack.getTagCompound().getInteger("particleTick");
    if (tick <= 20) {
      double x = itemstack.getTagCompound().getDouble("particleX");
      double y = itemstack.getTagCompound().getDouble("particleY");
      double z = itemstack.getTagCompound().getDouble("particleZ");
      x = x + MathHelper.getRandomDoubleInRange(itemRand, 0.1, 1);
      z = z + MathHelper.getRandomDoubleInRange(itemRand, 0.1, 1);
      world.spawnParticle("largesmoke", x, y, z, 0, 0.1, 0);
      itemstack.getTagCompound().setInteger("particleTick", tick + 1);
    }
  }
  
  public boolean hasEffect(ItemStack itemstack, int pass) {
    checkNBT(itemstack);
    return itemstack.getTagCompound().getInteger("particleTick") <= 20;
  }
  
  private void checkNBT(ItemStack itemstack) {
    if (itemstack.getTagCompound() == null) {
      itemstack.setTagCompound(new NBTTagCompound());
      itemstack.getTagCompound().setDouble("particleX", 0);
      itemstack.getTagCompound().setDouble("particleY", 0);
      itemstack.getTagCompound().setDouble("particleZ", 0);
      itemstack.getTagCompound().setInteger("particleTick", 0);
    }
  }
}
