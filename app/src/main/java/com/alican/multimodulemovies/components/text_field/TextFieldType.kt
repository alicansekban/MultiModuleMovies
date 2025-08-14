package com.alican.multimodulemovies.components.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alican.multimodulemovies.theme.AppTheme

@Composable
fun DynamicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLength: Int? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    shape: Shape = RoundedCornerShape(12.dp)
) {

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (maxLength == null || newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = label
                )
            }
        },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier,
        singleLine = singleLine,
        maxLines = maxLines,
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } },
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppTheme.colorScheme.primaryButton,
            focusedLabelColor = AppTheme.colorScheme.primaryButton,
            unfocusedBorderColor = AppTheme.colorScheme.secondaryText.copy(alpha = 0.7f),
            unfocusedLabelColor = AppTheme.colorScheme.secondaryText,
            focusedTextColor = AppTheme.colorScheme.primaryText,
            unfocusedTextColor = AppTheme.colorScheme.primaryText,
            cursorColor = AppTheme.colorScheme.primaryButton,
            focusedLeadingIconColor = AppTheme.colorScheme.primaryButton,
            unfocusedLeadingIconColor = AppTheme.colorScheme.secondaryText,
            focusedTrailingIconColor = AppTheme.colorScheme.primaryButton,
            unfocusedTrailingIconColor = AppTheme.colorScheme.secondaryText,
            errorBorderColor = AppTheme.colorScheme.errorColor,
            errorLabelColor = AppTheme.colorScheme.errorColor,
            errorLeadingIconColor = AppTheme.colorScheme.errorColor,
            errorTrailingIconColor = AppTheme.colorScheme.errorColor,
            errorSupportingTextColor = AppTheme.colorScheme.errorColor,
            errorTextColor = AppTheme.colorScheme.primaryText
        )
    )
}

// Helper composable for password trailing icon
@Composable
fun PasswordTrailingIcon(
    isVisible: Boolean,
    onToggle: () -> Unit
) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = if (isVisible) "Hide password" else "Show password"
        )
    }
}

// Preview Composables
@Preview(name = "Light Mode - Email Field")
@Composable
private fun DynamicTextFieldEmailPreview() {
    AppTheme {
        var email by remember { mutableStateOf("user@example.com") }
        DynamicTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            leadingIcon = Icons.Default.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            maxLength = 50
        )
    }
}

@Preview(name = "Light Mode - Password Field")
@Composable
private fun DynamicTextFieldPasswordPreview() {
    AppTheme {
        var password by remember { mutableStateOf("mypassword123") }
        var isVisible by remember { mutableStateOf(false) }

        DynamicTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            leadingIcon = Icons.Default.Lock,
            trailingIcon = {
                PasswordTrailingIcon(
                    isVisible = isVisible,
                    onToggle = { isVisible = !isVisible }
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            maxLength = 20
        )
    }
}

@Preview(name = "Dark Mode - Email Field")
@Composable
private fun DynamicTextFieldEmailDarkPreview() {
    AppTheme(isDarkMode = true) {
        var email by remember { mutableStateOf("user@example.com") }
        DynamicTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            leadingIcon = Icons.Default.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
    }
}