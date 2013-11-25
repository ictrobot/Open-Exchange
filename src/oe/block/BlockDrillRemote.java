package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import oe.block.tile.TileDrillRemote;
import oe.lib.util.Util;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDrillRemote extends BlockContainer {
  
  public BlockDrillRemote(int id) {
    super(id, Material.iron);
    setTextureName(Blocks.Texture("DrillRemote"));
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
    setUnlocalizedName("BlockDrillRemote");
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  @Override
  public TileEntity createNewTileEntity(World par1World) {
    TileDrillRemote drill = new TileDrillRemote();
    return drill;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    if (Util.isServerSide()) {
      TileEntity te = world.getBlockTileEntity(i, j, k);
      if (te == null || !(te instanceof TileDrillRemote) || world.isRemote) {
        return true;
      }
      TileDrillRemote drill = (TileDrillRemote) te;
      if (!player.isSneaking()) {
        player.addChatMessage("ID " + drill.RemoteID + ", Facing " + drill.getDirection());
      } else {
        player.addChatMessage("Now Facing " + drill.changeDirection());
      }
    }
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  private Icon[] icons;
  
  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister par1IconRegister) {
    icons = new Icon[2];
    icons[0] = par1IconRegister.registerIcon(Blocks.Texture("Drill"));
    icons[1] = par1IconRegister.registerIcon(Blocks.Texture("Drill_Side"));
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public Icon getIcon(int par1, int par2) {
    switch (par1) {
      case 1:
        return icons[0];
      case 0:
        return icons[0];
      default:
        return icons[1];
    }
  }
  
  @Override
  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
    if (Util.isServerSide()) {
      if (world.isAirBlock(x, y + 1, z)) {
        world.setBlock(x, y + 1, z, BlockIDs.drillRemoteReceiver);
      }
    }
  }
}
