package oe.block.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import oe.block.container.ContainerCondenser;
import oe.block.tile.TileCondenser;
import oe.value.Values;

public class GUICondenser extends GuiContainer {
  
  TileCondenser tile;
  
  public GUICondenser(InventoryPlayer inventoryPlayer, TileCondenser tileEntity) {
    super(new ContainerCondenser(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }
  
  @Override
  protected void drawGuiContainerForegroundLayer(int param1, int param2) {
    // fontRenderer.drawString("Condenser", 8, 6, 4210752);
    // Progress Bar
    ResourceLocation bar_texture = new ResourceLocation("oe:textures/gui/condenser_progress_bar.png");
    this.mc.renderEngine.bindTexture(bar_texture);
    int per = 0;
    if (tile.hasTarget) {
      per = tile.percent;
      if (per > 100) {
        per = 100;
      }
    } else {
      per = 100;
    }
    per = per + 3;
    this.drawTexturedModalRect(25, 5, 0, 20, per, 14);
    this.drawTexturedModalRect(25, 5, 0, 0, 106, 14);
    // Storage Text
    fontRenderer.drawString(tile.stored + "", 131, 6, 4210752);
    fontRenderer.drawString(Values.name, 131, 14, 4210752);
    // Inventory Text
    fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 77, 4210752);
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    ResourceLocation Resource = new ResourceLocation("oe:textures/gui/condenser.png");
    this.mc.renderEngine.bindTexture(Resource);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
  }
  
}
