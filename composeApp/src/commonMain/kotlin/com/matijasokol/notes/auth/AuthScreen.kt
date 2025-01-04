package com.matijasokol.notes.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matijasokol.notes.ui.components.EmailTextField
import com.matijasokol.notes.ui.components.PasswordTextField

@Composable
fun AuthScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedContent(targetState = state.authType) {
                when (state.authType) {
                    AuthType.Login -> Text(
                        text = state.loginTopText,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 20.dp),
                    )
                    AuthType.Registration -> Text(
                        text = state.registerTopText,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 20.dp),
                    )
                }
            }

            EmailTextField(
                value = state.email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onEvent(AuthEvent.EmailChanged(state.authType, it)) },
                labelValue = state.emailLabel,
            )

            PasswordTextField(
                value = state.password,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onEvent(AuthEvent.PasswordChanged(state.authType, it)) },
                valueVisible = state.passwordVisible,
                onValueVisibleToggle = { onEvent(AuthEvent.TogglePasswordVisibility) },
                labelValue = state.passwordLabel,
            )

            Spacer(modifier = Modifier.weight(1f))

            BottomAuthComponent(
                buttonText = when (state.authType) {
                    AuthType.Login -> state.loginLabel
                    AuthType.Registration -> state.registerLabel
                },
                spacerText = state.typeSpacerText,
                onButtonClick = {
                    when (state.authType) {
                        AuthType.Login -> onEvent(AuthEvent.LoginClicked(state.email, state.password))
                        AuthType.Registration -> onEvent(AuthEvent.RegistrationClicked(state.email, state.password))
                    }
                },
                onGoogleClick = { onEvent(AuthEvent.GoogleSignInClicked) },
                bottomText = when (state.authType) {
                    AuthType.Login -> state.loginBottomText
                    AuthType.Registration -> state.registerBottomText
                },
                onBottomTextClick = { onEvent(AuthEvent.ToggleAuthType) },
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
