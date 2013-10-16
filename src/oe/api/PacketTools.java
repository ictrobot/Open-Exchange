package oe.api;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class PacketTools {
  
  public static ItemStack readItemStack(DataInput data) throws IOException {
    ItemStack itemstack = null;
    short s = data.readShort();
    
    if (s >= 0) {
      byte b = data.readByte();
      short s2 = data.readShort();
      itemstack = new ItemStack(s, b, s2);
      itemstack.stackTagCompound = readNBTTagCompound(data);
    }
    
    return itemstack;
  }
  
  public static NBTTagCompound readNBTTagCompound(DataInput data) throws IOException {
    short s = data.readShort();
    
    if (s < 0) {
      return null;
    } else {
      byte[] abyte = new byte[s];
      data.readFully(abyte);
      return CompressedStreamTools.decompress(abyte);
    }
  }
  
  public static void writeItemStack(ItemStack stack, DataOutput data) throws IOException {
    if (stack == null) {
      data.writeShort(-1);
    } else {
      data.writeShort(stack.itemID);
      data.writeByte(stack.stackSize);
      data.writeShort(stack.getItemDamage());
      NBTTagCompound nbt = null;
      
      if (stack.getTagCompound() == null) {
        nbt = stack.stackTagCompound;
      }
      
      writeNBTTagCompound(nbt, data);
    }
  }
  
  public static void writeNBTTagCompound(NBTTagCompound nbt, DataOutput data) throws IOException {
    if (nbt == null) {
      data.writeShort(-1);
    } else {
      byte[] b = CompressedStreamTools.compress(nbt);
      data.writeShort((short) b.length);
      data.write(b);
    }
  }
}
