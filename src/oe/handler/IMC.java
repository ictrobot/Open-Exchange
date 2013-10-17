package oe.handler;

import oe.value.Values;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

public class IMC {
  
  public static String set = "SET";
  public static String get = "GET";
  
  public static void processIMCMessages(IMCEvent event) {
    
    for (IMCMessage imcMessage : event.getMessages()) {
      
      if (imcMessage.getMessageType() == NBTTagCompound.class) {
        
        if (imcMessage.key.equalsIgnoreCase(get)) {
          getQMC(imcMessage);
        } else if (imcMessage.key.equalsIgnoreCase(set)) {
          setQMC(imcMessage);
        }
      }
    }
  }
  
  private static void getQMC(IMCMessage imcMessage) {
    
    NBTTagCompound nbt = imcMessage.getNBTValue();
    
    int Value = Values.getValue(new ItemStack(nbt.getInteger("itemID"), 0, nbt.getInteger("meta")));
    
    NBTTagCompound message = new NBTTagCompound();
    message.setInteger("value", Value);
    message.setInteger("itemID", nbt.getInteger("itemID"));
    message.setInteger("meta", nbt.getInteger("meta"));
    FMLInterModComms.sendMessage(imcMessage.getSender(), "QMC_VALUE", message);
  }
  
  private static void setQMC(IMCMessage imcMessage) {
    
    NBTTagCompound nbt = imcMessage.getNBTValue();
    
    ItemStack stack = new ItemStack(nbt.getInteger("itemID"), 0, nbt.getInteger("meta"));
    
    Values.add(stack, nbt.getInteger("value"));
  }
}
