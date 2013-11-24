package oe.item;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import oe.lib.Debug;
import oe.lib.util.Util;

public class ItemReader extends ItemOE {
  
  public ItemReader(int id) {
    super(id);
    setTextureName(Items.Texture("Reader"));
    setCreativeTab(CreativeTabs.tabTools);
    setUnlocalizedName("ItemReader");
  }
  
  @Override
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    if (Util.isClientSide()) {
      if (Minecraft.getMinecraft().objectMouseOver != null) {
        int x = Minecraft.getMinecraft().objectMouseOver.blockX;
        int y = Minecraft.getMinecraft().objectMouseOver.blockY;
        int z = Minecraft.getMinecraft().objectMouseOver.blockZ;
        packet(x, y, z, player);
      }
    }
    return itemStack;
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
    packet.channel = "oeTileInfo";
    packet.data = bos.toByteArray();
    packet.length = bos.size();
    EntityClientPlayerMP player = (EntityClientPlayerMP) tmpplayer;
    player.sendQueue.addToSendQueue(packet);
  }
}
