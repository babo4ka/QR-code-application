package com.example.qrcodeapp.createQRActivity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.qrcodeapp.PremiumReminder
import com.example.qrcodeapp.R
import com.example.qrcodeapp.Reminder
import com.example.qrcodeapp.createQRActivity.ui.theme.QRCodeAppTheme
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.mainActivity.MainActivity
import com.example.qrcodeapp.mainActivity.Page
import java.time.Duration

class CreateQRMainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            QRCodeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreateQRActivityPage()
                }
            }
        }
    }
}


@Composable
fun CreateQRActivityPage() {

    val context = LocalContext.current

    val headerWeight = 1f
    val contentWeight = 6f
    val footerWeight = 3f

    val activeButton = remember {
        mutableStateOf(CurrentDataHandler.getQrTypeChoosed())
    }

    val textToEncode = remember {
        mutableStateOf(CurrentDataHandler.getTextEntered())
    }

    val createBtnGradient = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color.hsv(119f, .76f, .65f),
            0.8f to Color.hsv(190f, .79f, .94f)
        ),
        start = Offset(0f, 0f),
        end = Offset(500f, 500f)
    )

    val qrTypes = listOf("текст", "ссылка", "телеграм", "смс", "wifi")
    val painterResources = listOf(
        painterResource(id = R.drawable.text),
        painterResource(id = R.drawable.link),
        painterResource(id = R.drawable.telegram),
        painterResource(id = R.drawable.sms),
        painterResource(id = R.drawable.wifi)
    )
    val qrTypesEnum = listOf(QrType.TEXT, QrType.LINK, QrType.TG, QrType.SMS, QrType.WIFI)


    val smsDest = remember{
        mutableStateOf("")
    }

    val smsContent = remember{
        mutableStateOf("")
    }

    val wifiName = remember{
        mutableStateOf("")
    }

    val wifiPassWord = remember{
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    //.weight(headerWeight)

            ) {
                Button(
                    onClick = {
                        CurrentDataHandler.setMainActivityPage(Page.MAIN)
                        val intent = Intent(context, MainActivity::class.java)
                        ContextCompat.startActivity(context, intent, null)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = null
                    )
                }
                //Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Создать",
                        fontSize = 20.sp
                    )
                }

                //Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (textToEncode.value != "") {
                            CurrentDataHandler.setTextEntered(textToEncode.value)
                            val intent = Intent(context, CreateQRFinalActivity::class.java)
                            ContextCompat.startActivity(context, intent, null)
                        } else {
                            Toast.makeText(context, "Вы ничего не ввели!!", Toast.LENGTH_SHORT)
                                .show()
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.weight(2f).background(
                        brush = createBtnGradient,
                        shape = RoundedCornerShape(20)
                    )
                ) {
                    Text(text = "Создать")
                }
            }

            //content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(contentWeight)
            ) {

                when (activeButton.value) {
                    QrType.TEXT -> {
                        TextArea(
                            text = textToEncode.value,
                            label = "Текст",
                            placeholder = "Введите текст",
                            prefix = ""
                        ) {
                            textToEncode.value = it
                        }
                    }

                    QrType.LINK -> {
                        TextArea(
                            text = textToEncode.value,
                            label = "Ссылка",
                            placeholder = "Введите ссылку",
                            prefix = "https://"
                        ) {
                            textToEncode.value = it
                        }
                    }

                    QrType.TG -> {
                        TextArea(
                            text = textToEncode.value,
                            label = "Ссылка на телеграм",
                            placeholder = "Введите имя пользователя в телеграм",
                            prefix = "https://t.me/"
                        ) {
                            textToEncode.value = it
                        }
                    }

                    QrType.SMS -> {
                        Column ( modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(5.dp)){
                            TextField(
                                value = smsDest.value,
                                onValueChange = {
                                    smsDest.value = it
                                    textToEncode.value = "SMSTO:${smsDest.value}:${smsContent.value}"
                                },
                                label = { Text(text = "Кому") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                placeholder = { Text(text = "Введите адресата") }
                            )

                            TextField(
                                value = smsContent.value,
                                onValueChange = {
                                    smsContent.value = it
                                    textToEncode.value = "SMSTO:${smsDest.value}:${smsContent.value}"
                                },
                                label = { Text(text = "Сообщение") },
                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                placeholder = { Text(text = "Введите Сообщение") }
                            )
                        }
                    }
                    QrType.WIFI -> {
                        Column ( modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(5.dp)){
                            TextField(
                                value = wifiName.value,
                                onValueChange = {
                                    wifiName.value = it
                                    textToEncode.value = "WIFI:S:${wifiName.value};T:WPA2;P:${wifiPassWord.value};;"
                                },
                                label = { Text(text = "Название сети") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                placeholder = { Text(text = "Введите название сети") }
                            )

                            TextField(
                                value = wifiPassWord.value,
                                onValueChange = {
                                    wifiPassWord.value = it
                                    textToEncode.value = "WIFI:S:${wifiName.value};T:WPA2;P:${wifiPassWord.value};;"
                                },
                                label = { Text(text = "Пароль") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                placeholder = { Text(text = "Введите пароль") }
                            )
                        }
                    }
                }
            }

            //buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .weight(footerWeight)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                            .verticalScroll(state = rememberScrollState())
                    ) {

                        qrTypes.forEachIndexed{id, item ->
                            if(id < 3){
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    activeButton.value?.let {
                                        QRTypeButton(
                                            text = item,
                                            painter = painterResources[id],
                                            action = {
                                                CurrentDataHandler.setQrTypeChoosed(qrTypesEnum[id])
                                                activeButton.value = qrTypesEnum[id]
                                            },
                                            active = it,
                                            type = qrTypesEnum[id]
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                    ) {

                        qrTypes.forEachIndexed{id, item ->
                            if(id > 2){
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    activeButton.value?.let {
                                        QRTypeButton(
                                            text = item,
                                            painter = painterResources[id],
                                            action = {
                                                CurrentDataHandler.setQrTypeChoosed(qrTypesEnum[id])
                                                activeButton.value = qrTypesEnum[id]
                                            },
                                            active = it,
                                            type = qrTypesEnum[id]
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}


@Composable
fun QRTypeButton(
    text: String,
    painter: Painter,
    action: () -> Unit,
    type: QrType,
    active: QrType
) {

    val logoSize = 35.dp

    Button(modifier = Modifier
        .shadow(1.dp)
        .fillMaxSize(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active == type) Color.Black else Color.Transparent,
            contentColor = if (active == type) Color.White else Color.Black
        ),
        onClick = { action() }) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .width(logoSize)
                    .height(logoSize)
            )

            Text(
                text = text,
                fontSize = 12.sp
            )
        }

    }

}


@Composable
fun TextArea(
    text: String,
    prefix: String,
    label: String,
    placeholder: String,
    action: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = { action(it) },
        label = { Text(text = label) },
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        prefix = { Text(text = prefix) },
        placeholder = { Text(text = placeholder) }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreateQRActivityPagePreview() {
    QRCodeAppTheme {
        CreateQRActivityPage()
    }
}