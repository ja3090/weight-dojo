package com.example.weightdojo.screens.lock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weightdojo.MyApp
import com.example.weightdojo.R
import com.example.weightdojo.components.Loading
import com.example.weightdojo.components.icon.IconBuilder
import com.example.weightdojo.components.keypad.Keypad
import com.example.weightdojo.database.models.Config
import com.example.weightdojo.utils.Biometrics
import com.example.weightdojo.utils.VMFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun Lock(
    config: Config,
    context: FragmentActivity,
    canUseBiometrics: Boolean = Biometrics.canUseBiometrics(context),
    redirectToHome: () -> Unit,
    lockViewModel: LockViewModel = viewModel(factory = VMFactory.build {
        LockViewModel(config = config)
    }),
    isAuthenticated: Boolean,

    ) {
    if (
        config.bioEnabled &&
        canUseBiometrics &&
        !isAuthenticated &&
        !lockViewModel.state.declineBiometrics
    ) {
        Biometrics.authenticateWithBiometric(context,
            onSuccess = {
                lockViewModel.setIsLoading(true)
                redirectToHome()
            },
            onFail = {},
            onError = { lockViewModel.declineBiometrics(true) })
    }
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {

        Keypad(
            keyClick = lockViewModel::addInput,
            delete = lockViewModel::delete,
            submit = {
                if (lockViewModel.state.loading) return@Keypad

                lockViewModel.viewModelScope.launch {
                    val passes = async { lockViewModel.submit() }

                    if (passes.await()) redirectToHome()
                }
            },
            inputValue = lockViewModel.state.passcode,
            promptText = "Enter passcode",
            leftOfZeroCustomBtn = if (canUseBiometrics && config.bioEnabled) {
                {
                    IconBuilder(
                        id = R.drawable.fingerprint,
                        contentDescription = "Bring up fingerprint access",
                        testTag = "Fingerprint",
                        modifier = Modifier
                            .clickable {
                                lockViewModel.declineBiometrics(false)
                            }
                            .then(it)
                    )
                }
            } else {
                null
            },
            isLoading = lockViewModel.state.loading
        )
    }
}