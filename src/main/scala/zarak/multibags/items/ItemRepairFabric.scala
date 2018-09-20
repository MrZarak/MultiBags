package zarak.multibags.items

import net.minecraft.item.ItemStack
import zarak.multibags.container.TileBagUpgrader

class ItemRepairFabric(name: String, val repairCount: Int) extends ItemUpdateBase(name) {
  setMaxStackSize(16)

  override def isEnable(bag: ItemStack, tile: TileBagUpgrader): Boolean = bag.getItemDamage > 0

  override def onTick(bag: ItemStack, tile: TileBagUpgrader): Unit = {
    if (tile.getWorld.getTotalWorldTime % (20 + repairCount) == 0)
      tile.progress += 1
  }

  override def getResult(bag: ItemStack): ItemStack = {
    val stack = bag.copy()
    stack.setItemDamage(stack.getItemDamage - repairCount)
    stack
  }
}
