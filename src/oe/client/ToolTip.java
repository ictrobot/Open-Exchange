package oe.client;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import oe.value.*;

public class ToolTip {
  
  @ForgeSubscribe
  public void handleItemTooltipEvent(ItemTooltipEvent event) {
    
    if (Values.hasValue(event.itemStack)) {
      event.toolTip.add(Values.name + ": " + Values.getValue(event.itemStack));
    }
  }
}
