package com.example.qrcodeapp.mainActivity.pages.mainPage

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.qrcodeapp.PremiumReminder
import com.example.qrcodeapp.R
import com.example.qrcodeapp.Reminder
import com.example.qrcodeapp.createQRActivity.CreateQRMainActivity
import com.example.qrcodeapp.createQRActivity.QrType
import com.example.qrcodeapp.database.CurrentDataHandler


@Composable
fun MainPage(modifier: Modifier) {

    val qrLogoSize = 75.dp

    val btnGradient = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color.Red,
            0.8f to Color.Magenta
        ),
        start = Offset(0f, 0f),
        end = Offset(500f, 500f)
    )

    val context = LocalContext.current

    Box(modifier = modifier) {
        if(CurrentDataHandler.getActiveUser() == null){
            Reminder()
        }

        if(CurrentDataHandler.getActiveUser() != null
            && !CurrentDataHandler.getActiveUser()?.premium!!){
            PremiumReminder()
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {

            Button(modifier = Modifier
                .background(
                    brush = btnGradient,
                    shape = RoundedCornerShape(25.dp)
                )
                .height(200.dp)
                .width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                onClick = {
                    val intent = Intent(context, CreateQRMainActivity::class.java)
                    ContextCompat.startActivity(context, intent, null)
                }) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.qr_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .width(qrLogoSize)
                            .height(qrLogoSize)
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = "Создать QR-код",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }

            }
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainPagePrev() {
    MainPage(modifier = Modifier.fillMaxSize())
}