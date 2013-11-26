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
import oe.OpenExchange;
import oe.block.tile.TileExtractor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockExtractor extends BlockContainer {
  
  public BlockExtractor(int id) {
    super(id, Material.iron);
    setTextureName(Blocks.Texture(this.getClass().getSimpleName().substring(5).trim()));
    setUnlocalizedName(this.getClass().getSimpleName());
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  @Override
  public TileEntity createNewTileEntity(World par1World) {
    TileExtractor tileextractor = new TileExtractor();
    return tileextractor;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    TileEntity te = world.getBlockTileEntity(i, j, k);
    if (te == null || !(te instanceof TileExtractor) || world.isRemote) {
      return true;
    }
    te.onInventoryChanged();
    player.openGui(OpenExchange.instance, 0, world, i, j, k);
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  private Icon[] icons;
  
  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister par1IconRegister) {
    icons = new Icon[4];
    icons[0] = par1IconRegister.registerIcon(Blocks.Texture("Extractor_Bottom"));
    icons[1] = par1IconRegister.registerIcon(Blocks.Texture("Extractor_Top"));
    icons[2] = par1IconRegister.registerIcon(Blocks.Texture("Extractor_Side"));
    icons[3] = par1IconRegister.registerIcon(Blocks.Texture("Extractor_Side_Spin"));
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public Icon getIcon(int par1, int par2) {
    switch (par1) {
      case 1:
        return icons[1];
      case 0:
        return icons[0];
      default:
        switch (par2) {
          case 1:
            return icons[3];
          default:
            return icons[2];
        }
    }
  }
}
