package zarak.multibags.container

import baubles.api.BaublesApi
import javax.annotation.Nullable
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP, InventoryPlayer}
import net.minecraft.inventory._
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.{ItemArmor, ItemStack}
import net.minecraft.network.play.server.SPacketSetSlot
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraftforge.items.{ItemStackHandler, SlotItemHandler}
import zarak.multibags.MultiBags
import zarak.multibags.items.ItemBagBase

class BagContainer(playerInv: InventoryPlayer, val stack: ItemStack, val addX: Int, val addY: Int) extends Container {
  val player: EntityPlayer = playerInv.player
  val equipmentSlots: Array[EntityEquipmentSlot] = Array[EntityEquipmentSlot](EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET)
  val craftMatrix = new InventoryCrafting(this, 2, 2)
  val craftResult = new InventoryCraftResult
  val item: ItemBagBase = stack.getItem.asInstanceOf[ItemBagBase]
  var inventory: ItemStackHandler = load()
  var render = true
  var hand: EnumHand = _
  onCreate()

  def setHand(@Nullable hand: EnumHand): Unit = {
    this.hand = hand
  }

  def onCreate(): Unit = {

    val copy = stack.copy()
    if (copy.getItemDamage == copy.getMaxDamage - 1) {
      render = false
      if (!player.addItemStackToInventory(copy))
        if (!player.world.isRemote)
          InventoryHelper.spawnItemStack(player.world, player.posX, player.posY, player.posZ, copy)
      BaublesApi.getBaublesHandler(player).setStackInSlot(5, ItemStack.EMPTY)
    } else {
      stack.damageItem(1, player)
      for (i <- 0 until inventory.getSlots)
        addSlotToContainer(new SlotBag(inventory, i, i / 9 * 18 + 9, i % 9 * 18 + 8))

      addSlotToContainer(new SlotCrafting(player, craftMatrix, craftResult, 0, 187 + addX, 33))

      addSlotToContainer(new Slot(playerInv, 40, addX + 110, addY + 58) {
        @Nullable
        @SideOnly(Side.CLIENT) override def getSlotTexture = "minecraft:items/empty_armor_slot_shield"
      })

      for (i <- 0 until 4) {
        val slot = equipmentSlots(i)
        addSlotToContainer(new Slot(playerInv, 36 + (3 - i), addX + 41, addY + i * 18 + 4) {
          override def getSlotStackLimit = 1

          override def isItemValid(stack: ItemStack): Boolean = stack.getItem.isValidArmor(stack, slot, player)

          override def canTakeStack(playerIn: EntityPlayer): Boolean = {
            val itemStack = this.getStack
            if (!itemStack.isEmpty && !playerIn.isCreative && EnchantmentHelper.hasBindingCurse(itemStack)) false
            else super.canTakeStack(playerIn)
          }

          @Nullable
          @SideOnly(Side.CLIENT)
          override def getSlotTexture = ItemArmor.EMPTY_SLOT_NAMES(slot.getIndex)
        })
      }
      for (i <- 0 until 4)
        addSlotToContainer(new Slot(this.craftMatrix, i, addX + 131 + (i % 2) * 18, addY + 14 + (i / 2) * 18))
      val addW = 41
      val addH: Int = -4
      for (l <- 0 until 3)
        for (j1 <- 0 until 9)
          addSlotToContainer(new Slot(playerInv, j1 + l * 9 + 9, addX + addW + j1 * 18, addY + addH + l * 18 + 84))

      for (i1 <- 0 to 8)
        addSlotToContainer(new Slot(playerInv, i1, addX + addW + i1 * 18, addY + addH + 142))
    }
  }

  override def detectAndSendChanges(): Unit = {
    super.detectAndSendChanges()
    save()
  }

  override def onContainerClosed(playerIn: EntityPlayer): Unit = {
    super.onContainerClosed(playerIn)
    if (!playerIn.world.isRemote)
      InventoryHelper.dropInventoryItems(playerIn.world, playerIn.getPosition, craftMatrix)
  }

  override def canInteractWith(playerIn: EntityPlayer): Boolean = {
    if (hand == null)
      MultiBags.getBagBauble(playerIn) == stack
    else
      playerIn.getHeldItem(hand) == stack
  }

  override def transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack = {
    var itemstack = ItemStack.EMPTY

    val slot = this.inventorySlots.get(index)

    if (slot != null && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy
      if (index < inventory.getSlots + 10) {
        if (!this.mergeItemStack(itemstack1, inventorySlots.size() - 36, inventorySlots.size(), false))
          itemstack = ItemStack.EMPTY
      }
      else {
        if (EntityLiving.getSlotForItemStack(itemstack).getSlotType eq EntityEquipmentSlot.Type.ARMOR) {
          if (!this.mergeItemStack(itemstack1, inventory.getSlots + 2, inventory.getSlots + 6, false))
            if (!this.mergeItemStack(itemstack1, 0, inventory.getSlots, false))
              itemstack = ItemStack.EMPTY
        }
        else {
          if (!this.mergeItemStack(itemstack1, 0, inventory.getSlots, false))
            itemstack = ItemStack.EMPTY
        }
      }
    }
    onCraftMatrixChanged(craftMatrix)
    itemstack
  }

  override def onCraftMatrixChanged(inventoryIn: IInventory): Unit = {
    super.onCraftMatrixChanged(inventoryIn)
    slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult)
  }

  override def slotChangedCraftingGrid(world: World, player: EntityPlayer, crafting: InventoryCrafting, result: InventoryCraftResult): Unit = {
    if (!world.isRemote) {
      val entityplayermp = player.asInstanceOf[EntityPlayerMP]
      var itemstack = ItemStack.EMPTY
      val irecipe = CraftingManager.findMatchingRecipe(crafting, world)
      if (irecipe != null) {
        result.setRecipeUsed(irecipe)
        itemstack = irecipe.getCraftingResult(crafting)
      }
      result.setInventorySlotContents(0, itemstack)
      entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, inventory.getSlots, itemstack))
    }
  }

  def save(): Unit = {
    ItemBagBase.saveToNbt(stack, inventory)
  }

  def load(): ItemStackHandler = {
    ItemBagBase.loadFromNbt(stack)
  }
}

class SlotBag(inventoryIn: ItemStackHandler, index: Int, xPosition: Int, yPosition: Int) extends SlotItemHandler(inventoryIn, index, xPosition, yPosition) {
  override def isItemValid(stack: ItemStack): Boolean = !stack.getItem.isInstanceOf[ItemBagBase]
}