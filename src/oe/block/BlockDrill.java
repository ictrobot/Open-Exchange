package oe.block;

import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import oe.block.tile.TileDrill;

public class BlockDrill extends BlockContainer {
  
  public BlockDrill(int id) {
    super(id, Material.iron);
    setTextureName(Blocks.Texture("Drill"));
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
    setUnlocalizedName("BlockDrill");
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public TileEntity createNewTileEntity(World par1World) {
    TileDrill drill = new TileDrill();
    return drill;
  }
  
  public int quantityDropped(Random rand) {
    return 0;
  }
  
  @SideOnly(Side.CLIENT)
  private Icon[] icons;
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister par1IconRegister) {
    icons = new Icon[2];
    icons[0] = par1IconRegister.registerIcon(Blocks.Texture("Drill_Other"));
    icons[1] = par1IconRegister.registerIcon(Blocks.Texture("Drill_Side"));
  }
  
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
}
