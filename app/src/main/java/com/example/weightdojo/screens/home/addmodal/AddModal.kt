package com.example.weightdojo.screens.home.addmodal

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.*
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.components.mealcreation.MealCreation
import com.example.weightdojo.components.mealcreation.MealCreationOptions
import com.example.weightdojo.datatransferobjects.DayData
import com.example.weightdojo.datatransferobjects.SingleMealDetailed
import com.example.weightdojo.screens.home.HomeViewModel
import com.example.weightdojo.screens.home.addmodal.addweight.AddWeight
import com.example.weightdojo.screens.main.Screens
import java.util.UUID

@Composable
fun AddModal(
    homeViewModel: HomeViewModel = viewModel(),
    addModalVm: AddModalVm = viewModel()
) {
    Dialog(
        onDismissRequest = { homeViewModel.showModal(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        when (addModalVm.currentSubModal) {
            AddMenuSubModals.Initial -> Initial()
            AddMenuSubModals.AddWeight -> AddWeight(dayData = homeViewModel.getDayData())
            AddMenuSubModals.AddCalories -> MealCreation(
                detailedMeal = SingleMealDetailed(dayId = homeViewModel.getDayId()),
                openModal = {
                    addModalVm.backToInitial()
                    homeViewModel.showModal(it)
                },
                mealCreationOptions = MealCreationOptions.CREATING,
                onSubmit = homeViewModel::submitMeal,
                viewModel = homeViewModel
            )
        }
    }
}