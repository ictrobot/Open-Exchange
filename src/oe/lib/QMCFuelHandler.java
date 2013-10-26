package oe.lib;

import oe.lib.helper.ConfigHelper;
import oe.qmc.QMC;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class QMCFuelHandler implements IFuelHandler {
  @Override
  public int getBurnTime(ItemStack fuel) {
    if (QMC.hasValue(fuel)) {
      ConfigHelper.load();
      double Factor = ConfigHelper.other("FuelHandler", "Factor", 12.5);
      ConfigHelper.save();
      return (int) (Factor * QMC.getQMC(fuel));
    }
    return 0;
  }
}
