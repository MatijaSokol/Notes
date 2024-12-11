package com.matijasokol.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.matijasokol.notes.splash.SplashViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // this code block should be added just to trigger flow emission
        // if you don't have it, splashViewModel.loggedIn will be always null since it default value is null
        // revisit if there is workaround with using different SharingStarted value
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.loggedIn.collect {}
            }
        }

        splashScreen.setKeepOnScreenCondition { splashViewModel.loggedIn.value == null }

        setContent {
            splashViewModel.loggedIn.value?.let {
                AppContent(loggedIn = it)
            }
        }
    }
}
