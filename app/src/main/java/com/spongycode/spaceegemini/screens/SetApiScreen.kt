package com.spongycode.spaceegemini.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spongycode.spaceegemini.R
import com.spongycode.spaceegemini.data.MainViewModel
import com.spongycode.spaceegemini.navigation.MultiTurnMode
import com.spongycode.spaceegemini.navigation.SetApi
import com.spongycode.spaceegemini.ui.theme.DecentBlue
import com.spongycode.spaceegemini.ui.theme.DecentGreen
import com.spongycode.spaceegemini.ui.theme.DecentRed
import com.spongycode.util.datastore
import com.spongycode.util.getApiKey
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SetApiScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val validationState =
        viewModel.validationState.observeAsState().value
    val context = LocalContext.current

    var text by remember { mutableStateOf(TextFieldValue(runBlocking { context.datastore.getApiKey() })) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            singleLine = true,
            value = text,
            onValueChange = { newText ->
                text = newText
                viewModel.resetValidationState()
            },
            placeholder = { Text(text = "Enter your api key") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp),
            shape = RoundedCornerShape(28),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Black
            ),
            textStyle = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 18.sp
            )
        )

        Button(
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                if (validationState == MainViewModel.ValidationState.Valid) {
                    if (viewModel.isHomeVisit.value == true) {
                        navController.navigateUp()
                    } else {
                        navController.popBackStack(SetApi.route, true)
                        navController.navigate(MultiTurnMode.route)
                    }
                } else if (validationState == MainViewModel.ValidationState.Idle) {
                    viewModel.validate(context, text.text)
                }
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = when (validationState) {
                    MainViewModel.ValidationState.Checking -> Color.DarkGray
                    MainViewModel.ValidationState.Idle -> DecentBlue
                    MainViewModel.ValidationState.Invalid -> DecentRed
                    MainViewModel.ValidationState.Valid -> DecentGreen
                    null -> DecentBlue
                },
                contentColor = Color.White
            )
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = when (validationState) {
                    MainViewModel.ValidationState.Checking -> "Validating..."
                    MainViewModel.ValidationState.Idle -> "Validate"
                    MainViewModel.ValidationState.Invalid -> "Invalid Key"
                    MainViewModel.ValidationState.Valid -> "Proceed"
                    else -> "NULL"
                },
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.padding(50.dp))

        ApiSetupHelper()
    }
}

@Composable
fun ApiSetupHelper() {
    val uriHandler = LocalUriHandler.current

    val apiSetup = buildAnnotatedString {
        append("Learn how to set up your own API key. ")
        pushStringAnnotation(tag = "click", annotation = stringResource(id = R.string.api_setup_link))
        withStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Click here")
        }
        pop()
        append(" for details.")
    }

    Row(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            painter = painterResource(id = R.drawable.about_icon),
            contentDescription = "help"
        )
        ClickableText(
            modifier = Modifier.padding(10.dp),
            text = apiSetup,
            style = MaterialTheme.typography.titleMedium,
            onClick = { offset ->
                apiSetup.getStringAnnotations(
                    tag = "click",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    uriHandler.openUri(it.item)
                }
            }
        )
    }
}

