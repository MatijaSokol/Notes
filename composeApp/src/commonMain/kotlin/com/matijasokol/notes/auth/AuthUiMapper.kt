package com.matijasokol.notes.auth

import com.matijasokol.notes.ui.dictionary.Dictionary
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

class AuthUiMapper(private val dictionary: Dictionary) {

    suspend fun toUiState(
        email: String,
        password: String,
        passwordVisible: Boolean,
        isLoading: Boolean,
        authType: AuthType,
    ) = AuthState(
        email = email,
        password = password,
        passwordVisible = passwordVisible,
        isLoading = isLoading,
        authType = authType,
        loginTopText = dictionary.getString(Res.string.auth_login_top.key),
        loginBottomText = dictionary.getString(Res.string.auth_login_bottom.key),
        registerTopText = dictionary.getString(Res.string.auth_register_top.key),
        registerBottomText = dictionary.getString(Res.string.auth_register_bottom.key),
        emailLabel = dictionary.getString(Res.string.auth_email.key),
        passwordLabel = dictionary.getString(Res.string.auth_password.key),
        loginLabel = dictionary.getString(Res.string.auth_login.key),
        registerLabel = dictionary.getString(Res.string.auth_register.key),
        typeSpacerText = dictionary.getString(Res.string.or.key),
    )
}
