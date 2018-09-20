package zarak.multibags.container

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.items.ItemStackHandler

class EnderBagContainer(playerInv: InventoryPlayer, stack: ItemStack, addX: Int, addY: Int) extends BagContainer(playerInv, stack, addX, addY) {
  override def load(): ItemStackHandler = {
   val inventory = new ItemStackHandler(27)
    for (i <- 0 until player.getInventoryEnderChest.getSizeInventory)
      inventory.setStackInSlot(i, player.getInventoryEnderChest.getStackInSlot(i))
    inventory
  }

  override def save(): Unit = {
    for (i <- 0 until player.getInventoryEnderChest.getSizeInventory)
      player.getInventoryEnderChest.setInventorySlotContents(i,inventory.getStackInSlot(i))
  }
}