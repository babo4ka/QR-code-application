package com.example.qrcodeapp.widget.creator

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.background
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import com.example.qrcodeapp.R
import com.example.qrcodeapp.createQRActivity.CreateQRMainActivity
import com.example.qrcodeapp.createQRActivity.pages.colorsAndBackgroundPage.ui.theme.QRCodeAppTheme
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.mainActivity.MainActivity
import com.example.qrcodeapp.mainActivity.Page


class QrWidget : GlanceAppWidget(){

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            GlanceTheme {
                QrWidgetContent(context)
            }
        }
    }
}

@Composable
fun QrWidgetContent(context: Context) {
    val qrLogoSize = 75.dp

    val btnGradient = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color.Red,
            0.8f to Color.Magenta
        ),
        start = Offset(0f, 0f),
        end = Offset(500f, 500f)
    )


    Box(modifier = GlanceModifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier.fillMaxSize()
        ) {

            Button(modifier = GlanceModifier
                .height(50.dp)
                .width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = GlanceTheme.colors.primary,
                    contentColor = GlanceTheme.colors.onPrimary
                ),
                text = "создать QR-код", onClick = {
                val intent = Intent(context, CreateQRMainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextCompat.startActivity(context, intent, null)
            })

            Button(modifier = GlanceModifier
                .height(50.dp)
                .width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = GlanceTheme.colors.primary,
                    contentColor = GlanceTheme.colors.onPrimary
                ),
                text = "отсканировать QR-код", onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    CurrentDataHandler.setMainActivityPage(Page.SCANNER)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    ContextCompat.startActivity(context, intent, null)
                })



        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun QrCreatorWidgetPreview() {
//    QRCodeAppTheme {
//        QrWidgetContent()
//    }
//}