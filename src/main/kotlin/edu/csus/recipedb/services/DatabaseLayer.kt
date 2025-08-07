package edu.csus.recipedb.services

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import edu.csus.recipedb.framework.database.Driver
import edu.csus.recipedb.framework.database.MongoDatabase
import edu.csus.recipedb.framework.services.Service
import edu.csus.recipedb.services.objects.FavoritedRecipe
import edu.csus.recipedb.services.objects.SimpleRecipe
import edu.csus.recipedb.services.objects.json.FavoriteRecipeSerialContex

@Service.Database(driver = Driver.Type.MONGO, url = "SYS_PROP{mongodb.url}", username = "SYS_PROP{mongodb.username}", password = "SYS_PROP{mongodb.password}")
class DatabaseLayer: MongoDatabase() {

    private val gson: Gson = GsonBuilder().registerTypeAdapter(FavoritedRecipe::class.java, FavoriteRecipeSerialContex()).create()

    fun writeFavoriteToDB(recipe: FavoritedRecipe) {
        insert("productionDB", "favorites", """{_id: ${recipe.id}, "recipeName" : "${recipe.name}", "image": "${recipe.image}"}""")
    }

    fun readAllFavoritesFromDB(): Array<FavoritedRecipe> {
        val recipes = queryAll("productionDB", "favorites")
        val favorites = Array<FavoritedRecipe>(recipes.size) { gson.fromJson(recipes[it], FavoritedRecipe::class.java) }
        return favorites
    }

    fun removeFavoriteFromDB(id: Int) {
        delete("productionDB", "favorites", """{_id: $id}""")
    }
}