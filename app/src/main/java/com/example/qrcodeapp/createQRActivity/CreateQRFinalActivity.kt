package com.example.qrcodeapp.createQRActivity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.qrcodeapp.R
import com.example.qrcodeapp.Reminder
import com.example.qrcodeapp.createQRActivity.pages.colorsAndBackgroundPage.ColorChoosePage
import com.example.qrcodeapp.createQRActivity.pages.logoChooseActivity.LogoChoosePage
import com.example.qrcodeapp.createQRActivity.pages.shapePage.ShapeChoosePage
import com.example.qrcodeapp.createQRActivity.ui.theme.QRCodeAppTheme
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.database.databases.QrDatabase
import com.example.qrcodeapp.database.viewModels.CreatedCodesViewModel
import com.example.qrcodeapp.database.viewModels.UserViewModel
import com.example.qrcodeapp.database.viewModels.factories.CreatedCodesViewModelFactory
import com.example.qrcodeapp.database.viewModels.factories.UserViewModelFactory
import qrcode.QRCode
import qrcode.QRCodeBuilder
import qrcode.color.Colors
import qrcode.shape.QRCodeShapeFunction

class CreateQRFinalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val dao = QrDatabase.getInstance(application).createdCodesDao
        val viewModelFactory = CreatedCodesViewModelFactory(dao)
        val ccvm = ViewModelProvider(this, viewModelFactory).get(CreatedCodesViewModel::class.java)

        setContent {
            QRCodeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QRCodeCreator(ccvm = ccvm)
                }
            }
        }
    }
}

@Composable
fun QRCodeCreator(ccvm: CreatedCodesViewModel?) {

    val qrColor = remember {
        mutableStateOf(Colors.WHITE)
    }

    val qrBackColor = remember {
        mutableStateOf(Colors.BLACK)
    }

    val qrShape = remember {
        mutableStateOf("Круги")
    }

    val qrLogo = remember {
        mutableStateOf("")
    }

    val qrBuilder = remember {
        mutableStateOf(
            QRCode.ofCircles()
                .withBackgroundColor(Colors.BLACK)
                .withColor(qrColor.value)
        )
    }

    val activeCustomizeOption = remember {
        mutableStateOf("Цвет")
    }

    val context = LocalContext.current
    val assets = context.resources.assets

    val headerWeight = 1f
    val contentWeight = 5f
    val footerWeight = 3f

    fun qrToBytes(): ByteArray? {
        return qrBuilder.value.build(CurrentDataHandler.getTextEntered()).render().getBytes()
    }

    fun qrCode(): ImageBitmap? {
        val bytes = qrToBytes()
        return bytes?.size?.let { BitmapFactory.decodeByteArray(bytes, 0, it).asImageBitmap() }
    }

    fun saveQrCodeToDb() {
        val bytes = qrToBytes()
        val user = CurrentDataHandler.getActiveUser()

        if (user != null) {
            if (bytes != null) {
                ccvm?.addCode(user.userLogin, bytes)

                Toast.makeText(context, "QR-код сохранён в истории", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun rebuildQr() {
        var builder: QRCodeBuilder? = null

        when (qrShape.value) {
            "Круги" -> builder = QRCode.ofCircles()
            "Квадраты" -> builder = QRCode.ofSquares()
            "Закругленные квадраты" -> builder = QRCode.ofRoundedSquares()
        }

        if (qrLogo.value != "") {
            val logoFile = assets.open(qrLogo.value)
            val byteArray = logoFile.readBytes()


            if (builder != null) {
                builder = builder.withLogo(byteArray, 128, 128, true)
            }
        }

        if (builder != null) {
            qrBuilder.value = builder
                .withBackgroundColor(qrBackColor.value)
                .withColor(qrColor.value)
        }
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


            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .weight(headerWeight)

            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, CreateQRMainActivity::class.java)
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
                        .weight(1f)
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
                        saveQrCodeToDb()
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Green
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Сохранить")
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(contentWeight)
            ) {

                qrCode()?.let { Image(bitmap = it, contentDescription = null) }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(footerWeight)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            ChooseOptionToCustomizeBtn(
                                action = { activeCustomizeOption.value = "Цвет" },
                                text = "Цвет",
                                active = activeCustomizeOption.value
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            ChooseOptionToCustomizeBtn(
                                action = { activeCustomizeOption.value = "Фон" },
                                text = "Фон",
                                active = activeCustomizeOption.value
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            ChooseOptionToCustomizeBtn(
                                action = { activeCustomizeOption.value = "Форма" },
                                text = "Форма",
                                active = activeCustomizeOption.value
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            ChooseOptionToCustomizeBtn(
                                action = { activeCustomizeOption.value = "Лого" },
                                text = "Лого",
                                active = activeCustomizeOption.value
                            )
                        }
                    }

                    Box(modifier = Modifier.weight(4f)) {
                        when (activeCustomizeOption.value) {
                            "Цвет" -> {
                                ColorChoosePage {
                                    qrColor.value = it
                                    rebuildQr()
                                }
                            }

                            "Фон" -> {
                                ColorChoosePage {
                                    qrBackColor.value = it
                                    rebuildQr()
                                }
                            }

                            "Форма" -> {
                                ShapeChoosePage {
                                    qrShape.value = it
                                    rebuildQr()
                                }
                            }

                            "Лого" -> {
                                LogoChoosePage {
                                    qrLogo.value = it
                                    rebuildQr()
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}


@Composable
fun ChooseOptionToCustomizeBtn(
    action: () -> Unit,
    text: String,
    active: String
) {
    Button(
        modifier = Modifier.fillMaxSize(),
        onClick = { action() },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active == text) Color.Black else Color.Transparent,
            contentColor = if (active == text) Color.White else Color.Black
        )
    ) {

        Text(text = text)
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreatorPreview() {
    QRCodeAppTheme {
        QRCodeCreator(ccvm = null)
    }
}