package edu.csus.recipedb.services.objects.json

import com.google.gson.*
import edu.csus.recipedb.services.objects.FavoritedRecipe
import java.lang.reflect.Type

class FavoriteRecipeSerialContex: JsonSerializer<FavoritedRecipe>, JsonDeserializer<FavoritedRecipe> {
    override fun serialize(recipe: FavoritedRecipe, type: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("_id", JsonPrimitive(recipe.id))
        obj.add("recipeName", JsonPrimitive(recipe.name))
        obj.add("image", JsonPrimitive(recipe.image))
        return obj
    }

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): FavoritedRecipe {
        if (element.isJsonObject) {
            val obj = element.asJsonObject
            if (obj.has("_id")) {
                val recipeName = if (obj.has("recipeName")) { obj.get("recipeName").asString } else { "" }
                val image = if (obj.has("image")) { obj.get("image").asString } else { "" }
                return FavoritedRecipe(obj.get("_id").asInt, recipeName, image)
            }
        }
        return FavoritedRecipe(-1, "", "")
    }
}