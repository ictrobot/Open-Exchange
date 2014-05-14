package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import oe.OpenExchange;
import oe.block.tile.TileCharging;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCharging extends BlockContainer {
  
  public BlockCharging() {
    super(Material.iron);
    setBlockTextureName(OEBlocks.Texture(this.getClass().getSimpleName().substring(5).trim()));
    setBlockName(this.getClass().getSimpleName());
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundTypeMetal);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  @Override
  public TileEntity createNewTileEntity(World par1World, int i) {
    TileCharging tilecharging = new TileCharging();
    return tilecharging;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    TileEntity te = world.getTileEntity(i, j, k);
    if (te == null || !(te instanceof TileCharging) || world.isRemote) {
      return true;
    }
    player.openGui(OpenExchange.instance, 0, world, i, j, k);
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  private IIcon[] icons;
  
  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IconRegister) {
    icons = new IIcon[3];
    icons[0] = par1IconRegister.registerIcon(OEBlocks.Texture("Charging_Bottom"));
    icons[1] = par1IconRegister.registerIcon(OEBlocks.Texture("Charging_Top"));
    icons[2] = par1IconRegister.registerIcon(OEBlocks.Texture("Charging_Side"));
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    switch (par1) {
      case 1:
        return icons[1];
      case 0:
        return icons[0];
      default:
        return icons[2];
    }
  }
}
