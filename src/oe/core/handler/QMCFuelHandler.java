package oe.core.handler;

import net.minecraft.item.ItemStack;
import oe.core.util.ConfigUtil;
import oe.qmc.QMC;
import cpw.mods.fml.common.IFuelHandler;

public class QMCFuelHandler implements IFuelHandler {
  
  public static boolean enabled = false;
  public static double factor = 12.5;
  
  public QMCFuelHandler() {
    enabled = ConfigUtil.other("FuelHandler", "Allow any item to be burnt if it has a QMC value", false);
    factor = ConfigUtil.other("FuelHandler", "Factor", 12.5);
  }
  
  @Override
  public int getBurnTime(ItemStack fuel) {
    if (QMC.hasQMC(fuel)) {
      return (int) (factor * QMC.getQMC(fuel));
    }
    return 0;
  }
}
