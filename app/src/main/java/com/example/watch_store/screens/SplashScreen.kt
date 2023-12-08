package com.example.watch_store.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.watch_store.R
import com.example.watch_store.utils.NavigationControl
import com.example.watch_store.viewmodel.SharedViewModel


@Composable
fun SplashScreen(sharedViewModel: SharedViewModel, navigationControl: NavigationControl) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Blue, Color.Magenta
                )
            )
        ), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        val offset = Offset(5.0f, 10.0f)
        Text(text = "PostAPP", style = TextStyle(brush = Brush.linearGradient(colors = listOf(
            Color.Blue,Color.Red, Color.Magenta
        )),
            shadow = Shadow(
                color = Color.Black, offset = offset, blurRadius = 3f
            )
        ), fontSize = 30.sp

        )




        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.first_page))

        LottieAnimation(
            modifier = Modifier
                .width(180.dp)
                .height(180.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )

    }
}