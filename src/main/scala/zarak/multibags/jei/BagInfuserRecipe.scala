package zarak.multibags.jei

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import zarak.multibags.items.ItemUpdateBase

import scala.collection.JavaConverters.seqAsJavaListConverter

class BagInfuserRecipe(val bag: ItemStack, val update: ItemStack) extends IRecipeWrapper{
  val output:ItemStack = update.getItem.asInstanceOf[ItemUpdateBase].getResult(bag)

  override def getIngredients(ingredients: IIngredients): Unit = {
    ingredients.setInputs(classOf[ItemStack], List(bag,update).asJava)
    ingredients.setOutput(classOf[ItemStack], output)
  }

}
