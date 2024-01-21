package com.example.weightdojo.screens.home.addmodal.addcalories.searchmealtemplates

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weightdojo.database.AppDatabase
import com.example.weightdojo.database.models.MealTemplate
import com.example.weightdojo.repositories.MealTemplateRepo
import com.example.weightdojo.repositories.MealTemplateRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SearchMealTemplatesState(
    val mealTemplates: List<MealTemplate> = listOf(),
    val activeMealTemplate: MealTemplate? = null,
)

class SearchMealTemplatesVM(
    private val database: AppDatabase,
    private val mealTemplateRepo: MealTemplateRepo = MealTemplateRepoImpl(database.mealTemplateDao())
) : ViewModel() {

    var state by mutableStateOf(
        SearchMealTemplatesState()
    )

    fun makeActive(mealTemplate: MealTemplate) {
        val isActive = mealTemplate.mealTemplateId == state.activeMealTemplate?.mealTemplateId

        state = if (isActive) {
            state.copy(
                activeMealTemplate = null,
            )
        } else {
            state.copy(
                activeMealTemplate = mealTemplate,
            )
        }
    }

    fun setSearchResults(term: String) {
        if (term.isEmpty()) {
            state = state.copy(mealTemplates = listOf())
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    getSearchResults(term)
                } catch (e: Exception) {
                    Log.e("Error", e.message.toString())
                }
            }
        }
    }

    private suspend fun getSearchResults(term: String) {
        val searchResults = mealTemplateRepo.searchMealTemplates(term)

        withContext(Dispatchers.Main) {
            state = state.copy(mealTemplates = searchResults)
        }
    }
}