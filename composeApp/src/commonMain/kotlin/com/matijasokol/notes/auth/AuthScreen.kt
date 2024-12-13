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
import com.matijasokol.notes.ui.components.Toast
import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.auth_email
import notes.composeapp.generated.resources.auth_login
import notes.composeapp.generated.resources.auth_login_bottom
import notes.composeapp.generated.resources.auth_login_top
import notes.composeapp.generated.resources.auth_password
import notes.composeapp.generated.resources.auth_register
import notes.composeapp.generated.resources.auth_register_bottom
import notes.composeapp.generated.resources.auth_register_top
import notes.composeapp.generated.resources.or
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun AuthScreen(
    state: AuthState,
    toast: Toast = koinInject(),
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
                        text = stringResource(Res.string.auth_login_top),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 20.dp),
                    )
                    AuthType.Registration -> Text(
                        text = stringResource(Res.string.auth_register_top),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 20.dp),
                    )
                }
            }

            EmailTextField(
                value = state.email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onEvent(AuthEvent.EmailChanged(state.authType, it)) },
                labelValue = stringResource(Res.string.auth_email),
            )

            PasswordTextField(
                value = state.password,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onEvent(AuthEvent.PasswordChanged(state.authType, it)) },
                valueVisible = state.passwordVisible,
                onValueVisibleToggle = { onEvent(AuthEvent.TogglePasswordVisibility) },
                labelValue = stringResource(Res.string.auth_password),
            )

            Spacer(modifier = Modifier.weight(1f))

            BottomAuthComponent(
                buttonText = when (state.authType) {
                    AuthType.Login -> stringResource(Res.string.auth_login)
                    AuthType.Registration -> stringResource(Res.string.auth_register)
                },
                spacerText = stringResource(Res.string.or),
                onButtonClick = {
                    when (state.authType) {
                        AuthType.Login -> onEvent(AuthEvent.LoginClicked(state.email, state.password))
                        AuthType.Registration -> onEvent(AuthEvent.RegistrationClicked(state.email, state.password))
                    }
                },
                onGoogleClick = { toast.show("Not available at the moment") },
                bottomText = when (state.authType) {
                    AuthType.Login -> stringResource(Res.string.auth_login_bottom)
                    AuthType.Registration -> stringResource(Res.string.auth_register_bottom)
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
