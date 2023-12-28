package com.spongycode.spaceegemini.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.spaceegemini.Mode
import com.spongycode.spaceegemini.R

@Composable
fun MessageItem(
    text: String,
    mode: Mode
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(5.dp)
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