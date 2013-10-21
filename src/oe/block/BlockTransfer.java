package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import oe.block.tile.TileTransfer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTransfer extends BlockContainer {
  
  private int tier;
  
  public BlockTransfer(int id, int Tier) {
    super(id, Material.iron);
    tier = Tier;
    setTextureName(Blocks.Texture("Transfer"));
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
    setUnlocalizedName("BlockTransfer");
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public TileEntity createNewTileEntity(World par1World) {
    TileTransfer transfer = new TileTransfer(tier);
    return transfer;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    TileEntity te = world.getBlockTileEntity(i, j, k);
    if (te == null || !(te instanceof TileTransfer) || world.isRemote) {
      return true;
    }
    TileTransfer storage = (TileTransfer) te;
    storage.onInventoryChanged();
    // player.addChatMessage("Stored " + storage.stored + " Maximum " + storage.getMaxQMC() +
    // " Percentage " + QMC.formatter.format(storage.stored / storage.getMaxQMC() * 100));
    storage.onClick(player);
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  private Icon[] icons;
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister par1IconRegister) {
    icons = new Icon[1];
    icons[0] = par1IconRegister.registerIcon(Blocks.Texture("Transfer"));
  }
  
  @SideOnly(Side.CLIENT)
  public Icon getIcon(int par1, int par2) {
    return icons[0];
  }
}
