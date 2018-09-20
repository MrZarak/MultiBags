package zarak.multibags.jei

import com.google.common.collect.Lists
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack

import scala.collection.JavaConverters.asJavaIterableConverter
import scala.collection.mutable.ListBuffer

class BagCraftingRecipe(val items: ListBuffer[ItemStack], val result: ItemStack) extends IRecipeWrapper{

  override def getIngredients(ingredients: IIngredients): Unit = {
    ingredients.setInputs(classOf[ItemStack], Lists.newArrayList(items.toList.asJava))
    ingredients.setOutput(classOf[ItemStack], result)
  }

}
