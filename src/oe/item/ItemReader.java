package oe.item;

import oe.api.OETileInterface;
import oe.api.OE_API;
import oe.helper.Sided;
import oe.qmc.QMC;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemReader extends Item {
  
  public ItemReader(int id) {
    super(id);
    setTextureName(Items.Texture("Reader"));
    setCreativeTab(CreativeTabs.tabTools);
    setUnlocalizedName("ItemReader");
  }
  
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    if (Sided.isClient()) {
      if (Minecraft.getMinecraft().objectMouseOver != null) {
        int x = Minecraft.getMinecraft().objectMouseOver.blockX;
        int y = Minecraft.getMinecraft().objectMouseOver.blockY;
        int z = Minecraft.getMinecraft().objectMouseOver.blockZ;
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te != null) {
          if (OE_API.isOE(te.getClass())) {
            OETileInterface oe = (OETileInterface) te;
            player.addChatMessage("\u00A73\u00A7l " + QMC.name + " Reader:\u00A7r\u00A77 " + oe.getQMC() + " " + QMC.name + " Type: " + oe.getType());
          }
        }
      }
    }
    return itemStack;
  }
}
