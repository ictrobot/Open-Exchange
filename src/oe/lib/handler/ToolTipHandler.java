package oe.lib.handler;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import oe.qmc.QMC;

public class ToolTipHandler {
  
  @ForgeSubscribe
  public void handleItemTooltipEvent(ItemTooltipEvent event) {
    if (QMC.hasQMC(event.itemStack)) {
      event.toolTip.add("Each " + QMC.formatter.format(QMC.getQMC(event.itemStack)) + " " + QMC.name);
      if (event.itemStack.stackSize != 1) {
        event.toolTip.add(event.itemStack.stackSize + ": " + QMC.formatter.format(QMC.getQMC(event.itemStack) * event.itemStack.stackSize) + " " + QMC.name);
      }
    }
  }
}
