package com.example.weightdojo.database.dao

import com.example.weightdojo.database.models.Date
import java.time.LocalDate


interface ChartDao {

    fun insertDateRange(dates: List<Date>)

    fun deleteAll()

    fun getRangeByDay(from: LocalDate, to: LocalDate): List<ChartData>

    fun getRangeByMonth(): List<ChartData>

    fun getEarliestWeightDate(): EarliestDate
}