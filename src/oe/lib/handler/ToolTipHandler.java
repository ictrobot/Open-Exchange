package oe.lib.handler;

import java.text.DecimalFormat;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import oe.qmc.*;

public class ToolTipHandler {
  
  DecimalFormat df = new DecimalFormat("0.00");
  
  @ForgeSubscribe
  public void handleItemTooltipEvent(ItemTooltipEvent event) {
    if (QMC.hasValue(event.itemStack)) {
      event.toolTip.add(QMC.name + ": " + df.format(QMC.getQMC(event.itemStack)));
    }
  }
}
