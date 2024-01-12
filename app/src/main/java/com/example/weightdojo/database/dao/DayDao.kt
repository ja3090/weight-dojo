package com.example.weightdojo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.weightdojo.database.models.Day
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.datatransferobjects.MealData
import java.time.LocalDate

@Dao
interface DayDao {
    @Transaction
    fun getDay(date: LocalDate): DayData {
        var day = getDayByDate(date)

        if (day == null) {
            val id = insert(Day(date = date))
            day = getDayById(id)
        }

        day as Day

        val meals = getMeals(day.id)

        return DayData(day = day, meals = meals)
    }

    @Query(
    "SELECT SUM(calorie.total_calories) as totalCalories, meal_id as mealId, " +
            "meal.name as mealName, day_id as dayId " +
            "FROM day " +
            "JOIN calorie ON calorie.day_id = day.id " +
            "JOIN meal ON calorie.meal_id = meal.id " +
            "WHERE day.id = :dayId " +
            "GROUP BY calorie.meal_id "
    )
    fun getMeals(dayId: Long): List<MealData>?

    @Query(
        "SELECT * FROM day " +
                "WHERE date = :date"
    )
    fun getDayByDate(date: LocalDate): Day?

    @Query(
        "SELECT * FROM day " +
                "WHERE id = :id "
    )
    fun getDayById(id: Long): Day?

    @Insert
    fun insert(day: Day): Long

    @Deprecated("Used for testing purposes. Do not use anywhere")
    @Query("DELETE FROM day")
    fun _DELETE_ALL()

    @Query(
        "UPDATE day " +
                "SET weight = :weight " +
                "WHERE id = :dayId"
    )
    fun setWeight(weight: Float, dayId: Long)

    @Query(
        "SELECT * FROM day " +
                "WHERE weight IS NOT NULL " +
                "ORDER BY date DESC " +
                "LIMIT 1 "
    )
    fun getMostRecentWeight(): Day?
}