package zarak.multibags.items

import com.zarak.zaraklib.item.ItemBase
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.ItemStack
import net.minecraftforge.client.model.ModelLoader
import zarak.multibags.MultiBags
import zarak.multibags.container.{BagUpdaterContainer, TileBagUpgrader}
import zarak.multibags.init.BagItems

abstract class ItemUpdateBase(name: String) extends ItemBase(name) {
  setCreativeTab(MultiBags.TAB)
  BagItems.list += this

  def isEnable(bag: ItemStack, tile: TileBagUpgrader): Boolean

  def onEndUpdate(bag: ItemStack, tile: TileBagUpgrader): Unit ={
    val stack = getResult(bag)
    tile.inventory.setStackInSlot(12, stack)
    tile.inventory.setStackInSlot(10, BagUpdaterContainer.subsByCrafting(tile.inventory.getStackInSlot(10)))
    tile.inventory.setStackInSlot(11, BagUpdaterContainer.subsByCrafting(tile.inventory.getStackInSlot(11)))
    tile.progress = 0
  }

  def onTick(bag: ItemStack, tile: TileBagUpgrader): Unit = tile.progress += 1

  def update(bag: ItemStack, tile: TileBagUpgrader): Unit = {
    if (!isEnable(bag, tile)) tile.progress = 0
    else if (tile.progress < 100)
      onTick(bag, tile)
    else
      onEndUpdate(bag, tile)
  }

  def getResult(bag: ItemStack): ItemStack

  override def registerItemModel(): Unit = {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("multibags:" + name, "inventory"))
  }
}
