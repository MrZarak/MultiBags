package zarak.multibags.init

import com.zarak.zaraklib.item.ItemBase
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumParticleTypes
import net.minecraftforge.fml.common.registry.ForgeRegistries
import zarak.multibags.items.{ItemBagBase, ItemRepairFabric, ItemUpdateBase, ItemUpdatePerk}

import scala.collection.mutable.ListBuffer

object BagItems {
  final val list = new ListBuffer[ItemBase]

  val ENDER_BAG: ItemBagBase = new ItemBagBase("ender_bag", -1, 27) {
    setMaxDamage(45)

    override def onWornTick(itemstack: ItemStack, player: EntityLivingBase): Unit = {
      if(player.world.isRemote) {
        val rand = player.world.rand
        val yaw = (player.rotationYaw%360+90) / 180 * math.Pi
        val cx = player.posX - math.cos(yaw)*0.5 + player.motionX*10
        val cz = player.posZ - math.sin(yaw)*0.5 + player.motionZ*10
        if(rand.nextInt(10)==0)
          for (_ <- 0 to 2) {
            val j = rand.nextInt(2) * 2 - 1
            val k = rand.nextInt(2) * 2 - 1
            val d0 = cx + 0.1 * j.toDouble
            val d1 = (player.posY.toFloat + rand.nextFloat).toDouble + 0.5
            val d2 = cz + 0.1 * k.toDouble
            val d3 = (rand.nextFloat * j.toFloat).toDouble
            val d4 = (rand.nextFloat.toDouble - 0.5D) * 0.125D
            val d5 = (rand.nextFloat * k.toFloat).toDouble
            player.world.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5)
          }

      }

    }
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
