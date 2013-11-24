package oe.block;

import oe.lib.util.Util;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public abstract class BlockOE extends BlockContainer {
  
  public BlockOE(int id, Material material) {
    super(id, material);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public String getLocalizedName() {
    return Util.localize(this.getUnlocalizedName());
  }
}
