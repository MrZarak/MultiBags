package zarak.multibags.container

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraftforge.items.ItemStackHandler
import zarak.multibags.items.{ItemBagBase, ItemUpdateBase}

class TileBagUpgrader extends TileEntity with ITickable {
  val inventory = new ItemStackHandler(13)
  var progress = 0

  override def writeToNBT(compound: NBTTagCompound): NBTTagCompound = {
    compound.setTag("inv", inventory.serializeNBT())
    compound.setInteger("progress", progress)
    super.writeToNBT(compound)
  }

  override def readFromNBT(compound: NBTTagCompound): Unit = {
    super.readFromNBT(compound)
    inventory.deserializeNBT(compound.getCompoundTag("inv"))
    progress = compound.getInteger("progress")
  }

  override def update(): Unit = {
    val bag = inventory.getStackInSlot(10)
    bag.getItem match {
      case _: ItemBagBase =>
        val updater = inventory.getStackInSlot(11)
        updater.getItem match {
          case upr: ItemUpdateBase =>
            upr.update(bag, this)
          case _ => progress = 0
        }
      case _ => progress = 0
    }
  }
}