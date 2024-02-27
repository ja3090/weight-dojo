package com.example.weightdojo.components.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.weightdojo.R
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.inputs.Input
import com.example.weightdojo.components.inputs.InputArgs
import com.example.weightdojo.components.text.TextDefault
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DebouncingSearchBar(
    onSearch: (String) -> Unit,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    var textState by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var debounceJob by remember { mutableStateOf<Job?>(null) }

    Input(
        InputArgs(
            inputValue = textState,
            onValueChange = { newValue ->
                textState = newValue

                debounceJob?.cancel()

                debounceJob = coroutineScope.launch(dispatcher) {
                    delay(500)
                    onSearch(newValue)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            placeholder = { TextDefault(text = "Search")},
            trailingIcon = { IconBuilder(
                id = R.drawable.search,
                contentDescription = "Search bar",
                testTag = "SEARCH_BAR"
            )},
            textStyle = TextStyle(
                textAlign = TextAlign.Left
            ),
            modifier = Modifier.fillMaxWidth()
        )
    )
}
