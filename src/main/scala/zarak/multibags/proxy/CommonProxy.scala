package zarak.multibags.proxy

import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import zarak.multibags.api.BagRecipe
import zarak.multibags.init.{BagBlocks, BagItems}
import zarak.multibags.network.Networking


class CommonProxy {

  def preInit(event: FMLPreInitializationEvent): Unit = {
    BagItems
    BagBlocks
  }

  def init(event: FMLInitializationEvent): Unit = {
    Networking
  }

  def postInit(event: FMLPostInitializationEvent): Unit = {
    BagRecipe
  }

}
