package com.example.qrcodeapp

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.qrcodeapp.createQRActivity.CreateQRMainActivity
import com.example.qrcodeapp.createQRActivity.QrType
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.mainActivity.MainActivity
import com.example.qrcodeapp.mainActivity.Page
import com.example.qrcodeapp.ui.theme.QRCodeAppTheme


@Composable
fun Reminder() {

    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(text = "Вы не авторизированы!", fontSize = 10.sp)
        Button(
            onClick = {
                CurrentDataHandler.setMainActivityPage(Page.ACCOUNT)
                val intent = Intent(context, MainActivity::class.java)
                ContextCompat.startActivity(context, intent, null)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            )
        ) {
            Text(text = "Вход/Регистрация", fontSize = 10.sp)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ReminderPreview() {
    QRCodeAppTheme {
        Reminder()
    }
}