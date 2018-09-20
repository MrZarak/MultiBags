
package zarak.multibags.proxy

import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import zarak.multibags.api.Reference
import zarak.multibags.client.{Layers, RenderTileBagUpgrader}
import zarak.multibags.client.gui.BagGui
import zarak.multibags.container.TileBagUpgrader
import zarak.multibags.init.{BagBlocks, BagItems}

class ClientProxy extends CommonProxy {

  override def preInit(event: FMLPreInitializationEvent): Unit = {
    super.preInit(event)
    BagItems.list.foreach(item => {
      item.registerItemModel()
    })
    BagBlocks.list.foreach(block => {
      block.registerItemModel(Item.getItemFromBlock(block))
    })
  }

  override def init(event: FMLInitializationEvent): Unit = {
    super.init(event)
    BagGui.registerBackground(0, new ResourceLocation(Reference.ID, "textures/gui/first_bag.png"))
    BagGui.registerBackground(1, new ResourceLocation(Reference.ID, "textures/gui/second_bag.png"))
    BagGui.registerBackground(2, new ResourceLocation(Reference.ID, "textures/gui/third_bag.png"))
    BagGui.registerBackground(3, new ResourceLocation(Reference.ID, "textures/gui/fourth_bag.png"))
    BagGui.registerBackground(4, new ResourceLocation(Reference.ID, "textures/gui/fifth_bag.png"))
    BagGui.registerBackground(-1, new ResourceLocation(Reference.ID, "textures/gui/ender_bag.png"))
    Layers
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileBagUpgrader], new RenderTileBagUpgrader)
  }

  override def postInit(event: FMLPostInitializationEvent): Unit = {
    super.postInit(event)
  }

}

