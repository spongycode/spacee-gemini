package com.spongycode.spaceegemini.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.spongycode.spaceegemini.R
import com.spongycode.spaceegemini.navigation.TopBar

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutScreen(navController: NavHostController) {
    Scaffold(topBar = {
        TopBar(
            name = stringResource(id = R.string.about), navController = navController
        )
    }) {
        Column(
            Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center
        ) {
            Credits()
        }
    }
}

@Composable
fun Credits() {
    val uriHandler = LocalUriHandler.current

    val credits = buildAnnotatedString {
        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.primary)
        ) {
            append("Built by ")
        }
        pushStringAnnotation(tag = "person1", annotation = stringResource(id = R.string.person1))
        withStyle(
            style = SpanStyle(
                color = Color(0xFF267BC4),
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Himanshu")
        }
        pop()
        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.primary)
        ) {
            append(" & ")
        }
        pushStringAnnotation(tag = "person2", annotation = stringResource(id = R.string.person2))
        withStyle(
            style = SpanStyle(
                color = Color(0xFF267BC4),
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Vasanth")
        }
        pop()
    }

    val opensource = buildAnnotatedString {
        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.primary)
        ) {
            append("Explore the code on ")
        }
        pushStringAnnotation(
            tag = "opensource",
            annotation = stringResource(id = R.string.opensource)
        )
        withStyle(
            style = SpanStyle(
                color = Color(0xFF267BC4), textDecoration = TextDecoration.Underline
            )
        ) {
            append("GitHub")
        }
        pop()
    }



    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(0),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 10.dp, end = 10.dp, bottom = 30.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)

    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ClickableText(
                modifier = Modifier.padding(10.dp),
                text = credits,
                style = MaterialTheme.typography.titleMedium,
                onClick = { offset ->
                    credits.getStringAnnotations(
                        tag = "person1",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        uriHandler.openUri(it.item)
                    }

                    credits.getStringAnnotations(
                        tag = "person2",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        uriHandler.openUri(it.item)
                    }
                }
            )
            ClickableText(
                modifier = Modifier.padding(10.dp),
                text = opensource,
                style = MaterialTheme.typography.titleMedium,
                onClick = { offset ->
                    opensource.getStringAnnotations(
                        tag = "opensource",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        uriHandler.openUri(it.item)
                    }
                }
            )
        }
    }
}

