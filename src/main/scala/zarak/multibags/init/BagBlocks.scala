package zarak.multibags.init

import com.zarak.zaraklib.block.BlockBase
import net.minecraftforge.fml.common.registry.{ForgeRegistries, GameRegistry}
import zarak.multibags.blocks.BlockBagUpgrader
import zarak.multibags.container.TileBagUpgrader

import scala.collection.mutable.ListBuffer

object BagBlocks {
  final val list = new ListBuffer[BlockBase]
  val UPDATOR = new BlockBagUpgrader("bag_upgrade")

  GameRegistry.registerTileEntity(classOf[TileBagUpgrader], UPDATOR.getRegistryName)

  list.foreach(block => {
    ForgeRegistries.BLOCKS.register(block.onRegistry)
    ForgeRegistries.ITEMS.register(block.createItemBlock())
  })
}
