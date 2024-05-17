package com.example.qrcodeapp.createQRActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.qrcodeapp.R
import com.example.qrcodeapp.createQRActivity.ui.theme.QRCodeAppTheme
import com.example.qrcodeapp.mainActivity.MainActivity

class CreateQRMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreateQRActivityPage("Android")
                }
            }
        }
    }
}

@Composable
fun CreateQRActivityPage(name: String, modifier: Modifier = Modifier) {


    val context = LocalContext.current

    val headerWeight = 1f
    val contentWeight = 7f
    val footerWeight = 3f

    val activeButton = remember {
        mutableStateOf("Текст")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
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
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Green
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Создать")
                }
            }

            //content
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(contentWeight)) {

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
                    ) {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            QRTypeButton(
                                text = "Текст",
                                painter = painterResource(id = R.drawable.text),
                                action = { activeButton.value = "Текст" },
                                active = activeButton.value
                            )
                        }


                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            QRTypeButton(
                                text = "Ссылка",
                                painter = painterResource(id = R.drawable.link),
                                action = { activeButton.value = "Ссылка" },
                                active = activeButton.value
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            QRTypeButton(
                                text = "Телеграм",
                                painter = painterResource(id = R.drawable.telegram),
                                action = { activeButton.value = "Телеграм" },
                                active = activeButton.value
                            )
                        }

                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            QRTypeButton(
                                text = "Картинка",
                                painter = painterResource(id = R.drawable.img),
                                action = { activeButton.value = "Картинка" },
                                active = activeButton.value
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            QRTypeButton(
                                text = "Файл",
                                painter = painterResource(id = R.drawable.file),
                                action = { activeButton.value = "Файл" },
                                active = activeButton.value
                            )
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
    active: String
) {

    val logoSize = 35.dp

    Button(modifier = Modifier
        .shadow(1.dp)
        .fillMaxSize(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active == text) Color.Black else Color.Transparent,
            contentColor = if (active == text) Color.White else Color.Black
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreateQRActivityPagePreview() {
    QRCodeAppTheme {
        CreateQRActivityPage("Android")
    }
}