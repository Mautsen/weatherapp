package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.dp

val AntonRegular = FontFamily(
    Font(R.font.anton_regular)
)
val antonRegular = TextStyle(
    fontFamily = AntonRegular,
    fontWeight = FontWeight.Bold,
    color = Color(0xBF4C76DF),
    fontSize = 72.sp,
    lineHeight = 60.sp,
    letterSpacing = 0.5.sp,
    shadow = Shadow(
        color = Color.Black,
        offset = Offset(4f, 4f),
        blurRadius = 4f
    )
)

val ValeraRound = FontFamily(
    Font(R.font.valera_round_regular, FontWeight.Bold, FontStyle.Normal)
)
val valeraRound = TextStyle(
    fontFamily = ValeraRound,
    fontWeight = FontWeight.Bold,
    color = Color(0xFF262547),
    fontSize = 72.sp,
    lineHeight = 60.sp,
    letterSpacing = 0.25.sp,
    shadow = Shadow(
        color = Color.Black,
        offset = Offset(4f, 4f),
        blurRadius = 4f
    )
)
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

)
