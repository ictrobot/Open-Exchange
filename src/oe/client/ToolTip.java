package oe.client;

import java.text.DecimalFormat;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import oe.value.*;

public class ToolTip {
  
  DecimalFormat df = new DecimalFormat("0.00");
  
  @ForgeSubscribe
  public void handleItemTooltipEvent(ItemTooltipEvent event) {
    if (Values.hasValue(event.itemStack)) {
      event.toolTip.add(Values.name + ": " + df.format(Values.getValue(event.itemStack)));
    }
  }
}
