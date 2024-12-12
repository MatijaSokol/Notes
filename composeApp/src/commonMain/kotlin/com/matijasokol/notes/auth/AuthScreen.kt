package com.matijasokol.notes.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.auth_text_email
import notes.composeapp.generated.resources.auth_text_login
import notes.composeapp.generated.resources.auth_text_password
import org.jetbrains.compose.resources.stringResource

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
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = state.email,
                onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
                placeholder = {
                    Text(
                        text = stringResource(Res.string.auth_text_email),
                    )
                },
            )
            TextField(
                value = state.password,
                onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
                placeholder = {
                    Text(
                        text = stringResource(Res.string.auth_text_password),
                    )
                },
            )

            Button(
                onClick = {
                    onEvent(
                        AuthEvent.LoginClicked(
                            email = state.email,
                            password = state.password,
                        ),
                    )
                },
            ) {
                Text(
                    text = stringResource(Res.string.auth_text_login),
                )
            }

            // Text(
            //     text = stringResource(Res.string.or),
            // )

            // GoogleSignInButton(
            //     onClick = { onEvent(AuthEvent.GoogleSignInClicked) },
            // )
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
