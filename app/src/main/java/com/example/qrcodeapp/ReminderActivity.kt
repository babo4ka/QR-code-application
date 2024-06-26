package com.example.qrcodeapp

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.mainActivity.MainActivity
import com.example.qrcodeapp.mainActivity.Page
import com.example.qrcodeapp.ui.theme.QRCodeAppTheme


@Composable
fun Reminder() {

    val context = LocalContext.current
    val openModal = remember{
        mutableStateOf(false)
    }

    val dialogText:String = "Войдите в аккаунт или зарегистрируйтесь если у Вас его нет, " +
            "чтобы получить доступ к дополнительным функциям, таким как: " +
            "сохранение QR-кодов в базе данных и больший выбор кастомизации"

    when(openModal.value){
        true ->
            ModalDialog(title = "Вход или регистрация",
                text = dialogText,
                confirmText = "Вход/Регистрация",
                onDismissRequest = {
                    openModal.value = false
                }
            ) {
                openModal.value = false
                CurrentDataHandler.setMainActivityPage(Page.ACCOUNT)
                val intent = Intent(context, MainActivity::class.java)
                ContextCompat.startActivity(context, intent, null)
            }
        else -> {}
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                openModal.value = true
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            )
        ) {
            Image(
                painterResource(id = R.drawable.info),
                contentDescription = null
            )
        }


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
