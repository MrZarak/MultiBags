package zarak.multibags.client.gui

import net.minecraft.client.gui.inventory.GuiContainer
import zarak.multibags.container.BagContainer

class BagGuiTest(container:BagContainer) extends GuiContainer(container){
  override def drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int): Unit = {

  }
}
