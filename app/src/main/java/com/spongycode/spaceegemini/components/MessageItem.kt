package com.spongycode.spaceegemini.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.spaceegemini.Mode
import com.spongycode.spaceegemini.R
import com.spongycode.spaceegemini.ui.theme.DecentBlue
import com.spongycode.spaceegemini.ui.theme.DecentGreen


@Composable
fun MessageItem(
    text: String,
    mode: Mode
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val toast = Toast.makeText(context, stringResource(R.string.text_copied), Toast.LENGTH_LONG)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(100)),
                    painter = when (mode) {
                        Mode.GEMINI -> painterResource(id = R.drawable.gemini)
                        Mode.USER -> painterResource(id = R.drawable.user)
                    }, contentDescription = "logo"
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600, text = when (mode) {
                        Mode.GEMINI -> "GEMINI"
                        Mode.USER -> "YOU"
                    }
                )
            }

            TextButton(
                onClick = {
                    clipboardManager.setText(AnnotatedString(text))
                    toast.show()
                }
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    tint = (if (mode == Mode.USER) DecentBlue else DecentGreen),
                    painter = painterResource(id = R.drawable.baseline_content_copy_24),
                    contentDescription = "copy"
                )
                Text(
                    color = (if (mode == Mode.USER) DecentBlue else DecentGreen),
                    fontWeight = FontWeight.W600,
                    text = "COPY",
                    fontSize = 10.sp
                )
            }
        }
        SelectionContainer {
            Text(
                modifier = Modifier.padding(start = 35.dp),
                fontWeight = FontWeight.W500,
                fontSize = 15.sp,
                text = text
            )
        }
    }
}