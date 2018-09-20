package zarak.multibags

import java.util.UUID

import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import zarak.multibags.api.Reference

@EventBusSubscriber(modid = Reference.ID)
object Events {
  val speed: UUID = UUID.fromString("03f5d5ad-6610-4a01-ab5b-34b98f49d065")
  val speedAtr = new AttributeModifier(speed, "speed", 1.0221D, 1)

  @SubscribeEvent
  def tick(event: TickEvent.PlayerTickEvent): Unit = {
    if (event.phase == TickEvent.Phase.END) {
      if (event.side == Side.SERVER) {
        val player = event.player
        val map = player.getAttributeMap
        val speedIns = map.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED)
        if (MultiBags.hasBagBauble(player) && MultiBags.getBagBauble(player).hasTagCompound) {
          val bag = MultiBags.getBagBauble(player)
          val nbt = bag.getTagCompound
          if (nbt.getCompoundTag("perks").getBoolean("speed")) {
            if (!speedIns.hasModifier(speedAtr))
              speedIns.applyModifier(speedAtr)
          }
          else {
            if (speedIns.hasModifier(speedAtr))
              speedIns.removeModifier(speedAtr)
          }
        }
        else clearAttributes(player)
      }
    }
  }

  @SubscribeEvent
  def attack(event: LivingHurtEvent): Unit = {
    if (!event.getEntityLiving.world.isRemote) {
      event.getSource.getTrueSource match {
        case player: EntityPlayer =>
          if (MultiBags.hasBagBauble(player) && MultiBags.getBagBauble(player).hasTagCompound) {
            val bag = MultiBags.getBagBauble(player)
            val nbt = bag.getTagCompound
            if (nbt.getCompoundTag("perks").getBoolean("power")) {
             event.setAmount(event.getAmount*1.2F)
            }
          }
        case _ =>
      }
    }
  }

  def clearAttributes(player: EntityPlayer): Unit = {
    val map = player.getAttributeMap
    val speedIns = map.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED)
    if (speedIns.hasModifier(speedAtr))
      speedIns.removeModifier(speedAtr)
  }
}
