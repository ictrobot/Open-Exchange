package oe.lib.handler;

import net.minecraft.item.ItemStack;
import oe.lib.util.ConfigUtil;
import oe.qmc.QMC;
import cpw.mods.fml.common.IFuelHandler;

public class QMCFuelHandler implements IFuelHandler {
  @Override
  public int getBurnTime(ItemStack fuel) {
    if (QMC.hasQMC(fuel)) {
      ConfigUtil.load();
      double Factor = ConfigUtil.other("FuelHandler", "Factor", 12.5);
      ConfigUtil.save();
      return (int) (Factor * QMC.getQMC(fuel));
    }
    return 0;
  }
}
