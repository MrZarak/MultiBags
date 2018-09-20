package zarak.multibags.container

import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer}
import net.minecraft.inventory.{Container, InventoryCrafting, Slot}
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.items.SlotItemHandler
import zarak.multibags.api.BagRecipe
import zarak.multibags.items.{ItemBagBase, ItemUpdateBase}
import zarak.multibags.network.{Networking, SPacketSyncSlot}

import scala.collection.mutable.ListBuffer

class BagUpdaterContainer(playerInv: InventoryPlayer, val tile: TileBagUpgrader) extends Container {
  val ins: BagUpdaterContainer = this
  addSlotToContainer(new SlotItemHandler(tile.inventory, 0, 100, 16) {
    override def isItemValid(stack: ItemStack): Boolean = false

    override def onTake(thePlayer: EntityPlayer, stack: ItemStack): ItemStack = {
      for (i <- 1 until 10)
        tile.inventory.setStackInSlot(i, BagUpdaterContainer.subsByCrafting(tile.inventory.getStackInSlot(i)))
      BagUpdaterContainer.craft(ins)
      super.onTake(thePlayer, stack)
    }
  })

  for (i <- 0 until 9)
    addSlotToContainer(new SlotItemHandler(tile.inventory, i + 1, 32 + (i % 3) * 21, 12 + (i / 3) * 21) {
      override def onSlotChanged(): Unit = {
        super.onSlotChanged()
        BagUpdaterContainer.craft(ins)
      }
    })
  addSlotToContainer(new SlotItemHandler(tile.inventory, 10, 127, 15) {
    override def isItemValid(stack: ItemStack): Boolean = stack.getItem.isInstanceOf[ItemBagBase]
  })
  addSlotToContainer(new SlotItemHandler(tile.inventory, 11, 127, 51) {
    override def isItemValid(stack: ItemStack): Boolean = stack.getItem.isInstanceOf[ItemUpdateBase]
  })
  addSlotToContainer(new SlotItemHandler(tile.inventory, 12, 100, 50) {
    override def isItemValid(stack: ItemStack): Boolean = false

    override def onTake(thePlayer: EntityPlayer, stack: ItemStack): ItemStack = {

      super.onTake(thePlayer, stack)
    }
  })
  val addW = 8
  val addH: Int = 0
  for (l <- 0 until 3)
    for (j1 <- 0 until 9)
      addSlotToContainer(new Slot(playerInv, j1 + l * 9 + 9, addW + j1 * 18, addH + l * 18 + 84))

  for (i1 <- 0 to 8)
    addSlotToContainer(new Slot(playerInv, i1, addW + i1 * 18, addH + 142))


  override def canInteractWith(playerIn: EntityPlayer): Boolean = playerIn.getDistanceSq(tile.getPos) < 25 && !playerIn.getEntityWorld.isAirBlock(tile.getPos)

  override def transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack = {
    var itemstack = ItemStack.EMPTY
    val slot = this.inventorySlots.get(index)

    if (slot != null && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy
      if (index < 13) {
        if (!this.mergeItemStack(itemstack1, inventorySlots.size() - 36, inventorySlots.size(), false))
          itemstack = ItemStack.EMPTY
        else if (index == 0) {
          for (i <- 1 to 9) {
            tile.inventory.setStackInSlot(i, BagUpdaterContainer.subsByCrafting(tile.inventory.getStackInSlot(i)))
            Networking.sendToServer(new SPacketSyncSlot(windowId, i, tile.inventory.getStackInSlot(i)))
          }
        }
      }
      else if (!this.mergeItemStack(itemstack1, 0, 13, true))
        itemstack = ItemStack.EMPTY
    }
    itemstack
  }
}

object BagUpdaterContainer {
  def subsByCrafting(stack: ItemStack): ItemStack = {
    var returnStack = stack.copy()
    if (stack.getItem.hasContainerItem(stack))
      returnStack = stack.getItem.getContainerItem(stack)
    else
      returnStack.setCount(stack.getCount - 1)
    returnStack
  }

  def craft(container: BagUpdaterContainer): Unit = {
    val list = new ListBuffer[ItemStack]
    for (i <- 1 until 10)
      if (!container.tile.inventory.getStackInSlot(i).isEmpty)
        list += container.tile.inventory.getStackInSlot(i)
    var stackRes = BagRecipe.getResult(list)
    if (stackRes.isEmpty) {
      val inventoryCrafting = new InventoryCrafting(container, 3, 3)
      for (i <- 0 until 9)
        inventoryCrafting.setInventorySlotContents(i, container.tile.inventory.getStackInSlot(i + 1))
      stackRes = CraftingManager.findMatchingResult(inventoryCrafting, container.tile.getWorld)
    }
    container.tile.inventory.setStackInSlot(0, stackRes)
  }
}