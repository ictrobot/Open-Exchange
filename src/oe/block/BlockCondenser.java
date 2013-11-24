package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import oe.OpenExchange;
import oe.block.tile.TileCondenser;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCondenser extends BlockOE {
  
  public BlockCondenser(int id) {
    super(id, Material.iron);
    setTextureName(Blocks.Texture("Condenser"));
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
    setUnlocalizedName("BlockCondenser");
  }
  
  @Override
  public TileEntity createNewTileEntity(World par1World) {
    TileCondenser tilecondenser = new TileCondenser();
    return tilecondenser;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    TileEntity te = world.getBlockTileEntity(i, j, k);
    if (te == null || !(te instanceof TileCondenser) || world.isRemote) {
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
    icons = new Icon[3];
    icons[0] = par1IconRegister.registerIcon(Blocks.Texture("Condenser_Bottom"));
    icons[1] = par1IconRegister.registerIcon(Blocks.Texture("Condenser_Top"));
    icons[2] = par1IconRegister.registerIcon(Blocks.Texture("Condenser_Side"));
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
        return icons[2];
    }
  }
}
