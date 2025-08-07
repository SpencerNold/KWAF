package edu.csus.recipedb.services

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import edu.csus.recipedb.framework.Http
import edu.csus.recipedb.framework.Resource
import edu.csus.recipedb.framework.Route
import edu.csus.recipedb.framework.services.Implementation
import edu.csus.recipedb.framework.services.Service
import edu.csus.recipedb.services.objects.FavoritedRecipe
import java.io.InputStream
import java.io.File

@Service.Controller
class RootController: Implementation() {

    @Route.File(path = "/")
    fun root(): InputStream? {
        return Resource.get("index.html")
    }

    @Route.File(path = "/script.js")
    fun script(): InputStream?{
        return Resource.get("script.js")
    }

    @Route.File(path = "/recipe.html")
    fun recipeHtml(): InputStream?{
        return Resource.get("recipe.html")
    }

    @Route.File(path = "/recipe.js")
    fun recipeJs(): InputStream?{
        return Resource.get("recipe.js")
    }

    @Route.File(path = "/favorites.html")
    fun favoritesHtml(): InputStream?{
        return Resource.get("favorites.html")
    }
    
    @Route.File(path = "/favorites.js")
    fun favoritesJs(): InputStream?{
        return Resource.get("favorites.js")
    }

    @Route(Http.Method.POST, "/save_favorite_recipe", input = true)
    fun saveFavoriteRecipe(request: FavoritedRecipe) {
        getService(DatabaseLayer::class.java).writeFavoriteToDB(request)
    }

    @Route(Http.Method.GET, "/get_favorite_recipes")
    fun getFavoriteRecipes(): Array<FavoritedRecipe> {
        return getService(DatabaseLayer::class.java).readAllFavoritesFromDB()
    }

    @Route(Http.Method.DELETE, "/remove_favorite_recipe", input = true)
    fun removeFavoriteRecipe(request: JsonObject) {
        if (!request.has("id"))
            return
        val idElement = request.get("id")
        if (!idElement.isJsonPrimitive)
            return
        getService(DatabaseLayer::class.java).removeFavoriteFromDB(idElement.asJsonPrimitive.asInt)   
    }
}