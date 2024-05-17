package com.example.qrcodeapp.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.R
import com.example.qrcodeapp.mainActivity.pages.accountPage.AccountPage
import com.example.qrcodeapp.mainActivity.pages.mainPage.MainPage
import com.example.qrcodeapp.ui.theme.QRCodeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityPage("Android")
                }
            }
        }
    }
}

private enum class Page {
    MAIN, SCANNER, ACCOUNT
}

@Composable
fun MainActivityPage(name: String, modifier: Modifier = Modifier) {

    val page = remember {
        mutableStateOf(Page.MAIN)
    }

    val active = remember {
        mutableStateOf("главная")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            when (page.value) {
                Page.MAIN -> {
                    MainPage(
                        name = "fe",
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                }

                Page.SCANNER -> TODO()
                Page.ACCOUNT -> {
                    AccountPage(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    MenuButton(text = "главная", painterResource(id = R.drawable.home),
                        active = active.value,
                        action = {
                            page.value = Page.MAIN
                            active.value = "главная"
                        })
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    MenuButton(text = "сканер", painterResource(id = R.drawable.scanner),
                        active = active.value,
                        action = {
                            page.value = Page.SCANNER
                            active.value = "сканер"
                        })
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    MenuButton(text = "моё", painterResource(id = R.drawable.account),
                        active = active.value,
                        action = {
                            page.value = Page.ACCOUNT
                            active.value = "моё"
                        })
                }
            }
        }
    }
}


@Composable
fun MenuButton(
    text: String,
    painterResourse: Painter,
    action: () -> Unit,
    active: String
) {

    val logoSize = 25.dp

    Button(modifier = Modifier.fillMaxSize(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (active == text) Color.Black else Color.Gray
        ),
        onClick = { action() }) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResourse,
                contentDescription = null,
                modifier = Modifier
                    .width(logoSize)
                    .height(logoSize)
            )


            Text(
                text = text,
                fontSize = 15.sp
            )
        }

    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainActivityPreview() {
    QRCodeAppTheme {
        MainActivityPage("Android")
    }
}