package zarak.multibags.init

import com.zarak.zaraklib.item.ItemBase
import net.minecraftforge.fml.common.registry.ForgeRegistries
import zarak.multibags.items.{ItemBagBase, ItemRepairFabric, ItemUpdateBase, ItemUpdatePerk}

import scala.collection.mutable.ListBuffer

object BagItems {
  final val list = new ListBuffer[ItemBase]

  val ENDER_BAG: ItemBagBase = new ItemBagBase("ender_bag", -1, 27) {
    setMaxDamage(45)
  }
  val FIRST_BAG: ItemBagBase = new ItemBagBase("first_bag", 0, 9) {
    setMaxDamage(25)
  }
  val SECOND_BAG: ItemBagBase = new ItemBagBase("second_bag", 1, 18) {
    setMaxDamage(45)
  }
  val THIRD_BAG: ItemBagBase = new ItemBagBase("third_bag", 2, 27) {
    setMaxDamage(70)
  }
  val FOURTH_BAG: ItemBagBase = new ItemBagBase("fourth_bag", 3, 36) {
    setMaxDamage(100)
  }
  val FIFTH_BAG: ItemBagBase = new ItemBagBase("fifth_bag", 4, 45) {
    setMaxDamage(150)
  }

  val FIRST_FABRIC: ItemUpdateBase = new ItemRepairFabric("first_fabric", 1)
  val SECOND_FABRIC = new ItemRepairFabric("second_fabric", 3)
  val THIRD_FABRIC = new ItemRepairFabric("third_fabric", 6)
  val FOURTH_FABRIC = new ItemRepairFabric("fourth_fabric", 10)
  val FIFTH_FABRIC = new ItemRepairFabric("fifth_fabric", 25)

  val SPEED_UPDATE = new ItemUpdatePerk("speed_update", "speed")
  val POWER_UPDATE = new ItemUpdatePerk("power_update", "power")

  list.foreach(item => {
    ForgeRegistries.ITEMS.register(item.onRegistry)
  })
}
