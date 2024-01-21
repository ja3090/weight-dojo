package com.example.weightdojo.database.dao.seeder

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Ingredient
import com.example.weightdojo.database.models.IngredientTemplate
import com.example.weightdojo.database.models.Meal
import com.example.weightdojo.database.models.MealIngredientTemplate
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.datatransferobjects.MealWithIngredients

@Dao
interface SeedMealTemplateDao {
    @Query(
        "SELECT * FROM meal " +
                "WHERE id = :id "
    )
    fun getMeal(id: Long): Meal

    @Query(
        "SELECT * FROM ingredient " +
                "WHERE meal_id = :mealId "
    )
    fun getIngredientsByMealId(mealId: Long): List<Ingredient>
    @Transaction
    fun getMealWithIngredients(mealId: Long): MealWithIngredients {
        val meal = getMeal(mealId)
        val ingredients = getIngredientsByMealId(mealId)

        return MealWithIngredients(
            meal = meal,
            ingredient = ingredients
        )
    }

    @Transaction
    fun handleTemplateInsertion(mealId: Long) {
        val (meal, ingredients) = getMealWithIngredients(mealId)

        val mealTemplateId = insertMealTemplate(
            MealTemplate(
                totalCalories = meal.totalCalories,
                totalCarbohydrates = meal.totalCarbohydrates,
                totalFat = meal.totalFat,
                totalProtein = meal.totalProtein,
                name = meal.name
            )
        )

        for (ingredient in ingredients) {
            val ingredientId = insertIngredientTemplate(
                IngredientTemplate(
                    name = ingredient.name,
                    grams = ingredient.grams,
                    caloriesPer100 = ingredient.caloriesPer100,
                    carbohydratesPer100 = ingredient.carbohydratesPer100,
                    fatPer100 = ingredient.fatPer100,
                    proteinPer100 = ingredient.proteinPer100,
                )
            )

            insertMealIngredientJunction(
                MealIngredientTemplate(
                    mealTemplateId = mealTemplateId,
                    ingredientTemplateId = ingredientId
                )
            )
        }
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMealIngredientJunction(mealIngredientTemplate: MealIngredientTemplate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredientTemplate(ingredientTemplate: IngredientTemplate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMealTemplate(mealTemplate: MealTemplate): Long
}