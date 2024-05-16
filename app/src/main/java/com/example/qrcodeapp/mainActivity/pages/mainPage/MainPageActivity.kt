package com.example.qrcodeapp.mainActivity.pages.mainPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.R


@Composable
fun MainPage(name: String, modifier: Modifier) {

    val qrLogoSize = 75.dp

    val btnGradient = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color.Red,
            0.8f to Color.Magenta
        ),
        start = Offset(0f, 0f),
        end = Offset(500f, 500f)
    )

    Box(modifier = modifier){
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()) {
            Button(modifier = Modifier
                .background(brush = btnGradient,
                    shape = RoundedCornerShape(25.dp))
                .height(200.dp)
                .width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black),
                onClick = { /*TODO*/ }) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(id = R.drawable.qr_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .width(qrLogoSize)
                            .height(qrLogoSize))

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(text = "Создать QR-код",
                        fontSize = 20.sp)
                }

            }
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainPagePrev (){
    MainPage(name = "fe",
        modifier = Modifier.fillMaxSize())
}