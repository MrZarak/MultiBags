package zarak.multibags.items

import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.FMLCommonHandler
import zarak.multibags.container.TileBagUpgrader

import scala.collection.mutable

class ItemUpdatePerk(name: String, perkName: String) extends ItemUpdateBase(name) {
  if (FMLCommonHandler.instance().getEffectiveSide.isClient)
    ItemUpdatePerk.perksInfo += (perkName -> I18n.format(perkName + "_perk"))

  override def getResult(bag: ItemStack): ItemStack = {
    val tag = if (bag.getTagCompound == null) new NBTTagCompound else bag.getTagCompound
    tag.setTag("perks", new NBTTagCompound)
    tag.getCompoundTag("perks").setBoolean(perkName, true)
    bag.setTagCompound(tag)
    bag
  }


  override def onTick(bag: ItemStack, tile: TileBagUpgrader): Unit = {
    if (tile.getWorld.getTotalWorldTime % 20 == 0)
      tile.progress += 1
  }

  override def isEnable(bag: ItemStack, tile: TileBagUpgrader): Boolean = !(bag.hasTagCompound && bag.getTagCompound.getCompoundTag("perks").getSize >= ItemBagBase.MAX_PERKS && bag.getTagCompound.getCompoundTag("perks").getBoolean(perkName))

}

object ItemUpdatePerk {
  val perksInfo = new mutable.HashMap[String, String]()
}